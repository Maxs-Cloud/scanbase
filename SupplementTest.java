package ru.scanbase;

/* Нутриент/БАД-компонент не должен помечаться «не распознан»: если название найдено
   в supplement_ingredients, компонент распознан — classify даёт роль «ингредиент» и
   низкий риск (а «нутриент (БАД)» проставляется уровнем продукта по статусу «нутриент»).
   Второе имя проверяет, что решает именно sup-флаг: без него тот же текст ушёл бы в
   «не распознан».
   javac -cp android.jar:build/obj SupplementTest.java
   java  -cp android.jar:build/obj:. ru.scanbase.SupplementTest                    */
public class SupplementTest {

    static int fails = 0;
    static int total = 0;

    static void check(String what, String name, String sup, String wantRole, String wantRisk) {
        total++;
        MainActivity.Item item = new MainActivity.Item();
        item.name = name;
        item.status = sup != null ? "нутриент" : "не найден";
        item.role = "—";
        item.risk = "неизвестно";
        item.sup = sup;
        MainActivity.classify(item);
        boolean ok = wantRole.equals(item.role) && wantRisk.equals(item.risk);
        if (!ok) {
            fails++;
        }
        System.out.println((ok ? "ok   " : "FAIL ") + what + " [" + name + "] -> " + item.role + ", "
                + item.risk + (ok ? "" : "  ожидали «" + wantRole + ", " + wantRisk + "»"));
    }

    public static void main(String[] args) {
        check("нутриент распознан, не «не распознан»", "Креатина моногидрат", "Креатина моногидрат", "ингредиент", "низкий");
        check("коэнзим Q10 как нутриент", "Коэнзим Q10", "Коэнзим Q10", "ингредиент", "низкий");
        check("вымышленное имя без sup — не распознан", "Нутриолит", null, "не распознан", "неизвестно");
        check("то же имя со спасшим sup-флагом", "Нутриолит", "Нутриолит", "ингредиент", "низкий");
        System.out.println(total + " проверок, " + fails + " FAIL");
        System.exit(fails == 0 ? 0 : 1);
    }
}
