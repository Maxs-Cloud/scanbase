package ru.scanbase;
/* Критерий «спорный/ограниченный» компонент (секция «Вредные и спорные» и
   текстовый список в вердикте). Вода/глицерин с низким риском НЕ спорные;
   высокий/средний риск, превышение лимита и «с ограничением» — спорные. */
public class VerdictTest {
    static int fails = 0, total = 0;
    static void check(String name, String risk, String status, boolean want) {
        total++;
        MainActivity.Item it = new MainActivity.Item();
        it.name = name; it.risk = risk; it.status = status;
        boolean got = MainActivity.isRiskyItem(it);
        if (got != want) fails++;
        System.out.println((got == want ? "ok   " : "FAIL ") + name + " risk=" + risk +
                " status=" + status + " -> risky=" + got + (got == want ? "" : " (ожидали " + want + ")"));
    }
    static void checkHazard(String hz, String... subs) {
        total++;
        String got = MainActivity.hazardPlain(hz);
        boolean ok = subs.length == 0 ? got.isEmpty() : true;
        for (String s : subs) {
            if (!got.contains(s)) ok = false;
        }
        if (!ok) fails++;
        System.out.println((ok ? "ok   " : "FAIL ") + "hazardPlain[" + hz + "] -> " + got);
    }
    public static void main(String[] a) {
        check("aqua", "низкий", "найден", false);
        check("вода", "низкий", "найден", false);
        check("glycerin", "низкий", "CosIng", false);
        check("cetearyl alcohol", "низкий", "найден", false);
        check("limonene", "высокий", "найден", true);
        check("sodium lauryl sulfate", "средний", "найден", true);
        check("parfum", "средний", "найден", true);
        check("over-limit", "низкий", "превышение", true);
        check("restricted", "низкий", "с ограничением", true);
        check("unknown", "неизвестно", "по категории", false);
        checkHazard("Flam. Liq. 3, Asp. Tox. 1, Skin Irrit. 2, Skin Sens. 1B, Aquatic Acute 1, Aquatic Chronic 3",
                "токсичен при проглатывании", "раздражает кожу", "сенсибилизатор кожи", "легковоспламеняющийся", "токсичен для водной среды");
        checkHazard("Carc. 1B, Muta. 1B, Repr. 1B", "канцероген", "мутаген", "репродуктивной");
        checkHazard("", new String[0]);
        System.out.println("VerdictTest: " + (total - fails) + "/" + total);
        if (fails > 0) System.exit(1);
    }
}
