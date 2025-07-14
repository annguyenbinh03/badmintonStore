package fpt.edu.vn.badmintonpro.api;

import java.util.List;
import java.util.Map;

import fpt.edu.vn.badmintonpro.model.ApiResponse;
import fpt.edu.vn.badmintonpro.model.LoginRequest;
import fpt.edu.vn.badmintonpro.model.LoginResponse;
import fpt.edu.vn.badmintonpro.model.Product;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("rackets")
    Call<ApiResponse<List<Product>>> getrackets();

    @GET("rackets/{id}")
    Call<ApiResponse<Product>> getProductById(@Path("id") int id);

    @POST("/api/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/orders")
    Call<ApiResponse<Integer>> createOrder(@Body Map<String, Object> body);


}
