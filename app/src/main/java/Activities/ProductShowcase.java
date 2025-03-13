package Activities;

import static Utils.StringUtils.generateBarcode;

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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import Materials.Product;

public class ProductShowcase extends AppCompatActivity {

    private Product product;
    private LinearLayout productContainer;
    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_showcase);

        Intent intent = getIntent();
        this.product = (Product) intent.getSerializableExtra("product");
        productContainer = findViewById(R.id.productContainer);

        if(product == null) return;

        this.inflater = LayoutInflater.from(this);

        View productNameView = inflater.inflate(R.layout.product_name_section, productContainer, false);
        TextView productName = productNameView.findViewById(R.id.productName);
        TextView brandName = productNameView.findViewById(R.id.brandName);
        ImageView barcodeImage = productNameView.findViewById(R.id.barcodeImage);
        TextView barcodeText = productNameView.findViewById(R.id.barcodeText);

        productName.setText(product.getProductName());
        brandName.setText(product.getBrand());
        Bitmap barcodeBitmap = generateBarcode(product.getBarcode(), 600, 200);
        if (barcodeBitmap != null) {
            barcodeImage.setImageBitmap(barcodeBitmap);
        }
        barcodeText.setText(product.getBarcode());
        productContainer.addView(productNameView);

    }
}
