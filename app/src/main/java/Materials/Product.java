package Materials;

import java.util.ArrayList;

public class Product {

    String productName; // name
    String barcode; // barcode OPTIONAL EVEN
    String brand; // brand
    String imageUrl; // MUST BE FILLED VIA WEB-SCRAPING
    String quantity; // weight
    Integer ecoscore; // if ecoscore_score value exists
    ArrayList<String> allergens;
    ArrayList<String> ingredients;
    ArrayList<String> categories;

    public Product(){
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", barcode='" + barcode + '\'' +
                ", brand='" + brand + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantity='" + quantity + '\'' +
                ", ecoscore=" + ecoscore +
                ", allergens=" + allergens +
                ", ingredients=" + ingredients +
                ", categories=" + categories +
                '}';
    }

    public ArrayList<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(ArrayList<String> allergens) {
        this.allergens = allergens;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getEcoscore() {
        return ecoscore;
    }

    public void setEcoscore(Integer ecoscore) {
        this.ecoscore = ecoscore;
    }
}
