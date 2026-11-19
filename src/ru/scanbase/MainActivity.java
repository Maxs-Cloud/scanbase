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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    private Button pageButton;
    private WebView resultWeb;
    private Button tabAi;
    private Button tabRead;
    private EditText urlBox;
    private WebView web;
    static final String[] TEXTILE = {"хлопок", "cotton", "лен", "linen", "эластан", "elastane", "спандекс", "spandex", "lycra", "лайкра", "полиэстер", "polyester", "полиэфир", "нейлон", "nylon", "полиамид", "polyamide", "акрил", "acrylic", "вискоза", "viscose", "модал", "modal", "лиоцелл", "lyocell", "ацетат", "acetate", "шерсть", "wool", "кашемир", "cashmere"};
    static final String[] NATURAL_MATERIAL = {"хлопок", "cotton", "лен", "linen", "вискоза", "viscose", "модал", "modal", "лиоцелл", "lyocell", "ацетат", "acetate"};
    static final String[] SYNTH_MATERIAL = {"полиэстер", "polyester", "полиэфир", "нейлон", "nylon", "полиамид", "polyamide", "акрил", "acrylic"};
    static final String[] ELASTIC_MATERIAL = {"эластан", "elastane", "спандекс", "spandex", "lycra", "лайкра"};
    static final String[] WOOL_MATERIAL = {"шерсть", "wool", "кашемир", "cashmere"};
    static final String[] HUMECTANT = {"glycerin", "глицерин", "hyaluron", "гиалурон", "panthenol", "пантенол", "urea", "мочевина", "betaine", "бетаин", "propylene glycol", "butylene glycol", "pentylene glycol", "propanediol", "sodium pca", "aloe", "алоэ"};
    static final String[] ACTIVE = {"niacinamide", "ниацинамид", "retinol", "ретинол", "peptide", "пептид", "ceramide", "церамид", "salicylic", "салицил", "azelaic", "азелаин", "vitamin c", "ascorbic", "аскорбин", "tocopherol", "токоферол", "centella", "центелла", "adenosine", "аденозин", "zinc", "цинк", "aha", "bha", "hyaluron", "гиалурон"};
    static final String[] EMOLLIENT = {"squalane", "сквалан", "caprylic", "триглицерид", "oil", "масло", "butyrospermum", "ши", "cetearyl", "цетеариловый", "isopropyl myristate", "dimethicone", "диметикон", "shea", "jojoba", "жожоба"};
    static final String[] SURFACTANT = {"sodium lauryl sulfate", "sodium laureth sulfate", "sls", "sles", "cocamidopropyl betaine"};
    static final String[] PRESERVATIVE = {"phenoxyethanol", "phenethyl alcohol", "ethylhexylglycerin", "methylparaben", "propylparaben", "benzyl alcohol", "sodium benzoate", "potassium sorbate"};
    static final String[] FRAGRANCE = {"parfum", "fragrance", "отдушка", "linalool", "limonene"};
    static final String[] COLORANT = {"ci 77", "ci 75", "ci 74", "ci 15", "ci 42"};
    static final String[] SILICONE = {"dimethicone", "диметикон", "cyclomethicone", "siloxane"};
    static final String[] POLYMER = {"acrylates", "acrylate", "vp copolymer", "polymer", "carbomer"};
    static final String[] SOLVENT = {"water", "aqua", "alcohol", "butylene glycol", "pentylene glycol"};
    static final String[] HIGH_RISK = {"methylisothiazolinone", "methylchloroisothiazolinone", "formaldehyde", "formalin", "imidazolidinyl urea", "diazolidinyl urea", "quaternium-15", "dmdm hydantoin", "sodium lauryl sulfate", "sodium laureth sulfate", "paraben"};
    static final String[] MED_RISK = {"parfum", "fragrance", "отдушка", "alcohol denat", "denatured alcohol", "ci 77", "ci 75", "peg-", "ppg-", "lanolin"};
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
    private String lastVerdict = "";
    private int currentTab = 0;
    private boolean loading = false;
    private String lastRequested = "";
    private long lastRequestedAt = 0;
    private String rejectedPath = "";
    private long rejectedAt = 0;
    private final Map<String, String> ruNames = new HashMap();
    private boolean aiBusy = false;
    private volatile boolean aiStop = false;
    private int aiBudgetSec = 120;
    private String lastTitle = "";
    private String lastComposition = "";
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
            long jCurrentTimeMillis = System.currentTimeMillis();
            MainActivity mainActivity = this.act;
            String strGenerate = LlamaModel.generate(mainActivity, this.prompt, 240, this.seconds, 7, new ReviewSink(mainActivity));
            Log.i("ScanBase", "отзыв ИИ за " + ((System.currentTimeMillis() - jCurrentTimeMillis) / 1000) + " с: " + strGenerate);
            this.act.aiBusy = false;
            MainActivity mainActivity2 = this.act;
            mainActivity2.runOnUiThread(new AiReviewUiTask(mainActivity2, strGenerate));
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

        PageHost(PageDone pageDone) {
            this.done = pageDone;
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
        List<String[]> law;

        private Row() {
            this.law = new ArrayList();
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
        StringBuilder sb = new StringBuilder("Товар: ");
        sb.append(cleanTitle(this.lastTitle.isEmpty() ? this.lastComposition : this.lastTitle));
        sb.append('\n');
        if (!this.lastPriceText.isEmpty()) {
            sb.append("Цена: ");
            sb.append(this.lastPriceText);
            sb.append('\n');
        }
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
        for (Item item : this.lastItems) {
            String strRu = ruName(stripPercent(item.name));
            if (!item.law.isEmpty()) {
                appendName(sb4, strRu + " — " + lawPlain(item.law.get(0)));
            }
            if ("высокий".equals(item.risk) || "превышение".equals(item.status)) {
                appendName(sb3, strRu + roleTag(item.role) + " (" + helpFor(item) + ")");
            } else if ("низкий".equals(item.risk)) {
                appendName(sb2, strRu + roleTag(item.role));
            }
        }
        sb.append("Что в составе полезное: ");
        sb.append(few(sb2.toString(), 6));
        sb.append("\nЧто вредное или спорное: ");
        sb.append(few(sb3.toString(), 5));
        sb.append("\nЮридические ограничения: ");
        sb.append(few(sb4.toString(), 4));
        sb.append("\nНапиши подробный разбор в семь предложений одним абзацем. Не называй бренд, торговое название и артикул — их не существует, есть только состав. Первым предложением определи по составу, что это за средство (крем, шампунь, гель для умывания, сыворотка, бытовая химия) и для какой кожи или задачи оно подходит. Дальше назови до пяти вредных или спорных компонентов и для каждого скажи, какую роль он играет и что может вызвать: раздражение, аллергию, сухость, забитые поры, споры о гормональном действии. Полезные компоненты упомяни коротко, без перечисления всего состава и без латиницы. Последним предложением скажи прямо, кому это средство не подойдёт и стоит ли его брать за эту цену.");
        return sb.toString();
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
            if (!rowLookup2.law.isEmpty()) {
                item.status = "ограничен";
            } else if (rowLookup2.catalog) {
                item.status = "CosIng";
            } else if (rowLookup2.food != null) {
                item.status = "добавка";
            } else {
                item.status = "найден";
            }
        }
        classify(item);
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
        String strDetectCategory = detectCategory(str, list);
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
        sb.append("<!doctype html><html><head><meta charset='utf-8'><meta name='viewport' content='width=device-width, initial-scale=1'><style>body{font-family:sans-serif;margin:12px;background:#f7f8fb;color:#111}h2{font-size:18px;margin:6px 0 10px}h3{font-size:15px;margin:0 0 8px}.muted{color:#667085;font-size:13px}.cards{display:flex;flex-wrap:wrap;gap:8px;margin:10px 0}.card{background:#fff;border-radius:14px;padding:10px 12px;box-shadow:0 1px 4px rgba(0,0,0,.08);min-width:104px;flex:1 1 104px}.score{font-size:20px;font-weight:700;margin-top:2px}.bar{height:6px;background:#e5e7eb;border-radius:999px;margin-top:6px;overflow:hidden}.fill{height:100%;border-radius:999px}.good{color:#1a7f37}.warn{color:#b8860b}.bad{color:#b42318}.fill.good{background:#1a7f37}.fill.warn{background:#f0b429}.fill.bad{background:#b42318}.hint{background:#fff;border-left:4px solid #2f80ed;border-radius:12px;padding:10px 12px;margin:10px 0;box-shadow:0 1px 4px rgba(0,0,0,.06)}.desc p{margin:0 0 8px}section{background:#fff;border-radius:16px;padding:12px;margin:12px 0;box-shadow:0 1px 4px rgba(0,0,0,.06)}details{border-top:1px solid #eee;padding:8px 0}summary{font-weight:600;cursor:pointer}.tablewrap{max-height:62vh;overflow:auto;border-radius:12px;box-shadow:0 1px 4px rgba(0,0,0,.06);background:#fff}table{width:100%;border-collapse:collapse}th,td{padding:8px;border-bottom:1px solid #eee;text-align:left;vertical-align:top;font-size:14px}th{background:#eef2f7;font-size:12px;color:#344054;position:sticky;top:0;z-index:1}tr.bad{background:#fff5f5}tr.mid{background:#fffaf0}.ok{color:#1a7f37}.mid{color:#b8860b}.bad{color:#b42318}.wait{background:#fff;border-radius:14px;padding:12px;margin:10px 0;box-shadow:0 1px 4px rgba(0,0,0,.06);color:#667085;font-size:14px} .small{font-size:12px} .flag{background:#fff;border-radius:14px;padding:10px 12px;margin:8px 0;box-shadow:0 1px 4px rgba(0,0,0,.07);border-left:4px solid #9aa4b2} .flag.bad{border-left-color:#b42318}.flag.warn{border-left-color:#f0b429}.flag.info{border-left-color:#2f80ed} .flagtop{display:flex;justify-content:space-between;align-items:baseline;gap:8px} .flagname{font-weight:700;font-size:15px} .flagrisk{font-size:12px;color:#667085;white-space:nowrap} .flagmeta{color:#344054;font-size:13px;margin-top:2px} .flaginci{color:#98a2b3;font-size:11px;margin-top:2px} .flaghelp{margin-top:6px;font-size:13px;color:#182230} .lawlist{margin:6px 0 0 18px;padding:0}.lawlist li{margin:3px 0;font-size:13px}</style><script>function scoreClass(v){v=Number(v)||0;return v>=75?'good':v>=50?'warn':'bad';}function setScore(id,v){var e=document.getElementById(id);if(!e)return;v=Math.max(0,Math.min(100,Math.round(Number(v)||0)));e.textContent=String(v);e.className='score '+scoreClass(v);var f=e.parentNode.querySelector('.fill');if(f){f.style.width=v+'%';f.className='fill '+scoreClass(v);}}function escHtml(s){return String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/\"/g,'&quot;');}function reveal(){var w=document.getElementById('wait');if(w&&w.parentNode)w.parentNode.removeChild(w);}</script></head><body><h2>");
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
        sb.append("%</div><div class='cards'>");
        card(sb, "useful", "Полезно", iClamp4);
        card(sb, "safe", "Безопасно", iClamp);
        card(sb, "cheap", "Дёшево", iClamp3);
        card(sb, "comfort", "Комфорт", iClamp2);
        card(sb, "overall", "Итог", iClamp10);
        card(sb, "", "Цена", str5.isEmpty() ? "—" : str5, d > 0.0d ? priceCheap(d, str4) : 0);
        boolean z4 = z;
        card(sb, "", "Доля пользы", percentShareLabel(d14, z4), (int) Math.round(d14));
        double d16 = d2;
        card(sb, "", "Доля риска", percentShareLabel(d16, z4), (int) Math.round(d16));
        sb.append("</div>");
        StringBuilder sb2 = new StringBuilder();
        if ("material".equals(str13)) {
            sb2.append("<p>Это состав материала.</p>");
        } else if ("cosmetics".equals(str13)) {
            sb2.append("<p>Это косметический или бытовой состав.</p>");
        } else {
            sb2.append("<p>Тип состава не распознан однозначно.</p>");
        }
        sb2.append("<p>Распознано ");
        sb2.append(i2);
        sb2.append(" из ");
        sb2.append(size);
        sb2.append(" компонентов.</p>");
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

    private static void classify(Item item) {
        String lowerCase = item.name.toLowerCase(Locale.ROOT);
        if (containsAny(lowerCase, TEXTILE)) {
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
        } else if (!item.catalog && item.law.isEmpty() && item.food == null) {
            item.role = "не распознан";
            item.risk = "неизвестно";
        } else {
            item.role = "ингредиент";
            item.risk = item.law.isEmpty() ? "низкий" : "высокий";
        }
        String[] strArr = HIGH_RISK;
        if (containsAny(lowerCase, strArr)) {
            item.risk = "высокий";
        }
        if (containsAny(lowerCase, MED_RISK) && "низкий".equals(item.risk)) {
            item.risk = "средний";
        }
        if (item.law.isEmpty()) {
            return;
        }
        double dMaxConc = maxConc(item.law);
        if (dMaxConc <= 0.0d || item.pct <= dMaxConc + 1.0E-4d) {
            item.status = "с ограничением";
            item.risk = containsAny(lowerCase, strArr) ? "высокий" : "средний";
        } else {
            item.status = "превышение";
            item.risk = "высокий";
        }
    }

    private String cleanTitle(String str) {
        String strTrim = nz(str).replaceAll("(?iu)\\s*[-–—:,]*\\s*(купить|заказать|цена|стоимость)[^|]{0,60}$", "").replaceAll("\\s*\\(\\d{4,}\\)\\s*$", "").replaceAll("[\\s,;.:|-]+$", "").trim();
        if (strTrim.isEmpty()) {
            strTrim = nz(str).trim();
        }
        return trim(strTrim, 90);
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

    private static int countMaterialWords(String str) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String str2 : TEXTILE) {
            if (sb.length() > 0) {
                sb.append('|');
            }
            sb.append(Pattern.quote(str2));
        }
        while (Pattern.compile("(?<![\\p{L}])(" + String.valueOf(sb) + ")(?![\\p{L}])", 66).matcher(str).find()) {
            i++;
        }
        return i;
    }

    private String currentUrl() {
        String strExtractUrl = extractUrl(this.urlBox.getText().toString());
        return !strExtractUrl.isEmpty() ? strExtractUrl : this.lastUrl;
    }

    private static String cutToSentence(String str) {
        String strTrim = str.trim();
        int iMax = Math.max(Math.max(strTrim.lastIndexOf(46), strTrim.lastIndexOf(33)), strTrim.lastIndexOf(63));
        if (iMax < 40 || iMax >= strTrim.length() - 6) {
            return strTrim;
        }
        return strTrim.substring(0, iMax + 1) + " …";
    }

    private static String detectCategory(String str, List<Item> list) {
        String lowerCase = str.toLowerCase(Locale.ROOT);
        int iCountMaterialWords = countMaterialWords(lowerCase);
        int i = 0;
        for (Item item : list) {
            if (item.role.contains("актив") || item.role.contains("увлажнение") || item.role.contains("смягчение") || item.role.contains("консерв") || item.role.contains("отдуш") || item.catalog) {
                i++;
            }
        }
        if (containsAny(lowerCase, new String[]{"aqua", "water", "вода", "glycerin", "глицерин", "parfum", "отдуш"})) {
            i++;
        }
        if (iCountMaterialWords > 0 && iCountMaterialWords >= i) {
            return "material";
        }
        if (i > 0) {
            return "cosmetics";
        }
        return iCountMaterialWords > 0 ? "material" : "unknown";
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
        this.nameIdx = arrayList;
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
        if (strNorm.length() < 5) {
            return null;
        }
        String strNorm2 = norm(translit(strNorm));
        int i = 0;
        String str2 = null;
        for (String str3 : list) {
            String strNorm3 = norm(str3);
            if (strNorm3.equals(strNorm)) {
                return str3;
            }
            if (strNorm3.length() + 1 >= strNorm.length() && strNorm3.startsWith(strNorm)) {
                i++;
                str2 = str3;
            }
            if (!strNorm2.equals(strNorm)) {
                String strNorm4 = norm(translit(strNorm3));
                if (strNorm4.equals(strNorm2)) {
                    return str3;
                }
                if (strNorm4.length() + 1 >= strNorm2.length() && strNorm4.startsWith(strNorm2)) {
                    i++;
                    str2 = str3;
                }
            }
            if (i > 1) {
                return null;
            }
        }
        if (i == 1) {
            return str2;
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
            this.urlBox.post(new MainActivity$$ExternalSyntheticLambda13(this));
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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(ruName(stripPercent(list.get(i))));
        }
        return sb.toString();
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

    private Row lookup(String str) {
        if (this.db == null) {
            return null;
        }
        String strNorm = norm(str);
        if (strNorm.length() < 4) {
            return null;
        }
        Row row = new Row();
        Cursor cursorRawQuery = this.db.rawQuery("SELECT system, annex, ref, max_conc FROM inci_index WHERE norm=?", new String[]{strNorm});
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        while (cursorRawQuery.moveToNext()) {
            if (linkedHashSet.add(cursorRawQuery.getString(0) + cursorRawQuery.getString(1) + cursorRawQuery.getString(2))) {
                List<String[]> list = row.law;
                String[] strArr = new String[4];
                strArr[0] = cursorRawQuery.getString(0);
                strArr[1] = cursorRawQuery.getString(1);
                strArr[2] = cursorRawQuery.getString(2);
                strArr[3] = cursorRawQuery.getString(3) == null ? "" : cursorRawQuery.getString(3);
                list.add(strArr);
            }
        }
        cursorRawQuery.close();
        Cursor cursorRawQuery2 = this.db.rawQuery("SELECT 1 FROM cosmetic_ingredients WHERE lower(name)=? LIMIT 1", new String[]{str.toLowerCase(Locale.ROOT)});
        row.catalog = cursorRawQuery2.moveToFirst();
        cursorRawQuery2.close();
        Cursor cursorRawQuery3 = this.db.rawQuery("SELECT e FROM food_additives WHERE lower(name)=? OR lower(trts_name)=? LIMIT 1", new String[]{str.toLowerCase(Locale.ROOT), str.toLowerCase(Locale.ROOT)});
        if (cursorRawQuery3.moveToFirst()) {
            row.food = cursorRawQuery3.getString(0);
        }
        cursorRawQuery3.close();
        if (row.law.isEmpty() && !row.catalog && row.food == null) {
            return null;
        }
        return row;
    }

    private static LinearLayout.LayoutParams match() {
        return new LinearLayout.LayoutParams(-1, -2);
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

    private static String notFoundReason(JSONObject jSONObject) {
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
        String strReplaceFirst = lowerCase2.replaceFirst("(?i)^https?://(www\\.)?", "");
        int iIndexOf = strReplaceFirst.indexOf(47);
        String strSubstring = iIndexOf >= 0 ? strReplaceFirst.substring(iIndexOf) : "";
        if (strSubstring.isEmpty() || "?".equals(strSubstring)) {
            return "ссылка выбросила на главную страницу — карточки товара нет";
        }
        String[] strArr2 = {"/search", "/modal", "/cart", "/basket", "/order", "/error", "/404", "/notfound", "/not-found", "/catalog?", "chrome-error://", "about:blank"};
        for (int i2 = 0; i2 < 12; i2++) {
            String str = strArr2[i2];
            if (strSubstring.startsWith(str) || strSubstring.contains(str)) {
                return "ссылка ведёт не на карточку товара, а в «" + strSubstring.substring(0, Math.min(strSubstring.length(), 28)) + "»";
            }
        }
        return null;
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
        if (strTrim.isEmpty()) {
            showError("Вставь ссылку Ozon, Wildberries или Яндекс Маркет, либо текст состава.");
            return;
        }
        String strExtractUrl = extractUrl(strTrim);
        String lowerCase = strTrim.toLowerCase(Locale.ROOT);
        if (!strExtractUrl.isEmpty() || strTrim.length() <= 20 || (!strTrim.contains(",") && !strTrim.contains(";") && !strTrim.contains("%") && !strTrim.matches("(?iu).*\\d+\\s*%.*") && !lowerCase.contains("состав") && !lowerCase.contains("ingredients"))) {
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
            showText("Открываю страницу и жду описание…");
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

    private void openOzon() {
        String str;
        String strCurrentUrl = currentUrl();
        if (strCurrentUrl.isEmpty() || !strCurrentUrl.toLowerCase(Locale.ROOT).contains("ozon")) {
            showError("Кнопка «В Ozon» работает только для ссылок ozon.ru.");
            return;
        }
        Matcher matcher = Pattern.compile("(\\d{6,})").matcher(strCurrentUrl);
        if (matcher.find()) {
            str = "https://www.ozon.ru/product/" + matcher.group(1) + "/";
        } else {
            str = strCurrentUrl;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent.setPackage("ru.ozon.app.android");
            intent.addFlags(268435456);
            startActivity(intent);
            this.lastUrl = strCurrentUrl;
            showText("Открываю Ozon-приложение…");
        } catch (Exception e) {
            try {
                showText("Не удалось открыть Ozon: " + e.getMessage());
            } catch (Exception unused) {
                Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(str));
                intent2.addFlags(268435456);
                startActivity(intent2);
                showText("Ozon-приложение не нашлось — открыл в браузере.");
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
            return file;
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
        if (strOptString.isEmpty() && strNotFoundReason != null) {
            this.rejectedPath = pathOf(strOptString2);
            this.rejectedAt = SystemClock.elapsedRealtime();
            Log.i("ScanBase", "ОТКАЗ: товар не найден — " + strNotFoundReason);
            showPage(false);
            showError("Товар не найден — проверьте ссылку");
            return;
        }
        if (strOptString.isEmpty() && jSONObject.optBoolean("blocked", false)) {
            showText("Площадка " + strReplaceAll + " показала проверку «вы не робот».\n\nОткрой эту же ссылку в обычном браузере и вставь текст состава в поле сверху — разберу его.");
            return;
        }
        if (strOptString.isEmpty()) {
            showText("На странице " + strReplaceAll + " не найдено поле «Состав».\nВсего текста: " + jSONObject.optInt("len") + " знаков.");
            return;
        }
        clearError();
        showPage(false);
        this.lastKey = productKey(jSONObject.optString("title", ""), strOptString, jSONObject.optString("priceText", ""));
        this.lastComposition = strOptString;
        List<String> listSplitNames = splitNames(strOptString);
        if (listSplitNames.isEmpty()) {
            showText("Состав найден, но не удалось разбить его на названия.\nРазбей его запятыми или открой вкладку «Отзыв ИИ» — там текст читает модель.");
            return;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = listSplitNames.iterator();
        int i = 0;
        while (it.hasNext()) {
            Item itemAnalyzeItem = analyzeItem(it.next());
            if (!"не найден".equals(itemAnalyzeItem.status)) {
                i++;
            }
            arrayList.add(itemAnalyzeItem);
        }
        this.lastJson = jSONObject;
        this.lastItems = arrayList;
        this.lastTitle = jSONObject.optString("title", "");
        this.lastPriceText = jSONObject.optString("priceText", "");
        this.lastMatched = i;
        this.lastTotal = listSplitNames.size();
        Log.i("ScanBase", "report via=" + strOptString3 + " host=" + strReplaceAll + " names=" + listSplitNames.size() + " matched=" + i + " price=" + jSONObject.optString("priceText", ""));
        showHtml(buildHtml(jSONObject, arrayList, strOptString, strReplaceAll, strOptString3, i, jSONObject.optDouble("price", 0.0d), jSONObject.optString("currency", ""), jSONObject.optString("priceText", ""), jSONObject.optString("description", "")));
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
    }

    private void showTab(int i) {
        if (this.aiPanel == null) {
            return;
        }
        this.currentTab = i;
        if (i == 0 && this.web != null && this.resultWeb != null) {
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

    private static String statusPlain(String str) {
        if ("CosIng".equals(str)) {
            return "в реестре ЕС";
        }
        if ("добавка".equals(str)) {
            return "пищевая добавка";
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
        if ("материал".equals(str)) {
            return "не косметика";
        }
        if ("не найден".equals(str)) {
            return "не опознан";
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

    /** Кнопка «Страница»: только показать сайт. Обратно — «Проверить» или вкладка «Разбор». */
    private void togglePage() {
        if (nz(this.web.getUrl()).isEmpty()) {
            showError("Страницу ещё не открывали: вставьте ссылку и нажмите «Проверить».");
            return;
        }
        showError("Смотрите сайт? Вернуться к разбору — «Проверить» или вкладка «Разбор».");
        showPage(true);
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
        openOzon();
    }

    public void lambda$runExtraction$0() {
        this.web.evaluateJavascript("__extractJson()", new MainActivity$$ExternalSyntheticLambda12(this));
    }

    /* JADX INFO: renamed from: lambda$runExtraction$1, reason: merged with bridge method [inline-methods] */
    public void lambda$lambda$runExtraction$0$0(String str) {
        try {
            JSONObject jSONObject = new JSONObject(unwrap(str));
            String strOptString = jSONObject.optString("composition", "");
            Log.i("ScanBase", "attempt=" + this.extractAttempt + " via=" + jSONObject.optString("via") + " len=" + jSONObject.optInt("len") + " blocked=" + jSONObject.optBoolean("blocked") + " compLen=" + strOptString.length() + " url=" + jSONObject.optString("url"));
            if (!strOptString.isEmpty() || this.extractAttempt >= 5 || jSONObject.optBoolean("blocked", false) || notFoundReason(jSONObject) != null) {
                report(jSONObject);
                return;
            }
            int i = this.extractAttempt + 1;
            this.extractAttempt = i;
            showText("Ищу состав на странице… попытка " + (i + 1));
            this.web.postDelayed(new MainActivity$$ExternalSyntheticLambda14(this), 200L);
            this.web.postDelayed(new MainActivity$$ExternalSyntheticLambda15(this), 1200L);
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
        editText.setHint("Ссылка на товар или текст состава");
        this.urlBox.setInputType(1048579);
        this.urlBox.setMinLines(2);
        this.urlBox.setMaxLines(5);
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
        button4.setText("В Ozon");
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
        this.web.setWebViewClient(new PageHost(new MainActivity$$ExternalSyntheticLambda5(this)));
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
        showText("Готово. Вставь ссылку на товар или сам текст состава и нажми «Проверить».");
        loadRuNames();
        handleIntent(getIntent());
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    /* JADX INFO: renamed from: runExtraction, reason: merged with bridge method [inline-methods] */
    public void lambda$lambda$runExtraction$1$1() {
        if (this.extractJs.isEmpty()) {
            showText("Не загружен модуль разбора.");
            return;
        }
        this.loading = true;
        this.web.evaluateJavascript(this.extractJs, null);
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
