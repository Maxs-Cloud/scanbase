package ru.scanbase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: /home/karpov-m/work/scanbase/apk/unpacked/classes.dex */
public final class Rules {
    public static final HashMap<String, String> ALIAS = new HashMap<>();
    public static final String[][] ALIAS_RU;
    private static final char[] TR_FROM;
    private static final String[] TR_TO;

    static {
        String[][] strArr = {new String[]{"глицерин", "glycerin"}, new String[]{"токоферол", "tocopherol"}, new String[]{"диметикон", "dimethicone"}, new String[]{"ксилитол", "xylitol"}, new String[]{"цетеариловый спирт", "cetearyl alcohol"}, new String[]{"цетиловый спирт", "cetyl alcohol"}, new String[]{"стеариновая кислота", "stearic acid"}, new String[]{"кокамидопропил бетаин", "cocamidopropyl betaine"}, new String[]{"каприлилгликоль", "caprylyl glycol"}, new String[]{"салициловая кислота", "Salicylic acid"}, new String[]{"ниацинамид", "niacinamide"}, new String[]{"ретинол", "Retinol"}, new String[]{"сквалан", "squalane"}, new String[]{"мочевина", "urea"}, new String[]{"пантенол", "panthenol"}, new String[]{"аллантоин", "allantoin"}, new String[]{"кофеин", "caffeine"}, new String[]{"ментол", "Menthol"}, new String[]{"лецитин", "lecithin"}, new String[]{"ланолин", "lanolin"}, new String[]{"мёд", "mel"}, new String[]{"прополис", "propolis extract"}, new String[]{"глина", "kaolin"}, new String[]{"каолин", "kaolin"}, new String[]{"крахмал", "oryza sativa starch"}, new String[]{"рисовая мука", "oryza sativa powder"}, new String[]{"экстракт календулы", "calendula officinalis flower extract"}, new String[]{"календула", "calendula officinalis flower extract"}, new String[]{"ромашка", "chamomilla recutita flower extract"}, new String[]{"шалфей", "salvia officinalis leaf extract"}, new String[]{"экстракт шалфея", "salvia officinalis leaf extract"}, new String[]{"мята", "Mentha piperita oil"}, new String[]{"эвкалипт", "Eucalyptus Globulus Leaf Oil"}, new String[]{"экстракт эвкалипта", "Eucalyptus Globulus Leaf Oil"}, new String[]{"чайное дерево", "melaleuca alternifolia (tea tree) leaf"}, new String[]{"зверобой", "hypericum perforatum flower extract"}, new String[]{"экстракт зверобоя", "hypericum perforatum flower extract"}, new String[]{"кора дуба", "quercus robur bark extract"}, new String[]{"шиповник", "rosa canina fruit extract"}, new String[]{"экстракт шиповника", "rosa canina fruit extract"}, new String[]{"облепиха", "hippophae rhamnoides oil"}, new String[]{"масло облепихи", "hippophae rhamnoides oil"}, new String[]{"алоэ", "aloe barbadensis leaf juice"}, new String[]{"экстракт алоэ", "aloe barbadensis leaf juice"}, new String[]{"сок алоэ", "aloe barbadensis leaf juice"}, new String[]{"гель алоэ", "aloe barbadensis leaf juice"}, new String[]{"алоэ вера", "aloe barbadensis leaf juice"}, new String[]{"зелёный чай", "camellia sinensis leaf extract"}, new String[]{"экстракт зелёного чая", "camellia sinensis leaf extract"}, new String[]{"чёрный чай", "camellia sinensis leaf extract"}, new String[]{"овёс", "avena sativa kernel extract"}, new String[]{"овсянка", "avena sativa kernel extract"}, new String[]{"мука овсяная", "avena sativa kernel flour"}, new String[]{"экстракт овса", "avena sativa kernel extract"}, new String[]{"витамин е", "tocopherol"}, new String[]{"витамин а", "Retinol"}, new String[]{"витамин с", "ascorbic acid"}, new String[]{"витамин б3", "niacinamide"}, new String[]{"витамин б5", "panthenol"}, new String[]{"провитамин б5", "panthenol"}, new String[]{"витамин б12", "cyanocobalamin"}, new String[]{"биотин", "biotin"}, new String[]{"фолиевая кислота", "folic acid"}, new String[]{"глутаминовая кислота", "glutamic acid"}, new String[]{"глицин", "glycine"}, new String[]{"аргинин", "arginine"}, new String[]{"молочный сахар", "lactose"}, new String[]{"гиалуроновая кислота", "sodium hyaluronate"}, new String[]{"гиалуронка", "sodium hyaluronate"}, new String[]{"гиалурон", "hyaluronic acid"}, new String[]{"лимонная кислота", "citric acid"}, new String[]{"молочная кислота", "lactic acid"}, new String[]{"бензойная кислота", "benzoic acid"}, new String[]{"сорбат калия", "potassium sorbate"}, new String[]{"бензоат натрия", "Sodium Benzoate"}, new String[]{"гидроксид натрия", "sodium hydroxide"}, new String[]{"гидроксид калия", "Potassium hydroxide"}, new String[]{"хлорид натрия", "sodium chloride"}, new String[]{"соль", "sodium chloride"}, new String[]{"поваренная соль", "sodium chloride"}, new String[]{"сода", "sodium bicarbonate"}, new String[]{"сода пищевая", "sodium bicarbonate"}, new String[]{"бикарбонат натрия", "sodium bicarbonate"}, new String[]{"уксус", "acetic acid"}, new String[]{"уксусная кислота", "acetic acid"}, new String[]{"пчелиный воск", "cera alba"}, new String[]{"морская вода", "maris sal"}, new String[]{"спирт", "alcohol"}, new String[]{"диоксид титана", "Titanium dioxide"}, new String[]{"оксид цинка", "Zinc oxide"}, new String[]{"активированный уголь", "charcoal powder"}, new String[]{"угль активированный", "charcoal powder"}, new String[]{"масло виноградных косточек", "vitis vinifera seed oil"}, new String[]{"масло зародышей пшеницы", "triticum vulgare germ oil"}, new String[]{"масло чёрного тмина", "nigella sativa seed oil"}, new String[]{"масло грецкого ореха", "juglans regia seed oil"}, new String[]{"масло лесного ореха", "corylus avellana seed oil"}, new String[]{"кунжутовое масло", "sesamum indicum seed oil"}, new String[]{"масло кунжутное", "sesamum indicum seed oil"}, new String[]{"масло рыжика", "camelina sativa seed oil"}, new String[]{"масло расторопши", "silybum marianum seed oil"}, new String[]{"масло ши", "butyrospermum parkii butter"}, new String[]{"масло какао", "theobroma cacao seed butter"}, new String[]{"масло жожоба", "simmondsia chinensis seed oil"}, new String[]{"оливковое масло", "olea europaea fruit oil"}, new String[]{"подсолнечное масло", "helianthus annuus seed oil"}, new String[]{"масло подсолнечника", "helianthus annuus seed oil"}, new String[]{"кокосовое масло", "cocos nucifera oil"}, new String[]{"миндальное масло", "prunus amygdalus dulcis oil"}, new String[]{"льняное масло", "linum usitatissimum seed oil"}, new String[]{"касторовое масло", "ricinus communis seed oil"}, new String[]{"абрикосовое масло", "prunus armeniaca kernel oil"}, new String[]{"масло лаванды", "lavandula angustifolia oil"}, new String[]{"эфирное масло лаванды", "lavandula angustifolia oil"}, new String[]{"масло розы", "Rosa Damascena Flower Oil"}, new String[]{"масло иланг-иланга", "Cananga Odorata Flower Oil"}, new String[]{"масло мяты", "Mentha piperita oil"}, new String[]{"масло эвкалипта", "Eucalyptus Globulus Leaf Oil"}, new String[]{"экстракт ромашки", "chamomilla recutita flower extract"}, new String[]{"отвар ромашки", "chamomilla recutita flower extract"}, new String[]{"экстракт крапивы", "urtica dioica leaf extract"}, new String[]{"экстракт лопуха", "arctium lappa root extract"}, new String[]{"экстракт хмеля", "humulus lupulus extract"}, new String[]{"экстракт огурца", "cucumis sativus extract"}, new String[]{"экстракт винограда", "vitis vinifera fruit extract"}, new String[]{"экстракт граната", "punica granatum fruit extract"}, new String[]{"сок лимона", "citrus limon fruit extract"}, new String[]{"лаурилсульфат натрия", "sodium lauryl sulfate"}, new String[]{"содиум лаурил сульфат", "sodium lauryl sulfate"}, new String[]{"содиум лорет сульфат", "sodium laureth sulfate"}, new String[]{"сквалан растительный", "squalane"}, new String[]{"церамид", "ceramide np"}, new String[]{"коэнзим q10", "ubiquinone"}, new String[]{"экстракт прополиса", "propolis extract"}, new String[]{"мыло", "potassium cocoate"}, new String[]{"мёд натуральный", "mel"}};
        ALIAS_RU = strArr;
        for (String[] strArr2 : strArr) {
            ALIAS.put(norm(strArr2[0]), strArr2[1]);
        }
        /* Частые синонимы, которых нет в каталоге как отдельных имён: английские
           разговорные (glycerine, perfume) и французские формы с этикеток ЕС
           (acide citrique, gomme xanthane, beurre de karité). Значение — канон,
           который lookup() находит в каталоге. */
        String[][] strArrSyn = {
            new String[]{"glycerine", "glycerin"}, new String[]{"perfume", "parfum"},
            new String[]{"eau", "water"}, new String[]{"acide citrique", "citric acid"},
            new String[]{"gomme xanthane", "xanthan gum"}, new String[]{"beurre de karité", "butyrospermum parkii butter"},
            new String[]{"shea butter", "butyrospermum parkii butter"}, new String[]{"huile minérale", "paraffinum liquidum"},
            new String[]{"vitamine e", "tocopherol"}, new String[]{"vitamine c", "ascorbic acid"},
            new String[]{"allantoïne", "allantoin"}, new String[]{"alcool éthylique", "alcohol"},
            new String[]{"beurre de karite", "butyrospermum parkii butter"}, new String[]{"huile minerale", "paraffinum liquidum"}};
        for (String[] strArr3 : strArrSyn) {
            ALIAS.put(norm(strArr3[0]), strArr3[1]);
        }
        TR_FROM = new char[]{1072, 1073, 1074, 1075, 1076, 1077, 1105, 1078, 1079, 1080, 1081, 1082, 1083, 1084, 1085, 1086, 1087, 1088, 1089, 1090, 1091, 1092, 1093, 1094, 1095, 1096, 1097, 1098, 1099, 1100, 1101, 1102, 1103};
        TR_TO = new String[]{"a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "y", "", "e", "yu", "ya"};
    }

    private Rules() {
    }

    public static String norm(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : str.toLowerCase(new Locale("ru")).toCharArray()) {
            if ((c >= 'a' && c <= 'z') || ((c >= '0' && c <= '9') || ((c >= 1072 && c <= 1103) || c == 1105))) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static String nz(String str) {
        return str == null ? "" : str;
    }

    public static String pickVariant(String[] strArr, String str) {
        if (strArr == null || strArr.length == 0) {
            return "";
        }
        return strArr[(str == null ? 0 : str.hashCode() & Integer.MAX_VALUE) % strArr.length];
    }

    public static String productKey(String str, String str2, String str3) {
        ArrayList arrayList = new ArrayList(splitNames(nz(str2)));
        Collections.sort(arrayList);
        StringBuilder sb = new StringBuilder(norm(nz(str)));
        sb.append('|');
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            sb.append(norm((String) it.next()));
            sb.append(';');
        }
        sb.append('|');
        sb.append(norm(nz(str3)));
        return sb.toString();
    }

    public static List<String> splitNames(String str) {
        /* Ключ — название без процента и знаков препинания: «кожа 100%» и «кожа»
           это один компонент, а «соль» и «соль морская» — разные. Повтор едет из
           площадки часто: одно и то же стоит в поле «Состав» и в атрибуте
           материала, а в разборе читается как два разных компонента и раздувает
           число позиций. Из пары оставляем ту, где процент объявлен. */
        /* Перенос строки бывает двух родов: жёсткий перенос внутри одного имени
           (текст свёрстан по колонке: «Ethylhexy» + «lglycerin») и настоящая
           граница между компонентами. Склеиваем ТОЛЬКО перенос между строчными
           буквами — имена INCI начинаются с заглавной, поэтому «Aqua\nGlycerin»
           останется двумя, а «Ethylhexy\nlglycerin» соберётся в одно. Делаем это
           ДО правила про процент: оно само вставляет \n как разделитель, и те
           переводы склеивать нельзя. */
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        for (String str2 : str.replaceAll("(?iu)^\\s*(состав|ingredients)\\s*[:\\-]?\\s*", "").replaceAll("(?<=[a-zа-яё])\n(?=[a-zа-яё])", "").replaceAll("(?iu)(%)(\\s+)", "$1\n").split("[;,\n]|\\s{2,}")) {
            String strReplaceAll = str2.replaceAll("\\(.*?\\)", "").replaceAll("^[ .()№*]+|[ .()№*]+$", "").trim().replaceAll("\\s+", " ");
            if (strReplaceAll.length() > 2 && strReplaceAll.length() < 80 && (strReplaceAll.matches(".*[A-Za-zА-Яа-я]{3}.*") || strReplaceAll.matches("(?iu)^e\\d{3,4}$") || strReplaceAll.matches("(?iu)^ci\\s*\\d{3,5}$"))) {
                String strKey = nameKey(strReplaceAll);
                String strPrev = linkedHashMap.get(strKey);
                if (strPrev == null || (strPrev.indexOf(37) < 0 && strReplaceAll.indexOf(37) >= 0)) {
                    linkedHashMap.put(strKey, strReplaceAll);
                }
            }
        }
        return new ArrayList(linkedHashMap.values());
    }

    public static String nameKey(String str) {
        /* Снимаем только объявленную долю (число с знаком %): у добавок вроде
           E901/E903 цифры — это само название, их трогать нельзя. */
        return nz(str).toLowerCase(java.util.Locale.ROOT).replaceAll("(?iu)\\d+(?:[.,]\\d+)?\\s*%", "").replaceAll("[^a-zа-яё0-9]", "");
    }

    public static String translit(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : nz(str).toCharArray()) {
            int i = 0;
            while (true) {
                char[] cArr = TR_FROM;
                if (i >= cArr.length) {
                    i = -1;
                    break;
                }
                if (cArr[i] == c) {
                    break;
                }
                i++;
            }
            sb.append(i >= 0 ? TR_TO[i] : String.valueOf(c));
        }
        return sb.toString();
    }

