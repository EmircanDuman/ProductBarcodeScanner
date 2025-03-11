package Api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FoodApi {
    @GET("product/{barcode}")
    Call<JsonObject> getProductInfo(@Path("barcode") String barcode);
}
