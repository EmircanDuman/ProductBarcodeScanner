package Activities;

import static Utils.StringUtils.generateBarcode;
import static Utils.StringUtils.isValidUnit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.productbarcodescanner.R;

import Materials.Product;

public class ProductShowcase extends AppCompatActivity {

    private Product product;
    private LinearLayout productContainer;
    private LayoutInflater inflater;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_showcase);

        Intent intent = getIntent();
        this.product = (Product) intent.getSerializableExtra("product");
        productContainer = findViewById(R.id.productContainer);

        if(product == null) return;

        this.inflater = LayoutInflater.from(this);

        initializeProductNameSection();
        initalizeAllergensSection();
        initializeCategoriesSection();
        initializeIngredientsSection();
    }

    private void initializeProductNameSection() {
        View productNameView = inflater.inflate(R.layout.product_name_section, productContainer, false);
        TextView productName = productNameView.findViewById(R.id.productName);
        TextView brandName = productNameView.findViewById(R.id.brandName);
        ImageView barcodeImage = productNameView.findViewById(R.id.barcodeImage);
        TextView barcodeText = productNameView.findViewById(R.id.barcodeText);
        TextView ecoScore = productNameView.findViewById(R.id.ecoScoreText);

        String productNameString = product.getProductName()
                +(isValidUnit(product.getQuantityUnit()) ? " (" + product.getQuantity() + product.getQuantityUnit() + ")" : "");

        productName.setText(productNameString);
        if(product.getBrand().equals("Unknown")){
            brandName.setVisibility(View.GONE);
        } else {
            brandName.setText(product.getBrand());
        }
        Bitmap barcodeBitmap = generateBarcode(product.getBarcode(), 600, 200);
        if (barcodeBitmap != null) {
            barcodeImage.setImageBitmap(barcodeBitmap);
        }
        barcodeText.setText(product.getBarcode());

        if(product.getEcoscore() == 0){
            ecoScore.setVisibility(View.GONE);
        } else {
            ecoScore.append("" + product.getEcoscore());
        }
        productContainer.addView(productNameView);
    }

    @SuppressLint("SetTextI18n")
    private void initalizeAllergensSection() {
        if(product.getAllergens().isEmpty()) return;

        View allergensView = inflater.inflate(R.layout.allergens_section, productContainer, false);
        LinearLayout allergensContainer = allergensView.findViewById(R.id.allergensContainer);
        Typeface robotoFont = ResourcesCompat.getFont(this, R.font.roboto);

        for (String allergen : product.getAllergens()){
            TextView allergenView = new TextView(this);
            allergenView.setText("- " + allergen);
            allergenView.setTextSize(20);
            allergenView.setTextColor(Color.parseColor("#FFFFFF"));
            allergenView.setTypeface(robotoFont, Typeface.BOLD);
            allergensContainer.addView(allergenView);
        }

        productContainer.addView(allergensView);
    }

    @SuppressLint("SetTextI18n")
    private void initializeCategoriesSection() {
        if(product.getCategories().isEmpty()) return;

        View categoriesView = inflater.inflate(R.layout.categories_section, productContainer, false);
        LinearLayout categoriesContainer = categoriesView.findViewById(R.id.categoriesContainer);
        Typeface robotoFont = ResourcesCompat.getFont(this, R.font.roboto);

        for (String category : product.getCategories()){
            TextView categoryView = new TextView(this);
            categoryView.setText("- " + category);
            categoryView.setTextSize(20);
            categoryView.setTextColor(Color.parseColor("#FFFFFF"));
            categoryView.setTypeface(robotoFont, Typeface.BOLD);
            categoriesContainer.addView(categoryView);
        }

        productContainer.addView(categoriesView);
    }

    @SuppressLint("SetTextI18n")
    private void initializeIngredientsSection() {
        if(product.getIngredients().isEmpty()) return;

        View ingredientsView = inflater.inflate(R.layout.ingredients_section, productContainer, false);
        LinearLayout ingredientsContainer = ingredientsView.findViewById(R.id.ingredientsContainer);
        Typeface robotoFont = ResourcesCompat.getFont(this, R.font.roboto);

        for (String ingredient : product.getIngredients()){
            TextView ingredientView = new TextView(this);
            ingredientView.setText("- " + ingredient);
            ingredientView.setTextSize(20);
            ingredientView.setTextColor(Color.parseColor("#FFFFFF"));
            ingredientView.setTypeface(robotoFont, Typeface.BOLD);
            ingredientsContainer.addView(ingredientView);
        }

        productContainer.addView(ingredientsView);
    }
}
