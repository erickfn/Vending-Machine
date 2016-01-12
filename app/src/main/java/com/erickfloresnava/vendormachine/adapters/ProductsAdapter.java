package com.erickfloresnava.vendormachine.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erickfloresnava.vendormachine.R;
import com.erickfloresnava.vendormachine.beans.ProductBean;
import com.erickfloresnava.vendormachine.fragments.DetailsFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erickfloresnava on 1/11/16.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductBean> listProducts;

    public ProductsAdapter(Context context, List<ProductBean> listProducts){
        this.context = context;
        this.listProducts = listProducts;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        RelativeLayout rlCardContent;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvAvailability;
        ImageView imgIcon;

        ProductViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            rlCardContent = (RelativeLayout)itemView.findViewById(R.id.rlCardContent);
            tvTitle = (TextView)itemView.findViewById(R.id.tvProductTitle);
            tvPrice = (TextView)itemView.findViewById(R.id.tvProductPrice);
            tvAvailability = (TextView)itemView.findViewById(R.id.tvProductAvailability);
            imgIcon = (ImageView)itemView.findViewById(R.id.imgIcon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            changeFragment(getPosition());
        }
    }

    @Override
    public int getItemCount() {
        if(listProducts != null)
            return listProducts.size();
        return 0;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, viewGroup, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, int i) {
        productViewHolder.tvTitle.setText(listProducts.get(i).getTitle());
        productViewHolder.tvPrice.setText("$" + Double.toString(listProducts.get(i).getPrice()));
        productViewHolder.tvAvailability.setText("Available: " + listProducts.get(i).getAvailability());

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(listProducts.get(i).getImg(), productViewHolder.imgIcon);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void changeFragment(int position) {

        if(listProducts.get(position).getAvailability() > 0) {

            FragmentActivity fragment = (FragmentActivity) context;

            // Create new fragment and transaction
            DetailsFragment detailsFragment = new DetailsFragment();
            FragmentTransaction transaction = fragment.getSupportFragmentManager().beginTransaction();

            // Send data
            Bundle bundles = new Bundle();
            ProductBean productData = listProducts.get(position);
            bundles.putSerializable("productData", productData);
            detailsFragment.setArguments(bundles);

            // Animation
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_left);

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.fragment, detailsFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
        else {
            Toast.makeText(context, context.getString(R.string.productUnavailable), Toast.LENGTH_LONG).show();
        }
    }

    public void setData(ArrayList<ProductBean> listProducts) {
        this.listProducts = listProducts;
    }
}
