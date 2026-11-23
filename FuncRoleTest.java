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
        // частые функции, добавленные по покрытию БД
        check("plasticiser", "пластификатор");
        check("absorbent", "абсорбент");
        check("nail-conditioning", "уход за ногтями");
        check("flavouring", "ароматизатор");
        check("anticaking", "антислеживатель");
        check("stabilising", "стабилизатор");
        check("moisturising", "увлажнение");
        check("antiperspirant", "антиперспирант");
        check("gel-forming", "гелеобразователь");
        check("keratolytic", "кератолитик");
        check("detangling", "распутывание волос");
        check("pearlescent", "перламутр");
        // приоритет: film-forming идёт раньше plasticiser
        check("plasticiser , film-forming", "плёнкообразователь");
        System.out.println(fails == 0 ? "FuncRoleTest: всё зелёное (" + total + ")"
                : "FuncRoleTest: ПРОВАЛОВ " + fails + " из " + total);
        if (fails > 0) System.exit(1);
    }
}
