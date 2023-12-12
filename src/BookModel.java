public class BookModel {
    private int id;
    private String title;

    private String author;

    private float price;

    private int qty;

     BookModel(){
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public float getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }
}
