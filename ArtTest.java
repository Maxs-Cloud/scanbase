package ru.scanbase;

/* Проверка распознавания артикулов и ссылок Маркета. Методы статические и
   зависимостей от Android не имеют, поэтому гоняем их на десктопной JVM прямо
   из классов сборки:
   javac -cp android.jar:build/obj ArtTest.java
   java  -cp android.jar:build/obj:. ru.scanbase.ArtTest                        */
public class ArtTest {

    static int fails = 0;
    static int total = 0;

    static void art(String what, String in, String want) {
        total++;
        String got = MainActivity.articleUrl(in);
        boolean ok = want.equals(got);
        if (!ok) {
            fails++;
        }
        System.out.println((ok ? "ok   " : "FAIL ") + what + " [" + in + "] -> " + got +
                (ok ? "" : "  ожидали " + want));
    }

    static void card(String what, String in, String want) {
        total++;
        String got = MainActivity.marketCard(in);
        boolean ok = want.equals(got);
        if (!ok) {
            fails++;
        }
        System.out.println((ok ? "ok   " : "FAIL ") + what + " [" + in + "] -> " + got +
                (ok ? "" : "  ожидали " + want));
    }

    public static void main(String[] args) {
        String wb = "https://www.wildberries.ru/catalog/%s/detail.aspx";
        String mk = "https://market.yandex.ru/card/x/%s";

        art("голый 7-значный = WB", "370276378", String.format(wb, "370276378"));
        art("артикул 14453274 = WB", "артикул 14453274", String.format(wb, "14453274"));
        art("wb 123456", "wb 123456", String.format(wb, "123456"));
        art("озон 999888777", "озон 999888777", "https://www.ozon.ru/product/999888777/");
        art("маркет 123456789012", "маркет 123456789012", String.format(mk, "123456789012"));
        art("яндекс 555666", "яндекс 555666", String.format(mk, "555666"));
        art("id 555666", "id 555666", String.format(wb, "555666"));
        art("18 цифр без слова = Маркет", "228446906446868480",
                String.format(mk, "228446906446868480"));
        art("маркет 18 цифр", "маркет 228446906446868480",
                String.format(mk, "228446906446868480"));
        art("13 цифр = Маркет", "1234567890123", String.format(mk, "1234567890123"));
        art("12 цифр = WB", "123456789012", String.format(wb, "123456789012"));
        art("4 цифры — не артикул", "1234", "");
        art("два числа — не артикул", "хлопок 80%, полиэстер 20%", "");
        art("ссылка — не артикул", "https://example.com/a/123456789", "");
        art("текст состава — не артикул",
                "состав: вода, глицерин 999888777, пантенол", "");
        art("пробел-число-пробел-число", "370276378 14453274", "");
        art("21 цифра — слишком длинно", "123456789012345678901", "");

        card("product-- → card", "https://market.yandex.ru/product--228446906446868480",
                String.format(mk, "228446906446868480"));
        card("/product/ → card", "https://market.yandex.ru/product/123456789012",
                String.format(mk, "123456789012"));
        card("product-- с хвостом", "https://market.yandex.ru/product--555666?sku=777",
                String.format(mk, "555666"));
        card("живая /card/ не трогать",
                "https://market.yandex.ru/card/vois-krem/228446906446868480",
                "https://market.yandex.ru/card/vois-krem/228446906446868480");
        card("не маркет — не трогать", "https://www.wildberries.ru/catalog/123456/detail.aspx",
                "https://www.wildberries.ru/catalog/123456/detail.aspx");
        card("маркет без id — не трогать", "https://market.yandex.ru/search?text=крем",
                "https://market.yandex.ru/search?text=крем");
        card("пусто", "", "");

        System.out.println(fails == 0 ? "ВСЕ " + total + " ПРОВЕРОК ЗЕЛЕНЫЕ"
                : "ПРОВАЛЕНО " + fails + " из " + total);
        System.exit(fails == 0 ? 0 : 1);
    }
}