    /* Состав подают по-разному: «соль, сахар, мука» и «соль сахар мука». По
       запятой/точке с запятой/переносу режем всегда, а одиночный пробел — нет,
       иначе «sodium chloride» рассыпалось бы на два несуществующих компонента.
       Здесь добираем второй случай: если кусок сам по себе НЕ известен базе, но
       КАЖДОЕ его слово по отдельности — известный компонент, то это перечисление
       через пробел, и его надо разбить. «sodium chloride» остаётся целым, потому
       что целиком это тоже известное имя, а «Свёкла сахар мука» дробится, так как
       целого такого компонента нет, а каждое слово есть. known — проверка «есть ли
       имя в базе/синонимах», её даёт вызывающий код. */
    public static List<String> expandBySpace(List<String> names, java.util.function.Predicate<String> known) {
        ArrayList<String> out = new ArrayList<>();
        for (String name : names) {
            String nz = nz(name);
            if (nz.indexOf(32) < 0 || known.test(nz)) {
                out.add(name);
                continue;
            }
            String[] words = nz.split("\\s+");
            int significant = 0;
            boolean allKnown = words.length >= 2;
            for (String word : words) {
                boolean sig = word.length() > 2 || word.matches("(?iu)^e\\d{3,4}$") || word.matches("(?iu)^ci\\s*\\d{3,5}$");
                if (sig) {
                    significant++;
                    if (!known.test(word)) {
                        allKnown = false;
                    }
                }
            }
            if (allKnown && significant >= 2) {
                for (String word : words) {
                    if (word.length() > 2 || word.matches("(?iu)^e\\d{3,4}$") || word.matches("(?iu)^ci\\s*\\d{3,5}$")) {
                        out.add(word);
                    }
                }
            } else {
                out.add(name);
            }
        }
        return out;
    }

