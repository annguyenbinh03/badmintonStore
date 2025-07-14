package fpt.edu.vn.badmintonpro.ui.home;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.vn.badmintonpro.R;
import fpt.edu.vn.badmintonpro.adapters.ProductAdapter;
import fpt.edu.vn.badmintonpro.api.ApiClient;
import fpt.edu.vn.badmintonpro.api.ApiService;
import fpt.edu.vn.badmintonpro.model.ApiResponse;
import fpt.edu.vn.badmintonpro.model.Product;
import fpt.edu.vn.badmintonpro.ui.CartActivity;
import fpt.edu.vn.badmintonpro.ui.ProductDetailActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private LinearLayout errorView;
    private MaterialButton btnRetry;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Toolbar setup
        MaterialToolbar toolbar = root.findViewById(R.id.homeToolbar);
        toolbar.setOnMenuItemClickListener(this::onToolbarMenuItemClick);

        // View bindings
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        rvProducts = root.findViewById(R.id.rvProducts);
        progressBar = root.findViewById(R.id.progressBar);
        errorView = root.findViewById(R.id.errorView);
        btnRetry = root.findViewById(R.id.btnRetry);

        // RecyclerView
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvProducts.addItemDecoration(new GridSpacingItemDecoration(24));
        productAdapter = new ProductAdapter(getContext(), productList, this::navigateToProductDetails);
        rvProducts.setAdapter(productAdapter);

        // Swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(this::fetchProducts);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        // Retry button
        btnRetry.setOnClickListener(v -> fetchProducts());

        fetchProducts();
        return root;
    }

    private boolean onToolbarMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(getContext(), CartActivity.class));
            return true;
        }
        return false;
    }

    private void fetchProducts() {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        rvProducts.setVisibility(View.GONE);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getrackets().enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Product>>> call, @NonNull Response<ApiResponse<List<Product>>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    productList.clear();
                    productList.addAll(response.body().getData());
                    productAdapter.notifyDataSetChanged();
                    rvProducts.setVisibility(View.VISIBLE);
                    errorView.setVisibility(View.GONE);
                } else {
                    showError("Lỗi: " + (response.body() != null ? response.body().getMessage() : "Không có dữ liệu."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Product>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        rvProducts.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        TextView errorText = errorView.findViewById(R.id.errorText);
        errorText.setText(message);
    }

    private void navigateToProductDetails(Product p) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra("productId", p.getId());
        startActivity(intent);
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private final int spacing;

        public GridSpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % 2; // item column

            outRect.left = spacing - column * spacing / 2;
            outRect.right = (column + 1) * spacing / 2;

            if (position < 2) {
                outRect.top = spacing;
            }
            outRect.bottom = spacing;
        }
    }
}
