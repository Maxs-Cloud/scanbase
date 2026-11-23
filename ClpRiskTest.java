package ru.scanbase;

/* CLP-классификация → потребительский риск: канцерогены/мутагены/репротоксиканты
   1A/1B и острая токсичность — высокий; сенсибилизаторы/раздражители/острая 4 —
   средний; только эко- и физические классы риск не двигают.
   javac -cp android.jar:build/obj ClpRiskTest.java
   java  -cp android.jar:build/obj:. ru.scanbase.ClpRiskTest                     */
public class ClpRiskTest {
    static int fails = 0, total = 0;

    static void risk(String hz, String want) {
        total++;
        String got = MainActivity.clpRisk(hz);
        boolean ok = want == null ? got == null : want.equals(got);
        if (!ok) fails++;
        System.out.println((ok ? "ok   " : "FAIL ") + hz + " -> " + got + (ok ? "" : "  ожидали «" + want + "»"));
    }

    static void worse(String a, String b, String want) {
        total++;
        String got = MainActivity.worseRisk(a, b);
        boolean ok = want.equals(got);
        if (!ok) fails++;
        System.out.println((ok ? "ok   " : "FAIL ") + "worse(" + a + "," + b + ") -> " + got + (ok ? "" : "  ожидали «" + want + "»"));
    }

    public static void main(String[] a) {
        risk("Carc. 1B, Aquatic 1", "высокий");
        risk("Muta. 1B", "высокий");
        risk("Repr. 1A", "высокий");
        risk("Acute Tox. 3", "высокий");
        risk("Asp. Tox. 1", "высокий");
        risk("STOT RE 1", "высокий");
        risk("Skin Corr. 1A", "высокий");
        risk("Carc. 2", "средний");
        risk("Acute Tox. 4", "средний");
        risk("Skin Sens. 1", "средний");
        risk("Resp. Sens. 1A", "средний");
        risk("Eye Irrit. 2", "средний");
        risk("Skin Corr. 1B", "средний");
        risk("STOT SE 3", "средний");
        risk("Aquatic Acute 1, Aquatic Chronic 1", null);
        risk("Flam. Liq. 2, Press. Gas", null);
        risk("", null);
        risk(null, null);
        worse("низкий", "высокий", "высокий");
        worse("высокий", "средний", "высокий");
        worse("средний", "низкий", "средний");
        System.out.println(fails == 0 ? "ClpRiskTest: всё зелёное (" + total + ")"
                : "ClpRiskTest: ПРОВАЛОВ " + fails + " из " + total);
        if (fails > 0) System.exit(1);
    }
}
