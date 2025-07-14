package fpt.edu.vn.badmintonpro.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fpt.edu.vn.badmintonpro.R;
import fpt.edu.vn.badmintonpro.api.ApiClient;
import fpt.edu.vn.badmintonpro.api.ApiService;
import fpt.edu.vn.badmintonpro.model.ApiResponse;
import fpt.edu.vn.badmintonpro.model.CartItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartContainer;
    private TextView txtTotalPrice, txtCartStatus;
    private Button btnCheckout, btnContinue;
    private List<CartItem> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Giỏ hàng");

        cartContainer = findViewById(R.id.cartContainer);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtCartStatus = findViewById(R.id.txtCartStatus);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnContinue = findViewById(R.id.btnContinue);

        loadCart();

        btnContinue.setOnClickListener(v -> finish());
        btnCheckout.setOnClickListener(v -> placeOrder());
    }

    private void loadCart() {
        SharedPreferences prefs = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        String cartJson = prefs.getString("cart_items", "[]");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<CartItem>>() {}.getType();
        cartList = gson.fromJson(cartJson, listType);
        if (cartList == null) cartList = new ArrayList<>();

        cartContainer.removeAllViews();

        if (cartList.isEmpty()) {
            txtCartStatus.setVisibility(View.VISIBLE);
            txtTotalPrice.setText("0 đ");
            return;
        } else {
            txtCartStatus.setVisibility(View.GONE);
        }

        double total = 0;

        for (CartItem item : cartList) {
            View itemView = getLayoutInflater().inflate(R.layout.item_cart, null);

            ImageView imgProduct = itemView.findViewById(R.id.imgProduct);
            TextView txtName = itemView.findViewById(R.id.txtProductName);
            TextView txtPrice = itemView.findViewById(R.id.txtProductPrice);
            TextView txtQuantity = itemView.findViewById(R.id.txtQuantity);
            ImageView btnPlus = itemView.findViewById(R.id.btnPlus);
            ImageView btnMinus = itemView.findViewById(R.id.btnMinus);
            ImageView btnDelete = itemView.findViewById(R.id.btnDelete);

            txtName.setText(item.getName());
            txtPrice.setText(formatCurrency(item.getPrice()));
            txtQuantity.setText(String.valueOf(item.getQuantity()));
            Picasso.get().load(item.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(imgProduct);

            total += item.getPrice() * item.getQuantity();

            btnPlus.setOnClickListener(v -> {
                item.setQuantity(item.getQuantity() + 1);
                saveCart();
                loadCart();
            });

            btnMinus.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    saveCart();
                    loadCart();
                }
            });

            btnDelete.setOnClickListener(v -> {
                cartList.remove(item);
                saveCart();
                loadCart();
            });

            cartContainer.addView(itemView);
        }

        txtTotalPrice.setText(formatCurrency(total));
    }

    private void saveCart() {
        SharedPreferences prefs = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(cartList);
        editor.putString("cart_items", json);
        editor.apply();
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    private void showToast(String message) {
        Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void placeOrder() {
        if (cartList.isEmpty()) {
            showToast("Giỏ hàng trống!");
            return;
        }

        List<Map<String, Object>> items = new ArrayList<>();
        for (CartItem item : cartList) {
            Map<String, Object> map = new HashMap<>();
            map.put("racketId", item.getProductId());
            map.put("quantity", item.getQuantity());
            items.add(map);
        }

        Map<String, Object> order = new HashMap<>();
        order.put("userId", 1);
        order.put("shippingAddress", "Hanoi, Vietnam");
        order.put("paymentMethod", "COD");
        order.put("items", items);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiResponse<Integer>> call = apiService.createOrder(order);
        call.enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    showToast("Đặt hàng thành công! ID: " + response.body().getData());
                    cartList.clear();
                    saveCart();
                    loadCart();
                } else {
                    showToast("Lỗi đặt hàng: " + (response.body() != null ? response.body().getMessage() : "Không rõ lỗi"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                showToast("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
