package ru.scanbase;

/* Штрихкод ≠ артикул: ловим только валидные EAN-8/EAN-13 с верной контрольной
   цифрой, чтобы не утаскивать произвольные номера артикулов в API штрихкодов.
   javac -cp android.jar:build/obj EanTest.java
   java  -cp android.jar:build/obj:. ru.scanbase.EanTest                         */
public class EanTest {
    static int fails = 0, total = 0;

    static void check(String s, boolean want) {
        total++;
        boolean got = MainActivity.isEan(s);
        boolean ok = got == want;
        if (!ok) fails++;
        System.out.println((ok ? "ok   " : "FAIL ") + s + " -> " + got + (ok ? "" : "  ожидали «" + want + "»"));
    }

    public static void main(String[] a) {
        check("5901234123457", true);   // эталонный EAN-13
        check("8410757001090", true);   // реальный Crema Manos из Open Beauty Facts
        check("4006381333931", true);
        check("96385074", true);        // EAN-8
        check("5901234123456", false);  // неверная контрольная
        check("96385075", false);       // EAN-8 неверная контрольная
        check("123456789012", false);   // 12 цифр — не EAN
        check("12345678901234", false); // 14 цифр
        check("12345678", false);       // 8 цифр, контрольная не сходится
        check("14453274", true);        // артикул WB, но формально валидный EAN-8 — это норма
        check("1234567a", false);
        check("", false);
        check(null, false);
        System.out.println(fails == 0 ? "EanTest: всё зелёное (" + total + ")"
                : "EanTest: ПРОВАЛОВ " + fails + " из " + total);
        if (fails > 0) System.exit(1);
    }
}