    public static String[] verdictVariants(int i, int i2, int i3, String str, int i4, String str2) {
        String str3;
        String str4 = "";
        if (str2.isEmpty()) {
            str3 = "";
        } else {
            str3 = ", цена " + str2;
        }
        if (!str.isEmpty()) {
            str4 = " (" + str + ")";
        }
        if (i * 2 < i2) {
            return new String[]{"Мало данных: распознано " + i + " из " + i2 + ", остального нет в базе — точный вердикт дать нельзя" + str3 + ".", "Рано судить: в базе нашлось только " + i + " названий из " + i2 + str3 + ".", "Вердикт ненадёжен: " + (i2 - i) + " названий база не знает, распознано " + i + " из " + i2 + str3 + "."};
        }
        if (i3 >= 3) {
            return new String[]{"Не стоит: спорных " + i3 + str4 + ", оценка " + i4 + " из 100" + str3 + ".", "Лучше не брать: " + i3 + " спорных компонента" + str4 + ", оценка " + i4 + " из 100" + str3 + ".", "Спорное сочетание: " + i3 + " ограниченных компонента" + str4 + ", итог " + i4 + " из 100" + str3 + "."};
        }
        if (i3 > 0) {
            return new String[]{"Осторожно: спорных " + i3 + str4 + ", оценка " + i4 + " из 100" + str3 + ".", "В целом можно, но есть " + i3 + " спорный" + str4 + " — оценка " + i4 + " из 100" + str3 + ".", "Смотрите на спорное" + str4 + ": их " + i3 + ", общая оценка " + i4 + " из 100" + str3 + "."};
        }
        if (i4 >= 75) {
            return new String[]{"Брать можно: спорных нет, оценка " + i4 + " из 100" + str3 + ".", "Состав хороший: без спорных компонентов, " + i4 + " из 100" + str3 + ".", "Спокойно берите: спорных нет, оценка " + i4 + " из 100" + str3 + "."};
        }
        return new String[]{"Средний вариант: спорных нет, но оценка всего " + i4 + " из 100" + str3 + ".", "Неплохо, но без восторга: спорных нет, " + i4 + " из 100" + str3 + ".", "Обычный состав: спорных нет, оценка " + i4 + " из 100" + str3 + "."};
    }
}
