package Activities;

import static Utils.StringUtils.capitalizeFirstLetter;
import static Utils.StringUtils.capitalizeWithSyntax;
import static Utils.StringUtils.getProductBarcode;
import static Utils.StringUtils.parseProduct;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.view.WindowInsetsCompat;

import Api.FoodApi;
import Materials.Product;
import com.example.productbarcodescanner.R;
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

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.post(() -> {
            int spinnerHeight = spinner.getHeight();
            int verticalOffset = spinnerHeight;

            spinner.setDropDownVerticalOffset(verticalOffset);
        });
        spinner.setAdapter(adapter);

        Button directCallButton = findViewById(R.id.direct_call_button);
        directCallButton.setOnClickListener(v -> fetchProductInfo(getProductBarcode(spinner.getSelectedItem().toString())));
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
}