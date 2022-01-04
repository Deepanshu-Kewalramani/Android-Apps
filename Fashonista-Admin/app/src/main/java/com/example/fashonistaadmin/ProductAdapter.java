package com.example.fashonistaadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class ProductAdapter extends FirebaseRecyclerAdapter<Product , ProductAdapter.ProductViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {

        holder.cardView.setTag(position);

        holder.name.setText(model.getClothName());
        holder.price.setText(model.getPrice());
        holder.description.setText(model.getDescription());

        Picasso.get().load(model.getImageUrl()).into(holder.productImage);

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_cards , parent , false);
        return new ProductAdapter.ProductViewHolder(view);

    }



    class ProductViewHolder extends RecyclerView.ViewHolder{

        public CardView cardView;
        ImageView productImage;
        TextView name , description , price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.single_card);

            productImage = itemView.findViewById(R.id.cardImageView);
            name = itemView.findViewById(R.id.titleTextView);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);

        }

    }

}
