package Activities;

import static Utils.StringUtils.generateBarcode;
import static Utils.StringUtils.isValidUnit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    }

    private void initializeProductNameSection() {
        View productNameView = inflater.inflate(R.layout.product_name_section, productContainer, false);
        TextView productName = productNameView.findViewById(R.id.productName);
        TextView brandName = productNameView.findViewById(R.id.brandName);
        ImageView barcodeImage = productNameView.findViewById(R.id.barcodeImage);
        TextView barcodeText = productNameView.findViewById(R.id.barcodeText);
        TextView ecoscore = productNameView.findViewById(R.id.ecoScoreText);

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
            ecoscore.setVisibility(View.GONE);
        } else {
            ecoscore.append("" + product.getEcoscore());
        }
        productContainer.addView(productNameView);
    }
}
