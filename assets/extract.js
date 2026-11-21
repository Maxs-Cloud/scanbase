var __MARKS = ["проверяем браузер", "проверка вы не робот", "antibot", "доступ ограничен", "are you a robot", "captcha", "pardon our interruption", "cf-chl"];

/* Разворачивает вкладки «Состав» и обрывки текста. Прежняя версия брала ВСЕ div/span/li
   страницы и для каждого считала textContent всего поддерева — на мобильной карточке
   Ozon это десятки тысяч узлов и десятки секунд, JS-поток стоял и следующий
   __extractJson() уже не успевал отработать: «виснет на разборе».
   Теперь: только листовые подписи (childElementCount отсекает контейнеры за O(1)),
   потолок узлов и потолок времени. */
function __scanTabs() {
  var n = 0;
  var t0 = Date.now();
  var nodes = document.querySelectorAll('button,[role="tab"],summary,a,span,li');
  var cap = Math.min(nodes.length, 3000);
  for (var i = 0; i < cap; i++) {
    var el = nodes[i];
    if (el.childElementCount > 2) continue;
    var t = (el.textContent || '').replace(/\s+/g, ' ').trim();
    if (t.length > 40) continue;
    if (/^(состав|состав материала|состав продукта|состав и характеристики|ingredients|full ingredients)\s*[:\-]?$/i.test(t) ||
        /^(показать полностью|ещё|еще|развернуть|show more)$/i.test(t)) {
      try { el.click(); n++; } catch (e) {}
      if (n >= 6) break;
    }
    if ((i & 63) === 0 && Date.now() - t0 > 250) break;
  }
  return String(n);
}

function __apiUrl() {
  try {
    if (/wildberries\.ru/i.test(location.host)) {
      var m = location.href.match(/(?:nm|id)?[\/=](\d{6,})/);
      return m ? 'https://card.wb.ru/cards/v1/detail?appType=1&curr=rub&dest=-1257786&nm=' + m[1] : '';
    }
    if (/ozon\.ru/i.test(location.host)) {
      var m2 = location.href.match(/(?:product\/.*?)(\d{6,})/) || location.pathname.match(/(\d{6,})/);
      if (!m2) return '';
      var path = '/product/' + m2[1] + '/?layout_container=pdpMobileThemePage2&layout_page_index=2';
      return 'https://www.ozon.ru/api/entrypoint-api.bx/page/json/v2?url=' + encodeURIComponent(path);
    }
  } catch (e) {}
  return '';
}

/* Сеть вне UI-потока. Прежний XHR с флагом «синхронно» вставал на главный поток
   на весь ответ (мобильная карточка Ozon — несколько МБ) и вешал «Разбор» насмерть. */
function __kickApi() {
  var url = __apiUrl();
  if (!url) return 'none';
  if (window.__apiSent) return window.__apiRaw ? 'ready' : 'pending';
  window.__apiSent = 'sent';
  try {
    var x = new XMLHttpRequest();
    x.open('GET', url, true);
    x.withCredentials = true;
    x.timeout = 15000;
    x.onload = function () { try { window.__apiRaw = x.responseText || ''; } catch (e) {} };
    x.onerror = function () { window.__apiSent = 'failed'; };
    x.ontimeout = function () { window.__apiSent = 'failed'; };
    x.send();
    return 'sent';
  } catch (e) { window.__apiSent = 'failed'; return 'err'; }
}

function __apiText() {
  if (!window.__apiSent || window.__apiSent === 'failed') return '';
  return typeof window.__apiRaw === 'string' ? window.__apiRaw : '';
}

