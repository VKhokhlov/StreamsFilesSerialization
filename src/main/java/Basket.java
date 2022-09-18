import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Basket {
    private List<Item> items = new ArrayList<>();

    public void addToCart(Product product, int amount) {
        Optional<Item> optionalItem = items.stream().filter(item -> item.getProduct().equals(product)).findFirst();

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.setAmount(item.getAmount() + amount);
        } else {
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
            items.stream().map(Item::save).forEach(writer::println);
        }
    }

    public static Basket loadFromTxtFile(File textFile) throws IOException {
        Basket basket = new Basket();

        if (!textFile.exists()) {
            return basket;
        }

        BufferedReader reader = new BufferedReader(new FileReader(textFile));
        String line = null;

        while ((line = reader.readLine()) != null) {
            basket.items.add(new Item(line));
        }

        return basket;
    }

    public void saveJson(File jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(jsonFile, this);
    }

    public static Basket loadFromJson(File jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonFile, Basket.class);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void saveBasket(Config.Item save) throws IOException {
        if (!save.isEnabled()) {
            return;
        }

        switch (save.getFormat()) {
            case "json": {
                saveJson(new File(save.getFileName()));
                break;
            }
            case "txt": {
                saveTxt(new File(save.getFileName()));
                break;
            }
        }
    }

    public static Basket loadBasket(Config.Item load) throws IOException {
        if (!load.isEnabled()) {
            return new Basket();
        }

        switch (load.getFormat()) {
            case "json": {
                return loadFromJson(new File(load.getFileName()));
            }
            case "txt": {
                return loadFromTxtFile(new File(load.getFileName()));
            }
        }

        return new Basket();
    }
}
