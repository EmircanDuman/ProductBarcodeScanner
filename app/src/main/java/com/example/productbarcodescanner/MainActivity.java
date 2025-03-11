package com.example.productbarcodescanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.view.WindowInsetsCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCAN = 1;
    private static final String BASE_URL = "https://world.openfoodfacts.org/api/v0/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button scanButton = findViewById(R.id.scan_button);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            String barcode = data.getStringExtra("SCAN_RESULT");
            fetchProductInfo(barcode);
        }
    }

    private void fetchProductInfo(String barcode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.net/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoodApi api = retrofit.create(FoodApi.class);
        Call<JsonObject> call = api.getProductInfo(barcode);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                    assert response.body() != null;
                    JsonObject product = response.body().getAsJsonObject("product");
                    Product product1 = parseProduct(product);
                    Log.d("PRODUCT", product1.toString());
                }
                else {
                    Toast.makeText(MainActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Product parseProduct(JsonObject product) {
        Product product1 = new Product();
        product1.setProductName(capitalizeWithSyntax(product.get("product_name").getAsString()));
        product1.setBarcode(product.get("code").getAsString());
        product1.setBrand(capitalizeFirstLetter(product.get("brands").getAsString()));
        product1.setQuantity(product.get("quantity").getAsString());
        product1.setEcoscore(product.get("ecoscore_score").getAsInt());

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

    private String capitalizeWithSyntax(String productName) {
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


    private String capitalizeFirstLetter(String brand) {
        if (brand == null || brand.isEmpty()) {
            return brand;
        }
        return Character.toUpperCase(brand.charAt(0)) + brand.substring(1).toLowerCase();
    }

}