function __apiComp() {
  if (!/wildberries\.ru/i.test(location.host)) return '';
  if (window.__wbComp !== undefined) return window.__wbComp;
  window.__wbComp = '';
  var t = __apiText();
  if (!t) return '';
  try {
    var p = (JSON.parse(t).data.products || [])[0];
    if (!p) return '';
    var lists = (p.breakdown || []).concat(p.characteristics || []);
    for (var i = 0; i < lists.length; i++) {
      if (lists[i] && lists[i].name && /состав/i.test(lists[i].name) && lists[i].value) {
        /* Строка «Состав» у WB бывает и рекламной прозой, и всем описанием
           карточки сразу. Наружу отдаём только проверенный список компонентов:
           иначе splitNames() на телефоне разбивает страницу на тысячи кусков и
           разбор на главном потоке вешает приложение. */
        var v = __clean(lists[i].value);
        if (v.length <= 6000 && __isCompList(v)) { window.__wbComp = __stripLead(v); return window.__wbComp; }
      }
    }
    return '';
  } catch (e) { return ''; }
}

/* Разбор ответа Ozon кэшируется: JSON.parse нескольких МБ — тоже дорогой
   гость на главном потоке, повторять его на каждой попытке нельзя. */
function __ozonComp() {
  if (!/ozon\.ru/i.test(location.host)) return '';
  if (window.__ozComp !== undefined) return window.__ozComp;
  var t = __apiText();
  if (!t) return '';
  window.__ozComp = __ozonParse(t);
  return window.__ozComp;
}

function __stripLead(s) {
  return __clean(s).replace(/^\s*(?:состав(?:\s+продукта|\s+материала|\s+средства)?|ingredients?|full\s+ingredients|composition)\s*[:\-–—]\s*/i, '');
}

/* Чистый список компонентов, а не связный текст. Старому __isComp хватало слова
   «состав» или двух запятых, и в разбор попадало рекламное «Миндальное масло в
   составе – смягчает, питает и увлажняет кожу…». Здесь структурная проверка. */
function __isCompList(s) {
  s = __stripLead(s);
  if (!s || s.length < 8 || s.length > 4000) return false;
  if (/состав|ingredient/i.test(s)) return false;
  if (/[?!:]/.test(s)) return false;
  if (/\s[–—-]\s/.test(s)) return false;
  var parts = s.split(/[,;\n]+/);
  var keep = 0, longish = 0;
  for (var i = 0; i < parts.length; i++) {
    var p = parts[i].trim();
    if (!p) continue;
    if (/\.$/.test(p)) return false;
    if (/^(без|только|для|на|по|из|с)\s/i.test(p)) return false;
    keep++;
    if (p.length > 42) longish++;
  }
  if (keep < 3) return false;
  if (longish > keep * 0.2) return false;
  if (/(смягча|увлажня|увлажн|питает|питатель|обеспечив|помогает|снижает|защищ|создаёт|подходит|содерж|благодар|риска|кож|волос)/i.test(s)) return false;
  if (/(инновационн|уникальн|текстур|формул|нот[ыае]|свежест|новинк|хит\s|продаж|подарок|доставк|скидк|акци|реклам|бренд|премиум|идеальн|интенсивн|эффект|рекоменду)/i.test(s)) return false;
  if (/(нанесите|нанеси|втира|распредел|смой|массир|массажн|вспень|оставь|оставьте|удобно|легко|быстро)/i.test(s)) return false;
  return true;
}

