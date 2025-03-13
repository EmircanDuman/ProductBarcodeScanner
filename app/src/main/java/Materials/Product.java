package Materials;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {

    String productName; // name             ++
    String brand; // brand                  ++
    String barcode; // barcode              ++
    String imageUrl; // ignored             -
    String quantity; // weight              ++
    String quantityUnit; // weight_unit     ++
    Integer ecoscore; // ecoscore
    ArrayList<String> allergens;
    ArrayList<String> ingredients;
    ArrayList<String> categories;

    public Product(){
    }

    public String getQuantityUnit() {
        if(quantityUnit == null){
            setQuantityUnit("g");
        }
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public ArrayList<String> getAllergens() {
        if (allergens == null){
            setAllergens(new ArrayList<>());
        }
        return allergens;
    }

    public void setAllergens(ArrayList<String> allergens) {
        this.allergens = allergens;
    }

    public ArrayList<String> getIngredients() {
        if (ingredients == null){
            setIngredients(new ArrayList<>());
        }
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getCategories() {
        if (categories == null){
            setCategories(new ArrayList<String>());
        }
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getProductName() {
        if (productName == null){
            setProductName("Unknown");
        }
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBarcode() {
        if (barcode == null){
            setBarcode("Unknown");
        }
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBrand() {
        if (brand == null){
            setBrand("Unknown");
        }
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImageUrl() {
        if (imageUrl == null){
            setImageUrl("Unknown");
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getQuantity() {
        if (quantity == null){
            setQuantity("0");
        }
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getEcoscore() {
        if(ecoscore == null){
            setEcoscore(0);
        }
        return ecoscore;
    }

    public void setEcoscore(Integer ecoscore) {
        this.ecoscore = ecoscore;
    }
}
