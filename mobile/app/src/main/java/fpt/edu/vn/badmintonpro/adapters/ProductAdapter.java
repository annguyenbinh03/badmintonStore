package fpt.edu.vn.badmintonpro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import fpt.edu.vn.badmintonpro.R;
import fpt.edu.vn.badmintonpro.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private OnItemClickListener listener;

    // ✅ Interface callback cho sự kiện click
    public interface OnItemClickListener {
        void onDetailsClick(Product product);
    }

    // ✅ Constructor với 3 tham số
    public ProductAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice;
        MaterialButton btnDetails;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.txtName.setText(p.getName());
        holder.txtPrice.setText(String.format("Giá: %.2f VNĐ", (double) p.getPrice()));


        Picasso.get()
                .load(p.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imgProduct);

        View.OnClickListener openDetailListener = v -> {
            if (listener != null) {
                listener.onDetailsClick(p);
            }
        };

        holder.btnDetails.setOnClickListener(openDetailListener);

        holder.itemView.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.95f).scaleY(0.95f).setDuration(100)
                    .withEndAction(() -> {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                        openDetailListener.onClick(v);
                    }).start();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
