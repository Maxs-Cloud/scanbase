package ru.scanbase;

/* Еда в едином поиске: обычный продукт (свёкла, мука) — распознанный компонент,
   а не «не найден»; статус и категория соответствуют.
   javac -cp android.jar:build/obj FoodTest.java
   java  -cp android.jar:build/obj:. ru.scanbase.FoodTest                      */
public class FoodTest {

    static int fails = 0;
    static int total = 0;

    static void check(String what, boolean ok, String got) {
        total++;
        if (!ok) {
            fails++;
        }
        System.out.println((ok ? "ok   " : "FAIL ") + what + " -> " + got);
    }

    /* Продукт с foodName не должен превращаться в «не распознан». */
    static void checkNotUnrecognized(String name) {
        total++;
        MainActivity.Item it = new MainActivity.Item();
        it.name = name;
        it.status = "еда";
        it.role = "—";
        it.risk = "низкий";
        it.foodName = name;
        MainActivity.classify(it);
        boolean ok = !"не распознан".equals(it.role);
        if (!ok) {
            fails++;
        }
        System.out.println((ok ? "ok   " : "FAIL ") + "еда не «не распознан»: " + name + " -> " + it.role);
    }

    public static void main(String[] args) {
        check("статус «еда» читается", "пищевой продукт".equals(MainActivity.statusPlain("еда")),
                MainActivity.statusPlain("еда"));
        check("статус «добавка» не сломан", "пищевая добавка".equals(MainActivity.statusPlain("добавка")),
                MainActivity.statusPlain("добавка"));

        checkNotUnrecognized("свёкла");
        checkNotUnrecognized("мука");
        checkNotUnrecognized("сахар");

        /* Состав из продуктов без E-номеров и без косметики → категория «еда». */
        String[] got = MainActivity.categoryOf("свёкла, сахар, мука", "", "", "", 0, 0, 2, 0);
        check("пищевой состав → категория food", "food".equals(got[0]), got[0]);
        check("причина категории упоминает продукты",
                got[2] != null && got[2].contains("пищев"), got[2]);

        String[] one = MainActivity.categoryOf("мука", "", "", "", 0, 0, 1, 0);
        check("один продукт тоже food", "food".equals(one[0]), one[0]);

        /* Косметический состав (catalog) не должен уехать в еду. */
        String[] cos = MainActivity.categoryOf("aqua, glycerin", "", "", "", 0, 5, 0, 0);
        check("косметика остаётся косметикой", "cosmetics".equals(cos[0]), cos[0]);

        System.out.println(fails == 0 ? "FoodTest: всё зелёное (" + total + ")"
                : "FoodTest: ПРОВАЛОВ " + fails + " из " + total);
        if (fails > 0) {
            System.exit(1);
        }
    }
}
