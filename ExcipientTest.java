package ru.scanbase;

/* Вспомогательное вещество (эксципиент) не должно помечаться «не распознан»: если
   название найдено в excipient_ingredients, компонент распознан — classify даёт роль
   «ингредиент» и низкий риск (а «вспомогательное вещество» проставляется уровнем
   продукта по статусу «вспомогательное»). Второе имя проверяет, что решает именно
   exc-флаг: без него тот же текст ушёл бы в «не распознан».
   javac -cp android.jar:build/obj ExcipientTest.java
   java  -cp android.jar:build/obj:. ru.scanbase.ExcipientTest                     */
public class ExcipientTest {

    static int fails = 0;
    static int total = 0;

    static void check(String what, String name, String exc, String wantRole, String wantRisk) {
        total++;
        MainActivity.Item item = new MainActivity.Item();
        item.name = name;
        item.status = exc != null ? "вспомогательное" : "не найден";
        item.role = "—";
        item.risk = "неизвестно";
        item.exc = exc;
        MainActivity.classify(item);
        boolean ok = wantRole.equals(item.role) && wantRisk.equals(item.risk);
        if (!ok) {
            fails++;
        }
        System.out.println((ok ? "ok   " : "FAIL ") + what + " [" + name + "] -> " + item.role + ", "
                + item.risk + (ok ? "" : "  ожидали «" + wantRole + ", " + wantRisk + "»"));
    }

    public static void main(String[] args) {
        check("эксципиент распознан, не «не распознан»", "Магния стеарат", "Магния стеарат", "ингредиент", "низкий");
        check("повидон как вспомогательное", "Повидон", "Повидон", "ингредиент", "низкий");
        check("вымышленное имя без exc — не распознан", "Стеаролит", null, "не распознан", "неизвестно");
        check("то же имя со спасшим exc-флагом", "Стеаролит", "Стеаролит", "ингредиент", "низкий");
        System.out.println(total + " проверок, " + fails + " FAIL");
        System.exit(fails == 0 ? 0 : 1);
    }
}