function __ozonParse(t) {
  try {
    var j = JSON.parse(t || '{}');
    var joinContentRS = function (arr) {
      var parts = [];
      for (var i = 0; i < arr.length; i++) {
        var it = arr[i];
        if (it && typeof it.content === 'string') parts.push(it.content);
      }
      return __clean(parts.join(' '));
    };
    /* Строгий путь: Ozon кладёт компоненты в characteristics с заголовком ровно
       «Состав». «Особенности состава» и текст описания — не состав, их не берём. */
    var TITLES = { 'состав': 1, 'состав продукта': 1, 'состав материала': 1, 'состав средства': 1, 'ingredients': 1, 'full ingredients': 1, 'ingredient': 1, 'composition': 1 };
    var strict = '';
    var norm = function (x) { return __clean(typeof x === 'string' ? x : '').toLowerCase().replace(/[:\s]+$/, ''); };
    var strictWalk = function (o, depth) {
      if (strict || !o || depth > 14) return;
      if (Array.isArray(o)) { for (var i = 0; i < o.length && !strict; i++) strictWalk(o[i], depth + 1); return; }
      if (typeof o !== 'object') return;
      if (TITLES[norm(o.title)] || TITLES[norm(o.name)] || TITLES[norm(o.key)]) {
        var cand = [];
        if (typeof o.content === 'string') cand.push(o.content);
        if (typeof o.value === 'string') cand.push(o.value);
        if (Array.isArray(o.contentRS)) cand.push(joinContentRS(o.contentRS));
        for (var c = 0; c < cand.length; c++) { if (__isCompList(cand[c])) { strict = __clean(cand[c]); return; } }
      }
      for (var k in o) {
        if (!Object.prototype.hasOwnProperty.call(o, k)) continue;
        var v = o[k];
        if (v && typeof v === 'object') { strictWalk(v, depth + 1); if (strict) return; }
      }
    };
    var states = [];
    var ws0 = j.widgetStates || {};
    for (var k0 in ws0) {
      if (!Object.prototype.hasOwnProperty.call(ws0, k0)) continue;
      if (typeof ws0[k0] !== 'string') continue;
      try { states.push(JSON.parse(ws0[k0])); } catch (e) {}
    }
    for (var si = 0; si < states.length && !strict; si++) strictWalk(states[si], 0);
    if (!strict) strictWalk(j, 0);
    if (strict) return strict;
    var found = '';
    function compFromText(s) {
      var t = __clean(s || '');
      var mm = t.match(/(?:^|[\n.;])\s*(?:состав|ingredients?)\s*[:\-]?\s*([^\n<>]{20,3000})/i);
      if (mm && __isCompList(mm[1])) return __stripLead(mm[1]);
      return '';
    }
    function acceptComp(s) {
      var t = __clean(s || '');
      if (!t || t.length < 3 || t.length > 4000) return '';
      if (/^(https?:|javascript:|\{|\[|\")/i.test(t)) return '';
      if (__isComp(t)) return t;
      return '';
    }
    function walk(o, depth) {
      if (found || !o || depth > 12) return;
      if (Array.isArray(o)) { for (var i = 0; i < o.length && !found; i++) walk(o[i], depth + 1); return; }
      if (typeof o !== 'object') return;
      var title = typeof o.title === 'string' ? o.title : '';
      var key = typeof o.key === 'string' ? o.key : '';
      var val = '';
      if (typeof o.content === 'string') val = o.content;
      if (!val && Array.isArray(o.contentRS)) val = joinContentRS(o.contentRS);
      if (val && (/состав/i.test(title) || /composition/i.test(key))) {
        var ac = acceptComp(val);
        if (ac) { found = ac; return; }
      }
      if (typeof o.description === 'string') {
        var dc = compFromText(o.description);
        if (dc) { found = dc; return; }
      }
      for (var k in o) {
        if (!Object.prototype.hasOwnProperty.call(o, k)) continue;
        var v = o[k], lk = k.toLowerCase();
        if (typeof v === 'string') {
          if ((/description|content|text|value/.test(lk) || /состав/i.test(v)) && /состав|ingredient/i.test(v)) {
            var sc = compFromText(v);
            if (sc) { found = sc; return; }
          }
        } else if (v && typeof v === 'object') {
          walk(v, depth + 1);
          if (found) return;
        }
      }
    }
    var ws = j.widgetStates || {};
    for (var k in ws) {
      if (!Object.prototype.hasOwnProperty.call(ws, k)) continue;
      var s = ws[k];
      if (typeof s !== 'string') continue;
      if (s.indexOf('Состав') < 0 && s.indexOf('состав') < 0 && s.indexOf('ingredient') < 0) continue;
      try { walk(JSON.parse(s), 0); } catch (e) {}
      if (found) return found;
      var sc = compFromText(s);
      if (sc) return sc;
    }
    walk(j, 0);
    return found;
  } catch (e) { return ''; }
}

function __domComp() {
  var best = '', bestScore = -1;
  var t0 = Date.now();
  var nodes = document.querySelectorAll('li, tr, p, div, span, dt, dd, td');
  for (var i = 0, cap = Math.min(nodes.length, 2500); i < cap; i++) {
    var n = nodes[i];
    var t = (nodes[i].textContent || '').replace(/\s+/g, ' ').trim();
    if (!/^состав\s*[:\-]?\s*/i.test(t) && !/^ingredients?\s*[:\-]?\s*/i.test(t)) {
      if ((i & 127) === 0 && Date.now() - t0 > 300) break;
      continue;
    }
    var inner = false;
    for (var j = 0; j < nodes[i].children.length; j++) {
      var ct = (nodes[i].children[j].textContent || '').replace(/\s+/g, ' ').trim();
      if (/^(состав|ingredients)\s*[:\-]?/i.test(ct)) { inner = true; break; }
    }
    if (inner) continue;
    if (t.length < 14 || t.length > 4000) continue;
    if (!__isCompList(t)) continue;
    var commas = (t.match(/[,;]/g) || []).length;
    if (commas < 2 && t.length < 60) continue;
    var score = commas * 10 - Math.min(t.length / 200, 5);
    if (score > bestScore) { bestScore = score; best = t; }
  }
  return best ? __stripLead(best) : '';
}

/* Единственный валидатор состава для всех путей. Раньше здесь был мягкий
  recognizer: достаточно слова «состав» или двух запятых — и в разбор попадала
   рекламная проза («Миндальное масло в составе – смягчает и питает кожу…»).
   Всё сведено к строгой структурной проверке __isCompList. */
function __isComp(s) {
  return __isCompList(s);
}

function __clean(s) {
  return (s || '').replace(/\s+/g, ' ').replace(/\u00a0/g, ' ').trim();
}

function __metaComp() {
  var metas = document.querySelectorAll('meta[name="description"],meta[property="og:description"],meta[property="product:description"],meta[name="keywords"]');
  for (var i = 0; i < metas.length; i++) {
    var c = __clean(metas[i].getAttribute('content') || '');
    var m = c.match(/(?:^|[.;])\s*(состав\s*[:\-]\s*[^.;]{20,3000})/i) || c.match(/(?:^|[.;])\s*(ingredients?\s*[:\-]\s*[^.;]{20,3000})/i);
    if (m && __isComp(m[1])) return __stripLead(m[1]);
    if (__isComp(c) && /состав|ingredient/i.test(c)) return __stripLead(c);
  }
  return '';
}

function __ldComp() {
  var scripts = document.querySelectorAll('script[type="application/ld+json"]');
  var found = '';
  function walk(o, depth) {
    if (found || !o || depth > 8) return;
    if (Array.isArray(o)) { for (var i = 0; i < o.length; i++) walk(o[i], depth + 1); return; }
    if (typeof o !== 'object') return;
    if (typeof o.name === 'string' && typeof o.value === 'string' &&
            /ingredient|composition|состав|nutrition/i.test(o.name)) {
      var nv = __clean(o.value);
      var nm = nv.match(/(?:^|[.;])\s*(состав\s*[:\-]\s*[^.;]{20,3000})/i) ||
               nv.match(/(?:^|[.;])\s*(ingredients?\s*[:\-]\s*[^.;]{20,3000})/i);
      if (nm && __isComp(nm[1])) { found = nm[1]; return; }
      if (__isComp(nv)) { found = nv; return; }
    }
    for (var k in o) {
      if (!Object.prototype.hasOwnProperty.call(o, k)) continue;
      var v = o[k], lk = k.toLowerCase();
      if ((/ingredient|composition|состав|nutrition/.test(lk) || lk === 'description') && typeof v === 'string') {
        var c = __clean(v);
        var m = c.match(/(?:^|[.;])\s*(состав\s*[:\-]\s*[^.;]{20,3000})/i) || c.match(/(?:^|[.;])\s*(ingredients?\s*[:\-]\s*[^.;]{20,3000})/i);
        if (m && __isComp(m[1])) { found = m[1]; return; }
        if (/ingredient|composition|состав/i.test(lk) && __isComp(c)) { found = c; return; }
        if (lk === 'description' && __isComp(c) && /состав|ingredient/i.test(c)) { found = c; return; }
      }
      if (v && typeof v === 'object') walk(v, depth + 1);
    }
  }
  for (var i = 0; i < scripts.length; i++) {
    try { walk(JSON.parse(scripts[i].textContent || '{}'), 0); } catch (e) {}
    if (found) return __stripLead(found);
  }
  return '';
}

function __stateComp() {
  var found = '';
  function walk(o, depth) {
    if (found || !o || depth > 7) return;
    if (Array.isArray(o)) { for (var i = 0; i < o.length && !found; i++) walk(o[i], depth + 1); return; }
    if (typeof o !== 'object') return;
    if (typeof o.name === 'string' && typeof o.value === 'string' &&
            /ingredient|composition|состав|nutrition/i.test(o.name)) {
      var nv = __clean(o.value);
      if (__isComp(nv)) { found = nv; return; }
    }
    for (var k in o) {
      if (!Object.prototype.hasOwnProperty.call(o, k)) continue;
      var v = o[k], lk = k.toLowerCase();
      if (/ingredient|composition|состав|nutrition/.test(lk) && typeof v === 'string' && __isComp(__clean(v))) {
        found = __clean(v); return;
      }
      if (v && typeof v === 'object') walk(v, depth + 1);
    }
  }
  var roots = [];
  try { roots.push(window.__INITIAL_STATE__, window.__NUXT__, window.__PRELOADED_STATE__, window.__NEXT_DATA__, window.state, window.pageData); } catch (e) {}
  for (var i = 0; i < roots.length && !found; i++) walk(roots[i], 0);
  return __stripLead(found);
}

function __desc() {
  var metas = document.querySelectorAll('meta[name="description"],meta[property="og:description"],meta[property="product:description"],meta[name="keywords"]');
  for (var i = 0; i < metas.length; i++) {
    var c = (metas[i].getAttribute('content') || '').replace(/\s+/g, ' ').trim();
    if (c.length > 40) return c.slice(0, 1200);
  }
  return '';
}

/* Разбор строки с ценой — один на все пути: DOM, ответ площадки, JSON-LD. */
function __parsePrice(s) {
  var t = (s || '').replace(/\u00a0/g, ' ').replace(/\s+/g, ' ').trim();
  var m = t.match(/(\d[\d\s]*(?:[.,]\d+)?)\s*(₽|руб\.?|RUB|\$|USD|€|EUR|₸|KZT|BYN|Br)/i);
  if (!m) return null;
  var n = Number(m[1].replace(/\s+/g, '').replace(',', '.'));
  if (!isFinite(n) || n <= 0 || n >= 100000000) return null;
  return {price: n, currency: m[2].toUpperCase(), text: m[0].replace(/\s+/g, ' ')};
}

/* Символы валют для читаемой строки цены. */
var __CURSYM = { RUB: '\u20bd', RUB2: '', USD: '$', EUR: '\u20ac', KZT: '\u20b8', BYN: 'Br' };

function __money(v, cur) {
  var num = Number(String(v === undefined || v === null ? '' : v).replace(/[^\d.,]/g, '').replace(',', '.'));
  if (!isFinite(num) || num <= 0 || num >= 100000000) return null;
  var c = String(cur || 'RUB').toUpperCase();
  var n = Math.round(num);
  return { price: n, currency: c, text: n + ' ' + (__CURSYM[c] || c) };
}

/* Цена из ответа Wildberries: salePriceU — копейки. */
function __apiPrice() {
  if (window.__apiP !== undefined) return window.__apiP;
  var t = __apiText();
  if (!t) return null;
  window.__apiP = null;
  if (!/wildberries\.ru/i.test(location.host)) return null;
  try {
    var p = ((JSON.parse(t).data || {}).products || [])[0];
    if (p) window.__apiP = __money(p.salePriceU ? p.salePriceU / 100 : p.productPrice, 'RUB');
  } catch (e) {}
  return window.__apiP;
}

/* Цена из JSON-LD. На мобильной вёрстке Ozon классы генерируются («c35_6_3-a1»),
   и замер на телефоне дал n=0 по [class*="price"] — то есть узлов цены в DOM нет
   вовсе, а цена есть: площадка кладёт «@type":"Product" с "price" в
   application/ld+json. Читаем текст <script> — это не обход дерева. */
function __ldPrice() {
  if (window.__ldP !== undefined) return window.__ldP;
  window.__ldP = null;
  var scripts = document.querySelectorAll('script[type="application/ld+json"]');
  for (var i = 0; i < scripts.length && i < 6; i++) {
    var raw = (scripts[i].textContent || '').slice(0, 20000);
    if (!raw) continue;
    var root = null;
    try { root = JSON.parse(raw); } catch (e) { continue; }
    var stack = [root];
    for (var s0 = 0; s0 < stack.length && s0 < 40; s0++) {
      var cur = stack[s0];
      if (!cur || typeof cur !== 'object') continue;
      if (Array.isArray(cur)) {
        for (var q = 0; q < cur.length && q < 20; q++) stack.push(cur[q]);
        continue;
      }
      var off = cur.offers;
      var list = Array.isArray(off) ? off : [off];
      for (var k = 0; k < list.length; k++) {
        var it = list[k];
        if (!it || typeof it !== 'object') continue;
        var got = __money(it.price !== undefined ? it.price : it.lowPrice, it.priceCurrency);
        if (got) { window.__ldP = got; return got; }
      }
      for (var key in cur) {
        if (!Object.prototype.hasOwnProperty.call(cur, key)) continue;
        if (cur[key] && typeof cur[key] === 'object') stack.push(cur[key]);
      }
    }
  }
  return window.__ldP;
}

/* Последний резерв: первый лист страницы с ценой в собственном текстовом узле.
   Только листья — textContent листа стоит O(1), поэтому дёшево даже на 40 000 узлов.
   Порядок DOM ставит текущую цену раньше зачёркнутой. */
function __leafPrice() {
  if (window.__leafP !== undefined) return window.__leafP;
  window.__leafP = null;
  var all = document.querySelectorAll('body *');
  window.__nodes = all.length;
  var cap = Math.min(all.length, 4000);
  var t0 = Date.now();
  for (var i = 0; i < cap; i++) {
    var el = all[i];
    if (el.childElementCount !== 0) continue;
    var tx = (el.firstChild && el.firstChild.nodeType === 3) ? el.firstChild.data : '';
    if (!tx || tx.length > 24) continue;
    var pr = __parsePrice(tx);
    if (pr) { window.__leafP = pr; return pr; }
    if ((i & 255) === 0 && Date.now() - t0 > 200) break;
  }
  return window.__leafP;
}

function __price() {
  var out = {price: 0, currency: '', text: ''};
  function parse(s) {
    var t = (s || '').replace(/\u00a0/g, ' ').replace(/\s+/g, ' ').trim();
    var m = t.match(/(\d[\d\s]*(?:[.,]\d+)?)\s*(₽|руб\.?|RUB|\$|USD|€|EUR|₸|KZT|BYN|Br)/i);
    if (!m) return null;
    var n = Number(m[1].replace(/\s+/g, '').replace(',', '.'));
    if (!isFinite(n) || n <= 0) return null;
    return {price: n, currency: m[2].toUpperCase(), text: m[0].replace(/\s+/g, ' ')};
  }
  var metas = document.querySelectorAll('meta[itemprop="price"],meta[property="product:price:amount"],meta[property="og:price:amount"]');
  for (var i = 0; i < metas.length; i++) {
    var p = parse(metas[i].getAttribute('content') || '');
    if (p) return p;
  }
  var nodes = document.querySelectorAll('[itemprop="price"],[data-testid*="price" i],[class*="price" i]');
  var t0 = Date.now();
  for (var j = 0, jcap = Math.min(nodes.length, 400); j < jcap; j++) {
    var el = nodes[j];
    /* Крупные контейнеры не читаем: textContent считает всё поддерево.
       Порог 12, а не 3: на мобильной карточке Ozon цена лежит в узле с
       несколькими вложенными span, и жёсткий срез обнулял цену. */
    if (el.childElementCount > 12) continue;
    var p2 = parse(el.getAttribute('content') || el.textContent || '');
    if (p2 && p2.price < 100000000) return p2;
    if ((j & 63) === 0 && Date.now() - t0 > 150) break;
  }
  var byApi = __apiPrice();
  if (byApi) return byApi;
  /* Прицельная сверка двух источников. Замер на телефоне: JSON-LD Ozon объявляет
     461, а на экране 454 — площадка снимает часть по карте Ozon, и структурированные
     данные этой скидки не видят. Пользователю нужна та цена, что он видит, поэтому
     лист страницы в приоритете, но только когда он не расходится с JSON-LД больше
     чем на 20 %: иначе это цена соседнего товара из блока «с этим покупают». */
  var byLd = __ldPrice();
  var byLeaf = __leafPrice();
  if (byLeaf && (!byLd || Math.abs(byLeaf.price - byLd.price) <= Math.max(2, byLd.price * 0.2))) return byLeaf;
  if (byLd) return byLd;
  return byLeaf || out;
}

function __extractJson() {
  /* Только состав: тело страницы целиком не читаем — на мобильной карточке
     document.body.textContent/innerText вешает JS-поток WebView на десятки секунд.
     Для «заблокировано»/«не найдено» хватает заголовка окна и H1. */
  var __t0 = Date.now();
  var head = (document.title || '') + ' | ' + __h1();
  var low = head.toLowerCase();
  var blocked = false;
  for (var i = 0; i < __MARKS.length; i++) if (low.indexOf(__MARKS[i]) > -1) blocked = true;
  /* Пока ответ площадки в дороге, DOM-прозу не выдаём за состав: на Ozon в карточке
     лежит рекламный текст, он «не пустой» и затирал собой настоящий список ингредиентов. */
  var pending = window.__apiSent === 'sent' || window.__apiSent === 'pending';
  var api = __apiComp() || __ozonComp();
  /* DOM обходим только когда API молчит и ответ не в дороге; тяжёлые обходчики
     __labelComp (querySelectorAll('*')) и __innerTextComp (весь innerText) убраны. */
  var dom = (api || pending) ? '' : (__domComp() || __ldComp() || __metaComp() || __stateComp());
  var __t1 = Date.now();
  var pr = __price();
  var __t2 = Date.now();

function __h1() {
  var hs = document.querySelectorAll('h1');
  for (var i = 0; i < hs.length; i++) {
    var t = (hs[i].innerText || '').trim();
    if (t) return t.slice(0, 160);
  }
  return '';
}

/* «такого товара нет»: ловим редирект на главную/поиск и текст об отсутствии карточки. */
function __notFound(txt) {
  var hay = (document.title + ' | ' + __h1() + ' | ' + txt.slice(0, 4000)).toLowerCase();
  var marks = ['не найден', 'не найдена', 'не найдено', 'не существует', 'не существует',
               'такого товара', 'такой страницы', 'страница не найдена', 'ничего не найдено',
               'больше не продаётся', 'товар отсутств', '404', 'not found', 'page not found'];
  for (var i = 0; i < marks.length; i++) {
    if (hay.indexOf(marks[i]) >= 0) return marks[i];
  }
  var path = location.pathname || '';
  if (/^\/(search|modal|cart|basket|order|error|404|notfound|not-found|login|auth)/i.test(path)) return 'раздел ' + path.split('/')[1];
  if (path === '' || path === '/') return 'главная страница';
  return '';
}
  return {
    url: location.href, title: document.title, len: head.length, blocked: blocked,
    api: (window.__apiRaw ? 'ready' : String(window.__apiSent || '')),
    via: api ? 'tab' : (pending ? '' : (dom ? 'dom' : '')),
    composition: api || (pending ? '' : dom) || '',
    description: __desc(), price: pr.price || 0, currency: pr.currency || '', priceText: pr.text || '',
    h1: __h1(), notFound: __notFound(head),
    timing: 'price=' + (__t2 - __t1) + 'ms pre=' + (__t1 - __t0) + 'ms total=' + (Date.now() - __t0) +
            'ms nodes=' + (window.__nodes === undefined ? -1 : window.__nodes)
  };
}
