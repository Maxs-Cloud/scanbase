package ru.scanbase;

/* Синонимы из Rules.ALIAS должны резолвиться в канон, который есть в каталоге.
   Проверка на уровне словаря (lookup по БД в JVM недоступен).
   javac -cp android.jar:build/obj SynonymTest.java
   java  -cp android.jar:build/obj:. ru.scanbase.SynonymTest                     */
public class SynonymTest {
    static int fails = 0, total = 0;

    static void check(String alias, String canon) {
        total++;
        String got = Rules.ALIAS.get(Rules.norm(alias));
        boolean ok = canon.equals(got);
        if (!ok) fails++;
        System.out.println((ok ? "ok   " : "FAIL ") + alias + " -> " + got + (ok ? "" : "  ожидали «" + canon + "»"));
    }

    public static void main(String[] a) {
        check("glycerine", "glycerin");
        check("perfume", "parfum");
        check("eau", "water");
        check("acide citrique", "citric acid");
        check("gomme xanthane", "xanthan gum");
        check("beurre de karité", "butyrospermum parkii butter");
        check("shea butter", "butyrospermum parkii butter");
        check("huile minérale", "paraffinum liquidum");
        check("vitamine e", "tocopherol");
        check("vitamine c", "ascorbic acid");
        // русские из базового списка не должны потеряться
        check("гиалуроновая кислота", "sodium hyaluronate");
        check("масло ши", "butyrospermum parkii butter");
        System.out.println(fails == 0 ? "SynonymTest: всё зелёное (" + total + ")"
                : "SynonymTest: ПРОВАЛОВ " + fails + " из " + total);
        if (fails > 0) System.exit(1);
    }
}
