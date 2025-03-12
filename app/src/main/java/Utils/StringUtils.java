package Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Materials.Product;

public class StringUtils {

    static List<String> validUnits = Arrays.asList("g", "kg", "ml", "l", "oz", "lb", "mg"); // List of valid units

    public static String createSearchString(Product product) {
        return (product.getBrand().equals("Unknown") ? "" : product.getBrand()) + " "
                + (product.getBrand().equals(product.getProductName()) ? "" : product.getProductName()) + " "
                + (validUnits.contains(product.getQuantityUnit()) ? product.getQuantity() + product.getQuantityUnit() : "");
    }

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

    public static String getProductBarcode(String barcode) {
        switch (barcode) {
            case "Laviva":
                return "8690504065395";
            case "Halley":
                return "8690504034032";
            case "Cheetos Mini":
                return "8690624003093";
            case "Hanımeller":
                return "8690504012139";
            case "Nesquik Gofret":
                return "8690632049168";
            case "Karmen":
                return "8690766378400";

            default:
                return "NONE";
        }
    }

    private static int extractNumericValue(JsonObject product, String fieldName) {
        if (product.has(fieldName)) {
            String value = product.get(fieldName).getAsString().trim();

            // Use regular expression to find the first occurrence of digits at the beginning
            String numericValue = value.replaceAll("^\\D*(\\d+).*", "$1");

            if (!numericValue.isEmpty()) {
                return Integer.parseInt(numericValue);
            }
        }
        return 0; // Default to 0 if the value is missing or not numeric
    }

    // Extract the unit from the string after the first sequence of digits
    private static String extractUnit(String value) {
        // Regular expression to capture everything after the first sequence of digits
        String regex = "\\d+\\s*(.*)";  // Matches digits followed by optional spaces and then the rest (unit part)
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        if (matcher.find()) {
            // Return the unit part found after the digits (trim any leading/trailing spaces)
            return Objects.requireNonNull(matcher.group(1)).trim();
        } else {
            return "";  // Return empty if no unit is found
        }
    }

    private static boolean isValidUnit(String unit) {
        // For example, a valid unit can be anything like "g", "kg", "ml", etc.
        return validUnits.contains(unit.toLowerCase());
    }

    private static String capitalizeWords(String input) {
        String[] words = input.split(" ");
        StringBuilder capitalizedString = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedString.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        // Remove the trailing space at the end
        return capitalizedString.toString().trim();
    }

    public static ArrayList<String> parseAndFilterCategories(JsonArray categoriesArray) {
        Set<String> categoriesSet = new HashSet<>();

        // Parse each category, remove "en:" prefix, and apply formatting
        for (JsonElement categoryElement : categoriesArray) {
            String category = categoryElement.getAsString().replace("en:", "");
            String formattedCategory = capitalizeWords(category.replace("-", " "));
            categoriesSet.add(formattedCategory);
        }

        ArrayList<String> categoriesList = new ArrayList<>(categoriesSet);
        categoriesList.sort(Comparator.comparingInt(String::length).reversed()); // Sort by length (longest first)

        // Remove categories that are substrings of another category
        Iterator<String> iterator = categoriesList.iterator();
        while (iterator.hasNext()) {
            String category = iterator.next();
            for (String other : categoriesList) {
                if (!category.equals(other) && other.contains(category)) {
                    iterator.remove(); // Remove redundant category
                    break;
                }
            }
        }

        return categoriesList;
    }

    public static Product parseProduct(JsonObject product) {
        Product product1 = new Product();

        int quantityValue = 0;
        int productQuantityValue = 0;
        String quantityUnit = "";

        // Check if "product_name" exists and parse it, otherwise skip or set a default value
        if (product.has("product_name")) {
            product1.setProductName(capitalizeWithSyntax(product.get("product_name").getAsString()));
        }

        //-----------------------------------

        // Check if "code" exists and parse it
        if (product.has("code")) {
            product1.setBarcode(product.get("code").getAsString());
        }

        //-----------------------------------

        // Check if "brands" exists and parse it
        if (product.has("brands")) {
            String brand = capitalizeWords(product.get("brands").getAsString());
            String productName = product1.getProductName(); // Ensure product name is already set

            if (productName != null && !productName.isEmpty()) {
                String[] brandWords = brand.split("\\s+");

                // Check if the brand has more than 2 words and ends with the product name
                if (brandWords.length >= 2 && brand.endsWith(productName)) {
                    brand = brand.substring(0, brand.lastIndexOf(productName)).trim();
                }
            }

            product1.setBrand(brand);
        }


        //-----------------------------------

        // Check if "quantity" exists and parse it
        if (product.has("quantity") && !product.get("quantity").getAsString().isEmpty()) {
            quantityValue = extractNumericValue(product, "quantity");
        }

        if (product.has("product_quantity") && !product.get("product_quantity").getAsString().isEmpty()) {
            productQuantityValue = extractNumericValue(product, "product_quantity");
        }
        product1.setQuantity(String.valueOf(Math.max(quantityValue, productQuantityValue)));

        //-----------------------------------

        // Check quantity unit

        // Check if the unit is valid or not from the "product_quantity_unit" field
        if (product.has("product_quantity_unit") && !product.get("product_quantity_unit").getAsString().isEmpty()) {
            String unit = product.get("product_quantity_unit").getAsString().trim();
            if (isValidUnit(unit)) {  // Check if the unit is valid
                quantityUnit = unit;
            }
        }

        if (quantityUnit.isEmpty()) {
            // Check "quantity" field
            if (product.has("quantity") && !product.get("quantity").getAsString().isEmpty()) {
                quantityUnit = extractUnit(product.get("quantity").getAsString());
            }

            // Check "product_quantity" field if "quantity" does not provide a valid unit
            if (quantityUnit.isEmpty() && product.has("product_quantity") && !product.get("product_quantity").getAsString().isEmpty()) {
                quantityUnit = extractUnit(product.get("product_quantity").getAsString());
            }
        }

        product1.setQuantityUnit(quantityUnit.isEmpty() ? "g" : quantityUnit);

        //-----------------------------------

        // Check if "ecoscore_score" exists and parse it
        if (product.has("ecoscore_score")) {
            product1.setEcoscore(product.get("ecoscore_score").getAsInt());
        }

        //-----------------------------------

        // Parse allergens if present
        product1.setAllergens(extractUniqueAllergens(product));

        //-----------------------------------

        // Parse ingredients
        JsonArray ingredientsArray = product.getAsJsonArray("ingredients");
        ArrayList<String> ingredients = new ArrayList<>();
        if (ingredientsArray != null) {
            for (int i = 0; i < ingredientsArray.size(); i++) {
                JsonObject ingredientJson = ingredientsArray.get(i).getAsJsonObject();
                if (ingredientJson.has("text")) {
                    String ingredientText = ingredientJson.get("text").getAsString();
                    ingredients.add(capitalizeWithSyntax(ingredientText));
                }
            }
        }
        product1.setIngredients(ingredients);

        //-----------------------------------

        if (product.has("categories_hierarchy")) {
            JsonArray categoriesArray = product.getAsJsonArray("categories_hierarchy");
            product1.setCategories(parseAndFilterCategories(categoriesArray));
        }

        return product1;
    }

}
