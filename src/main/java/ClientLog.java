import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    private List<Item> items = new ArrayList<>();

    private static class Item {
        int productNum;
        int amount;

        public Item(int productNum, int amount) {
            this.productNum = productNum;
            this.amount = amount;
        }

        public String[] toStringArray() {
            return new String[]{Integer.toString(productNum), Integer.toString(amount)};
        }
    }

    public void log(int productNum, int amount) {
        items.add(new Item(productNum, amount));
    }

    public void exportAsCSV(File txtFile) throws IOException {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(txtFile), ',', Character.MIN_VALUE)) {
            csvWriter.writeNext("productNum,amount".toString().split(","));
            items.forEach(item -> csvWriter.writeNext(item.toStringArray()));
        }
    }
}
