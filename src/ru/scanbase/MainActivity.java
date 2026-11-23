package ru.scanbase;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/karpov-m/work/scanbase/apk/unpacked/classes.dex */
public class MainActivity extends Activity {
    private TextView aiBody;
    private Button aiButton;
    private LinearLayout aiPanel;
    private TextView aiStatus;
    private FrameLayout content;
    private SQLiteDatabase db;
    private TextView error;
    private List<Item> lastItems;
    private JSONObject lastJson;
    private List nameIdx;
    private java.util.HashSet<String> nameIdxSet;
    private Button pageButton;
    private WebView resultWeb;
    private Button tabAi;
    private Button tabRead;
    private EditText urlBox;
    private WebView web;
    static final String[] TEXTILE = {"хлопок", "cotton", "лен", "linen", "эластан", "elastane", "спандекс", "spandex", "lycra", "лайкра", "полиэстер", "polyester", "полиэфир", "нейлон", "nylon", "полиамид", "polyamide", "акрил", "acrylic", "вискоза", "viscose", "модал", "modal", "лиоцелл", "lyocell", "ацетат", "acetate", "шерсть", "wool", "кашемир", "cashmere", "шёлк", "silk", "купро", "купра", "тенсел", "tencel", "эвкалипт", "конопля", "крапива", "меринос", "мерино", "ангор", "мохер", "альпака", "мех", "замша", "нубук", "фетр", "войлок", "арамид", "кевлар", "купро", "купра", "полиолефин", "термохолст", "кручен", "муслин", "поплин", "сатин", "бязь", "фланел", "байк", "твид", "драп", "сукн"};
    static final String[] NATURAL_MATERIAL = {"хлопок", "cotton", "лен", "linen", "вискоза", "viscose", "модал", "modal", "лиоцелл", "lyocell", "ацетат", "acetate"};
    static final String[] SYNTH_MATERIAL = {"полиэстер", "polyester", "полиэфир", "нейлон", "nylon", "полиамид", "polyamide", "акрил", "acrylic"};
    static final String[] ELASTIC_MATERIAL = {"эластан", "elastane", "спандекс", "spandex", "lycra", "лайкра"};
    static final String[] WOOL_MATERIAL = {"шерсть", "wool", "кашемир", "cashmere"};
    static final String[] HUMECTANT = {"glycerin", "глицерин", "hyaluron", "гиалурон", "panthenol", "пантенол", "urea", "мочевина", "betaine", "бетаин", "propylene glycol", "butylene glycol", "pentylene glycol", "propanediol", "sodium pca", "aloe", "алоэ"};
    static final String[] ACTIVE = {"niacinamide", "ниацинамид", "retinol", "ретинол", "peptide", "пептид", "ceramide", "церамид", "salicylic", "салицил", "azelaic", "азелаин", "vitamin c", "ascorbic", "аскорбин", "tocopherol", "токоферол", "centella", "центелла", "adenosine", "аденозин", "zinc", "цинк", "aha", "bha", "hyaluron", "гиалурон"};
    static final String[] EMOLLIENT = {"squalane", "сквалан", "caprylic", "триглицерид", "oil", "масло", "butyrospermum", "ши", "cetearyl", "цетеариловый", "isopropyl myristate", "dimethicone", "диметикон", "shea", "jojoba", "жожоба", "цетиловый спирт", "стеариловый спирт", "миристиловый спирт", "цетеариловый спирт", "beeswax", "пчелиный воск", "ozokerite", "цера синтетика", "изопропилмиристат", "изопропилпальмитат", "caprylic/capric triglyceride", "cetyl alcohol", "stearyl alcohol", "myristyl alcohol", "cetearyl alcohol", "behenyl alcohol", "lanolin alcohol", "olus oil", "vegetable oil"};
    static final String[] SURFACTANT = {"sodium lauryl sulfate", "sodium laureth sulfate", "sls", "sles", "cocamidopropyl betaine", "ammonium lauryl sulfate", "tea-lauryl", "sodium coco sulfate", "cocoyl", "lauroyl", "decyl glucoside", "coco glucoside", "caprylyl/capryl glucoside", "sodium cocoyl glutamate", "sodium lauroyl sarcosinate", "disodium laureth sulfosuccinate", "sodium cocoyl isethionate", "guar hydroxypropyltrimonium", "лаурил сульфат", "лаурет сульфат", "коко сульфат", "кокамид", "кокоил", "лауроил", "глюкозид", "саркозинат", "глутамат кокоил", "сульфосукцинат", "изетионат", "мыло", "калийевое мыло", "натриевое мыло", "моющее средство", "поверхностно-активн"};
    static final String[] PRESERVATIVE = {"phenoxyethanol", "phenethyl alcohol", "ethylhexylglycerin", "methylparaben", "propylparaben", "benzyl alcohol", "sodium benzoate", "potassium sorbate", "dehydroacetic acid", "benzoic acid", "sorbic acid", "salicylic acid", "caprylyl glycol", "1,2-hexanediol", "chlorphenesin", "dmdm hydantoin", "феноксиэтанол", "фенэтиловый спирт", "парабен", "бензиловый спирт", "бензоат натрия", "сорбат калия", "сорбиновая кислота", "бензойная кислота", "дегидроуксусная кислота", "салициловая кислота", "каприлилгликоль", "хлорфенезин", "консервирующее вещество", "консервант", "isothiazolinone", "изотиазолинон", "diazolidinyl", "imidazolidinyl"};
    static final String[] FRAGRANCE = {"parfum", "fragrance", "отдушка", "aroma", "flavor", "linalool", "limonene", "citronellol", "geraniol", "coumarin", "citral", "eugenol", "isoeugenol", "farnesol", "amyl cinnamal", "hexyl cinnamal", "benzyl salicylate", "cinnamyl alcohol", "anise alcohol", "линалоол", "лимонен", "цитронеллол", "гераниол", "кумарин", "цитраль", "евгенол", "изоэвгенол", "фарнезол", "амилкоричный альдегид", "гексилкоричный альдегид", "салицилат бензила", "коричный спирт", "анизовый спирт", "ароматизатор", "парфюмерная композиция", "отдушка парфюмерная"};
    static final String[] COLORANT = {"ci 77", "ci 75", "ci 74", "ci 15", "ci 42",
            "краситель", "красящее вещество", "пигмент", "pigment", "dye", "colorant", "colourant"};
    static final String[] SILICONE = {"dimethicone", "диметикон", "cyclomethicone", "siloxane"};
    static final String[] POLYMER = {"acrylates", "acrylate", "vp copolymer", "polymer", "carbomer"};
    static final String[] SOLVENT = {"water", "aqua", "alcohol", "butylene glycol", "pentylene glycol", "propylene glycol", "propanediol", "ethanol", "alcohol denat", "isopropyl alcohol", "isopropanol", "glycerin", "вода", "вода дистиллированная", "вода очищенная", "спирт этиловый", "спирт денат", "денат", "этанол", "изопропанол", "изопропиловый спирт", "пропиленгликоль", "бутиленгликоль", "пентиленгликоль", "пропандиол", "растворитель"};
    static final String[] HIGH_RISK = {"methylisothiazolinone", "methylchloroisothiazolinone", "formaldehyde", "formalin", "imidazolidinyl urea", "diazolidinyl urea", "quaternium-15", "dmdm hydantoin", "methyldibromo glutamine", "2-bromo-2-nitropropane-1,3-diol", "metil izotiazolinon", "метилизотиазолинон", "метилхлоризотиазолинон", "формальдегид", "формалин", "имидазолидинил", "диазолидинил", "кватерниум-15", "донор формальдегида"};
    static final String[] MED_RISK = {"parfum", "fragrance", "отдушка", "alcohol denat", "denatured alcohol", "ci 77", "ci 75", "peg-", "ppg-", "lanolin", "sodium lauryl sulfate", "sodium laureth sulfate", "sls", "sles", "лаурил сульфат", "лаурет сульфат", "спирт денат", "денат", "ланолин", "пэг-", "полиэтиленгликоль", "этоксилат", "paraben", "парабен"};
    static final String[] CHEAP = {"water", "aqua", "glycerin", "sodium chloride", "cetearyl alcohol", "stearic acid", "palmitic acid", "cetyl alcohol", "butylene glycol", "propylene glycol", "хлопок", "cotton", "полиэстер", "polyester"};
    static final String[] EXPENSIVE = {"peptide", "пептид", "retinol", "ретинол", "hyaluron", "гиалурон", "ceramide", "церамид", "tocopherol", "токоферол", "ascorbic", "аскорбин", "centella", "центелла", "adenosine", "аденозин", "squalane", "сквалан", "шерсть", "wool", "кашемир", "cashmere"};
    private static final Pattern PRICE = Pattern.compile("(?iu)(\\d[\\d\\s\\u00a0]*(?:[.,]\\d+)?)\\s*(?:₽|руб(?:ль|ля|лей|лем)?\\.?|RUB|\\$|USD|€|EUR)");
    private String lastUrl = "";
    private String lastHtml = "";
    private int lastOverall = 0;
    private int lastUseful = 0;
    private int lastSafe = 0;
    private int lastCheap = 0;
    private int lastComfort = 0;
    private String lastRiskNames = "";
    private String extractJs = "";
    private int extractAttempt = 0;
    private int lateRound = 0;
    private String lastVerdict = "";
    private int currentTab = 0;
    private boolean pageShown = false;
    private int tabBeforePage = 0;
    private boolean loading = false;
    private String lastRequested = "";
    private long lastRequestedAt = 0;
    private String rejectedPath = "";
    private long rejectedAt = 0;
    private final Map<String, String> ruNames = new HashMap();
    private boolean aiBusy = false;
    private volatile boolean aiStop = false;
    private int aiBudgetSec = 100;
    private boolean autoAi = false;
    boolean aiShort = false;
    private String lastTitle = "";
    private String lastComposition = "";
    private String lastDescription = "";
    private String lastCatHint = "";
    private String lastAttrs = "";
    volatile String wbCard = "";
    private String[] lastCat = null;
    private String lastPriceText = "";
    private int lastMatched = 0;
    private int lastTotal = 0;
    private final ModelDownloader downloader = new ModelDownloader();
    private String lastPriceWord = "";
    private String lastKey = "";
    private final HashMap aiAlias = new HashMap();
    private final HashSet triedKeys = new HashSet();
    private boolean refining = false;

    static class AiReviewTask implements Runnable {
        private final MainActivity act;
        private final String prompt;
        private final int seconds;

        AiReviewTask(MainActivity mainActivity, String str, int i) {
            this.act = mainActivity;
            this.prompt = str;
            this.seconds = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            MainActivity mainActivity = this.act;
            String strBest = "";
            for (int iAttempt = 1; iAttempt <= 3; iAttempt++) {
                final String strStatus = iAttempt == 1 ? "Пишу разбор…" : "Модель сбилась на полуслове — переписываю, попытка " + iAttempt;
                mainActivity.runOnUiThread(new AiStatusTask(mainActivity, strStatus));
                long jStart = System.currentTimeMillis();
                String strGenerate = LlamaModel.generate(mainActivity, this.prompt, 190, this.seconds, 6, new ReviewSink(mainActivity));
                String strTrimmed = strGenerate == null ? "" : strGenerate.trim();
                Log.i("ScanBase", "отзыв ИИ, попытка " + iAttempt + ", за " + ((System.currentTimeMillis() - jStart) / 1000) + " с, " + strTrimmed.length() + " знаков: " + strTrimmed);
                if (strTrimmed.length() > strBest.length()) {
                    strBest = strTrimmed;
                }
                /* Обрывок вроде «Это кос» — не отзыв: модель то и дело бросает
                   генерацию на первых токенах. Пользователю такое показывать
                   нельзя, поэтому пробуем заново, а не выдаём недописанное. */
                if (reviewIsWhole(strTrimmed)) {
                    break;
                }
                if (mainActivity.aiStop) {
                    break;
                }
                strBest = "";
                Log.i("ScanBase", "слишком коротко — перегенерирую");
            }
            mainActivity.aiShort = !reviewIsWhole(strBest);
            mainActivity.aiBusy = false;
            mainActivity.runOnUiThread(new AiReviewUiTask(mainActivity, strBest));
        }
    }

    static class AiReviewUiTask implements Runnable {
        private final MainActivity act;
        private final String text;

        AiReviewUiTask(MainActivity mainActivity, String str) {
            this.act = mainActivity;
            this.text = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.act.showAiReview(this.text);
        }
    }

    static class DownloadCallback implements ModelDownloader.Callback {
        private final MainActivity act;

        DownloadCallback(MainActivity mainActivity) {
            this.act = mainActivity;
        }

        @Override // ru.scanbase.ModelDownloader.Callback
        public void onDone(String str) {
            this.act.aiStatus.setText("Модель готова, скачано с " + str + ".");
            this.act.refreshAiPanel();
        }

        @Override // ru.scanbase.ModelDownloader.Callback
        public void onFail(String str) {
            this.act.aiStatus.setText("Загрузка не вышла: " + str);
            this.act.refreshAiPanel();
        }

        @Override // ru.scanbase.ModelDownloader.Callback
        public void onProgress(long j, long j2) {
            if (j2 <= 0) {
                this.act.aiStatus.setText("Скачивание: " + MainActivity.mb(j));
                return;
            }
            this.act.aiStatus.setText("Скачивание: " + MainActivity.mb(j) + " из " + MainActivity.mb(j2) + " (" + ((j * 100) / j2) + "%)");
        }
    }

    static class Item {
        boolean catalog;
        String food;
        String foodName;
        String func;
        String hazard;
        List<String[]> law = new ArrayList();
        String name = "";
        String note = "";
        double pct;
        String risk;
        String role;
        String status;

        Item() {
        }
    }

    interface PageDone {
        void run();
    }

    static class PageHost extends WebViewClient {
        private final PageDone done;
        private final MainActivity act;

        PageHost(MainActivity act, PageDone pageDone) {
            this.act = act;
            this.done = pageDone;
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView webView, String str, android.graphics.Bitmap bitmap) {
            this.act.kickApi();
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            this.done.run();
        }
    }

    static class RerenderTask implements Runnable {
        private final MainActivity act;

        RerenderTask(MainActivity mainActivity) {
            this.act = mainActivity;
        }

        @Override // java.lang.Runnable
        public void run() {
            JSONObject jSONObject = this.act.lastJson;
            if (jSONObject != null) {
                this.act.report(jSONObject);
            }
        }
    }

    static class ReviewSink implements AiSink {
        private final MainActivity act;

        ReviewSink(MainActivity mainActivity) {
            this.act = mainActivity;
        }

        @Override // ru.scanbase.AiSink
        public void onPiece(String str) {
            if (this.act.aiStop) {
                throw new IllegalStateException("stop");
            }
            MainActivity mainActivity = this.act;
            mainActivity.runOnUiThread(new ShowPieceTask(mainActivity, str));
        }
    }

    private static class Row {
        boolean catalog;
        String food;
        String foodName;
        String func;
        String hazard;
        List<String[]> law;

        private Row() {
            this.law = new ArrayList();
        }

    }

    /* Анонимные new Runnable(){…} в этом файле не пишутся: d8 8.2 падает на
       их class-файлах внутренним NPE, сборка обрывается, а старый APK остаётся
       на месте — выглядит как «сборка прошла», хотя код не поехал. */
    static class AutoAiTask implements Runnable {
        private final MainActivity act;

        AutoAiTask(MainActivity mainActivity) {
            this.act = mainActivity;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.act.startAiReview();
        }
    }

    static class AiStatusTask implements Runnable {
        private final MainActivity act;
        private final String text;

        AiStatusTask(MainActivity mainActivity, String str) {
            this.act = mainActivity;
            this.text = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.act.aiBody.setText(this.text);
        }
    }

    static class ShowPieceTask implements Runnable {
        private final MainActivity act;
        private final String text;

