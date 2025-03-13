package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productbarcodescanner.R;

import Materials.Product;

public class ProductShowcase extends AppCompatActivity {

    private Product product;
    private LinearLayout productContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_showcase);

        productContainer = findViewById(R.id.productContainer);

        // Retrieve Product Object from Intent
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");

        // Populate UI dynamically
        if (product != null) {
            addTextView("Product Name: ", product.getProductName());
            addTextView("Barcode: ", product.getBarcode());
            addTextView("Brand: ", product.getBrand());
            addTextView("Quantity: ", product.getQuantity() + " " + product.getQuantityUnit());

            // Ecoscore check
            if (product.getEcoscore() != null) {
                addTextView("Eco Score: ", String.valueOf(product.getEcoscore()));
            }

            // Ingredients
            if (product.getIngredients() != null && !product.getIngredients().isEmpty()) {
                addTextView("Ingredients: ", String.join(", ", product.getIngredients()));
            }

            // Allergens
            if (product.getAllergens() != null && !product.getAllergens().isEmpty()) {
                addTextView("Allergens: ", String.join(", ", product.getAllergens()));
            }

            // Categories
            if (product.getCategories() != null && !product.getCategories().isEmpty()) {
                addTextView("Categories: ", String.join(", ", product.getCategories()));
            }
        }
    }

    private void addTextView(String label, String value) {
        if (value == null || value.isEmpty()) return;

        TextView textView = new TextView(this);
        textView.setText(label + value);
        textView.setTextSize(16);
        textView.setPadding(0, 10, 0, 10);
        productContainer.addView(textView);
    }
}
