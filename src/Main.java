import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Product> products = Arrays.asList(
                new Product("Хлеб", 30),
                new Product("Молоко", 85),
                new Product("Гречка", 70)
        );

        File basketFile = new File("Basket.txt");
        Basket basket;

        try {
            basket = Basket.loadFromTxtFile(basketFile);
        } catch (IOException e) {
            System.out.println("Произошла ошибка при загрузке корзины из файла: " + e.getMessage());

            basket = new Basket();
        }

        Scanner scanner = new Scanner(System.in);

        printProducts(products);

        while (true) {
            System.out.println("Выберите товар и количество или введите `end`.");

            String input = scanner.nextLine();

            if (input.equals("end")) {
                break;
            }

            String[] parts = input.split(" ");

            if (parts.length < 2) {
                System.out.println("Не верный ввод!");
                continue;
            }

            int productNumber = Integer.parseInt(parts[0]) - 1;
            int productCount = Integer.parseInt(parts[1]);

            if (productNumber > products.size() - 1) {
                System.out.println("Не верный номер продукта!");
                continue;
            }

            basket.addToCart(products.get(productNumber), productCount);

            try {
                basket.saveTxt(basketFile);
            } catch (IOException e) {
                System.out.println("Произошла ошибка при сохранении корзины в файл: " + e.getMessage());
            }
        }

        basket.printCart();
    }

    public static void printProducts(List<Product> products) {
        System.out.println("Список возможных товаров для покупки:");

        IntStream.range(0, products.size()).forEach(i -> System.out.println((i + 1) + ". " + products.get(i)));
    }
}