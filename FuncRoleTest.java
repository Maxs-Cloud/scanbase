package ru.scanbase;

/* Роль из INCI-функции: общий «ингредиент» должен превращаться в конкретную
   роль, приоритет — самая содержательная функция, а не первая в списке.
   javac -cp android.jar:build/obj FuncRoleTest.java
   java  -cp android.jar:build/obj:. ru.scanbase.FuncRoleTest                    */
public class FuncRoleTest {
    static int fails = 0, total = 0;

    static void check(String func, String want) {
        total++;
        String got = MainActivity.roleFromFunc(func);
        boolean ok = want == null ? got == null : want.equals(got);
        if (!ok) fails++;
        System.out.println((ok ? "ok   " : "FAIL ") + func + " -> " + got
                + (ok ? "" : "  ожидали «" + want + "»"));
    }

    public static void main(String[] a) {
        check("emollient , emulsifying , emulsion-stabilising", "эмульгатор");
        check("cleansing , emulsifying , surfactant", "ПАВ");
        check("skin-conditioning", "уход за кожей");
        check("preservative , antimicrobial", "консервант");
        check("cosmetic-colorant", "краситель");
        check("uv-absorber", "УФ-фильтр");
        check("perfuming", "отдушка");
        check("humectant , skin-conditioning", "увлажнение");
        check("viscosity-controlling", "загуститель");
        check("antioxidant", "антиоксидант");
        check("foam-boosting", "ПАВ");
        check("solvent", "растворитель");
        check("not-reported", null);
        check("unknown-magic-function", null);
        check("", null);
        check(null, null);
        System.out.println(fails == 0 ? "FuncRoleTest: всё зелёное (" + total + ")"
                : "FuncRoleTest: ПРОВАЛОВ " + fails + " из " + total);
        if (fails > 0) System.exit(1);
    }
}
