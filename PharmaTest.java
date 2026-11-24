package ru.scanbase;

/* Фармакологическое действующее вещество (МНН) не должно помечаться «не распознан»:
   если название найдено в pharma_ingredients, компонент распознан — classify даёт роль
   «ингредиент» и низкий риск (а «действующее вещество» проставляется уровнем продукта по
   статусу «вещество»). Второе имя проверяет, что решает именно pharma-флаг: без него тот
   же текст ушёл бы в «не распознан».
   javac -cp android.jar:build/obj PharmaTest.java
   java  -cp android.jar:build/obj:. ru.scanbase.PharmaTest                        */
public class PharmaTest {

    static int fails = 0;
    static int total = 0;

    static void check(String what, String name, String pharma, String wantRole, String wantRisk) {
        total++;
        MainActivity.Item item = new MainActivity.Item();
        item.name = name;
        item.status = pharma != null ? "вещество" : "не найден";
        item.role = "—";
        item.risk = "неизвестно";
        item.pharma = pharma;
        MainActivity.classify(item);
        boolean ok = wantRole.equals(item.role) && wantRisk.equals(item.risk);
        if (!ok) {
            fails++;
        }
        System.out.println((ok ? "ok   " : "FAIL ") + what + " [" + name + "] -> " + item.role + ", "
                + item.risk + (ok ? "" : "  ожидали «" + wantRole + ", " + wantRisk + "»"));
    }

    public static void main(String[] args) {
        check("МНН распознан, не «не распознан»", "Парацетамол", "Парацетамол", "ингредиент", "низкий");
        check("кофеин как вещество", "Кофеин", "Кофеин", "ингредиент", "низкий");
        check("вымышленное имя без pharma — не распознан", "Итропид", null, "не распознан", "неизвестно");
        check("то же имя со спасшим pharma-флагом", "Итропид", "Итропид", "ингредиент", "низкий");
        System.out.println(total + " проверок, " + fails + " FAIL");
        System.exit(fails == 0 ? 0 : 1);
    }
}
