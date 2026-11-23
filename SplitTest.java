package ru.scanbase;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/* Разбивка состава, поданного по-разному: через запятую и через пробел.
   expandBySpace — чистая функция, база подменяется предикатом.
   javac -cp build/obj SplitTest.java
   java  -cp build/obj:. ru.scanbase.SplitTest                              */
public class SplitTest {

    static int fails = 0;
    static int total = 0;

    static void check(String what, boolean ok, String got) {
        total++;
        if (!ok) {
            fails++;
        }
        System.out.println((ok ? "ok   " : "FAIL ") + what + " -> " + got);
    }

    /* База знает эти имена (в нижнем регистре для удобства предиката). */
    static final List<String> KNOWN = Arrays.asList(
            "соль", "сахар", "мука", "вода", "свёкла",
            "sodium chloride", "aqua", "glycerin");

    static final Predicate<String> KN = new Predicate<String>() {
        public boolean test(String s) {
            return KNOWN.contains(s.toLowerCase(java.util.Locale.ROOT));
        }
    };

    static boolean eq(List<String> got, String... want) {
        return got.equals(Arrays.asList(want));
    }

    public static void main(String[] args) {
        /* Запятая уже режется splitNames, сюда приходит по одному имени. */
        List<String> a = Rules.expandBySpace(Arrays.asList("соль", "сахар", "мука"), KN);
        check("запятая: по одному остаётся как есть", eq(a, "соль", "сахар", "мука"), a.toString());

        /* Пробельный список дробится: целого нет, каждое слово есть. */
        List<String> b = Rules.expandBySpace(Arrays.asList("соль сахар мука"), KN);
        check("пробел: дробится на компоненты", eq(b, "соль", "сахар", "мука"), b.toString());

        /* Многословное имя, известное целиком, НЕ дробится. */
        List<String> c = Rules.expandBySpace(Arrays.asList("sodium chloride"), KN);
        check("известное целиком не дробится", eq(c, "sodium chloride"), c.toString());

        /* Неизвестное целиком и неизвестные слова — не трогаем. */
        List<String> d = Rules.expandBySpace(Arrays.asList("экзотик микс"), KN);
        check("неизвестное не дробится", eq(d, "экзотик микс"), d.toString());

        /* Частично известное (одно слово известно, другое нет) — не дробится. */
        List<String> e = Rules.expandBySpace(Arrays.asList("соль экзотик"), KN);
        check("частично известное не дробится", eq(e, "соль экзотик"), e.toString());

        /* Одиночное имя без пробела — как есть. */
        List<String> f = Rules.expandBySpace(Arrays.asList("вода"), KN);
        check("одиночное без пробела", eq(f, "вода"), f.toString());

        /* Смешанный список: дробится только пробельный неизвестный кусок. */
        List<String> g = Rules.expandBySpace(Arrays.asList("aqua", "соль сахар", "sodium chloride"), KN);
        check("смешанный список", eq(g, "aqua", "соль", "сахар", "sodium chloride"), g.toString());

        /* Шлюз open(): список через пробел обязан попасть в разбор, а не в
           «это не ссылка». Решение = после дробления названий стало больше. */
        check("шлюз: пробельный список — состав",
                gate("соль сахар мука", KN), "состав");
        check("шлюз: запятая — состав",
                gate("соль, сахар, мука", KN), "состав");
        check("шлюз: название товара — не состав",
                !gate("экзотик микс", KN), "не состав");
        check("шлюз: одно слово — не состав",
                !gate("соль", KN), "не состав");

        System.out.println(fails == 0 ? ("SplitTest: все " + total + " проверок зелёные")
                : ("SplitTest: ПРОВАЛЕНО " + fails + " из " + total));
        if (fails != 0) {
            System.exit(1);
        }
    }

    /* Решение шлюза open(): текст ведём в разбор, если это список через пробел
       (дробление добавило имена) ИЛИ есть явные разделители состава. */
    static boolean gate(String raw, Predicate<String> kn) {
        List<String> base = Rules.splitNames(raw);
        boolean split = Rules.expandBySpace(base, kn).size() > base.size();
        String low = raw.toLowerCase(java.util.Locale.ROOT);
        boolean markers = raw.contains(",") || raw.contains(";") || raw.contains("%")
                || raw.matches("(?iu).*\\d+\\s*%.*") || low.contains("состав") || low.contains("ingredients");
        return split || markers;
    }
}
