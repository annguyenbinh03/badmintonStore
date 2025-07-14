package fpt.edu.vn.badmintonpro.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fpt.edu.vn.badmintonpro.R;
import fpt.edu.vn.badmintonpro.api.ApiClient;
import fpt.edu.vn.badmintonpro.api.ApiService;
import fpt.edu.vn.badmintonpro.model.ApiResponse;
import fpt.edu.vn.badmintonpro.model.CartItem;
import fpt.edu.vn.badmintonpro.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView txtName, txtDescription, txtPrice, txtBrand, txtWeight, txtBalance, txtTension;
    private Button btnAddToCart;
    private int productId;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgProduct = findViewById(R.id.imgProduct);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtBrand = findViewById(R.id.txtBrand);
        txtWeight = findViewById(R.id.txtWeight);
        txtBalance = findViewById(R.id.txtBalance);
        txtTension = findViewById(R.id.txtTension);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        productId = getIntent().getIntExtra("productId", -1);
        Log.d("DEBUG", "Received productId: " + productId);

        if (productId != -1) {
            fetchProductDetail(productId);
        } else {
            Toast.makeText(this, "Không có ID sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnAddToCart.setOnClickListener(v -> animateAddToCart());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchProductDetail(int id) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProductById(id).enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    currentProduct = response.body().getData();
                    txtName.setText(currentProduct.getName());
                    txtDescription.setText(currentProduct.getDescription());
                    String.format("Giá: %.2f VNĐ", (double) currentProduct.getPrice());
                    txtBrand.setText(currentProduct.getBrandName());
                    txtWeight.setText(currentProduct.getWeight());
                    txtBalance.setText(currentProduct.getBalance());
                    txtTension.setText(currentProduct.getTension());

                    Picasso.get()
                            .load(currentProduct.getImageUrl())
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .into(imgProduct);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Không tải được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateAddToCart() {
        btnAddToCart.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(() ->
                btnAddToCart.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
        ).start();

        float startX = imgProduct.getX() + imgProduct.getWidth() / 2;
        float startY = imgProduct.getY() + imgProduct.getHeight() / 2;
        float endX = getResources().getDisplayMetrics().widthPixels;
        float endY = getResources().getDisplayMetrics().heightPixels * 0.06f;

        ImageView flyingImage = new ImageView(this);
        flyingImage.setImageDrawable(imgProduct.getDrawable());
        flyingImage.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        ((ViewGroup) getWindow().getDecorView().getRootView()).addView(flyingImage);

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            float x = startX + (endX - startX) * progress;
            float y = startY + (endY - startY) * progress;
            flyingImage.setX(x - 50);
            flyingImage.setY(y - 50);
            flyingImage.setScaleX(1 - progress * 0.7f);
            flyingImage.setScaleY(1 - progress * 0.7f);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ((ViewGroup) getWindow().getDecorView().getRootView()).removeView(flyingImage);
                addToCart(currentProduct);
                Toast.makeText(ProductDetailActivity.this, txtName.getText() + " đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
        animator.start();
    }

    private void addToCart(Product product) {
        SharedPreferences prefs = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        String cartJson = prefs.getString("cart_items", "[]");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<CartItem>>() {}.getType();
        List<CartItem> cartList = gson.fromJson(cartJson, listType);
        if (cartList == null) cartList = new ArrayList<>();

        boolean found = false;
        for (CartItem item : cartList) {
            if (item.getProductId() == (product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            cartList.add(new CartItem(
                    product.getId(),
                    product.getName(),
                    product.getImageUrl(),
                    product.getPrice(),
                    1
            ));
        }

        String updatedJson = gson.toJson(cartList);
        prefs.edit().putString("cart_items", updatedJson).apply();
    }
}
