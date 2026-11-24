package ru.scanbase;

/* Единый «дополнительный» компонент (действующее вещество / нутриент / вспомогательное
   вещество — роль приходит строкой из extra_ingredients) не должен помечаться
   «не распознан»: если extra заполнен, classify даёт роль «ингредиент» и низкий риск
   (а человекочитаемую роль проставляет уровень продукта по статусу «распознан»).
   Второе имя проверяет, что решает именно extra-флаг: без него текст ушёл бы в
   «не распознан».
   javac -cp android.jar:build/obj ExtraTest.java
   java  -cp android.jar:build/obj:. ru.scanbase.ExtraTest                          */
public class ExtraTest {

    static int fails = 0;
    static int total = 0;

    static void check(String what, String name, String extra, String wantRole, String wantRisk) {
        total++;
        MainActivity.Item item = new MainActivity.Item();
        item.name = name;
        item.status = extra != null ? "распознан" : "не найден";
        item.role = "—";
        item.risk = "неизвестно";
        item.extra = extra;
        MainActivity.classify(item);
        boolean ok = wantRole.equals(item.role) && wantRisk.equals(item.risk);
        if (!ok) {
            fails++;
        }
        System.out.println((ok ? "ok   " : "FAIL ") + what + " [" + name + "] -> " + item.role + ", "
                + item.risk + (ok ? "" : "  ожидали «" + wantRole + ", " + wantRisk + "»"));
    }

    public static void main(String[] args) {
        check("действующее вещество распознано", "Парацетамол", "действующее вещество", "ингредиент", "низкий");
        check("нутриент распознан", "Коэнзим Q10", "нутриент (БАД)", "ингредиент", "низкий");
        check("вспомогательное распознано", "Магния стеарат", "вспомогательное вещество", "ингредиент", "низкий");
        check("вымышленное имя без extra — не распознан", "Ксенолит", null, "не распознан", "неизвестно");
        check("то же имя со спасшим extra-флагом", "Ксенолит", "нутриент (БАД)", "ингредиент", "низкий");
        System.out.println(total + " проверок, " + fails + " FAIL");
        System.exit(fails == 0 ? 0 : 1);
    }
}