        ShowPieceTask(MainActivity mainActivity, String str) {
            this.act = mainActivity;
            this.text = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.act.aiBusy) {
                this.act.aiBody.setText(this.text);
            }
        }
    }

    private String aiPrompt(int i) {
        /* Название товара в промпт не отдаём: с ним модель каждый раз
           начинала с бренда и таскала его по всем предложениям. Роль товара
           играет категория — та же самая, что показана на экране (catInfo),
           чтобы разбор и отзыв не противоречили друг другу. */
        String[] cat = catInfo();
        String strCat = cat[0];
        StringBuilder sb = new StringBuilder("Это: ");
        sb.append(metaWord(strCat));
        sb.append('\n');
        sb.append("На что смотреть в такой категории: ").append(catAiNote(strCat)).append('\n');
        if ("1".equals(cat[1])) {
            sb.append("Товар детский: отдушки, красители, фталаты и бисфенол A важнее, чем во взрослом товаре.\n");
        }
        if (!this.lastAttrs.isEmpty()) {
            sb.append("Материалы по частям: ").append(this.lastAttrs).append('\n');
        }
        if (!this.lastPriceText.isEmpty()) {
            sb.append("Цена: ");
            sb.append(this.lastPriceText);
            sb.append('\n');
        }
        StringBuilder sbGood = new StringBuilder();
        StringBuilder sbBad = new StringBuilder();
        for (Item item : this.lastItems) {
            String strName = stripPercent(item.name);
            if ("высокий".equals(item.risk)) {
                appendName(sbBad, strName + roleTag(item.role));
            } else if ("низкий".equals(item.risk)) {
                appendName(sbGood, strName + roleTag(item.role));
            }
        }
        sb.append("Хорошее в составе: ");
        sb.append(few(sbGood.toString(), 4));
        sb.append("\nСпорное и вредное в составе: ");
        sb.append(few(sbBad.toString(), 4));
        /* Формат «одним абзацем» здесь не прихоть: в этой сборке .so ответ с
           переносами строк и подписями обрывается на первых токенах, а абзац
           модель дописывает до конца. Порядок доводов задан явно — это и есть
           структура, только та, которую маленькая модель выдерживает. */
        sb.append("\nНапиши по-русски пять предложений одним абзацем, как человек, который читает составы профессионально и объясняет их другу. Порядок такой: ").append(catFirstHint(strCat)).append("; ");
        if ("electronics".equals(strCat) || "clothes".equals(strCat) || "other".equals(strCat)) {
            /* У вещи из ткани и у техники «компоненты состава» — не то, о чём
               спрашивают покупатели, и на этом месте модель начинала выдумывать
               ингредиенты. Просим говорить про материалы и эксплуатацию. */
            sb.append("из чего это сделано и какие из перечисленных названий за это отвечают; что может смутить; кому не подойдёт; брать ли за эту цену и ").append(catLastHint(strCat));
            sb.append(". Перечисленных названий не выдумывай и не переводя; если их мало или они не о товаре, так и скажи.");
        } else {
            sb.append("один-два компонента из строки «Хорошее» и что они делают; компонент из строки «Спорное» и чем он мешает; кому стоит пройти мимо; брать ли за эту цену и ").append(catLastHint(strCat));
            sb.append(". Компоненты называй только те, что перечислены выше, и ровно как они написаны, без перевода. Придуманные названия, проценты и бренд не упоминай.");
        }
        return sb.toString();
    }

    /* Категория в промпте — одним словом, без пояснений: «Косметика и гигиена»
       маленькая модель охотно повторяет в ответе дословно. */
    static String metaWord(String str) {
        if ("cosmetics".equals(str)) {
            return "косметика";
        }
        if ("food".equals(str)) {
            return "продукт питания";
        }
        if ("clothes".equals(str)) {
            return "вещь из ткани или кожи";
        }
        if ("household".equals(str)) {
            return "бытовая химия";
        }
        if ("pharma".equals(str)) {
            return "лекарство или добавка";
        }
        if ("electronics".equals(str)) {
            return "техника";
        }
        return "товар без явной категории";
    }

    static String catAiNote(String str) {
        if ("cosmetics".equals(str)) {
            return "порядок ингредиентов, консерванты, отдушки, ПАВ и вещества с ограничением";
        }
        if ("food".equals(str)) {
            return "добавки с индексом E и их норма, сахар, соль и жиры: что в начале списка, того больше";
        }
        if ("clothes".equals(str)) {
            return "состав волокна: натуральное дышит, синтетика носче; доля эластана, пропитки и красители";
        }
        if ("household".equals(str)) {
            return "ПАВ, щёлочи и кислоты, хлор, фосфаты, метилизотиазолинон; средство нельзя смешивать с другими";
        }
        if ("pharma".equals(str)) {
            return "действующее вещество и дозировку; норму приёма задают инструкция и врач";
        }
        if ("electronics".equals(str)) {
            return "материалы корпуса и покрытий; списка ингредиентов здесь нет";
        }
        return "только те названия, что перечислены, без догадок о категории";
    }

    static String catLastHint(String str) {
        if ("clothes".equals(str)) {
            return "что проверить при примерке и первой стирке";
        }
        if ("food".equals(str)) {
            return "что проверить на этикетке перед покупкой";
        }
        if ("electronics".equals(str)) {
            return "что проверить в характеристиках перед покупкой";
        }
        if ("household".equals(str)) {
            return "как пользоваться, чтобы не испортить поверхность и руки";
        }
        return "что сделать перед первым применением";
    }

    private String[] aliasFor(String str) {
        String strNorm = norm(str);
        if (strNorm.length() < 3) {
            return null;
        }
        String str2 = Rules.ALIAS.get(strNorm);
        if (str2 != null) {
            return new String[]{str2, "словарём"};
        }
        Object obj = this.aiAlias.get(strNorm);
        if (obj != null) {
            return new String[]{String.valueOf(obj), "уточнено ранее"};
        }
        String strFindDbName = this.nameIdx == null ? null : findDbName(str);
        if (strFindDbName != null) {
            return new String[]{strFindDbName, "транслитом"};
        }
        return null;
    }

    private static boolean allowedUrl(String str) {
        String lowerCase = nz(str).toLowerCase(Locale.ROOT);
        return lowerCase.contains("ozon.ru") || lowerCase.contains("ozon.by") || lowerCase.contains("ozon.kz") || lowerCase.contains("wildberries.ru") || lowerCase.contains("wb.ru") || lowerCase.contains("wildberries") || lowerCase.contains("yandex.") || lowerCase.contains("ya.ru");
    }

    private Item analyzeItem(String str) {
        String[] strArrAliasFor;
        Row rowLookup;
        Item item = new Item();
        item.name = str;
        item.status = "не найден";
        item.role = "—";
        item.risk = "неизвестно";
        item.pct = percentValue(str);
        Row rowLookup2 = lookup(str);
        if (rowLookup2 == null && (strArrAliasFor = aliasFor(str)) != null && (rowLookup = lookup(strArrAliasFor[0])) != null) {
            item.note = "название уточнено " + strArrAliasFor[1];
            rowLookup2 = rowLookup;
        }
        if (rowLookup2 != null) {
            item.law = rowLookup2.law;
            item.catalog = rowLookup2.catalog;
            item.food = rowLookup2.food;
            item.foodName = rowLookup2.foodName;
            item.func = rowLookup2.func;
            item.hazard = rowLookup2.hazard;
            if (!rowLookup2.law.isEmpty()) {
                item.status = "ограничен";
            } else if (rowLookup2.catalog) {
                item.status = "CosIng";
            } else if (rowLookup2.food != null) {
                item.status = "добавка";
            } else if (rowLookup2.foodName != null) {
                item.status = "еда";
            } else {
                item.status = "найден";
            }
        }
        classify(item);
        if ("еда".equals(item.status) && "—".equals(item.role)) {
            item.role = "пищевой продукт";
        }
        if ("не найден".equals(item.status) && !"не распознан".equals(item.role)) {
            item.status = "по категории";
        }
        return item;
    }

    private static void appendName(StringBuilder sb, String str) {
        if (sb.length() > 0) {
            sb.append(", ");
        }
        sb.append(str);
    }

    private void buildAiPanel(LinearLayout linearLayout) {
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(1);
        linearLayout2.setPadding(8, 8, 8, 8);
        this.aiPanel = linearLayout2;
        TextView textView = new TextView(this);
        textView.setText("Отзыв нейросети");
        textView.setTextSize(16.0f);
        linearLayout2.addView(textView, match());
        TextView textView2 = new TextView(this);
        textView2.setTextSize(12.0f);
        textView2.setTextColor(-6908267);
        this.aiStatus = textView2;
        linearLayout2.addView(textView2, match());
        Button button = new Button(this);
        button.setAllCaps(false);
        button.setTextSize(13.0f);
        button.setOnClickListener(new MainActivity$$ExternalSyntheticLambda9(this));
        this.aiButton = button;
        linearLayout2.addView(button, wrap());
        ScrollView scrollView = new ScrollView(this);
        TextView textView3 = new TextView(this);
        textView3.setTextSize(15.0f);
        textView3.setTextIsSelectable(true);
        textView3.setPadding(8, 16, 8, 8);
        this.aiBody = textView3;
        scrollView.addView(textView3, match());
        LinearLayout.LayoutParams layoutParamsWeight = weight();
        layoutParamsWeight.weight = 1.0f;
        linearLayout2.addView(scrollView, layoutParamsWeight);
        LinearLayout.LayoutParams layoutParamsWeight2 = weight();
        layoutParamsWeight2.weight = 1.0f;
        linearLayout.addView(linearLayout2, layoutParamsWeight2);
    }

    /* JADX WARN: Code duplicated, block: B:170:0x07c8  */
    /* JADX WARN: Code duplicated, block: B:185:0x080b  */
    /* JADX WARN: Code duplicated, block: B:188:0x0817  */
    /* JADX WARN: Code duplicated, block: B:191:0x0823  */
    /* JADX WARN: Code duplicated, block: B:194:0x082f  */
    /* JADX WARN: Code duplicated, block: B:197:0x083b  */
    /* JADX WARN: Code duplicated, block: B:199:0x083f  */
    /* JADX WARN: Code duplicated, block: B:202:0x084b  */
    /* JADX WARN: Code duplicated, block: B:203:0x0850  */
    /* JADX WARN: Code duplicated, block: B:205:0x0858  */
    /* JADX WARN: Code duplicated, block: B:208:0x0866  */
    /* JADX WARN: Code duplicated, block: B:210:0x0870  */
    /* JADX WARN: Code duplicated, block: B:213:0x0880  */
    /* JADX WARN: Code duplicated, block: B:216:0x0890  */
    /* JADX WARN: Code duplicated, block: B:219:0x08a0  */
    /* JADX WARN: Code duplicated, block: B:223:0x08a6 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:224:0x08a6 A[SYNTHETIC] */
    private String buildHtml(JSONObject jSONObject, List<Item> list, String str, String str2, String str3, int i, double d, String str4, String str5, String str6) {
        String str7;
        boolean z;
        double d2;
        int iClamp;
        int iClamp2;
        int iClamp3;
        int iClamp4;
        int i2;
        int i3;
        String str8;
        String str9;
        ArrayList arrayList;
        int i4 = i;
        Collections.sort(list, new MainActivity$$ExternalSyntheticLambda7());
        String[] cat = catInfo();
        String strDetectCategory = cat[0];
        int size = list.size();
        int i5 = size - i4;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        Iterator<Item> it = list.iterator();
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        double d3 = 0.0d;
        double d4 = 0.0d;
        double d5 = 0.0d;
        double d6 = 0.0d;
        double d7 = 0.0d;
        double d8 = 0.0d;
        double d9 = 0.0d;
        int i13 = 0;
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        int i17 = 0;
        while (true) {
            Iterator<Item> it2 = it;
            str7 = "средний";
            boolean z2 = true;
            if (!it.hasNext()) {
                break;
            }
            int i18 = i6;
            int i19 = i7;
            String str10 = strDetectCategory;
            ArrayList arrayList4 = arrayList3;
            int i20 = i4;
            ArrayList arrayList5 = arrayList2;
            Item next = it2.next();
            if (!next.role.contains("актив") && !next.role.contains("увлажнение") && !next.role.contains("смягчение") && !next.role.contains("натуральное") && !next.role.contains("целлюлозное") && !next.role.contains("эластичность")) {
                z2 = false;
            }
            if ("высокий".equals(next.risk)) {
                i10++;
            } else {
                if (str7.equals(next.risk)) {
                    i6 = i18 + 1;
                }
                if (z2) {
                    i19++;
                }
                if (z2 && arrayList5.size() < 6) {
                    arrayList5.add(stripPercent(next.name));
                }
                if ((!"высокий".equals(next.risk) || str7.equals(next.risk)) && arrayList4.size() < 6) {
                    arrayList = arrayList4;
                    arrayList.add(stripPercent(next.name));
                } else {
                    arrayList = arrayList4;
                }
                if (next.role.contains("актив")) {
                    i14++;
                }
                if (next.role.contains("увлажнение")) {
                    i13++;
                }
                if (next.role.contains("смягчение")) {
                    i15++;
                }
                if (containsAny(next.name, EXPENSIVE)) {
                    i16++;
                }
                if (containsAny(next.name, CHEAP)) {
                    i17++;
                }
                if (z2) {
                    d7 += next.pct;
                }
                if ("высокий".equals(next.risk)) {
                    d8 += next.pct;
                } else if (str7.equals(next.risk)) {
                    d9 += next.pct;
                }
                if (!containsAny(next.name, TEXTILE)) {
                    if (containsAny(next.name, NATURAL_MATERIAL)) {
                        i8++;
                        d3 += next.pct;
                    }
                    if (containsAny(next.name, SYNTH_MATERIAL)) {
                        i9++;
                        d4 += next.pct;
                    }
                    if (containsAny(next.name, ELASTIC_MATERIAL)) {
                        i12++;
                        d5 += next.pct;
                    }
                    if (containsAny(next.name, WOOL_MATERIAL)) {
                        i11++;
                        d6 += next.pct;
                    }
                }
                arrayList2 = arrayList5;
                arrayList3 = arrayList;
                i4 = i20;
                i7 = i19;
                strDetectCategory = str10;
                it = it2;
            }
            i6 = i18;
            if (z2) {
                i19++;
            }
            if (z2) {
                arrayList5.add(stripPercent(next.name));
            }
            if ("высокий".equals(next.risk)) {
                arrayList = arrayList4;
                arrayList.add(stripPercent(next.name));
            } else {
                arrayList = arrayList4;
                arrayList.add(stripPercent(next.name));
            }
            if (next.role.contains("актив")) {
                i14++;
            }
            if (next.role.contains("увлажнение")) {
                i13++;
            }
            if (next.role.contains("смягчение")) {
                i15++;
            }
            if (containsAny(next.name, EXPENSIVE)) {
                i16++;
            }
            if (containsAny(next.name, CHEAP)) {
                i17++;
            }
            if (z2) {
                d7 += next.pct;
            }
            if ("высокий".equals(next.risk)) {
                d8 += next.pct;
            } else if (str7.equals(next.risk)) {
                d9 += next.pct;
            }
            if (!containsAny(next.name, TEXTILE)) {
                if (containsAny(next.name, NATURAL_MATERIAL)) {
                    i8++;
                    d3 += next.pct;
                }
                if (containsAny(next.name, SYNTH_MATERIAL)) {
                    i9++;
                    d4 += next.pct;
                }
                if (containsAny(next.name, ELASTIC_MATERIAL)) {
                    i12++;
                    d5 += next.pct;
                }
                if (containsAny(next.name, WOOL_MATERIAL)) {
                    i11++;
                    d6 += next.pct;
                }
            }
            arrayList2 = arrayList5;
            arrayList3 = arrayList;
            i4 = i20;
            i7 = i19;
            strDetectCategory = str10;
            it = it2;
        }
        double d10 = d3 + d4 + d5 + d6;
        String str11 = "высокий";
        int i21 = i8 + i9 + i12 + i11;
        if (d10 <= 0.0d && i21 > 0) {
            double d11 = 100.0d / ((double) i21);
            d3 = ((double) i8) * d11;
            d4 = ((double) i9) * d11;
            d5 = ((double) i12) * d11;
            d6 = ((double) i11) * d11;
        }
        boolean z3 = d10 > 0.0d || d7 > 0.0d || d8 > 0.0d || d9 > 0.0d;
        double d12 = d8 + d9;
        if (!z3 && size > 0) {
            double d13 = size;
            d7 = (((double) i7) * 100.0d) / d13;
            d8 = (((double) i10) * 100.0d) / d13;
            d9 = (((double) i6) * 100.0d) / d13;
            d12 = d8 + d9;
        }
        double d14 = d7;
        if ("material".equals(strDetectCategory)) {
            double d15 = d5 * 0.1d;
            z = z3;
            d2 = d12;
            iClamp4 = clamp(((((int) Math.min(30.0d, d3 * 0.3d)) + 55) + ((int) Math.min(10.0d, d15))) - ((int) Math.min(20.0d, 0.12d * d4)));
            iClamp = clamp(((((100 - (i10 * 20)) - (i6 * 5)) - i5) - ((int) Math.min(25.0d, 0.25d * d8))) - ((int) Math.min(15.0d, d9 * 0.1d)));
            iClamp3 = clamp(((((int) Math.min(15.0d, d4 * 0.1d)) + 85) - ((int) Math.min(35.0d, 0.2d * d6))) - ((int) Math.min(10.0d, 0.05d * d3)));
            iClamp2 = clamp((((((int) Math.min(25.0d, 0.25d * d3)) + 55) + ((int) Math.min(10.0d, d15))) - ((int) Math.min(25.0d, 0.18d * d4))) - ((int) Math.min(10.0d, d6 * 0.1d)));
        } else {
            z = z3;
            d2 = d12;
            if ("cosmetics".equals(strDetectCategory)) {
                int i22 = i13 * 3;
                int iClamp5 = clamp((((((Math.min(35, i14 * 8) + 45) + Math.min(20, i22)) + Math.min(10, i15 * 2)) + ((int) Math.min(15.0d, d14 * 0.2d))) - Math.min(20, i5)) - ((int) Math.min(20.0d, d2 * 0.1d)));
                int i23 = i6 * 5;
                int iClamp6 = clamp(((((100 - (i10 * 25)) - i23) - Math.min(20, i5)) - ((int) Math.min(35.0d, 0.35d * d8))) - ((int) Math.min(20.0d, 0.15d * d9)));
                int iClamp7 = clamp((100 - (i16 * 12)) + Math.min(10, i17 * 2));
                int iClamp8 = clamp((((((Math.min(25, i22) + 55) + Math.min(20, i15 * 3)) - i23) - (i10 * 8)) - ((int) Math.min(20.0d, d9 * 0.1d))) - ((int) Math.min(25.0d, 0.2d * d8)));
                iClamp4 = iClamp5;
                iClamp3 = iClamp7;
                iClamp2 = iClamp8;
                iClamp = iClamp6;
            } else {
                int iClamp9 = clamp(((int) Math.min(15.0d, d14 * 0.15d)) + 35);
                iClamp = clamp(((75 - (i10 * 20)) - (i6 * 5)) - ((int) Math.min(20.0d, d2 * 0.15d)));
                iClamp2 = 40;
                iClamp3 = 50;
                iClamp4 = iClamp9;
            }
        }
        int iPriceCheap = priceCheap(d, str4);
        if (d > 0.0d) {
            iClamp3 = clamp((int) Math.round((((double) iClamp3) * 0.35d) + (((double) iPriceCheap) * 0.65d)));
        }
        if (size == 0) {
            i2 = i;
            i3 = 0;
        } else {
            i2 = i;
            i3 = (i2 * 100) / size;
        }
        int iClamp10 = clamp((((iClamp4 + iClamp) + iClamp3) + iClamp2) / 4);
        String str12 = "text".equals(str3) ? "вставленный текст" : ("tab".equals(str3) || "api".equals(str3)) ? "вкладка «Состав»" : "страница";
        String str13 = strDetectCategory;
        String strOptString = jSONObject.optString("title", "Проверка состава");
        if (strOptString.length() > 120) {
            strOptString = strOptString.substring(0, 120) + "…";
        }
        this.lastOverall = iClamp10;
        this.lastUseful = iClamp4;
        this.lastSafe = iClamp;
        this.lastCheap = iClamp3;
        this.lastComfort = iClamp2;
        this.lastRiskNames = joinRuNames(arrayList3);
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html><html><head><meta charset='utf-8'><meta name='viewport' content='width=device-width, initial-scale=1'><style>body{font-family:sans-serif;margin:12px;background:#f7f8fb;color:#111}h2{font-size:18px;margin:6px 0 10px}h3{font-size:15px;margin:0 0 8px}.muted{color:#667085;font-size:13px}.cards{display:flex;flex-wrap:wrap;gap:8px;margin:10px 0}.card{background:#fff;border-radius:14px;padding:10px 12px;box-shadow:0 1px 4px rgba(0,0,0,.08);min-width:104px;flex:1 1 104px}.score{font-size:20px;font-weight:700;margin-top:2px}.bar{height:6px;background:#e5e7eb;border-radius:999px;margin-top:6px;overflow:hidden}.fill{height:100%;border-radius:999px}.good{color:#1a7f37}.warn{color:#b8860b}.bad{color:#b42318}.fill.good{background:#1a7f37}.fill.warn{background:#f0b429}.fill.bad{background:#b42318}.hint{background:#fff;border-left:4px solid #2f80ed;border-radius:12px;padding:10px 12px;margin:10px 0;box-shadow:0 1px 4px rgba(0,0,0,.06)}.desc p{margin:0 0 8px}section{background:#fff;border-radius:16px;padding:12px;margin:12px 0;box-shadow:0 1px 4px rgba(0,0,0,.06)}details{border-top:1px solid #eee;padding:8px 0}summary{font-weight:600;cursor:pointer}.tablewrap{max-height:62vh;overflow:auto;border-radius:12px;box-shadow:0 1px 4px rgba(0,0,0,.06);background:#fff}table{width:100%;border-collapse:collapse}th,td{padding:8px;border-bottom:1px solid #eee;text-align:left;vertical-align:top;font-size:14px}th{background:#eef2f7;font-size:12px;color:#344054;position:sticky;top:0;z-index:1}tr.bad{background:#fff5f5}tr.mid{background:#fffaf0}.ok{color:#1a7f37}.mid{color:#b8860b}.bad{color:#b42318}.wait{background:#fff;border-radius:14px;padding:12px;margin:10px 0;box-shadow:0 1px 4px rgba(0,0,0,.06);color:#667085;font-size:14px} .small{font-size:12px} .flag{background:#fff;border-radius:14px;padding:10px 12px;margin:8px 0;box-shadow:0 1px 4px rgba(0,0,0,.07);border-left:4px solid #9aa4b2} .flag.bad{border-left-color:#b42318}.flag.warn{border-left-color:#f0b429}.flag.info{border-left-color:#2f80ed} .flagtop{display:flex;justify-content:space-between;align-items:baseline;gap:8px} .flagname{font-weight:700;font-size:15px} .flagrisk{font-size:12px;color:#667085;white-space:nowrap} .flagmeta{color:#344054;font-size:13px;margin-top:2px} .flaginci{color:#98a2b3;font-size:11px;margin-top:2px} .flaghelp{margin-top:6px;font-size:13px;color:#182230} .lawlist{margin:6px 0 0 18px;padding:0}.lawlist li{margin:3px 0;font-size:13px}.catrow{display:flex;flex-wrap:wrap;gap:6px;margin:10px 0 4px} .chip{display:inline-flex;align-items:center;gap:6px;background:#fff;border:1px solid #e6e9ef;border-radius:999px;padding:6px 12px;font-size:13px;font-weight:600;box-shadow:0 1px 3px rgba(16,24,40,.06)} .chip .em{font-size:15px;line-height:1} .chip.kids{background:#fff7ed;border-color:#fdba74;color:#9a3412} .chip.soft{font-weight:500;color:#475467;background:#f2f4f7;border-style:dashed} body{background:linear-gradient(180deg,#e7eefb 0,#f7f8fb 280px) no-repeat;min-height:100vh} h2{background:linear-gradient(135deg,#1d4ed8,#0e9aa7);color:#fff;padding:12px 14px;border-radius:14px;box-shadow:0 2px 10px rgba(29,78,216,.24);margin:0 0 8px;line-height:1.3} .muted{color:#5b6472} .card{border:1px solid #eef1f6;transition:transform .12s} .bar{height:8px} .flag,.hint,section,.wait{border:1px solid #eef1f6} section>h3,.hint>h3{display:flex;align-items:center;gap:7px} .catnote{color:#475467;font-size:13px;margin:6px 0 0}</style><script>function scoreClass(v){v=Number(v)||0;return v>=75?'good':v>=50?'warn':'bad';}function setScore(id,v){var e=document.getElementById(id);if(!e)return;v=Math.max(0,Math.min(100,Math.round(Number(v)||0)));e.textContent=String(v);e.className='score '+scoreClass(v);var f=e.parentNode.querySelector('.fill');if(f){f.style.width=v+'%';f.className='fill '+scoreClass(v);}}function escHtml(s){return String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/\"/g,'&quot;');}function reveal(){var w=document.getElementById('wait');if(w&&w.parentNode)w.parentNode.removeChild(w);}</script></head><body><h2>");
        sb.append(esc(strOptString));
        sb.append("</h2><div class='muted'>Источник: ");
        sb.append(esc(str12));
        sb.append(" (");
        sb.append(esc(str2));
        sb.append(") · распознано ");
        sb.append(i2);
        sb.append(" из ");
        sb.append(size);
        sb.append(" · точность ");
        sb.append(i3);
        sb.append("%</div>");
        catChips(sb, cat);
        sb.append("<div class='cards'>");
        card(sb, "useful", "Полезно", iClamp4);
        card(sb, "safe", "Безопасно", iClamp);
        card(sb, "cheap", "Дёшево", iClamp3);
        card(sb, "comfort", "Комфорт", iClamp2);
        card(sb, "overall", "Итог", iClamp10);
        card(sb, "", "Цена", str5.isEmpty() ? "—" : str5, d > 0.0d ? priceCheap(d, str4) : 0);
        boolean z4 = z;
        double[] dShare = sharesOf(list);
        boolean zWeighted = dShare[5] > 0.0d;
        card(sb, "", "Доля пользы", percentShareLabel(dShare[0], zWeighted), (int) Math.round(dShare[0]));
        double d16 = dShare[1];
        card(sb, "", "Доля риска", percentShareLabel(dShare[1], zWeighted), (int) Math.round(dShare[1]));
        sb.append("</div>");
        StringBuilder sb2 = new StringBuilder();
        String[] meta = catMeta(str13);
        /* У обуви и мебели состав разложен по деталям — без этой строки
           «шерсть» из поля «Состав» выглядит как полная чепуха. */
        if (!this.lastAttrs.isEmpty()) {
            sb2.append("<p><b>Материалы по частям:</b> ").append(esc(this.lastAttrs)).append("</p>");
        }
        /* Вместо прежнего «это косметический состав» на любой товар — что именно
           разбираем и почему решили, что товар относится к этой категории. */
        sb2.append("<p>").append(esc(meta[2])).append("</p>");
        if ("1".equals(cat[1])) {
            sb2.append("<p>").append(esc(kidsNote())).append("</p>");
        }
        if ("low".equals(cat[3]) || "низкая".equals(cat[3])) {
            sb2.append("<p class='muted'>Категория определена неуверенно — сверьтесь с карточкой.</p>");
        }
        sb2.append("<p>Распознано ");
        sb2.append(i2);
        sb2.append(" из ");
        sb2.append(size);
        sb2.append(" компонентов.</p>");
        /* Состав мог дойти только из карточки площадки, когда сама страница
           товара не открылась: предупреждаем, а не молчим — карточка бывает и по
           снятой с продажи позиции. */
        String strCardName = nz(jSONObject.optString("cardName", ""));
        if (!strCardName.isEmpty() && ("tab".equals(str3) || "api".equals(str3))
                && cardOverlap(strOptString, strCardName) == 0) {
            Log.i("ScanBase", "название карточки не совпало со страницей: карточка «" + trim(strCardName, 50)
                    + "», страница «" + trim(strOptString, 50) + "»");
            sb2.append("<p class='warn'><b>Состав взят из карточки площадки, а не со страницы:</b> на открытой странице этого товара нет — карточка может относиться к позиции, снятой с продажи. Сверьте название заголовка с тем, что вы открывали.</p>");
        }
        if (!arrayList2.isEmpty()) {
            sb2.append("<p>Полезные или нейтральные: ");
            sb2.append(esc(joinRuNames(arrayList2)));
            sb2.append(".</p>");
        }
        if (arrayList3.isEmpty()) {
            sb2.append("<p>Явных вредных компонентов не найдено.</p>");
        } else {
            sb2.append("<p>Спорные или ограниченные: ");
            sb2.append(esc(joinRuNames(arrayList3)));
            sb2.append(".</p>");
        }
        if (d > 0.0d) {
            sb2.append("<p>Цена ");
            sb2.append(esc(str5));
            sb2.append(" — ");
            String strPriceWord = priceWord(iPriceCheap);
            this.lastPriceWord = strPriceWord;
            sb2.append(strPriceWord);
            sb2.append(".</p>");
        }
        String strTrim = trim(this.lastRiskNames, 120);
        this.lastVerdict = pickVariant(verdictVariants(i, list.size(), strTrim.isEmpty() ? 0 : strTrim.split(",").length, strTrim, this.lastOverall, this.lastPriceWord), this.lastKey);
        Iterator<Item> it3 = list.iterator();
        int i24 = 0;
        while (it3.hasNext()) {
            if ("не найден".equals(it3.next().status)) {
                i24++;
            }
        }
        sb.append("<div class='hint desc'><h3>Вердикт и разбор</h3><div id='descText'>");
        sb.append((CharSequence) sb2);
        sb.append("</div><div id='aiVerdict'><b>Вердикт:</b> ");
        sb.append(esc(this.lastVerdict));
        sb.append("</div>");
        if (i24 > 0) {
            sb.append("<div id='aiLine' class='muted'>Ещё ");
            sb.append(i24);
            sb.append(" названий нет в базе — по ним разбор ограничен, вердикт считается по распознанным.</div>");
        }
        sb.append("</div>");
        sb.append("<section><h3>Вредные и спорные</h3>");
        boolean z5 = false;
        for (Item item : list) {
            if (isRiskyItem(item)) {
                String str14 = str11;
                if (str14.equals(item.risk) || "превышение".equals(item.status)) {
                    str8 = str7;
                    str9 = "bad";
                } else {
                    str8 = str7;
                    str9 = str8.equals(item.risk) ? "warn" : "info";
                }
                sb.append("<div class='flag ");
                sb.append(str9);
                sb.append("'><div class='flagtop'>");
                sb.append("<span class='flagname'>");
                sb.append(esc(ruName(stripPercent(item.name))));
                sb.append("</span>");
                sb.append("<span class='flagrisk'>");
                sb.append(esc(riskWord(item)));
                sb.append("</span></div>");
                sb.append("<div class='flagmeta'>");
                sb.append(esc(item.role));
                String strPercentLabel = percentLabel(item);
                if (!strPercentLabel.isEmpty() && !"—".equals(strPercentLabel)) {
                    sb.append(" · ");
                    sb.append(esc(strPercentLabel));
                }
                if (!item.law.isEmpty()) {
                    sb.append(" · ");
                    sb.append(esc(lawPlain(item.law.get(0))));
                }
                sb.append("</div>");
                if (!ruName(stripPercent(item.name)).equals(stripPercent(item.name))) {
                    sb.append("<div class='flaginci'>");
                    sb.append(esc(stripPercent(item.name)));
                    sb.append("</div>");
                }
                sb.append("<div class='flaghelp'>");
                sb.append(esc(helpFor(item)));
                sb.append("</div></div>");
                str11 = str14;
                str7 = str8;
                z5 = true;
            }
        }
        String str15 = str7;
        String str16 = str11;
        if (!z5) {
            sb.append("<div class='muted'>Явных вредных или ограниченных компонентов не найдено.</div>");
        }
        sb.append("</section>");
        sb.append("<section><h3>Весь состав</h3><div class='tablewrap'><table><thead><tr>");
        sb.append("<th>Ингредиент</th><th>%</th><th>Что делает</th><th>Статус</th><th>Риск</th>");
        sb.append("</tr></thead><tbody>");
        for (Item item2 : list) {
            String str17 = (str16.equals(item2.risk) || "превышение".equals(item2.status)) ? "bad" : (str15.equals(item2.risk) || "с ограничением".equals(item2.status)) ? "mid" : "";
            sb.append("<tr class='");
            sb.append(str17);
            sb.append("'><td>");
            String strRuName = ruName(stripPercent(item2.name));
            sb.append(esc(strRuName));
            if (!strRuName.equals(stripPercent(item2.name))) {
                sb.append("<div class='muted small'>");
                sb.append(esc(stripPercent(item2.name)));
                sb.append("</div>");
            }
            sb.append("</td><td>");
            sb.append(esc(percentLabel(item2)));
            sb.append("</td><td>");
            sb.append(esc(item2.role));
            sb.append("</td><td class='");
            sb.append(statusClass(item2));
            sb.append("'>");
            sb.append(esc(statusPlain(item2.status)));
            if (!item2.note.isEmpty()) {
                sb.append("<div class='muted'>");
                sb.append(esc(item2.note));
                sb.append("</div>");
            }
            if (!item2.law.isEmpty()) {
                sb.append("<div class='muted'>");
                sb.append(esc(lawPlain(item2.law.get(0))));
                sb.append("</div>");
            }
            sb.append("</td><td class='");
            sb.append(riskClass(item2.risk));
            sb.append("'>");
            sb.append(esc(item2.risk));
            sb.append("</td></tr>");
        }
        sb.append("</tbody></table></div>");
        sb.append(lawLegend(list));
        sb.append("</section>");
        sb.append("<details><summary>Исходный состав</summary>");
        sb.append(esc(str.replaceAll("(?iu)^\\s*(состав|ingredients)\\s*[:\\-]?\\s*", "")));
        sb.append("</details>");
        sb.append("<script>(function(){var w=document.getElementById('wait');if(w&&w.parentNode)w.parentNode.removeChild(w);})();</script></body></html>");
        return sb.toString();
    }

    private void buildTabs(LinearLayout linearLayout) {
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(0);
        Button button = new Button(this);
        button.setText("Разбор");
        button.setAllCaps(false);
        button.setTextSize(13.0f);
        button.setOnClickListener(new MainActivity$$ExternalSyntheticLambda10(this));
        this.tabRead = button;
        linearLayout2.addView(button, tabSlot());
        Button button2 = new Button(this);
        button2.setText("Отзыв ИИ");
        button2.setAllCaps(false);
        button2.setTextSize(13.0f);
        button2.setOnClickListener(new MainActivity$$ExternalSyntheticLambda11(this));
        this.tabAi = button2;
        linearLayout2.addView(button2, tabSlot());
        linearLayout.addView(linearLayout2, match());
    }

    /* Плашка категории над оценками. Показываем не только саму категорию, но и
       уверенность с причиной: если категория определена неверно, пользователь
       видит это сразу и не верит разбору на слово. */
    private static void catChips(StringBuilder sb, String[] cat) {
        String[] meta = catMeta(cat[0]);
        sb.append("<div class='catrow'><span class='chip'><span class='em'>").append(meta[0]);
        sb.append("</span>").append(esc(meta[1])).append("</span>");
        if ("1".equals(cat[1])) {
            sb.append("<span class='chip kids'><span class='em'>🧸</span>детское</span>");
        }
        sb.append("</div>");
    }

    private static void card(StringBuilder sb, String str, int i) {
        card(sb, "", str, String.valueOf(i), i);
    }

    private static void card(StringBuilder sb, String str, String str2, int i) {
        card(sb, str, str2, String.valueOf(i), i);
    }

    private static void card(StringBuilder sb, String str, String str2, String str3, int i) {
        int iClamp = clamp(i);
        sb.append("<div class='card'><div class='muted'>");
        sb.append(esc(str2));
        sb.append("</div><div class='score ");
        sb.append(scoreClass(i));
        sb.append("'");
        if (!str.isEmpty()) {
            sb.append(" id='");
            sb.append(esc(str));
            sb.append("'");
        }
        sb.append(">");
        sb.append(esc(str3));
        sb.append("</div><div class='bar'><div class='fill ");
        sb.append(scoreClass(i));
        sb.append("' style='width:");
        sb.append(iClamp);
        sb.append("%'></div></div></div>");
    }

    private static int clamp(int i) {
        return Math.max(0, Math.min(100, i));
    }

    static void classify(Item item) {
        String lowerCase = item.name.toLowerCase(Locale.ROOT);
        if (containsAny(lowerCase, HIGH_RISK)) {
            item.role = "консервант";
            item.risk = "высокий";
        } else if (containsAny(lowerCase, TEXTILE)) {
            item.role = materialRole(lowerCase);
            item.status = "материал";
            item.risk = materialRisk(lowerCase);
        } else if (containsAny(lowerCase, ACTIVE)) {
            item.role = "актив";
            item.risk = "низкий";
        } else if (containsAny(lowerCase, HUMECTANT)) {
            item.role = "увлажнение";
            item.risk = "низкий";
        } else if (containsAny(lowerCase, EMOLLIENT)) {
            item.role = "смягчение";
            item.risk = "низкий";
        } else if (containsAny(lowerCase, SURFACTANT)) {
            item.role = "ПАВ";
            item.risk = "средний";
        } else if (containsAny(lowerCase, PRESERVATIVE)) {
            item.role = "консервант";
            item.risk = "низкий";
        } else if (containsAny(lowerCase, FRAGRANCE)) {
            item.role = "отдушка";
            item.risk = "средний";
        } else if (containsAny(lowerCase, COLORANT)) {
            item.role = "краситель";
            item.risk = "низкий";
        } else if (containsAny(lowerCase, SILICONE)) {
            item.role = "силикон";
            item.risk = "низкий";
        } else if (containsAny(lowerCase, POLYMER)) {
            item.role = "полимер";
            item.risk = "низкий";
        } else if (containsAny(lowerCase, SOLVENT)) {
            item.role = "растворитель";
            item.risk = "низкий";
        } else if (item.law.isEmpty() && !item.catalog && item.food == null && item.foodName == null && householdOf(lowerCase) != null) {
            /* Бытовая химия: роль и риск берём из своего словаря, а не из
               косметического — у ПАВ и щёлочи другие нормировки. */
            String[] strArrTech = householdOf(lowerCase);
            item.role = strArrTech[1];
            item.status = "бытовая химия";
            item.risk = strArrTech[2];
            item.note = strArrTech[3];
        } else if (item.law.isEmpty() && !item.catalog && item.food == null && item.foodName == null && materialOf(lowerCase) != null) {
            /* Только то, чего нет в косметической базе: иначе материал мог бы
               перекрыть регламентное ограничение на реальный ингредиент. */
            String[] strArrMaterial = materialOf(lowerCase);
            item.role = strArrMaterial[1];
            item.status = "материал";
            item.risk = strArrMaterial[2];
            item.note = strArrMaterial[3];
        } else if (!item.catalog && item.law.isEmpty() && item.food == null && item.foodName == null) {
            item.role = "не распознан";
            item.risk = "неизвестно";
        } else {
            item.role = "ингредиент";
            item.risk = item.law.isEmpty() ? "низкий" : "высокий";
        }
        /* Роль из авторитетной INCI-функции (Open Beauty Facts), но только там,
           где наш словарь не дал ничего конкретнее общего «ингредиент». Риск при
           этом не трогаем — он идёт из права и словарей риска. */
        if ("ингредиент".equals(item.role) && item.func != null && !item.func.isEmpty()) {
            String strFuncRole = roleFromFunc(item.func);
            if (strFuncRole != null) {
                item.role = strFuncRole;
            }
        }
        String[] strArr = HIGH_RISK;
        if (containsAny(lowerCase, strArr)) {
            item.risk = "высокий";
        }
        if (containsAny(lowerCase, MED_RISK) && "низкий".equals(item.risk)) {
            item.risk = "средний";
        }
        if (!item.law.isEmpty()) {
            double dMaxConc = maxConc(item.law);
            if (isProhibited(item.law)) {
                /* Внесён в список запрещённого (Annex II / Прил 1): в косметике его
                   быть не должно — это не «разрешено в малой дозе», а тревога. */
                item.status = "с ограничением";
                item.risk = "высокий";
            } else if (dMaxConc <= 0.0d || item.pct <= dMaxConc + 1.0E-4d) {
                item.status = "с ограничением";
                item.risk = containsAny(lowerCase, strArr) ? "высокий" : "средний";
            } else {
                item.status = "превышение";
                item.risk = "высокий";
            }
        }
        /* Гармонизированная классификация CLP (Annex VI) по CAS — это пол риска:
           вещество-канцероген не может быть «низким» только потому, что в косметике
           оно разрешено в дозе. Понижать уже выставленный риск не будем. */
        if (item.hazard != null && !item.hazard.isEmpty()) {
            String strClp = clpRisk(item.hazard);
            if (strClp != null) {
                item.risk = worseRisk(item.risk, strClp);
            }
            item.note = (item.note == null || item.note.isEmpty() ? "" : item.note + " · ")
                    + "классификация CLP: " + item.hazard;
        }
    }

    /* Класс опасности CLP → уровень потребительского риска. Только вредность для
       здоровья; физические (горючесть, взрыв) и экоклассы риск не двигают. */
    static String clpRisk(String hazard) {
        if (hazard == null || hazard.isEmpty()) {
            return null;
        }
        String[] high = {"Carc. 1A", "Carc. 1B", "Muta. 1A", "Muta. 1B", "Repr. 1A", "Repr. 1B",
            "Asp. Tox. 1", "Acute Tox. 1", "Acute Tox. 2", "Acute Tox. 3", "STOT RE 1", "Skin Corr. 1A"};
        String[] med = {"Carc. 2", "Muta. 2", "Repr. 2", "Acute Tox. 4", "Skin Sens.", "Resp. Sens.",
            "Eye Dam. 1", "Eye Irrit. 2", "Skin Irrit. 2", "Skin Corr. 1B", "Skin Corr. 1C",
            "STOT SE 3", "STOT RE 2"};
        for (String s : high) {
            if (hazard.contains(s)) {
                return "высокий";
            }
        }
        for (String s : med) {
            if (hazard.contains(s)) {
                return "средний";
            }
        }
        return null;
    }

    static String worseRisk(String a, String b) {
        return rank(a) >= rank(b) ? a : b;
    }

    private static int rank(String risk) {
        if ("высокий".equals(risk)) return 3;
        if ("средний".equals(risk)) return 2;
        if ("низкий".equals(risk)) return 1;
        return 0;
    }

    private static boolean isProhibited(List<String[]> law) {
        for (String[] e : law) {
            String a = nz(e[1]);
            if (a.contains("запрещ") || a.equals("Ann II") || a.equals("Прил 1")) {
                return true;
            }
        }
        return false;
    }

    /* INCI-функция (Open Beauty Facts) → роль по-русски. Порядок важнее алфавита:
       берём самую содержательную функцию, а не первую в списке. Совпадение — по
       точному токену, чтобы «foam-boosting» не спутать с «foaming». */
    private static final String[][] FUNC_ROLE = {
        {"preservative", "консервант"}, {"antimicrobial", "консервант"},
        {"hair-dyeing", "краситель"}, {"cosmetic-colorant", "краситель"}, {"colourant", "краситель"},
        {"sunscreen", "УФ-фильтр"}, {"uv-absorber", "УФ-фильтр"}, {"uv-filter", "УФ-фильтр"},
        {"fragrance", "отдушка"}, {"perfuming", "отдушка"},
        {"surfactant", "ПАВ"}, {"cleansing", "ПАВ"}, {"foam-boosting", "ПАВ"}, {"foaming", "ПАВ"},
        {"emulsifying", "эмульгатор"}, {"emulsifier", "эмульгатор"}, {"emulsion-stabilising", "эмульгатор"},
        {"viscosity-controlling", "загуститель"}, {"thickening", "загуститель"},
        {"humectant", "увлажнение"}, {"emollient", "смягчение"}, {"skin-protecting", "защита кожи"},
        {"skin-conditioning", "уход за кожей"}, {"hair-conditioning", "уход за волосами"},
        {"film-forming", "плёнкообразователь"}, {"antioxidant", "антиоксидант"},
        {"buffering", "регулятор pH"}, {"solvent", "растворитель"}, {"denaturant", "денатурат"},
        {"abrasive", "абразив"}, {"bleaching", "отбеливатель"}, {"astringent", "вяжущее"},
        {"antistatic", "антистатик"}, {"deodorant", "дезодорант"}, {"hair-fixing", "стилинг"},
        {"opacifying", "мутнитель"}, {"propellant", "пропеллент"},
        {"chelating", "хелатор"}, {"sequestering", "хелатор"}, {"masking", "маскирующий"},
        {"antidandruff", "против перхоти"}, {"antiseborrhoeic", "против себореи"},
        {"antiseborrheic", "против себореи"}, {"hair-bleaching", "осветление волос"},
        {"hair-perming", "завивка"}, {"oxidising", "окислитель"}, {"oxidizing", "окислитель"},
        {"reducing", "восстановитель"}, {"depilatory", "депилятор"}, {"oral-care", "полость рта"},
        {"nourishing", "питание"}, {"soothing", "успокоение"}, {"tonic", "тоник"},
        {"hydrotrope", "гидротроп"}, {"binding", "связующее"}, {"bulking", "наполнитель"},
        {"plasticiser", "пластификатор"}, {"plasticizer", "пластификатор"},
        {"absorbent", "абсорбент"}, {"nail-conditioning", "уход за ногтями"},
        {"flavouring", "ароматизатор"}, {"flavoring", "ароматизатор"},
        {"anticaking", "антислеживатель"}, {"stabilising", "стабилизатор"}, {"stabilizing", "стабилизатор"},
        {"antiplaque", "против налёта"}, {"hair-waving-or-straightening", "завивка/выпрямление"},
        {"hair-waving", "завивка"}, {"hair-straightening", "выпрямление"},
        {"moisturising", "увлажнение"}, {"antifoaming", "пеногаситель"}, {"antifoam", "пеногаситель"},
        {"antiperspirant", "антиперспирант"}, {"smoothing", "разглаживание"}, {"refreshing", "освежение"},
        {"anticorrosive", "антикоррозийное"}, {"gel-forming", "гелеобразователь"}, {"gel forming", "гелеобразователь"},
        {"refatting", "жирообновление"}, {"keratolytic", "кератолитик"}, {"detangling", "распутывание волос"},
        {"pearlescent", "перламутр"}, {"opacifying", "мутнитель"}, {"tanning", "автозагар"},
    };

    static String roleFromFunc(String func) {
        if (func == null || func.isEmpty()) {
            return null;
        }
        String lower = func.toLowerCase(Locale.ROOT);
        for (String[] pair : FUNC_ROLE) {
            if (tokenHas(lower, pair[0])) {
                return pair[1];
            }
        }
        return null;
    }

    /* Токен-вхождение: функция лежит в строке «a , b , c» целым токеном. */
    private static boolean tokenHas(String lowerCsv, String token) {
        for (String part : lowerCsv.split(",")) {
            if (part.trim().equals(token)) {
                return true;
            }
        }
        return false;
    }

    private String cleanTitle(String str) {
        String strTrim = nz(str).replaceAll("(?iu)\\s*[-–—:,]*\\s*(купить|заказать|цена|стоимость)[^|]{0,60}$", "").replaceAll("\\s*\\(\\d{4,}\\)\\s*$", "").replaceAll("[\\s,;.:|-]+$", "").trim();
        if (strTrim.isEmpty()) {
            strTrim = nz(str).trim();
        }
        return trim(strTrim, 90);
    }

    /* Отказ обязан прятать прежний разбор: без этого под сообщением остаются
       оценки, таблица и отзыв ИИ про предыдущий товар, и их читают как про
       тот, что не открылся. */
    void clearReport() {
        this.lastItems = null;
        this.lastComposition = "";
        this.lastKey = "";
        this.lastAttrs = "";
        this.lastCatHint = "";
        this.lastDescription = "";
        this.lastRiskNames = "";
        this.lastOverall = 0;
        this.lastUseful = 0;
        this.lastSafe = 0;
        this.lastCheap = 0;
        this.lastComfort = 0;
        this.currentTab = 0;
        FrameLayout frameLayout = this.content;
        if (frameLayout != null) {
            frameLayout.setVisibility(0);
        }
        LinearLayout linearLayout = this.aiPanel;
        if (linearLayout != null) {
            linearLayout.setVisibility(8);
        }
        clearError();
    }

    private void clearError() {
        TextView textView = this.error;
        if (textView != null) {
            textView.setVisibility(8);
        }
    }

    private static boolean containsAny(String str, String[] strArr) {
        String lowerCase = nz(str).toLowerCase(Locale.ROOT);
        for (String str2 : strArr) {
            if (lowerCase.contains(str2)) {
                return true;
            }
        }
        return false;
    }

    /* Паттерн строится из неизменного TEXTILE, поэтому компилируется один раз.
       Прежний вид был таким:
           while (Pattern.compile(...).matcher(str).find()) { i++; }
       и это бесконечный цикл: каждая итерация создавала НОВЫЙ Matcher на том же
       входе, поэтому find() всякий раз возвращал первое же совпадение. Стоило
       слову из TEXTILE попасть в текст — и главный поток стоял насмерть
       («зависает при разборе»). Счётчик ведёт один матчер, потолок на всякий
       случай оставлен. */
    private static Pattern materialPattern;

    private static int countMaterialWords(String str) {
        String s = nz(str);
        if (s.isEmpty()) {
            return 0;
        }
        Pattern pattern = materialPattern;
        if (pattern == null) {
            StringBuilder sb = new StringBuilder();
            for (String str2 : TEXTILE) {
                if (sb.length() > 0) {
                    sb.append('|');
                }
                sb.append(Pattern.quote(str2));
            }
            pattern = Pattern.compile("(?<![\\p{L}])(" + sb + ")(?![\\p{L}])", 66);
            materialPattern = pattern;
        }
        int i = 0;
        Matcher matcher = pattern.matcher(s);
        while (matcher.find() && i < 500) {
            i++;
        }
        return i;
    }

    private String currentUrl() {
        String strExtractUrl = extractUrl(this.urlBox.getText().toString());
        return !strExtractUrl.isEmpty() ? strExtractUrl : this.lastUrl;
    }

    /* Отзыв считается целым по двум признакам: длина и число знаков конца
       предложения. Один только подсчёт знаков пропускал «воду» из одного
       предложения, а одна только длина — обрывки без точки. */
    static boolean reviewIsWhole(String str) {
        String strTrim = str == null ? "" : str.trim();
        if (strTrim.length() < 150) {
            return false;
        }
        int iEnds = 0;
        for (int i = 0; i < strTrim.length(); i++) {
            char c = strTrim.charAt(i);
            if (c == '.' || c == '!' || c == '?') {
                iEnds++;
            }
        }
        return iEnds >= 3;
    }

    private static String cutToSentence(String str) {
        String strTrim = str.trim();
        int iMax = Math.max(Math.max(strTrim.lastIndexOf(46), strTrim.lastIndexOf(33)), strTrim.lastIndexOf(63));
        if (iMax < 40 || iMax >= strTrim.length() - 6) {
            return strTrim;
        }
        return strTrim.substring(0, iMax + 1) + " …";
    }

    /* ── Категории разбора ───────────────────────────────────────────────────
       Прежде было три значения («материал», «косметика», «неизвестно»), и печенье
       получало тот же рассказ, что и крем: «это косметический состав». Теперь
       семь категорий плюс признак «детское». Детское — не категория, а усилитель
       строгости: детский крем остаётся косметикой, но отдушки и консерванты в нём
       считаем критичнее, чем во взрослом.

       Признак берём из трёх мест по убыванию доверия: категория площадки (её
       отдаёт сама карточка — subj_name у WB, хлебные крошки у Ozon и Маркета),
       затем сам состав (попадания в базу косметики, пищевые добавки E-xxxx,
       слова-материалы), затем название с описанием. Каждая находка добавляет вес,
       берём максимальный; при равенстве выигрывает тот, что выше в CAT_ORDER. */
    static final String[] CAT_ORDER = {"cosmetics", "food", "clothes", "household", "pharma", "electronics", "other"};

    static final String[] CAT_KEYS_COSMETICS = {"космет", "крем", "шампун", "бальзам для волос", "гель для душа", "мыло",
            "сыворотк", "маска для", "лосьон", "дезодорант", "зубная паст", "зубной эликсир", "ополаскиватель для полости",
            "помад", "бальзам для губ", "тушь", "подводк", "лак для ногтей", "парфюм", "духи", "туалетная вода",
            "умыван", "мицелляр", "скраб", "тоник", "пенка для", "мусс для", "гигиен", "интимн", "солнцезащит", "spf",
            "автозагар", "гидролат", "эссенц", "патчи", "бров", "ресниц", "бритья", "шугар", "воск для", "маникюр",
            "педикюр", "лак для", "крем-гель", "молочко для", "тон для лица", "праймер", "консилер", "тональн"};

    static final String[] CAT_KEYS_FOOD = {"пищев", "продукт питан", "печенье", "пряник", "вафл", "шоколад", "конфет",
            "карамель", "ириск", "мармелад", "пастила", "зефир", "халва", "козинак", "напиток", "морс", "квас", "молоко",
            "кефир", "ряженк", "йогурт", "творог", "сыр", "масло сливочн", "спред", "колбас", "сосиск", "сардельк",
            "ветчин", "каша", "хлопья", "мюсли", "гранола", "мука", "макарон", "крупа", "булгур", "киноа", "варенье",
            "джем", "конфитур", "соус", "кетчуп", "майонез", "аджика", "горчица", "цикорий", "снек", "чипсы", "сухарик",
            "гренки", "батончик", "хлебц", "сухари", "орех", "сухофрукт", "урюк", "изюм", "консерв", "салат", "икра",
            "пельмен", "вареник", "блин", "сырок", "мороженое", "десерт", "торт", "пирожн", "булочн", "бакалея",
            "кондитерск", "лакомств", "перекус", "глазур", "какао", "безалкогольн", "хлебобулочн", "гречк", "творожн", "мясн",
            "рыбн", "птицы", "индейк", "куриц", "говяди", "свини", "фасол", "горох", "чечевич", "томатн", "огурц",
            "квашен", "мед ", "мёд", "сахар-песок", "сироп топинамбур", "арахисов", "паста тахин", "хумус"};

    static final String[] CAT_KEYS_CLOTHES = {"футболк", "поло", "рубашк", "блузк", "брюк", "джинс", "бембер", "платье",
            "платьев", "сарафан", "юбк", "кофт", "свитер", "водолазк", "толстовк", "худи", "лонгслив", "кардиган",
            "жилет", "куртк", "пальто", "плащ", "тренч", "пуховик", "комбинезон", "костюм", "пижам", "халат", "белье",
            "бельё", "трусы", "носк", "гольфы", "леггинс", "тайтс", "перчатк", "варежек", "шарф", "шапк", "обув",
            "кроссовк", "кед", "туфл", "ботинк", "сапог", "ботильон", "тапочк", "шлепанц", "сланц", "балетк", "лодочк",
            "мюли", "эспадрильи", "уги", "дутик", "сумк", "рюкзак", "шоппер", "клатч", "ремень", "кожан", "текстил",
            "постельн", "полотенц", "простын", "наволочк", "пододеяльник", "покрывал", "плед", "скатерть", "штор",
            "гардин", "махра", "трикотаж", "футер", "габардин", "жаккард", "костюмн", "термобель", "носки",
            "купальник", "плавк", "толстов", "дождевик", "накидк", "пончо", "шорты", "бридж", "капри", "галстук",
            "бабочк", "подтяжк", "бандан", "платок", "палантин", "экипировк", "форма спортивн"};

    static final String[] CAT_KEYS_HOUSEHOLD = {"средство для", "очистит", "очистк", "пятновыводит", "отбеливат",
            "пятновывод", "стиральн", "капсул для стирк", "кондиционер для белья", "освежитель", "чистящ", "антижир",
            "антинакипин", "унитаза", "раковин", "сантехник", "стёкол", "для пола", "для плиты", "для духовки",
            "посудомоечн", "посудомойк", "для посуды", "туалетн", "ванн", "душевой кабины", "удалит жир", "плесен",
            "размораживат", "антибактериальн", "дезинфек", "хозяйствен", "бытовая химия", "уборк", "мусора",
            "средство от", "универсальн очистк", "накип", "известков", "спрей для уборк", "полирол",
            "средство для стирк", "для стирк", "отбеливател", "кислородн пятновывод", "гель для унитаза", "освежитель воздуха",
            "антиплесен", "средство для сантехник", "очиститель духовок", "удалитель обоев", "средство для стекл"};

    static final String[] CAT_KEYS_PHARMA = {"бад", "биологически активн", "таблетк", "капсул", "драже", "шипуч",
            "витамин", "минеральн", "аминокислот", "дозировк", "дневная норма", "приём внутрь", "показани",
            "противопоказан", "побочн", "аптечн", "мазь", "наружного примен", "настойк", "сироп", "капли",
            "назальн", "антисептик", "перекись", "хлоргексидин", "мирамистин", "бинт", "шприц", "пластыр",
            "термометр", "тонометр", "глюкометр", "раствор для инфузий", "свечи ректал", "суспензия", "гранул", "аптек",
            "порошок для приготовления", "фиточай", "сбор аптечн", "лечебн", "медизз"};

    static final String[] CAT_KEYS_ELECTRONICS = {"смартфон", "телефон", "наушник", "гарнитур", "колонк", "зарядн",
            "сетевой адаптер", "кабель", "аккумулятор", "повербанк", "power bank", "внешний аккумулятор", "клавиатур",
            "мышь", "монитор", "ноутбук", "планшет", "компьютер", "телевизор", "роутер", "маршрутизатор", "смарт-час",
            "часы смарт", "фитнес-браслет", "трекер", "электрощётк", "триммер", "машинка для стрижк", "эпилятор",
            "фен", "стайлер", "выпрямител", "плойк", "микроволнов", "чайник", "кофеварк", "кофемолк", "блендер",
            "миксер", "мясорубк", "тостер", "гриль", "пароварк", "аэрогриль", "пылесос", "утюг", "отпаривател",
            "вентилятор", "обогревател", "кондиционер", "увлажнител", "очистител воздуха", "лампочк", "светильник",
            "люстр", "фонарик", "батарейк", "элемент питания", "карта памяти", "флешк", "жесткий диск", "ssd",
            "проектор", "квадрокоптер", "электросамокат", "веб-камер", "микрофон", "геймпад", "джойстик", "стабилизатор"};

    /* Товары, которым разбор по составу не положен в принципе: диван из
        жаккарда — не одежда, пусть в составе и ткань. Ярлык такого домена
        весит три — больше, чем догадка по составу (два), но меньше, чем
        прямой ярлык разбираемой категории (пять). */
    static final String[] CAT_KEYS_OTHER_DOMAIN = {"мебель", "кресло", "диван", "комод", "стул", "шкаф", "полк",
            "тумбочк", "посуда", "сковород", "кастрюл", "канцеляр", "папка", "степлер", "инструмент", "дрель",
            "перфоратор", "шуруповёрт", "гаечн", "автотовары", "автомобильн", "моторн", "книга", "книги", "книжк",
            "лоток", "корм", "животным", "когтеточк", "спорттовар", "турист", "палатк", "спальник", "удобрени",
            "игрушк", "пазл", "головоломк", "конструктор", "кукл", "лепк", "пластилин", "радиоуправляем",
            "набор для творчеств", "мягка игрушк", "антистресс", "фиджет", "сортир", "пирамидк", "стекломасс",
            "рассада", "лакокрасочн", "ламинат", "плитка", "обои", "герметик", "монтажн пен"};

    /* Не категория, а признак: «детский крем» — косметика, но разбираем строже. */
    static final String[] CAT_KEYS_KIDS = {"детск", "детям", "ребёнк", "ребенок", "малыш", "игрушк", "соск",
            "прорезыват", "пустышк", "бутылочк", "поильник", "подгузник", "присыпк", "коляск", "автолюльк", "кроватк",
            "манеж", "конструктор", "пазл", "головоломк", "кукол", "мягка игрушк", "лепк", "пластилин", "раскраск",
            "книжк для детей", "малышам", "с рождения", "0+", "новорождённ", "ясельн", "детская комната",
            "девочк", "мальчик", "для детей", "подростк", "школьник", "первоклассн"};

    private static final String[] COSMETIC_MARKS = {"aqua", "water", "вода", "glycerin", "глицерин", "parfum",
            "отдуш", "sodium laureth", "sodium lauryl", "cetearyl", "butyrospermum", "пантенол", "ниацинамид"};

    /* «кофе» и «чай» как подстроки ловят кофеварку и чайник, поэтому их ищем
       целым словом — тем же приёмом, что и слова-материалы в countMaterialWords. */
    static final String[] CAT_WORDS_FOOD = {"кофе", "чай", "мёд", "мед", "какао", "халва"};

    private static Pattern wordsPattern;
    private static String[] wordsSource;

    private static boolean containsAnyWord(String str, String[] strArr) {
        if (strArr == null || strArr.length == 0 || str == null || str.isEmpty()) {
            return false;
        }
        if (strArr != wordsSource) {
            StringBuilder sb = new StringBuilder();
            for (String str2 : strArr) {
                if (sb.length() > 0) {
                    sb.append('|');
                }
                sb.append(Pattern.quote(str2));
            }
            wordsPattern = Pattern.compile("(?<![\\p{L}\\p{N}])(" + sb + ")(?![\\p{L}])", 66);
            wordsSource = strArr;
        }
        return wordsPattern.matcher(str).find();
    }

    private static String[] catWords(String str) {
        return "food".equals(str) ? CAT_WORDS_FOOD : null;
    }

    private static String[] catKeys(String str) {
        switch (str) {
            case "cosmetics": return CAT_KEYS_COSMETICS;
            case "food": return CAT_KEYS_FOOD;
            case "clothes": return CAT_KEYS_CLOTHES;
            case "household": return CAT_KEYS_HOUSEHOLD;
            case "pharma": return CAT_KEYS_PHARMA;
            case "electronics": return CAT_KEYS_ELECTRONICS;
            default: return null;
        }
    }

    /* Э-шки ищем только как индекс добавки: «витамин Е 100 мг» — не номер E,
       поэтому единицу измерения отбрасываем сразу. */
    private static Pattern eNumberPattern;

    private static int countENumbers(String str) {
        String s = nz(str);
        if (s.isEmpty()) {
            return 0;
        }
        Pattern pattern = eNumberPattern;
        if (pattern == null) {
            pattern = Pattern.compile("(?i)(?<![\\p{L}\\p{N}])(?:e|е)[-–—\\s]{0,2}\\d{3}[\\p{L}]{0,2}(?!\\s*(?:мг|мкг|mg|mcg|iu|ме\\b))");
            eNumberPattern = pattern;
        }
        int i = 0;
        Matcher matcher = pattern.matcher(s);
        while (matcher.find() && i < 200) {
            i++;
        }
        return i;
    }

    /* Сколько категорий бытовой химии узналось в составе. Считаем не слова, а
       категории: гипохлорит и гидроксид натрия — два разных признака, а два
       синонима одной строки («едкий натр» и «каустическая сода») — один. */
    static int countHouseholdWords(String str) {
        int i = 0;
        for (String[] household : HOUSEHOLD) {
            if (containsAny(str, household[0].split("\\|"))) {
                i++;
            }
        }
        return i;
    }

    /* {ключ, «1» если детское, почему так решили, уверенность, вес} */
    static String[] categoryOf(String strComp, String strTitle, String strDesc, String strPlat,
                               int iMaterials, int iCatalog, int iFood, int iHousehold) {
        String lowerCase = nz(strPlat).toLowerCase(Locale.ROOT);
        String lowerCase2 = (nz(strTitle) + " " + nz(strDesc)).toLowerCase(Locale.ROOT);
        String lowerCase3 = nz(strComp).toLowerCase(Locale.ROOT);
        int i = CAT_ORDER.length;
        int[] iArr = new int[i];
        String[] strArrWhy = new String[i];
        for (int i2 = 0; i2 < i; i2++) {
            String[] catKeys = catKeys(CAT_ORDER[i2]);
            String[] catWords = catWords(CAT_ORDER[i2]);
            if (catKeys == null && catWords == null) {
                continue;
            }
            if (containsAny(lowerCase, catKeys) || containsAnyWord(lowerCase, catWords)) {
                /* Пять: собственный ярлык площадки перевешивает любые наши
                   догадки по составу (максимум 3 + 1). */
                iArr[i2] = iArr[i2] + 5;
                strArrWhy[i2] = "категория площадки";
            } else if (containsAny(lowerCase2, catKeys) || containsAnyWord(lowerCase2, catWords)) {
                iArr[i2] = iArr[i2] + 2;
                strArrWhy[i2] = "название и описание";
            }
        }
        int iCos = catIndex("cosmetics");
        int iFoodIdx = catIndex("food");
        int iClo = catIndex("clothes");
        int iHouse = catIndex("household");
        if (iCatalog >= 3) {
            iArr[iCos] = iArr[iCos] + 3;
            strArrWhy[iCos] = "состав совпал с базой косметики";
        } else if (iCatalog >= 1) {
            iArr[iCos] = iArr[iCos] + 2;
            strArrWhy[iCos] = "часть названий есть в базе косметики";
        }
        if (iFood >= 3) {
            iArr[iFoodIdx] = iArr[iFoodIdx] + 3;
            strArrWhy[iFoodIdx] = "в составе несколько пищевых продуктов и добавок из базы";
        } else if (iFood >= 1) {
            iArr[iFoodIdx] = iArr[iFoodIdx] + 2;
            strArrWhy[iFoodIdx] = "в составе есть пищевые продукты или добавки из базы";
        } else if (countENumbers(lowerCase3) >= 2) {
            iArr[iFoodIdx] = iArr[iFoodIdx] + 2;
            strArrWhy[iFoodIdx] = "в составе номера E";
        }
        if (iMaterials >= 3) {
            iArr[iClo] = iArr[iClo] + 3;
            strArrWhy[iClo] = "в составе перечислены волокна";
        } else if (iMaterials >= 1) {
            iArr[iClo] = iArr[iClo] + 2;
            strArrWhy[iClo] = "в составе есть волокно";
        }
        if (iHousehold >= 3) {
            iArr[iHouse] = iArr[iHouse] + 3;
            strArrWhy[iHouse] = "в составе несколько признаков бытовой химии";
        } else if (iHousehold >= 1) {
            iArr[iHouse] = iArr[iHouse] + 2;
            strArrWhy[iHouse] = "в составе есть признак бытовой химии";
        }
        /* Вода и глицерин есть и в средстве для посуды: пока в составе
           угадалась бытовая химия, эти маркеры косметикой не считаем. */
        if (iHousehold < 1 && containsAny(lowerCase3, COSMETIC_MARKS)) {
            iArr[iCos] = iArr[iCos] + 1;
            if (strArrWhy[iCos] == null) {
                strArrWhy[iCos] = "в составе вода, глицерин или отдушка";
            }
        }
        int iOther = catIndex("other");
        if (containsAny(lowerCase, CAT_KEYS_OTHER_DOMAIN) && iArr[iOther] < 3) {
            iArr[iOther] = 3;
            strArrWhy[iOther] = "товар не из разбираемых доменов";
        }
        int iBest = 0;
        int iBestIdx = i - 1;
        for (int i3 = 0; i3 < i; i3++) {
            if (iArr[i3] > iBest) {
                iBest = iArr[i3];
                iBestIdx = i3;
            }
        }
        String strKey = CAT_ORDER[iBestIdx];
        boolean zKids = containsAny(lowerCase + " " + lowerCase2, CAT_KEYS_KIDS);
        return new String[]{strKey, zKids ? "1" : "0",
                iBest == 0 ? "узнаваемых признаков нет" : nz(strArrWhy[iBestIdx]),
                iBest >= 5 ? "высокая" : iBest >= 2 ? "средняя" : "низкая",
                String.valueOf(iBest)};
    }

    private static int catIndex(String str) {
        for (int i = 0; i < CAT_ORDER.length; i++) {
            if (CAT_ORDER[i].equals(str)) {
                return i;
            }
        }
        return CAT_ORDER.length - 1;
    }

    /* {значок, название, что разбираем} */
    static String[] catMeta(String str) {
        if ("cosmetics".equals(str)) {
            return new String[]{"💄", "Косметика и гигиена", "Что разбираем: порядок ингредиентов (что в начале — того больше), консерванты, отдушки, поверхностно-активные вещества и позиции с ограничением по регламенту."};
        }
        if ("food".equals(str)) {
            return new String[]{"🍽", "Продукты питания", "Что разбираем: пищевые добавки с индексом E и их допустимую норму, а также сахар, соль и жиры — что стоит в начале списка, того в продукте больше всего."};
        }
        if ("clothes".equals(str)) {
            return new String[]{"👕", "Одежда и текстиль", "Что разбираем: состав волокна — натуральное дышит и приятнее к телу, синтетика носче и меньше мнётся; отдельно доля эластана и отделка: пропитки и плотные красители."};
        }
        if ("household".equals(str)) {
            return new String[]{"🧴", "Бытовая химия", "Что разбираем: поверхностно-активные вещества, щёлочи и кислоты, хлор, фосфаты и консерванты вроде метилизотиазолинона — они раздражают кожу и дыхательные пути."};
        }
        if ("pharma".equals(str)) {
            return new String[]{"💊", "БАД и аптека", "Что разбираем: действующие вещества и дозировки. Это не косметика: норму приёма и сочетаемость определяет инструкция и врач, а не оценка состава."};
        }
        if ("electronics".equals(str)) {
            return new String[]{"🔌", "Электроника и техника", "Что разбираем: список ингредиентов здесь неприменим — смотрим материалы корпуса и покрытий, а косметическую оценку не применяем."};
        }
        return new String[]{"📦", "Прочее", "Что разбираем: товар не отнёсся к известной группе, поэтому названия компонентов сверяем с базой без категорных правил."};
    }

    static String kidsNote() {
        return "Товар детский — требования строже: отдушки, красители, фталаты и бисфенол A считаем критичнее, чем во взрослом товаре.";
    }

    /* Что маленькая модель должна сказать первой фразой: «для чего это
       средство» у печенья и у кроссовок звучит одинаково неправильно. */
    static String catFirstHint(String str) {
        if ("cosmetics".equals(str)) {
            return "для чего это средство";
        }
        if ("food".equals(str)) {
            return "что это за продукт и кому он подойдёт";
        }
        if ("clothes".equals(str)) {
            return "что это за вещь и как она поведёт себя в носке";
        }
        if ("household".equals(str)) {
            return "для какой уборки это средство и с чем его нельзя смешивать";
        }
        if ("pharma".equals(str)) {
            return "для чего этот препарат или добавка и зачем обсудить приём с врачом";
        }
        if ("electronics".equals(str)) {
            return "для чего эта техника и на что смотреть в характеристиках";
        }
        return "что это за товар";
    }

    private int countByKind(boolean zCatalog) {
        List<Item> list = this.lastItems;
        if (list == null) {
            return 0;
        }
        int i = 0;
        for (Item item : list) {
            if (zCatalog ? item.catalog : (item.food != null || item.foodName != null)) {
                i++;
            }
        }
        return i;
    }

    /* Категория считается один раз на разбор и переиспользуется экраном и
       промптом ИИ: иначе на экране было «косметика», а модель получала
       «неизвестно», и пользователь видел расхождение в двух абзацах. */
    private String[] catInfo() {
        String[] strArr = this.lastCat;
        if (strArr != null) {
            return strArr;
        }
        String strLower = nz(this.lastComposition).toLowerCase(Locale.ROOT);
        String[] strArrCompute = categoryOf(this.lastComposition, this.lastTitle, this.lastDescription,
                this.lastCatHint, countMaterialWords(strLower), countByKind(true), countByKind(false),
                countHouseholdWords(strLower));
        this.lastCat = strArrCompute;
        return strArrCompute;
    }

    private void ensureNameIndex() {
        if (this.nameIdx != null || this.db == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        Cursor cursorRawQuery = this.db.rawQuery("SELECT name FROM cosmetic_ingredients", null);
        while (cursorRawQuery.moveToNext()) {
            arrayList.add(cursorRawQuery.getString(0));
        }
        cursorRawQuery.close();
        Cursor cursorRawQuery2 = this.db.rawQuery("SELECT DISTINCT name FROM inci_index", null);
        while (cursorRawQuery2.moveToNext()) {
            arrayList.add(cursorRawQuery2.getString(0));
        }
        cursorRawQuery2.close();
        /* Еда тоже должна попадать в индекс названий: иначе список компонентов,
           поданный через пробел («соль сахар мука»), не дробится — expandBySpace
           видит слова, которых нет в nameIdx, и оставляет кусок склеенным. lookup
           еду находит, а findDbName — нет; заводим еду в тот же индекс. */
        Cursor cursorFoodNames = this.db.rawQuery("SELECT DISTINCT name FROM food_ingredients", null);
        while (cursorFoodNames.moveToNext()) {
            arrayList.add(cursorFoodNames.getString(0));
        }
        cursorFoodNames.close();
        this.nameIdx = arrayList;
        this.nameIdxSet = new java.util.HashSet<>(arrayList.size() * 2);
        for (Object o : arrayList) this.nameIdxSet.add(Rules.norm((String) o));
        Log.i("ScanBase", "индекс названий: " + arrayList.size());
    }

    private static String esc(String str) {
        return nz(str).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private static String extractUrl(String str) {
        if (str == null) {
            return "";
        }
        Matcher matcher = Pattern.compile("(?i)https?://[^\\s<>\"]+").matcher(str.trim());
        if (matcher.find()) {
            return matcher.group();
        }
        Matcher matcher2 = Pattern.compile("(?iu)\\b(?:www\\.)?[\\w-]+\\.[a-z]{2,}(?:/[^\\s<>\"]*)?").matcher(str.trim());
        return (!matcher2.find() || str.trim().contains(",")) ? "" : matcher2.group();
    }

    /* Артикул вместо ссылки: плошадка по умолчанию — Wildberries, потому что словом
       «артикул» пользуются именно там. Площадку можно назвать словами. Строка с
       несколькими числами или с прочим текстом артикулом не считается: так текст
       состава («хлопок 80%, полиэстер 20%») не превращается в ссылку. */
    static String articleUrl(String str) {
        String s = nz(str).trim();
        if (s.isEmpty() || s.length() > 40 || s.contains("://")) {
            return "";
        }
        Matcher matcher = Pattern.compile("(?<!\\d)\\d{5,20}(?!\\d)").matcher(s);
        if (!matcher.find()) {
            return "";
        }
        String strNum = matcher.group();
        if (matcher.find()) {
            return "";
        }
        String lowerCase = s.replace(strNum, "").toLowerCase(Locale.ROOT).replaceAll("[^a-zа-яё]", "");
        if (lowerCase.isEmpty()) {
            /* До 12 цифр — артикул Wildberries, длиннее — id Яндекс Маркета:
               у WB артикулы семизначные, карточки Маркета длиннее 12 цифр. */
            return strNum.length() > 12 ? "https://market.yandex.ru/card/x/" + strNum
                    : "https://www.wildberries.ru/catalog/" + strNum + "/detail.aspx";
        }
        if (lowerCase.contains("ozon") || lowerCase.contains("озон")) {
            return "https://www.ozon.ru/product/" + strNum + "/";
        }
        if (lowerCase.contains("маркет") || lowerCase.contains("market") || lowerCase.contains("yandex") || lowerCase.contains("яндекс")) {
            /* /card/<id> без слага у Маркета 404, а /product/<id> редиректит на
               /card/model/0?productId=..., где Маркет рисует «такой страницы».
               Слаг в пути не проверяется: /card/x/<id> отдаёт живую карточку. */
            return "https://market.yandex.ru/card/x/" + strNum;
        }
        if (lowerCase.contains("wb") || lowerCase.contains("wildberries") || lowerCase.contains("вб")
                || lowerCase.contains("вайлдберриз") || lowerCase.contains("артикул") || lowerCase.contains("article")
                || lowerCase.contains("номер") || lowerCase.contains("арт") || lowerCase.equals("nm")
                || lowerCase.equals("id") || lowerCase.equals("sku")) {
            return "https://www.wildberries.ru/catalog/" + strNum + "/detail.aspx";
        }
        return "";
    }

    /* Мёртвые форматы ссылок Маркета: /product--<id> и /product/<id> рисуют «такой
       страницы». Живой формат — /card/<слаг>/<id>; слаг в пути не проверяется. */
    static String marketCard(String str) {
        String s = nz(str);
        if (!s.contains("market.yandex.")) {
            return s;
        }
        Matcher matcher = Pattern.compile("(?:product--|/product/)(\\d{5,20})(?!\\d)").matcher(s);
        if (!matcher.find()) {
            return s;
        }
        return "https://market.yandex.ru/card/x/" + matcher.group(1);
    }

    private static String few(String str, int i) {
        if (str == null || str.trim().isEmpty()) {
            return "ничего";
        }
        String[] strArrSplit = str.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < strArrSplit.length && i2 < i; i2++) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(strArrSplit[i2].trim());
        }
        return sb.toString();
    }

    private String findDbName(String str) {
        ensureNameIndex();
        List<String> list = this.nameIdx;
        if (list == null) {
            return null;
        }
        String strNorm = norm(str);
        if (strNorm.isEmpty()) {
            return null;
        }
        String strNorm2 = norm(translit(strNorm));
        /* Точное совпадение ищем ПЕРВЫМ проходом и на ЛЮБОЙ длине: короткое имя
           (aqua, bht) и имя, которое ещё и префикс десятков других (dimethicone),
           раньше терялись — цикл натыкался на два префикса и выходил с null, так и
           не дойдя до точного совпадения. */
        for (String str3 : list) {
            String strNorm3 = norm(str3);
            if (strNorm3.equals(strNorm)) {
                return str3;
            }
            if (!strNorm2.equals(strNorm) && norm(translit(strNorm3)).equals(strNorm2)) {
                return str3;
            }
        }
        /* Префикс — только для длинных запросов и только если кандидат один. */
        if (strNorm.length() >= 5) {
            int i = 0;
            String str2 = null;
            for (String str3 : list) {
                String strNorm3 = norm(str3);
                if (strNorm3.length() + 1 >= strNorm.length() && strNorm3.startsWith(strNorm)) {
                    i++;
                    str2 = str3;
                    if (i > 1) {
                        return null;
                    }
                }
                if (!strNorm2.equals(strNorm)) {
                    String strNorm4 = norm(translit(strNorm3));
                    if (strNorm4.length() + 1 >= strNorm2.length() && strNorm4.startsWith(strNorm2)) {
                        i++;
                        str2 = str3;
                        if (i > 1) {
                            return null;
                        }
                    }
                }
            }
            if (i == 1) {
                return str2;
            }
        }
        /* Двуязычная этикетка ЕС: «Aqua/Water/Eau», «Parfum/Fragrance». Целое не
           нашлось — пробуем части по слэшу. «Caprylic/Capric Triglyceride» при этом
           не режется: целое находится выше и сюда не доходит. */
        if (str.indexOf('/') >= 0) {
            for (String part : str.split("/")) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty() && !trimmed.equals(str)) {
                    String found = findDbName(trimmed);
                    if (found != null) {
                        return found;
                    }
                }
            }
        }
        return null;
    }

    private void handleIntent(Intent intent) {
        String stringExtra;
        String dataString = intent.getDataString();
        if (dataString == null && (dataString = intent.getStringExtra("android.intent.extra.TEXT")) == null) {
            dataString = "";
        }
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        if (!dataString.isEmpty() && dataString.equals(this.lastRequested) && jElapsedRealtime - this.lastRequestedAt < 6000) {
            Log.i("ScanBase", "та же ссылка уже обрабатывается, пропускаю");
            return;
        }
        this.lastRequested = dataString;
        this.lastRequestedAt = jElapsedRealtime;
        if (intent == null) {
            return;
        }
        if (!"android.intent.action.VIEW".equals(intent.getAction()) || intent.getData() == null) {
            stringExtra = "android.intent.action.SEND".equals(intent.getAction()) ? intent.getStringExtra("android.intent.extra.TEXT") : null;
        } else {
            stringExtra = intent.getData().toString();
        }
        if (stringExtra == null || stringExtra.trim().isEmpty()) {
            return;
        }
        this.urlBox.setText(stringExtra.trim());
        open();
        if ("1".equals(intent.getStringExtra("scanbase_ai"))) {
            /* Гонка: сразу после open() состава ещё нет, и startAiReview()
               молча выходился по пустому разбору. Флаг доживает до момента,
               когда разбор действительно показан. */
            this.autoAi = true;
        }
    }

    private static String helpFor(Item item) {
        String lowerCase = item.name.toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder();
        if (item.pct > 0.0d) {
            sb.append("Заявленная доля в составе: ");
            sb.append(percentLabel(item));
            sb.append(". ");
        }
        if (!item.law.isEmpty()) {
            sb.append("Есть юридическое ограничение в ");
            sb.append(lawLabel(item.law.get(0)));
            sb.append(". ");
            String strMaxLabel = maxLabel(item.law);
            if (!strMaxLabel.isEmpty()) {
                sb.append("Максимально допустимая доля: ");
                sb.append(strMaxLabel);
                sb.append(". ");
                if (item.pct <= 0.0d) {
                    sb.append("Но процент в карточке не указан — проверить «граммовку» нельзя.");
                } else if (item.pct > maxConc(item.law) + 1.0E-4d) {
                    sb.append("Текущая доля выше лимита — компонент стоит считать проблемным.");
                } else {
                    sb.append("Текущая доля укладывается в лимит.");
                }
            }
        }
        if (containsAny(lowerCase, new String[]{"methylisothiazolinone", "methylchloroisothiazolinone", "mit", "cmit"})) {
            sb.append("MIT/CMIT — сильные консерванты-аллергены; в несмываемой косметике их лучше избегать.");
        } else if (containsAny(lowerCase, new String[]{"formaldehyde", "formalin", "imidazolidinyl urea", "diazolidinyl urea", "quaternium-15", "dmdm hydantoin"})) {
            sb.append("Донор формальдегида: потенциальный раздражитель и сенсибилизатор.");
        } else if (containsAny(lowerCase, new String[]{"phenoxyethanol"})) {
            sb.append("Phenoxyethanol — распространённый консервант; обычно допустим до 1%, но у чувствительной кожи может раздражать.");
        } else if (containsAny(lowerCase, new String[]{"paraben"})) {
            sb.append("Парабены — консерванты; в разрешённых концентрациях обычно нормальны, но есть ограничения и споры.");
        } else if (containsAny(lowerCase, new String[]{"parfum", "fragrance", "отдушка"})) {
            sb.append("Отдушка — частый источник аллергии и раздражения; сама по себе не «яд», но для чувствительной кожи минус.");
        } else if (containsAny(lowerCase, new String[]{"sodium lauryl sulfate", "sodium laureth sulfate", "sls", "sles"})) {
            sb.append("Жёсткий ПАВ: может сушить и раздражать кожу, особенно в несмываемых средствах и при высокой доле.");
        } else if (containsAny(lowerCase, new String[]{"alcohol denat", "denatured alcohol"})) {
            sb.append("Денатурированный спирт: растворитель; при высокой доле сушит и может нарушать барьер кожи.");
        } else if (containsAny(lowerCase, new String[]{"lanolin"})) {
            sb.append("Ланолин: смягчает, но у части людей вызывает контактный дерматит.");
        } else if (containsAny(lowerCase, new String[]{"ci 77", "ci 75", "ci 74", "ci 15", "ci 42"})) {
            sb.append("Пигмент/краситель: обычно допустим, но может быть спорным для чувствительной кожи.");
        } else if (containsAny(lowerCase, new String[]{"peg-", "ppg-"})) {
            sb.append("Этоксилированный компонент: сам по себе обычно не опасен, но спорен из-за примесей и экологии.");
        } else if (containsAny(lowerCase, new String[]{"niacinamide", "ниацинамид"})) {
            sb.append("Ниацинамид — актив: себорегуляция, барьер, тон кожи; обычно хорошо переносится.");
        } else if (containsAny(lowerCase, new String[]{"hyaluron", "гиалурон"})) {
            sb.append("Гиалуроновая кислота/гиалуронат — увлажнитель; держит воду в роговом слое.");
        } else if (containsAny(lowerCase, new String[]{"glycerin", "глицерин"})) {
            sb.append("Глицерин — базовый увлажнитель; дёшево и надёжно.");
        } else if (containsAny(lowerCase, new String[]{"panthenol", "пантенол"})) {
            sb.append("Пантенол — успокаивает и поддерживает восстановление кожи.");
        } else if (containsAny(lowerCase, new String[]{"ceramide", "церамид"})) {
            sb.append("Церамиды — липиды барьера кожи; полезны при сухости.");
        } else if (containsAny(lowerCase, new String[]{"retinol", "ретинол"})) {
            sb.append("Ретинол — актив с доказанной пользой, но может раздражать; важна низкая концентрация и SPF.");
        } else if (containsAny(lowerCase, new String[]{"peptide", "пептид"})) {
            sb.append("Пептиды — сигнальные/увлажняющие компоненты; польза зависит от конкретного пептида.");
        } else if (containsAny(lowerCase, new String[]{"tocopherol", "токоферол"})) {
            sb.append("Витамин E — антиоксидант и смягчитель; обычно полезен.");
        } else if (containsAny(lowerCase, new String[]{"ascorbic", "аскорбин"})) {
            sb.append("Витамин C — антиоксидант/осветление; нестабилен и может раздражать при высокой доле.");
        } else if (containsAny(lowerCase, new String[]{"squalane", "сквалан"})) {
            sb.append("Сквалан — лёгкий эмолент, подходит многим типам кожи.");
        } else if (containsAny(lowerCase, new String[]{"cotton", "хлопок"})) {
            sb.append("Хлопок — дышащее натуральное волокно, комфортный контакт с кожей.");
        } else if (containsAny(lowerCase, new String[]{"elastane", "эластан", "spandex", "спандекс", "lycra", "лайкра"})) {
            sb.append("Эластан — даёт тягу; малая доля нормальна, большая может хуже пропускать воздух.");
        } else if (containsAny(lowerCase, new String[]{"polyester", "полиэстер", "полиэфир", "nylon", "нейлон", "polyamide", "полиамид", "acrylic", "акрил"})) {
            sb.append("Синтетика — прочная и дешёвая, но хуже отводит влагу и может электризоваться.");
        } else if (containsAny(lowerCase, new String[]{"viscose", "вискоза", "modal", "модал", "lyocell", "лиоцелл"})) {
            sb.append("Целлюлозное волокно — мягкое и воздухопроницаемое, хорошо впитывает влагу.");
        } else if (containsAny(lowerCase, new String[]{"acetate", "ацетат"})) {
            sb.append("Ацетат — гладкое целлюлозное волокно, имитация шёлка; требует аккуратного ухода.");
        } else if (containsAny(lowerCase, new String[]{"wool", "шерсть"})) {
            sb.append("Шерсть — тёплая и гигроскопичная, но у чувствительных людей может колоть и раздражать.");
        } else if (containsAny(lowerCase, new String[]{"cashmere", "кашемир"})) {
            sb.append("Кашемир — мягкая шерсть, комфортная, но дорогая и деликатная.");
        } else if (containsAny(lowerCase, new String[]{"linen", "лен"})) {
            sb.append("Лён — прочное дышащее волокно; хорошо для жары, но мнётся.");
        } else if ("не найден".equals(item.status) && "неизвестно".equals(item.risk)) {
            sb.append("Компонент не найден в базе: это не обязательно вредно — просто нет юридической/справочной записи.");
        } else if ("низкий".equals(item.risk)) {
            sb.append("Явных ограничений не найдено; компонент выглядит рутинным.");
        } else if ("средний".equals(item.risk)) {
            sb.append("Компонент спорный/потенциально раздражающий — важен процент и тип продукта.");
        }
        return sb.toString();
    }

    private boolean inventedNumbers(String str) {
        String strAiPrompt = aiPrompt(this.aiBudgetSec);
        Matcher matcher = Pattern.compile("\\d{1,4}").matcher(str);
        while (matcher.find()) {
            if (!strAiPrompt.contains(matcher.group())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRiskyItem(Item item) {
        return "высокий".equals(item.risk) || "средний".equals(item.risk) || "превышение".equals(item.status) || "с ограничением".equals(item.status);
    }

    private static String joinNames(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    private String joinRuNames(List<String> list) {
        /* Одно вещество часто пришло и латиницей, и по-русски («Glycerin» и
           «глицерин»): по-русски имена совпадают, и в приговоре компонент
           перечислен дважды — читается как два разных. */
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (int i = 0; i < list.size(); i++) {
            String strRuName = ruName(stripPercent(list.get(i)));
            if (!strRuName.isEmpty()) {
                linkedHashSet.add(strRuName);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Object object : linkedHashSet) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append((String) object);
        }
        return sb.toString();
    }

    /* Сворачивает позиции, которые по-русски называются одинаково: остаётся
       первая, но берётся объявленный процент, худший риск и уточнение названия.
       Ключи приходят извне, чтобы метод можно было проверить на десктопе. */
    static List<Item> mergeByKey(List<Item> list, String[] strKeys) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (int i = 0; i < list.size(); i++) {
            Item item = list.get(i);
            String strKey = nz(i < strKeys.length ? strKeys[i] : "").toLowerCase(Locale.ROOT);
            if (strKey.isEmpty()) {
                linkedHashMap.put("\u0000" + i, item);
                continue;
            }
            Item itemKept = (Item) linkedHashMap.get(strKey);
            if (itemKept == null) {
                linkedHashMap.put(strKey, item);
            } else {
                if (itemKept.pct <= 0.0d && item.pct > 0.0d) {
                    itemKept.pct = item.pct;
                }
                if (riskRank(item.risk) > riskRank(itemKept.risk)) {
                    itemKept.risk = item.risk;
                }
                if (nz(itemKept.note).isEmpty() && !nz(item.note).isEmpty()) {
                    itemKept.note = item.note;
                }
                if ("не найден".equals(itemKept.status) && !"не найден".equals(item.status)) {
                    itemKept.status = item.status;
                    itemKept.role = item.role;
                    itemKept.catalog = item.catalog;
                    itemKept.food = item.food;
                    itemKept.foodName = item.foodName;
                    itemKept.law = item.law;
                }
            }
        }
        return new ArrayList(linkedHashMap.values());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildAiPanel$0(View view) {
        onAiButton();
    }

    static int lambda$buildHtml$0(Item item, Item item2) {
        int iCompare = Double.compare(item2.pct, item.pct);
        return iCompare != 0 ? iCompare : Integer.compare(riskRank(item2.risk), riskRank(item.risk));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildTabs$0(View view) {
        showTab(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buildTabs$1(View view) {
        showTab(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleIntent$0() {
        showTab(1);
        onAiButton();
    }

    private static String lawLabel(String[] strArr) {
        String str = "EU".equals(nz(strArr[0])) ? "ЕС" : "ЕАЭС";
        String str2 = str + " " + nz(strArr[1]) + " №" + nz(strArr[2]);
        if (nz(strArr[3]).isEmpty()) {
            return str2;
        }
        return str2 + " до " + strArr[3];
    }

    private String lawLegend(List<Item> list) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Item item : list) {
            if (!item.law.isEmpty()) {
                linkedHashSet.add("<li><b>" + esc(ruName(stripPercent(item.name))) + "</b> — " + esc(lawPlain(item.law.get(0))) + "</li>");
            }
        }
        if (linkedHashSet.isEmpty()) {
            return "<div class='muted'>Ни один компонент не попал в списки ограничений ЕС или ЕАЭС.</div>";
        }
        StringBuilder sb = new StringBuilder("<details open><summary>Что означают ограничения</summary><div class='muted'>«Не более» — предельная доля в средстве по регламенту; «консервант» и «краситель» — разрешены только в своей роли и своей дозе; «запрещён» — в косметике быть не должно.</div><ul class='lawlist'>");
        Iterator it = linkedHashSet.iterator();
        while (it.hasNext()) {
            sb.append((String) it.next());
        }
        sb.append("</ul></details>");
        return sb.toString();
    }

    private static String lawPlain(String[] strArr) {
        String str;
        String str2 = "EU".equals(nz(strArr[0])) ? "ЕС" : "ЕАЭС";
        String lowerCase = nz(strArr[1]).toLowerCase(Locale.ROOT);
        if (lowerCase.contains("запрещ")) {
            str = "запрещён";
        } else if (lowerCase.contains("консерв")) {
            str = "консервант";
        } else if (lowerCase.contains("крас")) {
            str = "краситель";
        } else if (lowerCase.contains("ультрафиол") || lowerCase.contains("uv")) {
            str = "ультрафиолетовый фильтр";
        } else {
            str = lowerCase.contains("огранич") ? "разрешён в ограниченной дозе" : "в списке регулирования";
        }
        StringBuilder sb = new StringBuilder(str);
        if (!nz(strArr[3]).isEmpty()) {
            sb.append(", не более ");
            sb.append(strArr[3].trim());
        }
        sb.append(" (");
        sb.append(str2);
        if (!nz(strArr[2]).isEmpty()) {
            sb.append(", позиция ");
            sb.append(strArr[2].trim());
        }
        sb.append(')');
        return sb.toString();
    }

    private void loadAliasCache() {
        try {
            this.aiAlias.putAll(getSharedPreferences("aliases", 0).getAll());
        } catch (Exception e) {
            Log.i("ScanBase", "кэш уточнений не прочитан: " + String.valueOf(e));
        }
    }

    private void loadRuNames() {
        try {
            InputStream inputStreamOpen = getAssets().open("ru_names.json");
            byte[] bArr = new byte[inputStreamOpen.available()];
            inputStreamOpen.read(bArr);
            inputStreamOpen.close();
            JSONObject jSONObject = new JSONObject(new String(bArr, "UTF-8"));
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                this.ruNames.put(next, jSONObject.getString(next));
            }
            Log.i("ScanBase", "русских названий: " + this.ruNames.size());
        } catch (Exception e) {
            Log.i("ScanBase", "ru_names.json не прочитан: " + String.valueOf(e));
        }
    }

    private static void addLaw(Row row, LinkedHashSet seen, String sys, String annex, String ref, String maxc) {
        if (seen.add(nz(sys) + nz(annex) + nz(ref))) {
            row.law.add(new String[]{nz(sys), nz(annex), nz(ref), maxc == null ? "" : maxc});
        }
    }

    private static void collectCas(LinkedHashSet set, String cas) {
        if (cas == null) {
            return;
        }
        for (String part : cas.split("[;/]")) {
            String t = part.trim();
            if (t.length() >= 5) {
                set.add(t);
            }
        }
    }

    private Row lookup(String str) {
        if (this.db == null) {
            return null;
        }
        String strNorm = norm(str);
        if (strNorm.length() < 4) {
            return null;
        }
        Row row = new Row();
        Cursor cursorRawQuery = this.db.rawQuery("SELECT system, annex, ref, max_conc, cas FROM inci_index WHERE norm=?", new String[]{strNorm});
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        LinkedHashSet casSet = new LinkedHashSet();
        while (cursorRawQuery.moveToNext()) {
            addLaw(row, linkedHashSet, cursorRawQuery.getString(0), cursorRawQuery.getString(1), cursorRawQuery.getString(2), cursorRawQuery.getString(3));
            collectCas(casSet, cursorRawQuery.getString(4));
        }
        cursorRawQuery.close();
        /* Списки ограничений ЕС и ЕАЭС (запрещено / разрешено с пределом). В
           inci_index лежат в основном INCI-названия, а запрещённые медикаменты и
           ограниченные химикаты — в eu_cosmetics_law / trts_cosmetics_law под
           химическими и INN-именами. Без этих двух запросов они не матчились. */
        Cursor cursorEu = this.db.rawQuery("SELECT 'EU', annex, ref, max_conc FROM eu_cosmetics_law WHERE lower(name)=? OR lower(inci)=?", new String[]{str.toLowerCase(Locale.ROOT), str.toLowerCase(Locale.ROOT)});
        while (cursorEu.moveToNext()) {
            if (linkedHashSet.add(cursorEu.getString(0) + cursorEu.getString(1) + cursorEu.getString(2))) {
                row.law.add(new String[]{cursorEu.getString(0), cursorEu.getString(1), cursorEu.getString(2), cursorEu.getString(3) == null ? "" : cursorEu.getString(3)});
            }
        }
        cursorEu.close();
        Cursor cursorEaeu = this.db.rawQuery("SELECT 'EAEU', annex, ref, max_conc FROM trts_cosmetics_law WHERE lower(name_ru)=? OR lower(name_en)=?", new String[]{str.toLowerCase(Locale.ROOT), str.toLowerCase(Locale.ROOT)});
        while (cursorEaeu.moveToNext()) {
            if (linkedHashSet.add(cursorEaeu.getString(0) + cursorEaeu.getString(1) + cursorEaeu.getString(2))) {
                row.law.add(new String[]{cursorEaeu.getString(0), cursorEaeu.getString(1), cursorEaeu.getString(2), cursorEaeu.getString(3) == null ? "" : cursorEaeu.getString(3)});
            }
        }
        cursorEaeu.close();
        Cursor cursorRawQuery2 = this.db.rawQuery("SELECT cas, func FROM cosmetic_ingredients WHERE lower(name)=?", new String[]{str.toLowerCase(Locale.ROOT)});
        while (cursorRawQuery2.moveToNext()) {
            row.catalog = true;
            collectCas(casSet, cursorRawQuery2.getString(0));
            if (row.func == null) {
                row.func = cursorRawQuery2.getString(1);
            }
        }
        cursorRawQuery2.close();
        Cursor cursorRawQuery3 = this.db.rawQuery("SELECT e FROM food_additives WHERE lower(name)=? OR lower(trts_name)=? LIMIT 1", new String[]{str.toLowerCase(Locale.ROOT), str.toLowerCase(Locale.ROOT)});
        if (cursorRawQuery3.moveToFirst()) {
            row.food = cursorRawQuery3.getString(0);
        }
        cursorRawQuery3.close();
        /* Обычные продукты (свёкла, мука, сахар) — не добавки и не косметика.
           Тот же единый поиск: если слово нашлось в пищевой таксономии,
           это тоже распознанный компонент, а не «не найден». */
        Cursor cFood = this.db.rawQuery("SELECT display, e FROM food_ingredients WHERE name=? LIMIT 1", new String[]{str.toLowerCase(Locale.ROOT)});
        if (cFood.moveToFirst()) {
            row.foodName = cFood.getString(0);
            if (row.food == null) {
                row.food = cFood.getString(1);
            }
        }
        cFood.close();
        /* Присоединение списков ЕС/ЕАЭС по CAS: точно, без ложных срабатываний на
           «грязных» юридических именах («Lidocaine (INN)», «…and its salts»). Даёт
           и «запрещено», которого нет в inci_index: напр. гидрохинон там только
           «Ann III 0.02 %», а по CAS 123-31-9 в праве ЕС он ещё и Annex II. */
        for (Object casObj : casSet) {
            String cas = (String) casObj;
            Cursor cEu = this.db.rawQuery("SELECT 'EU', annex, ref, max_conc FROM eu_cosmetics_law WHERE cas=?", new String[]{cas});
            while (cEu.moveToNext()) {
                addLaw(row, linkedHashSet, cEu.getString(0), cEu.getString(1), cEu.getString(2), cEu.getString(3));
            }
            cEu.close();
            Cursor cEa = this.db.rawQuery("SELECT 'EAEU', annex, ref, max_conc FROM trts_cosmetics_law WHERE cas=?", new String[]{cas});
            while (cEa.moveToNext()) {
                addLaw(row, linkedHashSet, cEa.getString(0), cEa.getString(1), cEa.getString(2), cEa.getString(3));
            }
            cEa.close();
            /* Гармонизированная классификация CLP по тому же CAS: берём первую
               попавшуюся запись — это свойство вещества, а не продукта. */
            if (row.hazard == null) {
                Cursor cClp = this.db.rawQuery("SELECT hazard FROM clp_hazard WHERE cas=?", new String[]{cas});
                if (cClp.moveToFirst()) {
                    row.hazard = cClp.getString(0);
                }
                cClp.close();
            }
        }
        if (row.law.isEmpty() && !row.catalog && row.food == null && row.foodName == null) {
            return null;
        }
        return row;
    }

    private static LinearLayout.LayoutParams match() {
        return new LinearLayout.LayoutParams(-1, -2);
    }

    /* Материалы деталей — обувь, мебель, посуда, инвентарь. Косметическая база
       их не знает, и без этой таблицы они валились бы в «не опознан»: на
       ботинках разбор и показывал одну «шерсть» из поля «Состав». Риск тут
       бытовой, а не косметический — что реально мешает при носке. */
    static final String[][] MATERIALS = {
        {"экокожа|искусственная кожа|коззам|винилискожа|эко кожа|faux leather|eco leather|pu leather", "искусственная кожа", "низкий", "Полиуретановая замена коже: не дышит и на морозе дубеет, но сама по себе инертна."},
        {"натуральная кожа|кожа|кожан|leather", "кожа", "низкий", "Натуральная кожа дышит и носится долго; новая может пахнуть — проветрите перед ноской."},
        {"замша|нубук|спилок", "кожа с ворсом", "низкий", "Ворсовая кожа: боится реагентов и воды, требует пропитки."},
        {"мех", "мех", "средний", "Мех — натуральный и искусственный — накапливает пыль и клещей, чувствительным нужен частый уход."},
        {"текстиль|ткань|тканев|fabric|textile|полотн", "текстиль", "низкий", "Тканая основа: важны воздухопроницаемость и стойкость окраски."},
        {"мембран|gore-tex|гортекс|membrane", "мембрана", "низкий", "Мембрана держит воду и отводит пар; работает только с правильной ноской."},
        {"синтепон|синтепух|холлофайбер|файбертек|теплофибер|тинсулейт|thermolite|sintepon", "утеплитель", "низкий", "Синтетический утеплитель: тёплый, неаллергенный, слегка накапливает статику."},
        {"флис|полартек|polartec|fleece", "флис", "низкий", "Флис: тёплый и лёгкий, электризуется."},
        {"дюспо|твинтек|twintec|оксфорд|кордура|габардин|полиэстер|polyester", "смесовая ткань", "низкий", "Плотная синтетическая ткань: прочная, хуже пропускает воздух."},
        {"тэп|tep|термоэластопласт", "ТЭП", "низкий", "Термоэластопласт: гибкая морозостойкая подошва, обычный и безопасный материал."},
        {"эва|eva|этиленвинилацетат", "ЭВА", "низкий", "Пеноматериал ЭВА: лёгкая амортизирующая подошва, на гладком мокром полу скользит."},
        {"пвх|pvc|поливинилхлорид|винил", "ПВХ", "средний", "В ПВХ добавляют фталаты-пластификаторы — для детской обуви и контакта с кожей нежелательно."},
        {"поролон|пенополиуретан|порист", "пенополиуретан", "средний", "Поролоновый наполнитель со временем крошится и пылит."},
        {"полиуретан|polyurethane", "полиуретан", "низкий", "Полиуретановая подошва лёгкая, но сильный мороз переносит плохо."},
        {"резин|rubber", "резина", "низкий", "Резина: прочная и нескользящая подошва; дешёвая может заметно пахнуть."},
        {"латекс|latex", "латекс", "средний", "Латекс — сильный аллерген, чувствительным людям лучше избегать."},
        {"силикон|silicone", "силикон", "низкий", "Силикон инертен, не пахнет, аллергии обычно не даёт."},
        {"пробк|cork", "пробка", "низкий", "Пробка: натуральная, дышащая, не любит долгую влагу."},
        {"войлок|felt|валян", "войлок", "низкий", "Войлок тёплый и натуральный, но впитывает влагу."},
        {"дерево|деревян|wood|бук|берёз|сосн", "дерево", "низкий", "Дерево: натуральный материал, боится влаги и пересыхания."},
        {"оргстекл|plexiglass|pmma|акриловое стекло|полиметилметакрилат|акриловый лист|акриловый щит", "оргстекло (ПММА)", "низкий", "Прозрачный и лёгкий пластик; царапается моющими с абразивом, не для духовки и кипятка."},
        {"стекл|glass|керамик", "стекло/керамика", "низкий", "Инертный материал, боится удара."},
        {"бумаг|картон|paper|cardboard", "бумага/картон", "низкий", "Бумага и картон: безопасны, могут краситься дешёвыми красителями."},
        {"лдсп|дсп|мдф|древесно-стружеч|древесно-волокнист", "древесная плита", "средний", "ДСП, ЛДСП и МДФ склеены смолами на основе формальдегида: безопасен класс эмиссии E1 и ниже, новую мебель проветривают."},
        {"бамбук|bamboo", "бамбук", "низкий", "Бамбук прочный и лёгкий; лакированная поверхность впитывает меньше, чем голая."},
        {"ротанг|плетён|ивов", "плетёный натуральный материал", "низкий", "Натуральная лоза и ротанг безопасны, но накапливают пыль в плетении."},
        {"велюр|шенилл|рогожка|микрофибра|микровелюр|жаккард", "мебельная ткань", "низкий", "Плотная обивочная ткань: важна стойкость к истиранию и качество красителя."},
        {"перо|пухов", "наполнитель пух/перо", "средний", "Натуральный пух и перо — сильный аллерген и среда для клещей; нужна частая стирка при 60 °C."},
        {"тефлон|ptfe|политетрафторэтилен|антипригарн", "антипригарное покрытие", "средний", "Покрытие безопасно примерно до 260 °C: перегретая пустая сковорода даёт токсичные пары, царапины разрушают слой."},
        {"меламин", "меламин", "средний", "Меламиновая посуда не рассчитана на горячее и кислое: при нагреве в пищу мигрирует формальдегид."},
        {"поликарбонат|бисфенол|bisphenol", "поликарбонат", "средний", "При нагреве может выделять бисфенол A; для детского питания ищите маркировку «BPA free»."},
        {"фторопласт", "фторопласт", "средний", "Тот же PTFE: боится перегрева и острых предметов."},
        {"нержав|нержавеющ", "нержавеющая сталь", "низкий", "Пищевая нержавейка (18/10, AISI 304) инертна и не выделяет ничего в еду."},
        {"алюмин|алюминий", "алюминий", "низкий", "Лёгкий металл с инертной оксидной плёнкой; анодированное покрытие прочнее обычного."},
        {"латунь|бронз", "сплав меди", "средний", "Латунь и бронза могут содержать свинец; для посуды и детских изделий нужна сертификация."},
        {"медь|медн", "медь", "средний", "Медь токсична в контакте с кислой пищей; безопасна посуда с никелевым или жестяным покрытием."},
        {"чугун", "чугун", "низкий", "Чугун инертен и служит десятилетиями, требует защиты от ржавчины."},
        {"оцинков", "оцинковка", "низкий", "Цинковое покрытие защищает сталь от коррозии; не для кислой пищи и не для готовки."},
        {"никелирован|никель|nickel", "никелевое покрытие", "средний", "Никель — частый контактный аллерген: у чувствительных даёт дерматит от застёжек и украшений."},
        {"нихром|нихромов", "нихром", "низкий", "Сплав нагревательных нитей; в готовом приборе безопасен, при перегреве и подгорании еды — проветривание."},
        {"слюд", "слюда", "низкий", "Слюда в нагревателях и изоляции инертна и термостойка; опасны только острые сколы при разборке."},
        {"хром|хромирован", "гальваническое покрытие", "низкий", "Тонкий слой металла на пластике: безопасен, пока цел, и не для длительного контакта с едой."},
        {"эмал|эмалирован", "эмалевое покрытие", "низкий", "Стеклянное покрытие на металле: инертно, но сколы открывают сталь."},
        {"карбон|углеродное волокно|углеродист|carbon fiber", "карбон", "низкий", "Углеволокно лёгкое и прочное; связующая смола инертна после полимеризации."},
        {"стекловолокн|фиберглас|fiberglass", "стекловолокно", "низкий", "Прочный наполнитель; опасны только частицы при шлифовке, а не готовое изделие."},
        {"термопластичный полиуретан|ТПУ|tpu", "ТПУ", "низкий", "Эластичный и износостойкий пластик, обычный для чехлов и подошв."},
        {"абс-пластик|акрилонитрил|abs plastic", "ABS-пластик", "низкий", "Жёсткий корпусной пластик; резкий запах — признак дешёвого вторичного сырья."},
        {"полистирол|пенополистирол|пенопласт|ps-пластик", "полистирол", "низкий", "Лёгкий пластик; не грейте в микроволновке без маркировки."},
        {"полипропилен|полипропилен|pp-пластик", "полипропилен", "низкий", "Самый безопасный из распространённых пищевых пластиков, переносит мойку и кипячение."},
        {"литий|литиев|li-ion|li-pol", "литиевый аккумулятор", "средний", "Такую батарею нельзя разбирать, гнуть, мять и заряжать повреждённой: риск возгорания."},
        {"никель-металлгидрид|nimh", "аккумулятор NiMH", "низкий", "Щелочной аккумулятор без кадмия, безопаснее старых форматов."},
        {"свинец", "свинец", "высокий", "Свинец токсичен и накапливается в организме; в детских изделиях и посуде запрещён."},
        {"кадмий", "кадмий", "высокий", "Кадмий канцероген; в электронике и яркой глазури посудных росписей его быть не должно."},
        {"бромированн|антипирен", "антипирен", "средний", "Бромированные антипирены переходят в пыль; корпус электроники не разбирают и не держат у кроватки."},
        {"фталат", "фталат-пластификатор", "высокий", "Фталаты влияют на гормональный фон; в детской игрушке, посуде и прорезывателях недопустимы."},
        {"силикагель|силика гел|silica gel", "силикагель", "низкий", "Саше с гелем-осушителем инертны, но их нельзя есть — их выбрасывают сразу после распаковки."},
        {"термопластичная резина", "термопластичная резина", "низкий", "Гибкий и моющийся материал, обычный для подошв и ковриков."},
        {"латекс натуральный|натуральный каучук", "натуральный каучук", "средний", "Каучук натурального латекса — сильный контактный аллерген, у детей реакция встречается чаще."},
        {"глазур", "глазурь", "низкий", "Стекловидное покрытие керамики безопасно после правильного обжига; роспись внутри чашки проверяют на стойкость."},
        {"папье-маше", "папье-маше", "низкий", "Прессованная бумага со связующим; боится влаги, для игр малышей не подходит."},
        {"титан|титанов", "титан", "низкий", "Инертный и лёгкий металл, аллергии не даёт — его ставят в украшения и посуду."},
        {"минеральное стекло|закалённ|gorilla|сапфир", "закалённое стекло", "низкий", "Прочное стекло с химическим упрочнением; при разрушении крошки безопаснее обычного."},
        {"джут|сизаль|пеньк|конофл|рафия", "грубое натуральное волокно", "низкий", "Натуральное волокно без пропитки: безопасно, но впитывает влагу и запах."},
        {"кинетический песок", "кинетический песок", "средний", "Песок с полимерной связкой: его не едят, после игры моют руки, при проглатывании большого количества — к врачу."},
        {"полимерная глина|запекаемая глина|пластилин запек", "полимерная глина", "средний", "Запекаемую глину греют строго до указанной температуры: перегретая выделяет хлороводород."},
        {"ортопедическая стелька|гелевая стелька", "гелевая вставка", "низкий", "Гель на полиуретановой основе инертен; важен режим стирки — при 60 °C теряет форму."},
        {"неопрен|neoprene|хлоропрен|полихлоропрен", "неопрен", "низкий", "Вспененный каучук: тёплый и упругий, у чувствительной кожи при долгом контакте даёт опрелость."},
        {"тефлоновое покрытие", "антипригарное покрытие", "средний", "Тот же PTFE: боится перегрева выше 260 °C и металлических ложек."},
        {"фарфор|фаянс|санфаянс|костяной фарфор|керамогранит|энотерм|стеклокерамик|майолик|терракот|гончарн", "обожжённая керамика", "низкий", "Инертна после обжига; глазурь с росписью внутри посуды проверяйте на стойкость — в дешёвой росписи бывают свинец и кадмий."},
        {"гранит|мрамор|базальт|габбро|порфир|сланец|травертин", "натуральный камень", "низкий", "Гранит и базальт инертны и прочны; мрамор, травертин и сланец точечно боятся кислот (уксус, лимон, вино) — пролитое стирают сразу."},
        {"кварц|агат|нефрит|бирюз|аметист|оникс|малахит|родонит|янтар|жемчуг|коралл", "поделочный камень", "низкий", "Натуральные камни безопасны; янтарь, жемчуг и коралл — органические, боятся кислоты, спирта и нагрева, их не моют ультразвуком."},
        {"серебр|sterling|чернёное серебро|черненое серебро", "серебро", "низкий", "Проба 925 инертна; в сплав иногда вводят никель — у чувствительных даёт реакцию кожи. Серебро темнеет от серы (яйца, резина) — это не порча."},
        {"золот|позолот|родирован|золочен|сусальное золото", "драгоценное покрытие", "низкий", "Золото и родий инертны; со временем покрытие стирается и проступает базовый сплав, в котором бывает никель."},
        {"кевлар|aramid|кевларов", "арамидное волокно", "низкий", "Сверхпрочное термостойкое волокно; в готовом изделии безопасно, опасна пыль при резке и шлифовке."},
        {"ламинат", "ламинат", "средний", "Основа HDF склеена смолами с формальдегидом: берите класс эмиссии E1 и ниже, новый пол проветривают."},
        {"линолеум", "линолеум", "средний", "Чаще ПВХ с пластификаторами: новый линолеум выветривают, для детской комнаты ищите безфталатный."},
        {"паркет|инженерная доска|паркетная доска", "паркет", "низкий", "Натуральное дерево; важна эмиссия лака и клея — свежий лак проветривают до исчезновения запаха."},
        {"вспененный полиэтилен|вспененный полипропилен|пенополиэтилен|изолон|пенотерм|вспененный пенополиэтилен", "вспененный полиэтилен", "низкий", "Инертный пеноматериал подложек и упаковки; не грейте и не давайте малышам как игрушку — откусывает и глотает."},
        {"полиэтилентерефталат|пэтф|пэт-|пэт б|pet-бутылк", "ПЭТ", "низкий", "Прозрачный пищевой пластик бутылок; одноразовый — не мойте горячей водой и не наполняйте многократно."},
        {"тритан|tritan|сополиэфир", "тритан", "низкий", "Прозрачный пластик без бисфенола A, переносит мойку; обычная для детских поильников и спортивных бутылок."},
        {"силумин|дюралюмин|магний|магние|магниев", "лёгкий сплав", "низкий", "Алюминиевые и магниевые сплавы лёгкие; анодирование повышает стойкость, для пищевого контакта нужна сертификация."},
        {"ондулин|шифер|битумная черепиц|металлочерепиц|полимерпесчан", "кровельный материал", "низкий", "Волнистые битумные и полимерные листы; при резке и нагреве нужно проветривание, в готовой кровле инертны."},
        {"эпоксидн|полиэфирн смол|полиэфирная смола|гелькоут|смол эпоксид|эпоксидк", "эпоксидная смола", "средний", "Застывшая смола инертна, но жидкий компонент и отвердитель — раздражители и сенсибилизаторы: работайте в перчатках и проветривании."},
        {"полиоксиметилен|ацеталь|делрин|pom-пластик", "полиоксиметилен", "низкий", "Прочный инженерный пластик шестерён и фурнитуры; в готовом изделии безопасен."},
        {"кокосовая койра|койра|кокосовое волокно", "кокосовое волокно", "низкий", "Натуральный наполнитель матрасов; часто связан латексом — у чувствительных к латексу возможна реакция."},
        {"пружинн|блок пружин|бонель|пружины", "пружинный блок", "низкий", "Стальные пружины в матрасе; к коже безопасны, со временем проседают и начинают поскрипывать."},
        {"искусственный камень|литьевой мрамор|аглом", "искусственный камень", "средний", "Кварц или мраморная крошка на смоле: готовое изделие инертно, режут и шлифуют в респираторе (кремнезёмная пыль)."},
        {"lvt-плитк|spc-плитк|lvt плитк|spc плитк|кварцвинил|кварц-винил", "кварц-виниловая плитка", "средний", "ПВХ с кварцевым наполнителем: новый пол выветривают, в спальню и детскую ищите с низким классом эмиссии."},
        {"керамич", "керамика", "низкий", "Керамика (оксид или цирконий циркония) инертна и не окисляет еду; боится точечного удара и сколов."},
        {"солом", "солома", "низкий", "Натуральная прессованная солома; боится влаги, может пылить — при аллергии пропылесосьте."},
        {"гусиный пух|утиный пух|утиным пухом|пух гусиный", "наполнитель пух/перо", "средний", "Натуральный пух — аллерген и среда для клещей; стирайте при 60 °C и сушите полностью, иначе заведётся перо."},
        {"полиэтилен|полипропилен|полиамид|пластик|abs|поликарбонат", "пластик", "низкий", "Пищевой и технический пластик инертен; резкий запах — признак дешёвого сырья, такое проветривают."},
        {"металл|металлич|сталь|стально|алюмин|steel|нержав", "металл", "низкий", "Металл прочный; nickel-содержащие сплавы у чувствительных дают реакцию кожи."},
    };

    /* Бытовая и техническая химия: то, чего нет в косметической базе, но что
       сплошь и рядом встречается в средстве для сантехники, стиральном порошке
       и автохимии. Без этой таблицы они валились в «нужно проверить». */
    static final String[][] HOUSEHOLD = {
        {"гипохлорит|хлорамин|хлорная известь", "хлорный агент", "высокий", "Хлор раздражает дыхательные пути и кожу, с кислотами и аммиаком даёт ядовитые газы: не смешивать, применять в проветриваемом помещении."},
        {"гидроксид натрия|гидроксид калия|едкий натр|едкое кали|каустич|натрия гидроокис|калия гидроокис", "щёлочь", "высокий", "Растворяет жир за счёт разрушения белков: вызывает ожоги кожи и глаз, нужны перчатки и защита."},
        {"соляная кислота|серная кислота|фосфорная кислота|кислота ортофосфатн", "минеральная кислота", "высокий", "Сильные кислоты в средстве от накипи и ржавчины: ожоги, нельзя смешивать с хлором и щёлочью."},
        {"натрия лаурилсульфат|натрия лауретсульфат|анионн|пав анион", "анионный ПАВ", "средний", "Рабочая пена, смывающая жир: при контакте с кожей разрушает её барьер, для рук — перчатки."},
        {"катионн|четвертичн|бензалконий|бензалкон|дидецилдиметил", "катионный ПАВ", "средний", "Кондиционер и антистатик: токсичен при проглатывании и раздражает кожу, для детской одежды нежелателен."},
        {"неионогенн|этоксилат|алкилполиглюкозид|глюкозид", "неионогенный ПАВ", "низкий", "Мягкая непенящаяся моющая основа, работает в холодной воде, кожу раздражает слабо."},
        {"амфотерн|бетаин", "мягкий амфотерный ПАВ", "низкий", "Работает в мягком режиме, почти не раздражает кожу — его ставят в средства для детей."},
        {"эдтк|эдта|edta|тринатрия эдт|динатрия эдт|тетранатрия эдт|нитрилотриуксус|динатрия нта", "комплексообразователь", "средний", "Связывает соли жёсткости; стойкий в природе и переносит за собой тяжёлые металлы, смывайте средством полностью."},
        {"фосфонат|этидронов|фосфонат натрия", "фосфонат", "низкий", "Умягчает воду и удерживает накипь; в разумной дозе безопасен, в водоёмах мешает росту водорослей."},
        {"фосфат|триполифосфат", "фосфат", "средний", "Делает порошок агрессивнее к грязи и к коже; в водоёмах вызывает цветение воды — есть бесфосфатные версии."},
        {"оптический отбеливател|стильбен|люмофлуор", "оптический отбеливатель", "средний", "Оптически «отбеливает» ткань, оставаясь на ней: контактный аллерген, для детского белья лучше без него."},
        {"кислородн отбел|перкарбонат|перборат|гидроперит|пероксид карбамида", "кислородный отбеливатель", "средний", "Выделяет активный кислород: безопаснее хлора, но в концентрированном виде раздражает кожу и глаза."},
        {"пищевая сода|сода пищевая|бикарбонат|гидрокарбонат", "гидрокарбонат натрия", "низкий", "Пищевая сода: мягкий абразив и нейтрализатор запаха, безопасна; при вдыхании порошка берегите дыхательные пути."},
        {"цитрат", "цитрат", "низкий", "Соль лимонной кислоты — регулятор кислотности и умягчитель, безопасна."},
        {"цеолит|силикать|силикат натрия|карбонат натрия|сода кальцинирован|кальцинированн", "щелочной наполнитель", "низкий", "Сухая основа порошка: абразив и слабощелочная среда, при вдыхании пыли защищаете дыхательные пути."},
        {"протеаза|амилаза|целлюлаза|липаза|энзим|феррит", "фермент", "низкий", "Расщепляет белковые и крахмальные загрязнения при низких температурах; в сухом виде при вдыхании — аллерген."},
        {"ароматизир|отдушк|парфюм", "отдушка", "средний", "Ароматизатор оседает на поверхностях и руках: у чувствительной кожи и детей чаще даёт раздражение."},
        {"органический растворитель", "растворитель", "средний", "Растворяет жир и клей, пары раздражают дыхательные пути: проветривайте и не держите флакон открытым."},
        {"амилциннам|бензизотиазолон|метилизотиазолон|изотиазолин", "консервант", "высокий", "Изотиазолиноны — сильные контактные аллергены; в смываемых средствах их доля ограничена."},
        {"антипен|силикон|диметикон", "антипенный агент", "низкий", "Кремнийорганическая добавка, гасит пену; в стирке не мешает, на ткани не накапливается значимо."},
        {"антистатик|четвертичные аммониев", "антистатик", "средний", "Осаждается на ткани и снимает статику; у чувствительной кожи и у младенцев вызывает раздражение."},
        {"антифриз|тосол|этиленгликоль", "антифриз", "высокий", "Этиленгликоль смертельно токсичен при проглатывании и сладок на вкус — для дома с детьми и животными опасен."},
        {"нашатырный спирт|нашатырн|гидроксид аммония|аммиак", "аммиак", "высокий", "Резко раздражает дыхательные пути и глаза, с хлором даёт ядовитый газ; применяют только в проветривании."},
        {"щавелевая кислота", "щавелевая кислота", "средний", "Растворяет ржавчину, токсична при проглатывании и раздражает кожу: перчатки и полное смывание."},
        {"лимонная кислота|уксусная кислота|молочная кислота", "пищевая кислота", "низкий", "Мягкое средство от накипи, но при долгом контакте разрушает эмаль и полированный камень."},
        {"перекись водорода|пероксид водорода", "пероксид", "средний", "Обесцвечивает ткани и раздражает слизистые: выше 3 % работаете в перчатках и вдали от глаз."},
        {"толуол|ксилол|ацетон|растворитель 646|лак нц|эмаль пф|нитрокраска", "лакокрасочный состав", "высокий", "Толуол и ксилол токсичны при вдыхании и горючи: проветривание и респиратор, хранение вне жилья."},
        {"перметрин|хлорпирифос|фипронил|имидаклоприд|диметилфумарат|инсектицид|родентицид", "пестицид", "высокий", "Яд против насекомых и грызунов: перметрин и фипронил опасны для кошек, средство убирают от детей и животных."},
        {"глифосат|гербицид|фунгицид|удобрен азотн|аммиачная селитра|карбамид", "агрохимия", "высокий", "Гербициды и минеральные удобрения раздражают кожу и дыхательные пути; работают по инструкции и в перчатках."},
        {"цианоакрилат|клей момент|суперклей|герметик силикон|мастик|битум", "клей и герметик", "средний", "Цианоакрилат мгновенно склеивает кожу и раздражает слизистые пары; битумные составы применяют в проветривании."},
        {"меламиновая губка|меламиновый поролон", "меламиновая губка", "средний", "Работает как мелкий абразив: не для посуды и рта, при трении выделяет мелкую пыль."},
        {"гликоль|диэтиленгликоль|пропиленгликоль", "гликоль", "средний", "Растворитель и антифриз: токсичен при проглатывании, для дома с детьми и животными берите нетоксичные составы."},
        {"углеводород|нефтян|уайт-спирит|сольвент|керосин|нефрас", "нефтяной растворитель", "средний", "Растворяет битум и масло, пары угнетают нервную систему: работаете в проветривании, вдали от огня."},
        {"бура|боракс|тетраборат|борная кислота|борат натрия", "бораты", "средний", "Бура и борная кислота усиливают мойку и консервируют; токсичны при проглатывании и вредны для репродукции — не для детской посуды и игрушек."},
        {"хлоргексидин|мирамистин", "антисептик", "низкий", "Кожный и поверхностный антисептик: в быту безопасен, но с анионными моющими средствами гаснет и не лейте в глаза."},
        {"триклозан|triclosan", "триклозан", "высокий", "Антибактериальная добавка: влияет на гормональный фон и способствует устойчивости бактерий, в ЕС ограничен — берите средства без него."},
        {"перманганат калия|марганцовк", "перманганат калия", "высокий", "Сильный окислитель: концентрированный раствор обжигает кожу и слизистые, храните вдали от детей и горючих веществ."},
        {"трилон", "комплексообразователь", "средний", "Трилон Б — тот же ЭДТА: связывает жёсткость воды, стоек в природе и переносит тяжёлые металлы, смывайте средство полностью."},
        {"крахмал|мальтодекстрин|декстрин|ксантан|гуаров|целлюлоза|метилцеллюлоза|гидроксипропилцеллюлоза", "загуститель", "низкий", "Растительный загуститель (клеевая основа обоев, гели, кремы): инертен и безопасен."},
        {"сорбит|ксилит|маннит", "полиол", "низкий", "Сахарный спирт — мягкий увлажнитель; безопасен, в больших количествах внутрь слабит."},
        {"скипидар|терпентин|живичный растворитель", "скипидар", "средний", "Натуральный растворитель красок: пары раздражают дыхательные пути и дают аллергию — проветривайте, храните плотно закрытым."},
        {"глутаров", "альдегид-дезинфектант", "высокий", "Медицинский дезинфектант: сильный раздражитель и сенсибилизатор, работаете в перчатках и вытяжке, смываете полностью."},
        {"перуксусн|надуксусн", "перуксусная кислота", "высокий", "Сильный окислитель-дезинфектант: разъедает кожу и дыхательные пути, применяют строго в разведении и проветривании."},
        {"сульфаминов", "сульфаминовая кислота", "средний", "Мягкая кислота от накипи: безопаснее соляной, но концентрат раздражает кожу и глаза — перчатки."},
        {"тиосульфат|гипосульфит", "тиосульфат натрия", "низкий", "Обезвреживает хлор, фиксаж и антидот при отравлениях; в быту малотоксичен."},
        {"жидкое стекло|силикат калия|силикат натрия", "жидкое стекло", "средний", "Щелочной силикатный клей и пропитка: едкий, раздражает кожу и глаза, работаете в перчатках."},
        {"медный купорос|железный купорос|бордоск|купорос", "купорос (медь/железо)", "средний", "Фунгицид и пропитка дерева: медь токсична при проглатывании и для водоёмов — перчатки, убрать от животных."},
        {"фенол|крезол|карболов|лизол", "фенольный антисептик", "высокий", "Фенол и крезол обжигают кожу и всасываются через неё, угнетают нервную систему; в быту вытеснены — не для кожи и детей."},
        {"олифа|оксоль", "олифа", "средний", "Высыхающее масло для пропитки: малотоксично, но промасленная ветошь самовозгорается — залейте водой или сожгите."},
        {"цемент|портландцемент|алебастр|гипс строительн|штукатурк|шпатл|пескобетон|смесь строительн", "строительная смесь", "средний", "Цементно-гипсовые смеси щелочные: пыль сушит и раздражает кожу и дыхательные пути — перчатки и маска."},
        {"антисептик для дерев|биозащит|пропитк для дерев", "пропитка для дерева", "средний", "Биозащита от грибка и жучка содержит биоциды: наносите в проветривании и сушите до исчезновения запаха перед контактом."},
        {"бустилат|флизелинов|клей для обоев|обоев клей|обоев клеевой", "клеевая основа", "низкий", "Клей на модифицированном крахмале: безопасен, раздражает только сухой порошок при вдыхании."},
        {"мел|воск|парафин|озокерит", "воск/мел", "низкий", "Натуральная полироющая и абразивная основа, практически инертна."},
    };

    static String[] householdOf(String str) {
        for (String[] strArr : HOUSEHOLD) {
            if (containsAny(str, strArr[0].split("\\|"))) {
                return strArr;
            }
        }
        return null;
    }


    static String[] materialOf(String str) {
        for (String[] strArr : MATERIALS) {
            if (containsAny(str, strArr[0].split("\\|"))) {
                return strArr;
            }
        }
        return null;
    }

    private static String materialRisk(String str) {
        return containsAny(str, WOOL_MATERIAL) ? "средний" : "низкий";
    }

    private static String materialRole(String str) {
        if (containsAny(str, WOOL_MATERIAL)) {
            return "шерсть";
        }
        if (containsAny(str, ELASTIC_MATERIAL)) {
            return "эластичность";
        }
        if (containsAny(str, SYNTH_MATERIAL)) {
            return "синтетика";
        }
        if (containsAny(str, new String[]{"вискоза", "viscose", "модал", "modal", "лиоцелл", "lyocell", "ацетат", "acetate"})) {
            return "целлюлозное волокно";
        }
        return containsAny(str, new String[]{"хлопок", "cotton", "лен", "linen"}) ? "натуральное волокно" : "волокно";
    }

    private static double maxConc(List<String[]> list) {
        Iterator<String[]> it = list.iterator();
        double d = -1.0d;
        while (it.hasNext()) {
            Matcher matcher = Pattern.compile("(?iu)(\\d+(?:[.,]\\d+)?)\\s*%").matcher(nz(it.next()[3]));
            if (matcher.find()) {
                try {
                    double d2 = Double.parseDouble(matcher.group(1).replace(',', '.'));
                    if (d < 0.0d || d2 < d) {
                        d = d2;
                    }
                } catch (Exception unused) {
                }
            }
        }
        return d;
    }

    private static String maxLabel(List<String[]> list) {
        double dMaxConc = maxConc(list);
        if (dMaxConc <= 0.0d) {
            return "";
        }
        return String.format(Locale.ROOT, "%.2f", Double.valueOf(dMaxConc)).replace(".00", "") + "%";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String mb(long j) {
        return String.format(Locale.ROOT, "%.0f", Double.valueOf(j / 1048576.0d)) + " МБ";
    }

    private static String norm(String str) {
        return Rules.norm(str);
    }

    static String notFoundReason(JSONObject jSONObject) {
        String lowerCase = nz(jSONObject.optString("notFound", "")).toLowerCase(Locale.ROOT);
        if (!lowerCase.isEmpty()) {
            return "на странице написано «" + lowerCase + "»";
        }
        String lowerCase2 = nz(jSONObject.optString("url", "")).toLowerCase(Locale.ROOT);
        String lowerCase3 = nz(jSONObject.optString("title", "")).toLowerCase(Locale.ROOT);
        String[] strArr = {"не найден", "не найдена", "не существует", "не доступен", "такого товара нет", "товар отсутствует", "ничего не найдено", "404", "not found", "страница не найдена", "поиск по сайту"};
        for (int i = 0; i < 11; i++) {
            if (lowerCase3.contains(strArr[i])) {
                return "площадка сообщает: «" + jSONObject.optString("title", "") + "»";
            }
        }
        /* Схему снимаем любую: у chrome-error:// путь резался по «://» и
           проверка ниже не видела ни главной страницы, ни страницы ошибки. */
        String strReplaceFirst = lowerCase2.replaceFirst("(?i)^[a-z][a-z0-9+.-]*://(www\\.)?", "");
        int iIndexOf = strReplaceFirst.indexOf(47);
        String strSubstring = iIndexOf >= 0 ? strReplaceFirst.substring(iIndexOf) : "";
        String[] strArr2 = {"/search", "/modal", "/cart", "/basket", "/order", "/error", "/404", "/notfound", "/not-found", "/catalog?", "chrome-error://", "about:blank"};
        for (int i2 = 0; i2 < 12; i2++) {
            String str = strArr2[i2];
            /* Служебные адреса Chromium (chrome-error, about:blank) ищутся и по
               пути, и по адресу целиком: у них путь пустой или «/». */
            if (strSubstring.startsWith(str) || strSubstring.contains(str) || lowerCase2.contains(str)) {
                String strShown = strSubstring.isEmpty() ? lowerCase2 : strSubstring;
                return "ссылка ведёт не на карточку товара, а в «" + strShown.substring(0, Math.min(strShown.length(), 28)) + "»";
            }
        }
        /* Пустой url — это вставленный вручную текст, а не редирект на главную:
           без проверки ссылки отказ по ней выкосил бы разбор из буфера обмена. */
        if (!lowerCase2.isEmpty() && (strSubstring.isEmpty() || "/".equals(strSubstring) || "?".equals(strSubstring))) {
            return "ссылка выбросила на главную страницу — карточки товара нет";
        }
        return null;
    }

    /* Карточка WB приходит из корзины даже когда страница товара — заглушка или
       404: состав доходит, а разбираемого товара на экране нет (живьём: по
       несуществующему id разбор показывал чехол для телефона). Живая карточка
       печатает своё название и в заголовке страницы, поэтому считаем совпавшие
       слова; ноль совпадений — страница про другой товар или про ничего. */
    static int cardOverlap(String strTitle, String strCardName) {
        String[] strArrTitle = titleWords(strTitle);
        int i = 0;
        for (String str : titleWords(strCardName)) {
            for (String str2 : strArrTitle) {
                if (str.equals(str2)) {
                    i++;
                    break;
                }
            }
        }
        return i;
    }

    /* Слова от четырёх знаков: они же отсекает и служебные «для», «из», «на». */
    static String[] titleWords(String str) {
        String[] strArr = nz(str).toLowerCase(Locale.ROOT).split("[^a-zа-яё0-9]+");
        java.util.ArrayList<String> arrayList = new java.util.ArrayList<>();
        for (String str2 : strArr) {
            if (str2.length() >= 4) {
                arrayList.add(str2);
            }
        }
        return arrayList.toArray(new String[0]);
    }

    private static String nz(String str) {
        return str == null ? "" : str;
    }

    private void onAiButton() {
        if (this.aiBusy) {
            this.aiStop = true;
            this.aiStatus.setText("Останавливаю…");
        } else if (this.downloader.running()) {
            this.downloader.cancel();
            this.aiStatus.setText("Скачивание отменено.");
        } else if (LlamaModel.modelSize(this) <= 10000000) {
            startDownload();
        } else {
            startAiReview();
        }
    }

    private void open() {
        clearError();
        showPage(false);
        String strTrim = this.urlBox.getText().toString().trim();
        if (strTrim.indexOf(92) >= 0) {
            strTrim = strTrim.replace("\\n", "\n").replace("\\r", "\r").replace("\\t", " ").replace("\\\\", "\\");
        }
        if (strTrim.isEmpty()) {
            showError("Вставь ссылку Ozon, Wildberries или Яндекс Маркет, артикул товара, либо текст состава.");
            return;
        }
        /* EAN-8/EAN-13 с верной контрольной цифрой — это штрихкод: сначала ищем
           реальный состав товара в Open Beauty/Food Facts, и только если товара
           там нет — пробуем открыть как артикул маркетплейса. */
        if (isEan(strTrim)) {
            handleBarcode(strTrim);
            return;
        }
        String strExtractUrl = extractUrl(strTrim);
        if (strExtractUrl.isEmpty()) {
            String strArticle = articleUrl(strTrim);
            if (!strArticle.isEmpty()) {
                Log.i("ScanBase", "распознал артикул: " + strTrim + " -> " + strArticle);
                strExtractUrl = strArticle;
            }
        }
        strExtractUrl = marketCard(strExtractUrl);
        String lowerCase = strTrim.toLowerCase(Locale.ROOT);
        boolean hasMarkers = strTrim.contains(",") || strTrim.contains(";") || strTrim.contains("%")
                || strTrim.matches("(?iu).*\\d+\\s*%.*") || lowerCase.contains("состав") || lowerCase.contains("ingredients");
        boolean isComp = hasMarkers || (strTrim.length() > 20 && isIngredientList(strTrim));
        if (!strExtractUrl.isEmpty() || (!isComp && strTrim.length() <= 20)) {
            if (strExtractUrl.isEmpty()) {
                showError("Это не ссылка. Нужна ссылка Ozon, Wildberries или Яндекс Маркет, либо текст состава.");
                return;
            }
            if (!allowedUrl(strExtractUrl)) {
                showError("Разрешены только ссылки Ozon, Wildberries и Яндекс Маркет.");
                return;
            }
            if (!strExtractUrl.matches("(?i)^[a-z][a-z0-9+.-]*://.*")) {
                strExtractUrl = "https://" + strExtractUrl;
            }
            this.urlBox.setText(strExtractUrl);
            this.urlBox.setSelection(strExtractUrl.length());
            this.lastUrl = strExtractUrl;
            this.extractAttempt = 0;
            this.lateRound = 0;
            showText("Открываю страницу и жду описание…");
            fetchWbCard(strExtractUrl);
            this.web.loadUrl(strExtractUrl);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("url", "");
            jSONObject.put("title", textTitle(strTrim));
            jSONObject.put("len", strTrim.length());
            jSONObject.put("blocked", false);
            jSONObject.put("via", "text");
            jSONObject.put("composition", strTrim);
            double dTextPrice = textPrice(strTrim);
            if (dTextPrice > 0.0d) {
                jSONObject.put("price", dTextPrice);
                jSONObject.put("currency", "RUB");
                jSONObject.put("priceText", textPriceText(strTrim));
            }
            report(jSONObject);
        } catch (Exception e) {
            showError("Не удалось разобрать текст: " + e.getMessage());
        }
    }

    /* Кнопка «В приложение»: открывает товар в приложении площадки, а когда его
       на телефоне нет — в браузере. Раньше кнопка умела только Ozon и называлась
       «В Ozon», а на Wildberries и Маркете отказывала. Фолбэк в браузер тоже был
       недостижим: он стоял во внутреннем catch, куда попадало только исключение от
       showText, а не от отсутствующего приложения. */
    private void openInApp() {
        String strCurrentUrl = currentUrl();
        if (strCurrentUrl.isEmpty()) {
            showError("Ссылка ещё не открыта — нечего показывать приложению.");
            return;
        }
        String lowerCase = strCurrentUrl.toLowerCase(Locale.ROOT);
        String strPackage = "";
        String strTarget = strCurrentUrl;
        if (lowerCase.contains("ozon")) {
            strPackage = "ru.ozon.app.android";
            Matcher matcher = Pattern.compile("(\\d{6,})").matcher(strCurrentUrl);
            if (matcher.find()) {
                strTarget = "https://www.ozon.ru/product/" + matcher.group(1) + "/";
            }
        } else if (lowerCase.contains("wildberries") || lowerCase.contains("wb.ru")) {
            strPackage = "ru.wildberries";
        } else if (lowerCase.contains("market") || lowerCase.contains("yandex") || lowerCase.contains("ya.ru")) {
            strPackage = "ru.yandex.market";
        }
        if (strPackage.isEmpty()) {
            showError("Кнопка «В приложение» работает для ссылок Ozon, Wildberries и Яндекс Маркет.");
            return;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(strTarget));
            intent.setPackage(strPackage);
            intent.addFlags(268435456);
            startActivity(intent);
            this.lastUrl = strCurrentUrl;
            showText("Открываю приложение площадки…");
        } catch (Exception e) {
            try {
                Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(strTarget));
                intent2.addFlags(268435456);
                startActivity(intent2);
                showText("Приложения площадки нет — открыл в браузере.");
            } catch (Exception e2) {
                showError("Не удалось открыть ссылку: " + e2.getMessage());
            }
        }
    }

    private static String pathOf(String str) {
        String strReplaceFirst = str.replaceFirst("(?i)^https?://(www\\.)?", "");
        int iIndexOf = strReplaceFirst.indexOf(63);
        return iIndexOf < 0 ? strReplaceFirst : strReplaceFirst.substring(0, iIndexOf);
    }

    private static String percentLabel(Item item) {
        if (item.pct <= 0.0d) {
            return "—";
        }
        return String.format(Locale.ROOT, "%.2f", Double.valueOf(item.pct)).replace(".00", "").replace(",00", "") + "%";
    }

    /* Позиция что-то делает, если она либо полезна, либо рискованна: нейтральная
       база (растворитель, эмульгатор, регулятор кислотности) не польза и не
       вред, и в числителе ей не место. Низкорисковый материал (дерево, хлопок,
       силикон) считаем полезным: для вещи из материала это и есть хороший
       профиль. */
    static boolean isUsefulItem(Item item) {
        String strRole = nz(item.role);
        if (strRole.contains("актив") || strRole.contains("увлажнение") || strRole.contains("смягчение")
                || strRole.contains("натуральное") || strRole.contains("целлюлозное") || strRole.contains("эластичность")) {
            return true;
        }
        return "материал".equals(item.status) && "низкий".equals(item.risk);
    }

    static boolean isKnownItem(Item item) {
        return !nz(item.role).contains("не распознан") && !"не найден".equals(item.status);
    }

    /* {польза %, риск %, распознано, полезных, рискованных, 1 если считали по
       объявленным процентам} */
    static double[] sharesOf(List<Item> list) {
        int iKnown = 0;
        int iUseful = 0;
        int iRisky = 0;
        double dPctKnown = 0.0d;
        double dPctUseful = 0.0d;
        double dPctRisky = 0.0d;
        for (Item item : list) {
            if (!isKnownItem(item)) {
                continue;
            }
            iKnown++;
            dPctKnown += item.pct;
            if (isUsefulItem(item)) {
                iUseful++;
                dPctUseful += item.pct;
            }
            if ("высокий".equals(item.risk) || "средний".equals(item.risk)) {
                iRisky++;
                dPctRisky += item.pct;
            }
        }
        if (iKnown == 0) {
            return new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d};
        }
        /* Проценты печатают далеко не все составы, и частично объявленные
           (один компонент из двадцати) считать весомее числа нельзя:
           знаменатель должен покрывать хотя бы половину списка. */
        if (dPctKnown >= 50.0d) {
            return new double[]{(dPctUseful * 100.0d) / dPctKnown, (dPctRisky * 100.0d) / dPctKnown,
                    (double) iKnown, (double) iUseful, (double) iRisky, 1.0d};
        }
        return new double[]{((double) iUseful) * 100.0d / ((double) iKnown), ((double) iRisky) * 100.0d / ((double) iKnown),
                (double) iKnown, (double) iUseful, (double) iRisky, 0.0d};
    }

    private static String percentShareLabel(double d, boolean z) {
        int iClamp = clamp((int) Math.round(d));
        return (z ? "" : "≈") + iClamp + "%";
    }

    private static double percentValue(String str) {
        Matcher matcher = Pattern.compile("(?iu)(\\d+(?:[.,]\\d+)?)\\s*%").matcher(nz(str));
        if (!matcher.find()) {
            return 0.0d;
        }
        try {
            return Double.parseDouble(matcher.group(1).replace(',', '.'));
        } catch (Exception unused) {
            return 0.0d;
        }
    }

    private static String pickVariant(String[] strArr, String str) {
        return Rules.pickVariant(strArr, str);
    }

    private File prepareDb() throws Exception {
        File file = new File(getFilesDir(), "ingredients.db");
        if (file.exists() && file.length() > 1000000) {
            if (dbSchemaOk(file)) {
                return file;
            }
            file.delete();
        }
        InputStream inputStreamOpen = getAssets().open("ingredients.db");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bArr = new byte[65536];
        while (true) {
            int i = inputStreamOpen.read(bArr);
            if (i <= 0) {
                fileOutputStream.close();
                inputStreamOpen.close();
                return file;
            }
            fileOutputStream.write(bArr, 0, i);
        }
    }

    private static boolean dbSchemaOk(File f) {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(f.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
            db.rawQuery("SELECT func FROM cosmetic_ingredients LIMIT 1", null).close();
            db.rawQuery("SELECT name FROM food_ingredients LIMIT 1", null).close();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (db != null) db.close();
        }
    }

    /* JADX WARN: Code duplicated, block: B:26:0x005d A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:28:0x005f  */
    /* JADX WARN: Code duplicated, block: B:30:0x0068 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:32:0x006b  */
    /* JADX WARN: Code duplicated, block: B:34:0x0074 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:36:0x0077  */
    /* JADX WARN: Code duplicated, block: B:38:0x0080 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:40:0x0083  */
    /* JADX WARN: Code duplicated, block: B:42:0x008c A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:44:0x008f  */
    /* JADX WARN: Code duplicated, block: B:46:0x0098 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:48:0x009b  */
    /* JADX WARN: Code duplicated, block: B:50:0x00a4 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:52:0x00a7 A[RETURN] */
    private static int priceCheap(double d, String str) {
        double d2;
        String upperCase = nz(str).toUpperCase(Locale.ROOT);
        if (upperCase.contains("$") || upperCase.contains("USD")) {
            d2 = 90.0d;
        } else if (upperCase.contains("€") || upperCase.contains("EUR")) {
            d2 = 100.0d;
        } else {
            if (!upperCase.contains("₸") && !upperCase.contains("KZT")) {
                if (upperCase.contains("BYN")) {
                    d2 = 28.0d;
                }
                if (d <= 0.0d) {
                    return 0;
                }
                if (d <= 300.0d) {
                    return 95;
                }
                if (d <= 700.0d) {
                    return 85;
                }
                if (d <= 1500.0d) {
                    return 70;
                }
                if (d <= 3000.0d) {
                    return 55;
                }
                if (d <= 7000.0d) {
                    return 40;
                }
                if (d <= 15000.0d) {
                    return 25;
                }
                return 10;
            }
            d2 = 0.18d;
        }
        d *= d2;
        if (d <= 0.0d) {
            return 0;
        }
        if (d <= 300.0d) {
            return 95;
        }
        if (d <= 700.0d) {
            return 85;
        }
        if (d <= 1500.0d) {
            return 70;
        }
        if (d <= 3000.0d) {
            return 55;
        }
        if (d <= 7000.0d) {
            return 40;
        }
        if (d <= 15000.0d) {
            return 25;
        }
        return 10;
    }

    private static String priceWord(int i) {
        if (i >= 80) {
            return "очень дёшево";
        }
        if (i >= 65) {
            return "дёшево";
        }
        if (i >= 50) {
            return "средне";
        }
        return i >= 30 ? "дороговато" : "дорого";
    }

    private static String productKey(String str, String str2, String str3) {
        return Rules.productKey(str, str2, str3);
    }

    private String readAsset(String str) throws Exception {
        InputStream inputStreamOpen = getAssets().open(str);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[16384];
        while (true) {
            int i = inputStreamOpen.read(bArr);
            if (i <= 0) {
                inputStreamOpen.close();
                return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
            }
            byteArrayOutputStream.write(bArr, 0, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshAiPanel() {
        if (this.aiStatus == null) {
            return;
        }
        if (this.aiBusy) {
            this.aiButton.setEnabled(true);
            this.aiButton.setText("Стоп");
            this.aiStatus.setText("Пишу общее мнение, ждём до " + this.aiBudgetSec + " с — текст появляется по мере генерации.");
            return;
        }
        this.aiButton.setEnabled(true);
        long jModelSize = LlamaModel.modelSize(this);
        if (jModelSize > 10000000) {
            this.aiStatus.setText("Модель на месте: " + mb(jModelSize) + ". Напишу общее мнение в четыре предложения.");
            this.aiButton.setText(this.lastItems == null ? "Сначала сделай разбор" : "Написать отзыв заново");
            return;
        }
        this.aiStatus.setText("Модель не скачана. Понадобится " + mb(1117320736L) + " (Qwen2.5-1.5B, q4_k_m) — файл ляжет во внешние данные приложения, разбор без него работает как обычно.");
        this.aiButton.setText("Скачать модель " + mb(1117320736L));
    }

    private void rememberAlias(String str, String str2) {
        String strNorm = norm(str);
        if (strNorm.length() < 3) {
            return;
        }
        this.aiAlias.put(strNorm, str2);
        try {
            getSharedPreferences("aliases", 0).edit().putString(strNorm, str2).apply();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void report(JSONObject jSONObject) {
        this.loading = false;
        String strOptString = jSONObject.optString("composition", "");
        String strOptString2 = jSONObject.optString("url", "");
        String strOptString3 = jSONObject.optString("via", "");
        String strReplaceAll = strOptString2.replaceAll("(?i)^https?://", "").replaceAll("(?i)^www\\.", "").replaceAll("/.*", "");
        if (strReplaceAll.isEmpty()) {
            strReplaceAll = "text".equals(strOptString3) ? "вставленный текст" : "страница";
        }
        this.lastComposition = "";
        this.lastKey = "";
        String strNotFoundReason = notFoundReason(jSONObject);
        /* Площадка сказала «не найден» — разбор не показываем, даже если под
           сообщением выловился какой-то состав: на такой странице это блок
           «похожие товары» или текст ошибки, а не компоненты этой карточки. */
        if (strNotFoundReason != null) {
            this.rejectedPath = pathOf(strOptString2);
            this.rejectedAt = SystemClock.elapsedRealtime();
            Log.i("ScanBase", "ОТКАЗ: товар не найден — " + strNotFoundReason + " (состав на странице: " + strOptString.length() + " знаков)");
            clearReport();
            showText("Товар не найден.\n\nПлощадка пишет: «" + strNotFoundReason + "».\nПроверьте ссылку или откройте карточку в браузере площадки."
                    + (strOptString.isEmpty() ? "" : "\n\nТекст, похожий на состав, на этой странице есть, но брать его не буду: карточки этого товара здесь нет — это чужие компоненты или блок «похожие товары»."));
            return;
        }
        if (strOptString.isEmpty() && jSONObject.optBoolean("blocked", false)) {
            clearReport();
            showText("Площадка " + strReplaceAll + " показала проверку «вы не робот».\n\nОткрой эту же ссылку в обычном браузере и вставь текст состава в поле сверху — разберу его.");
            return;
        }
        if (strOptString.isEmpty()) {
            clearReport();
            showText("На странице " + strReplaceAll + " не найдено поле «Состав».\nВсего текста: " + jSONObject.optInt("len") + " знаков.");
            return;
        }
        clearError();
        showPage(false);
        this.lastKey = productKey(jSONObject.optString("title", ""), strOptString, jSONObject.optString("priceText", ""));
        this.lastComposition = strOptString;
        List<String> listSplitNames = expandBySpace(splitNames(strOptString));
        if (listSplitNames.isEmpty()) {
            clearReport();
            showText("Состав найден, но не удалось разбить его на названия.\nРазбей его запятыми или открой вкладку «Отзыв ИИ» — там текст читает модель.");
            return;
        }
        /* Страховка от зависания: разбор идёт на главном потоке и на каждое
           название делает запрос в базу. Настоящий список компонентов редко
           бывает длиннее пары сотен, а тысячи кусков означают, что вместо
           состава в поле попал текст всей страницы. */
        if (listSplitNames.size() > 400) {
            Log.i("ScanBase", "отказ: названий слишком много — " + listSplitNames.size());
            clearReport();
            showText("Похоже, вместо состава захвачен текст всей страницы (" + listSplitNames.size() + " фрагментов), поэтому разбор не запущен — иначе приложение зависнет.\nСкопируй список компонентов вручную и вставь его в поле ввода.");
            return;
        }
        long tStart = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = listSplitNames.iterator();
        int i = 0;
        StringBuilder missed = new StringBuilder();
        while (it.hasNext()) {
            Item itemAnalyzeItem = analyzeItem(it.next());
            if (!"не найден".equals(itemAnalyzeItem.status)) {
                i++;
            } else if (missed.length() > 0) {
                missed.append(" | ");
                missed.append(itemAnalyzeItem.name);
            } else {
                missed.append(itemAnalyzeItem.name);
            }
            arrayList.add(itemAnalyzeItem);
        }
        int iTotal = listSplitNames.size();
        String[] strKeys = new String[arrayList.size()];
        for (int iKey = 0; iKey < arrayList.size(); iKey++) {
            strKeys[iKey] = norm(ruName(stripPercent(((Item) arrayList.get(iKey)).name)));
        }
        List<Item> listMerged = mergeByKey(arrayList, strKeys);
        if (listMerged.size() != arrayList.size()) {
            Log.i("ScanBase", "повторов в составе: было " + arrayList.size() + ", стало " + listMerged.size());
            int iKnown = 0;
            missed.setLength(0);
            for (Item item2 : (List<Item>) listMerged) {
                if ("не найден".equals(item2.status)) {
                    if (missed.length() > 0) {
                        missed.append(" | ");
                    }
                    missed.append(item2.name);
                } else {
                    iKnown++;
                }
            }
            i = iKnown;
            iTotal = listMerged.size();
        }
        this.lastJson = jSONObject;
        this.lastItems = listMerged;
        this.lastTitle = jSONObject.optString("title", "");
        this.lastDescription = jSONObject.optString("description", "");
        this.lastCatHint = jSONObject.optString("cat", "");
        this.lastAttrs = trim(jSONObject.optString("attrs", ""), 240);
        this.lastCat = null;
        this.lastPriceText = jSONObject.optString("priceText", "");
        this.lastMatched = i;
        this.lastTotal = iTotal;
        String[] catDbg = catInfo();
        double[] dDbg = sharesOf(arrayList);
        Log.i("ScanBase", "report via=" + strOptString3 + " host=" + strReplaceAll + " names=" + iTotal + " matched=" + i + " price=" + jSONObject.optString("priceText", "") + " cat=" + catDbg[0] + ("1".equals(catDbg[1]) ? "/kids" : "") + " conf=" + catDbg[3] + " why=" + catDbg[2] + " plat=" + trim(this.lastCatHint, 60) + " attrs=" + trim(this.lastAttrs, 80) + " share=" + Math.round(dDbg[0]) + "/" + Math.round(dDbg[1]) + " known=" + ((int) dDbg[2]) + " mode=" + (dDbg[5] > 0.0d ? "pct" : "count"));
        if (missed.length() > 0) {
            Log.i("ScanBase", "не распознано (" + (iTotal - i) + "): " + missed);
        }
        long tHtml = System.currentTimeMillis();
        Log.i("ScanBase", "разбор " + iTotal + " названий занял " + (tHtml - tStart) + " мс");
        String html = buildHtml(jSONObject, listMerged, strOptString, strReplaceAll, strOptString3, i, jSONObject.optDouble("price", 0.0d), jSONObject.optString("currency", ""), jSONObject.optString("priceText", ""), jSONObject.optString("description", ""));
        Log.i("ScanBase", "сборка html заняла " + (System.currentTimeMillis() - tHtml) + " мс, длина " + html.length());
        showHtml(html);
    }

    private static String riskClass(String str) {
        if ("высокий".equals(str)) {
            return "bad";
        }
        return ("средний".equals(str) || "неизвестно".equals(str)) ? "mid" : "ok";
    }

    private static int riskRank(String str) {
        if ("высокий".equals(str)) {
            return 3;
        }
        if ("средний".equals(str)) {
            return 2;
        }
        return "неизвестно".equals(str) ? 1 : 0;
    }

    private static String riskWord(Item item) {
        if ("превышение".equals(item.status)) {
            return "превышен лимит";
        }
        if ("высокий".equals(item.risk)) {
            return "высокий риск";
        }
        if ("средний".equals(item.risk)) {
            return "средний риск";
        }
        return "неизвестно".equals(item.risk) ? "данных мало" : "риск низкий";
    }

    private static String roleTag(String str) {
        if (str == null || str.isEmpty() || "ингредиент".equals(str)) {
            return "";
        }
        return " (" + str + ")";
    }

    private String ruName(String str) {
        String str2 = this.ruNames.get(nz(str).toLowerCase(Locale.ROOT).trim());
        return (str2 == null || str2.isEmpty()) ? nz(str) : str2;
    }

    private static String scoreClass(int i) {
        if (i >= 80) {
            return "good";
        }
        return i >= 55 ? "warn" : "bad";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAiReview(String str) {
        String str2;
        this.aiBusy = false;
        refreshAiPanel();
        if (this.aiShort) {
            this.aiBody.setText("Модель трижды оборвала ответ на первых словах — показывать недописанное не буду. Попробуй ещё раз, когда телефон не нагружен; достоверный разбор лежит во вкладке «Разбор».");
            return;
        }
        if (str != null && !str.trim().isEmpty()) {
            StringBuilder sb = new StringBuilder(cutToSentence(str));
            if (inventedNumbers(sb.toString())) {
                sb.append("\n\nВнимание: в тексте есть числа, которых нет в разборе, — им доверять не стоит.");
            }
            sb.append("\n\nЭто текст нейросети, а не вывод из баз: проверять по этикетке и по вкладке «Разбор».");
            this.aiBody.setText(sb.toString());
            return;
        }
        TextView textView = this.aiBody;
        if (LlamaModel.lastError.isEmpty()) {
            str2 = ".";
        } else {
            str2 = ": " + LlamaModel.lastError;
        }
        textView.setText("Модель ничего не ответила" + str2 + "\n\nРазбор без нейросети остался во вкладке «Разбор».");
    }

    private void showError(String str) {
        TextView textView = this.error;
        if (textView == null) {
            return;
        }
        textView.setText(str);
        this.error.setVisibility(0);
    }

    private void showHtml(String str) {
        this.lastHtml = str;
        WebView webView = this.resultWeb;
        if (webView == null) {
            Log.i("ScanBase", "showHtml: resultWeb == null");
            return;
        }
        if (str == null || str.isEmpty()) {
            Log.i("ScanBase", "showHtml: пустой html");
            return;
        }
        Log.i("ScanBase", "показываю разбор: " + str.length() + " знаков");
        if (this.autoAi && this.lastItems != null) {
            this.autoAi = false;
            this.urlBox.post(new AutoAiTask(this));
        }
        MainActivity$$ExternalSyntheticLambda8 mainActivity$$ExternalSyntheticLambda8 = new MainActivity$$ExternalSyntheticLambda8(webView, str);
        if (webView.getHeight() > 0) {
            mainActivity$$ExternalSyntheticLambda8.run();
        } else {
            webView.post(mainActivity$$ExternalSyntheticLambda8);
        }
    }

    private void showPage(boolean z) {
        this.web.setVisibility(z ? 0 : 8);
        this.resultWeb.setVisibility(z ? 8 : 0);
        this.pageShown = z;
        Button button = this.pageButton;
        if (button != null) {
            button.setText(z ? "Скрыть" : "Страница");
        }
    }

    private void showTab(int i) {
        if (this.aiPanel == null) {
            return;
        }
        this.currentTab = i;
        if (this.web != null && this.resultWeb != null) {
            showPage(false);
        }
        int i2 = i != 1 ? 0 : 1;
        this.content.setVisibility(i2 != 0 ? 8 : 0);
        this.aiPanel.setVisibility(i2 == 0 ? 8 : 0);
        this.tabRead.setTypeface(null, i2 ^ 1);
        this.tabAi.setTypeface(null, i2);
        this.tabRead.setBackgroundColor(i2 != 0 ? -12566464 : -1);
        this.tabAi.setBackgroundColor(i2 != 0 ? -1 : -12566464);
        if (i2 != 0) {
            refreshAiPanel();
        }
    }

    private void showText(String str) {
        showHtml("<!doctype html><html><head><meta charset='utf-8'><meta name='viewport' content='width=device-width, initial-scale=1'><style>body{font-family:sans-serif;margin:14px;color:#111;background:#f7f8fb}</style></head><body>" + esc(str).replace("\n", "<br>") + "</body></html>");
    }

    private static List<String> splitNames(String str) {
        return Rules.splitNames(str);
    }

    /* Состав бывает и «соль, сахар, мука», и «соль сахар мука». Разбиваем кусок
       по пробелу только когда он сам не известен базе, а каждое слово — известно. */
    private List<String> expandBySpace(List<String> names) {
        return Rules.expandBySpace(names, this::isKnownName);
    }

    private boolean isKnownName(String str) {
        if (this.nameIdxSet != null) {
            String n = Rules.norm(str);
            if (this.nameIdxSet.contains(n)) return true;
            String t = Rules.norm(Rules.translit(str));
            if (!t.equals(n) && this.nameIdxSet.contains(t)) return true;
        } else {
            if (findDbName(str) != null) return true;
        }
        return Rules.ALIAS.containsKey(Rules.norm(str));
    }

    /* Состав через пробел: splitNames отдаёт его одним куском, а expandBySpace
       дробит на известные компоненты. Если после дробления названий стало больше —
       перед нами список ингредиентов, а не название товара и не мусор. */
    private boolean isIngredientList(String str) {
        List<String> base = splitNames(str);
        return expandBySpace(base).size() > base.size();
    }

    private void startAiReview() {
        String str = this.lastComposition;
        if (str == null || str.isEmpty()) {
            this.aiStatus.setText("Сначала откройте ссылку на товар: состава нет, писать не по чему.");
            if (this.loading) {
                return;
            }
            showError("Состава нет — проверьте ссылку");
            return;
        }
        if (this.lastItems == null) {
            this.aiBody.setText("Сначала открой вкладку «Разбор» и нажми «Проверить» — модели нужен разбор, на который опираться.");
            return;
        }
        if (this.aiBusy) {
            return;
        }
        this.aiBusy = true;
        this.aiStop = false;
        refreshAiPanel();
        this.aiBody.setText("Пишу разбор…");
        new Thread(new AiReviewTask(this, aiPrompt(this.aiBudgetSec), this.aiBudgetSec)).start();
    }

    private void startDownload() {
        this.aiBody.setText("");
        this.aiButton.setText("Отменить");
        this.aiStatus.setText("Начинаю загрузку…");
        this.downloader.start(this, new DownloadCallback(this));
    }

    private static String statusClass(Item item) {
        if ("превышение".equals(item.status) || "высокий".equals(item.risk)) {
            return "bad";
        }
        return ("с ограничением".equals(item.status) || "средний".equals(item.risk) || "не найден".equals(item.status)) ? "mid" : "ok";
    }

    static String statusPlain(String str) {
        if ("CosIng".equals(str)) {
            return "в реестре ЕС";
        }
        if ("добавка".equals(str)) {
            return "пищевая добавка";
        }
        if ("еда".equals(str)) {
            return "пищевой продукт";
        }
        if ("ограничен".equals(str) || "с ограничением".equals(str)) {
            return "с ограничением";
        }
        if ("превышение".equals(str)) {
            return "превышен лимит";
        }
        if ("по категории".equals(str)) {
            return "похоже по назначению";
        }
        if ("бытовая химия".equals(str)) {
            return "средство для дома";
        }
        if ("материал".equals(str)) {
            /* Раньше здесь было «не косметика» — статус-отказ. Материал такой
               же компонент, как и ингредиент: показываем его как материал. */
            return "материал";
        }
        if ("не найден".equals(str)) {
            return "нужно проверить";
        }
        return "найден".equals(str) ? "в базе" : nz(str);
    }

    private static String stripPercent(String str) {
        return nz(str).replaceAll("(?iu)\\s*\\d+(?:[.,]\\d+)?\\s*%\\s*", " ").replaceAll("\\s+", " ").trim();
    }

    private static LinearLayout.LayoutParams tabSlot() {
        return new LinearLayout.LayoutParams(0, -2, 1.0f);
    }

    private static double textPrice(String str) {
        Matcher matcher = PRICE.matcher(str.replace((char) 160, ' '));
        if (!matcher.find()) {
            return 0.0d;
        }
        try {
            return Double.parseDouble(matcher.group(1).replaceAll("[\\s\\u00a0]", "").replace(',', '.'));
        } catch (Exception unused) {
            return 0.0d;
        }
    }

    private static String textPriceText(String str) {
        Matcher matcher = PRICE.matcher(str.replace((char) 160, ' '));
        return matcher.find() ? matcher.group().replaceAll("\\s+", " ").replaceAll("[.,;:]+$", "").trim() : "";
    }

    private static String textTitle(String str) {
        String strReplaceAll = str.split("(?iu)\\b(?:состав|ingredients)\\s*:", 2)[0].trim().replaceAll("^[\\s,;.:\\-]+", "").replaceAll("[\\s,;.:\\-]+$", "");
        if (strReplaceAll.isEmpty()) {
            return "Товар из вставленного текста";
        }
        return strReplaceAll.length() > 120 ? strReplaceAll.substring(0, 120).trim() : strReplaceAll;
    }

    /** Кнопка «Страница»: показать сайт, повторный тап — скрыть и вернуться на прежнюю вкладку. */
    private void togglePage() {
        if (nz(this.web.getUrl()).isEmpty()) {
            showError("Страницу ещё не открывали: вставьте ссылку и нажмите «Проверить».");
            return;
        }
        if (this.pageShown) {
            showTab(this.tabBeforePage);
            showError("Вернуться к сайту — снова «Страница».");
            return;
        }
        this.tabBeforePage = this.currentTab;
        showTab(0);
        showPage(true);
        showError("Скрыть страницу — снова нажмите «Страница».");
    }

    private static String translit(String str) {
        return Rules.translit(str);
    }

    private static String unwrap(String str) {
        if (str == null) {
            return "{}";
        }
        String strTrim = str.trim();
        if (!strTrim.startsWith("\"") || !strTrim.endsWith("\"")) {
            return strTrim;
        }
        try {
            return new JSONArray("[" + strTrim + "]").getString(0);
        } catch (Exception unused) {
            return strTrim.substring(1, strTrim.length() - 1).replace("\\\"", "\"").replace("\\/", "/").replace("\\n", "\n");
        }
    }

    private static String verdict(int i) {
        if (i >= 80) {
            return "хороший вариант";
        }
        if (i >= 65) {
            return "нормальный вариант, есть нюансы";
        }
        return i >= 45 ? "спорный вариант" : "не рекомендую";
    }

    private static String[] verdictVariants(int i, int i2, int i3, String str, int i4, String str2) {
        return Rules.verdictVariants(i, i2, i3, str, i4, str2);
    }

    private static LinearLayout.LayoutParams weight() {
        return new LinearLayout.LayoutParams(-1, 0, 1.0f);
    }

    private static LinearLayout.LayoutParams wrap() {
        return new LinearLayout.LayoutParams(-2, -2);
    }

    public boolean lambda$onCreate$0(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 2 && (keyEvent == null || keyEvent.getKeyCode() != 66 || keyEvent.getAction() != 0)) {
            return false;
        }
        open();
        return true;
    }

    public void lambda$onCreate$1(View view) {
        open();
    }

    public void lambda$onCreate$2(View view) {
        CharSequence charSequenceCoerceToText;
        try {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService("clipboard");
            ClipData primaryClip = clipboardManager == null ? null : clipboardManager.getPrimaryClip();
            if (primaryClip != null && primaryClip.getItemCount() > 0 && (charSequenceCoerceToText = primaryClip.getItemAt(0).coerceToText(this)) != null) {
                this.urlBox.setText(charSequenceCoerceToText.toString().trim());
            }
        } catch (Exception unused) {
        }
    }

    public void lambda$onCreate$3(View view) {
        togglePage();
    }

    public void lambda$onCreate$4(View view) {
        openInApp();
    }

    public void lambda$runExtraction$0() {
        this.web.evaluateJavascript("__extractJson()", new MainActivity$$ExternalSyntheticLambda12(this));
    }

    /* JADX INFO: renamed from: lambda$runExtraction$1, reason: merged with bridge method [inline-methods] */
    public void lambda$lambda$runExtraction$0$0(String str) {
        try {
            JSONObject jSONObject = new JSONObject(unwrap(str));
            String strOptString = jSONObject.optString("composition", "");
            Log.i("ScanBase", "timing " + jSONObject.optString("timing", ""));
            Log.i("ScanBase", "attempt=" + this.extractAttempt + " late=" + this.lateRound + " via=" + jSONObject.optString("via") + " len=" + jSONObject.optInt("len") + " nodes=" + jSONObject.optInt("nodes") + " blocked=" + jSONObject.optBoolean("blocked") + " compLen=" + strOptString.length() + " title=\"" + trim(jSONObject.optString("title", ""), 50) + "\" url=" + jSONObject.optString("url"));
            String strApi = jSONObject.optString("api", "");
            boolean zWaiting = "sent".equals(strApi) || "pending".equals(strApi);
            int iCap = zWaiting ? 9 : 5;
            if (!strOptString.isEmpty() || (!zWaiting && (jSONObject.optBoolean("blocked", false) || notFoundReason(jSONObject) != null))) {
                report(jSONObject);
                return;
            }
            if (this.extractAttempt >= iCap) {
                /* Маркет дорисовывает карточку в разы дольше отпущенных попыток: малое число
                   узлов значит, что страница не битая, а ещё не собралась, — даём ей длинные
                   ожидания вместо ложного «состава нет». Узлы, а не len: len меряет только
                   заголовок и при SPA-дорисовке не растёт. */
                if (jSONObject.optInt("nodes", 0) < 1500 && this.lateRound < 3) {
                    this.lateRound++;
                    this.extractAttempt = 0;
                    showText("Страница ещё грузится — жду (" + this.lateRound + "/3)…");
                    this.web.postDelayed(new MainActivity$$ExternalSyntheticLambda15(this), 20000L);
                    return;
                }
                report(jSONObject);
                return;
            }
            int i = this.extractAttempt + 1;
            this.extractAttempt = i;
            showText("Ищу состав на странице… попытка " + (i + 1));
            if (zWaiting) {
                Log.i("ScanBase", "ответ площадки ещё в дороге — жду, DOM-прозу не выдаю");
            }
            this.web.postDelayed(new MainActivity$$ExternalSyntheticLambda14(this), zWaiting ? 1000L : 200L);
            this.web.postDelayed(new MainActivity$$ExternalSyntheticLambda15(this), zWaiting ? 1500L : 1200L);
        } catch (Exception e) {
            showText("Страница не отдала содержимое: " + e.getMessage());
        }
    }

    /* JADX INFO: renamed from: lambda$runExtraction$2, reason: merged with bridge method [inline-methods] */
    public void lambda$lambda$runExtraction$1$0() {
        this.web.evaluateJavascript("__scanTabs()", null);
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        WebView webView = this.web;
        if (webView != null && webView.getVisibility() == 0 && this.web.canGoBack()) {
            this.web.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            this.db = SQLiteDatabase.openDatabase(prepareDb().getAbsolutePath(), null, 1);
            this.extractJs = readAsset("extract.js");
        } catch (Exception unused) {
            this.extractJs = "";
        }
        loadAliasCache();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(24, 24, 24, 24);
        EditText editText = new EditText(this);
        this.urlBox = editText;
        editText.setHint("Ссылка на товар, артикул или текст состава");
        this.urlBox.setInputType(16);
        this.urlBox.setSingleLine(true);
        this.urlBox.setImeOptions(2);
        this.urlBox.setOnEditorActionListener(new MainActivity$$ExternalSyntheticLambda0(this));
        linearLayout.addView(this.urlBox, match());
        TextView textView = new TextView(this);
        this.error = textView;
        textView.setTextColor(-4971752);
        this.error.setTextSize(12.0f);
        this.error.setVisibility(8);
        this.error.setText("Можно проверять только Ozon, Wildberries и Яндекс Маркет.");
        linearLayout.addView(this.error, match());
        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(0);
        Button button = new Button(this);
        button.setText("Проверить");
        button.setAllCaps(false);
        button.setTextSize(12.0f);
        button.setOnClickListener(new MainActivity$$ExternalSyntheticLambda1(this));
        linearLayout2.addView(button, wrap());
        Button button2 = new Button(this);
        button2.setText("Вставить");
        button2.setAllCaps(false);
        button2.setTextSize(12.0f);
        button2.setOnClickListener(new MainActivity$$ExternalSyntheticLambda2(this));
        linearLayout2.addView(button2, wrap());
        Button button3 = new Button(this);
        button3.setText("Страница");
        this.pageButton = button3;
        button3.setAllCaps(false);
        button3.setTextSize(12.0f);
        button3.setOnClickListener(new MainActivity$$ExternalSyntheticLambda3(this));
        linearLayout2.addView(button3, wrap());
        Button button4 = new Button(this);
        button4.setText("В приложение");
        button4.setAllCaps(false);
        button4.setTextSize(12.0f);
        button4.setOnClickListener(new MainActivity$$ExternalSyntheticLambda4(this));
        linearLayout2.addView(button4, wrap());
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
        horizontalScrollView.addView(linearLayout2, wrap());
        linearLayout.addView(horizontalScrollView, match());
        WebView webView = new WebView(this);
        this.resultWeb = webView;
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        this.resultWeb.setBackgroundColor(-526085);
        WebView webView2 = new WebView(this);
        this.web = webView2;
        WebSettings settings2 = webView2.getSettings();
        settings2.setJavaScriptEnabled(true);
        settings2.setDomStorageEnabled(true);
        settings2.setDatabaseEnabled(true);
        settings2.setLoadWithOverviewMode(true);
        settings2.setUseWideViewPort(true);
        settings2.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings2.setSupportZoom(true);
        settings2.setBuiltInZoomControls(true);
        settings2.setDisplayZoomControls(false);
        settings2.setMixedContentMode(0);
        settings2.setAllowFileAccess(false);
        settings2.setJavaScriptCanOpenWindowsAutomatically(false);
        settings2.setUserAgentString("Mozilla/5.0 (Linux; Android 13; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Mobile Safari/537.36");
        try {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this.web, true);
        } catch (Exception unused2) {
        }
        WebView.setWebContentsDebuggingEnabled(true);
        this.web.setWebViewClient(new PageHost(this, new MainActivity$$ExternalSyntheticLambda5(this)));
        this.web.setVisibility(8);
        FrameLayout frameLayout = new FrameLayout(this);
        this.content = frameLayout;
        frameLayout.addView(this.resultWeb, new FrameLayout.LayoutParams(-1, -1));
        this.content.addView(this.web, new FrameLayout.LayoutParams(-1, -1));
        LinearLayout.LayoutParams layoutParamsWeight = weight();
        layoutParamsWeight.weight = 1.0f;
        linearLayout.addView(this.content, layoutParamsWeight);
        buildAiPanel(linearLayout);
        buildTabs(linearLayout);
        setContentView(linearLayout);
        showTab(0);
        showText("Введите ссылку или артикул товара");
        loadRuNames();
        handleIntent(getIntent());
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    /* Номер товара в ссылке WB: /catalog/<nm>/detail.aspx, /product/<nm>/ и
       старые /catalog/<nm>.detail.aspx. */
    static String wbNm(String strUrl) {
        String strLower = nz(strUrl).toLowerCase(Locale.ROOT);
        if (!strLower.contains("wildberries") && !strLower.contains("wb.ru")) {
            return "";
        }
        Matcher matcher = Pattern.compile("(?:catalog|product|item)/[^/]*?(\\d{6,})").matcher(nz(strUrl));
        return matcher.find() ? matcher.group(1) : "";
    }

    /* Карточку WB просим и из страницы, и нативно. Страница товара приходит
       заглушкой без og:image — тогда JS считает резервный хост корзины, а его
       режет CSP страницы, и состав не доходит (живьём: мыло 1256652084 и
       кружка 877876999 давали compLen=0 при 1178 узлах в DOM). Нативный запрос
       идёт мимо CSP, а CDN корзины открыт любому:
       access-control-allow-origin: * и антибота нет. */
    void fetchWbCard(String strUrl) {
        this.wbCard = "";
        final String strNm = wbNm(strUrl);
        if (strNm.isEmpty()) {
            return;
        }
        long jNm = Long.parseLong(strNm);
        final String strPath = "/vol" + (jNm / 100000) + "/part" + (jNm / 1000) + "/" + strNm + "/info/ru/card.json";
        new Thread(() -> fetchCardPath(strNm, strPath)).start();
    }

    /* Нативно полученную карточку кладём в то же окно, куда её положил бы запрос
       из страницы: разбор не различает эти два пути. Кэш разбора при этом
       сбрасываем — __wbJson запоминает пустой ответ как null и на следующих
       попытках уже не перечитывает карточку. */
    String cardPre() {
        String strCard = this.wbCard;
        if (strCard == null || strCard.isEmpty()) {
            return "";
        }
        return "window.__apiRaw=" + JSONObject.quote(strCard) + ";window.__apiSent='sent';"
                + "window.__wbJ=undefined;window.__wbComp=null;window.__wbPartsC=undefined;";
    }

    /* Ответ приходит вне главного потока, поле volatile: читают его при разборе. */
    void fetchCardPath(String strNm, String strPath) {
        for (String strHost : new String[]{"mow-basket-cdn-01.geobasket.ru", "mow-basket-cdn-31.geobasket.ru", "basket-05.wbbasket.ru"}) {
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) new URL("https://" + strHost + strPath).openConnection();
                httpURLConnection.setConnectTimeout(4000);
                httpURLConnection.setReadTimeout(6000);
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                if (httpURLConnection.getResponseCode() != 200) {
                    continue;
                }
                java.io.InputStream inputStream = httpURLConnection.getInputStream();
                java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
                byte[] bArr = new byte[8192];
                while (true) {
                    int i = inputStream.read(bArr);
                    if (i <= 0) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, i);
                }
                inputStream.close();
                String strBody = byteArrayOutputStream.toString("UTF-8");
                if (strBody.startsWith("{")) {
                    this.wbCard = strBody;
                    Log.i("ScanBase", "карточка WB нативно: " + strBody.length() + " знаков, " + strHost);
                    return;
                }
            } catch (Exception unused) {
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }
        Log.i("ScanBase", "карточка WB нативно не получена: " + strNm);
    }

    /* EAN-8/EAN-13: ровно 8 или 13 цифр и верная контрольная цифра. Это отличает
       штрихкод от произвольного артикула маркетплейса (тот любой длины). */
    static boolean isEan(String s) {
        if (s == null) {
            return false;
        }
        String t = s.trim();
        int n = t.length();
        if (n != 8 && n != 13) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (!Character.isDigit(t.charAt(i))) {
                return false;
            }
        }
        int[] w = n == 13 ? new int[]{1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3} : new int[]{3, 1, 3, 1, 3, 1, 3};
        int sum = 0;
        for (int i = 0; i < w.length; i++) {
            sum += (t.charAt(i) - '0') * w[i];
        }
        return (10 - sum % 10) % 10 == (t.charAt(n - 1) - '0');
    }

    /* Штрихкод → реальный состав из Open Beauty/Food Facts (тот же report, что и
       для текста). Не нашли — откат к артикулу маркетплейса. Сеть вне главного
       потока, разбор возвращаем через runOnUiThread. */
    private void handleBarcode(String strEan) {
        this.loading = true;
        showText("Ищу товар по штрихкоду " + esc(strEan) + " …");
        new Thread(() -> {
            final String[] res = fetchBarcode(strEan);
            runOnUiThread(() -> {
                loading = false;
                if (res != null && !res[1].isEmpty()) {
                    try {
                        JSONObject j = new JSONObject();
                        j.put("composition", res[1]);
                        j.put("title", res[0]);
                        j.put("url", "штрихкод " + strEan);
                        j.put("via", "barcode");
                        j.put("len", res[1].length());
                        report(j);
                    } catch (Exception e) {
                        showError("Не удалось разобрать ответ по штрихкоду.");
                    }
                    return;
                }
                String strArticle = articleUrl(strEan);
                if (!strArticle.isEmpty()) {
                    urlBox.setText(strArticle);
                    open();
                } else {
                    showError("Товар со штрихкодом " + strEan + " не найден в Open Beauty/Food Facts, и как артикул маркетплейса не открывается.");
                }
            });
        }).start();
    }

    /* OBF (косметика) в приоритете, затем OFF (еда). Ответ v2 вложен: товар под
       ключом "product"; проекция ?fields= ненадёжна, берём полный ответ. Английский
       состав в приоритете, иначе общий ingredients_text. */
    String[] fetchBarcode(String strEan) {
        for (String strHost : new String[]{"world.openbeautyfacts.org", "world.openfoodfacts.org"}) {
            String strBody = httpGet("https://" + strHost + "/api/v2/product/" + strEan);
            if (strBody == null || !strBody.startsWith("{")) {
                continue;
            }
            try {
                JSONObject j = new JSONObject(strBody);
                if (j.optInt("status", 0) == 0) {
                    continue;
                }
                JSONObject p = j.optJSONObject("product");
                if (p == null) {
                    continue;
                }
                String strComp = p.optString("ingredients_text_en", "");
                if (strComp.isEmpty()) {
                    strComp = p.optString("ingredients_text", "");
                }
                if (strComp.isEmpty()) {
                    continue;
                }
                Log.i("ScanBase", "штрихкод " + strEan + " найден на " + strHost + ": " + strComp.length() + " знаков");
                return new String[]{p.optString("product_name", ""), strComp};
            } catch (Exception unused) {
            }
        }
        return null;
    }

    private String httpGet(String strUrl) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(strUrl).openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(8000);
            httpURLConnection.setRequestProperty("User-Agent", "ScanBase/1.0");
            if (httpURLConnection.getResponseCode() != 200) {
                return null;
            }
            java.io.InputStream inputStream = httpURLConnection.getInputStream();
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            byte[] bArr = new byte[8192];
            while (true) {
                int i = inputStream.read(bArr);
                if (i <= 0) {
                    break;
                }
                out.write(bArr, 0, i);
            }
            inputStream.close();
            return out.toString("UTF-8");
        } catch (Exception unused) {
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    /* Запрос к API площадки стартует вместе с навигацией и идёт мимо главного потока,
       пока страница рендерится. Повторные вызовы гасит флаг __apiSent внутри extract.js. */
    void kickApi() {
        WebView webView = this.web;
        if (webView == null || this.extractJs.isEmpty()) {
            return;
        }
        webView.evaluateJavascript(cardPre() + this.extractJs + "\n__kickApi()", null);
    }

    /* JADX INFO: renamed from: runExtraction, reason: merged with bridge method [inline-methods] */
    public void lambda$lambda$runExtraction$1$1() {
        if (this.extractJs.isEmpty()) {
            showText("Не загружен модуль разбора.");
            return;
        }
        this.loading = true;
        this.web.evaluateJavascript(cardPre() + this.extractJs, null);
        this.web.evaluateJavascript("__kickApi()", null);
        this.web.evaluateJavascript("__scanTabs()", null);
        this.web.postDelayed(new MainActivity$$ExternalSyntheticLambda6(this), this.extractAttempt == 0 ? 900L : 1200L);
    }

    /* JADX INFO: renamed from: startExtraction, reason: merged with bridge method [inline-methods] */
    public void lambda$onCreate$5() {
        String strPathOf = pathOf(nz(this.web.getUrl()));
        if (!strPathOf.isEmpty() && strPathOf.equals(this.rejectedPath) && SystemClock.elapsedRealtime() - this.rejectedAt < 8000) {
            Log.i("ScanBase", "ссылка уже отбита, повторный разбор не запускаю");
        } else {
            this.extractAttempt = 0;
            this.lateRound = 0;
            lambda$lambda$runExtraction$1$1();
        }
    }

    public String trim(String str, int i) {
        if (str == null) {
            return "";
        }
        String strTrim = str.trim();
        if (strTrim.length() <= i) {
            return strTrim;
        }
        return strTrim.substring(0, i) + "…";
    }
}
