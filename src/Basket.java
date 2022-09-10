import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Basket implements Serializable {
    private List<Item> items = new ArrayList<>();

    private static class Item implements Serializable {
        Product product;
        int amount;

        public Item(String saveString) {
            String[] values = saveString.split(" ");
            this.product = new Product(values[0], Integer.parseInt(values[1]));
            this.amount = Integer.parseInt(values[2]);
        }

        public Item(Product product, int amount) {
            this.product = product;
            this.amount = amount;
        }

        public int sum() {
            return product.getPrice() * amount;
        }

        public void print() {
            System.out.println(product.getName() + " " + amount + " шт " + product.getPrice() + " руб/шт " + sum() + " руб в сумме");
        }

        public String save() {
            return product.getName() + " " + product.getPrice() + " " + amount;
        }
    }

    public void addToCart(Product product, int amount) {
        Optional<Item> optionalItem = items.stream().filter(item -> item.product.equals(product)).findFirst();

        if (optionalItem.isPresent()) {
            optionalItem.get().amount += amount;
        }
        else {
            items.add(new Item(product, amount));
        }
    }

    public void printCart() {
        System.out.println("Ваша корзина:");

        items.forEach(Item::print);

        System.out.println("Итого: " + items.stream().mapToInt(Item::sum).sum() + " руб");
    }

    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(textFile)) {
            items.forEach(item -> writer.println(item.save()));
        }
    }

    public static Basket loadFromTxtFile(File textFile) throws IOException {
        Basket basket = new Basket();

        if (!textFile.exists()) {
            return basket;
        }

//        BufferedReader reader = new BufferedReader(new FileReader(textFile));
//        String line = null;
//
//        while ((line = reader.readLine()) != null) {
//            basket.items.add(new Basket.Item(line));
//        }

        Files.readAllLines(Path.of(textFile.getAbsolutePath())).forEach(line -> basket.items.add(new Basket.Item(line)));

        return basket;
    }

    public void saveBin(File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(this);
        }
    }

    public static Basket loadFromBinFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream(file))) {
            return (Basket) out.readObject();
        }
    }
}
