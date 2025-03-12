package Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.productbarcodescanner.R;

import Materials.Product;

public class ProductShowcase extends AppCompatActivity {

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        this.product = (Product) intent.getSerializableExtra("product_key");

        //setContentView(R.layout.activity_product_showcase);
    }
}
