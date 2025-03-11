package Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import Materials.Product;

public class StringUtils {

    public static String capitalizeWithSyntax(String productName) {
        if (productName == null || productName.isEmpty()) {
            return productName;
        }

        String[] words = productName.toLowerCase().split(" ");
        StringBuilder capitalized = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0 || (!word.equals("and") && !word.equals("the"))) {
                word = Character.toUpperCase(word.charAt(0)) + word.substring(1);
            }
            capitalized.append(word).append(" ");
        }

        return capitalized.toString().trim();
    }


    public static String capitalizeFirstLetter(String brand) {
        if (brand == null || brand.isEmpty()) {
            return brand;
        }
        return Character.toUpperCase(brand.charAt(0)) + brand.substring(1).toLowerCase();
    }

    /**
     * Extracts unique allergens from a product JSON object.
     * @param product The JSON object containing allergen information
     * @return ArrayList of unique allergens with proper formatting
     */
    public static ArrayList<String> extractUniqueAllergens(JsonObject product) {
        Set<String> uniqueAllergens = new HashSet<>();

        // Process string fields
        String[] stringFields = {"allergens", "allergens_from_ingredients", "allergens_from_user"};
        for (String field : stringFields) {
            if (product.has(field) && !product.get(field).isJsonNull()) {
                String allergenStr = product.get(field).getAsString();
                String[] allergens = allergenStr.split(",");
                for (String allergen : allergens) {
                    processAllergen(allergen.trim(), uniqueAllergens);
                }
            }
        }

        // Process array fields
        String[] arrayFields = {"allergens_hierarchy", "allergens_tags"};
        for (String field : arrayFields) {
            if (product.has(field) && !product.get(field).isJsonNull()) {
                JsonArray allergenArray = product.getAsJsonArray(field);
                for (JsonElement element : allergenArray) {
                    if (!element.isJsonNull()) {
                        String allergen = element.getAsString();
                        processAllergen(allergen, uniqueAllergens);
                    }
                }
            }
        }

        // Convert set to ArrayList and return
        return new ArrayList<>(uniqueAllergens);
    }

    /**
     * Process a single allergen entry and add it to the set if valid.
     */
    private static void processAllergen(String allergen, Set<String> uniqueAllergens) {
        if (allergen.startsWith("en:")) {
            // Remove "en:" prefix
            String cleanAllergen = allergen.substring(3);

            // Format: capitalize first letter, rest lowercase
            if (!cleanAllergen.isEmpty()) {
                cleanAllergen = cleanAllergen.substring(0, 1).toUpperCase() +
                        cleanAllergen.substring(1).toLowerCase();

                // Add to set of unique allergens
                uniqueAllergens.add(cleanAllergen);
            }
        }
    }

    public static Product parseProduct(JsonObject product) {
        Product product1 = new Product();
        product1.setProductName(capitalizeWithSyntax(product.get("product_name").getAsString()));
        product1.setBarcode(product.get("code").getAsString());
        product1.setBrand(capitalizeFirstLetter(product.get("brands").getAsString()));
        product1.setQuantity(product.get("quantity").getAsString());
        product1.setEcoscore(product.get("ecoscore_score").getAsInt());
        product1.setAllergens(extractUniqueAllergens(product));

        // Parse ingredients
        JsonArray ingredientsArray = product.getAsJsonArray("ingredients");
        ArrayList<String> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientsArray.size(); i++) {
            JsonObject ingredientJson = ingredientsArray.get(i).getAsJsonObject();
            String ingredientText = ingredientJson.get("text").getAsString();
            ingredients.add(capitalizeWithSyntax(ingredientText));
        }
        product1.setIngredients(ingredients);

        return product1;
    }
}
