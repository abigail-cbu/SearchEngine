package Classes;

public class Product {

    private int ID;
    private String URL;
    private String date_time;
    private String price;
    private String productName;

    public String getURL()
    {
        return this.URL;
    }
    public String getPrice()
    {
        return this.price;
    }
    public String getProductName()
    {
        return this.productName;
    }
    public String getDate_time()
    {
        return this.date_time;
    }
    public int getID()
    {
        return this.ID;
    }

    public static class Builder {
        private int ID;
        private String URL;
        private String date_time;
        private String price;
        private String productName;

        public Builder withID(int ID) {
            this.ID = ID;
            return this;
        }
        public Builder withURL(String URL){
            this.URL = URL;
            return this;
        }
        public Builder withDateTime(String dateTime){
            this.date_time = dateTime;
            return this;
        }
        public Builder withPrice(String price){
            this.price = price;
            return this;
        }
        public Builder withProductName(String productName){
            this.productName = productName;
            return this;
        }

        public Product build(){
            Product product = new Product();
            product.ID = this.ID;
            product.price = this.price;
            product.productName = this.productName;
            product.date_time = this.date_time;
            product.URL = this.URL;
            return product;
        }
    }

    private Product(){

    }
}
