
public class Item {
    private Product product;
    private int amount;

    public Item() {
    }

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

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
