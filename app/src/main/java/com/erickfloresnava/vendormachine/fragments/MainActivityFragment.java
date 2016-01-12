package com.erickfloresnava.vendormachine.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.erickfloresnava.vendormachine.R;
import com.erickfloresnava.vendormachine.adapters.ProductsAdapter;
import com.erickfloresnava.vendormachine.beans.ProductBean;
import com.erickfloresnava.vendormachine.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by erickfloresnava on 1/11/16.
 */
public class MainActivityFragment extends Fragment {

    private Activity activity;
    private RecyclerView rvProducts;
    private ProductsAdapter adapter;
    private ArrayList<ProductBean> listProducts = null;
    private TextView tvTitle;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private AsyncTask productTask = null;

    public static String FRAGMENT_TAG = "MainFragment";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragment = inflater.inflate(R.layout.fragment_main, container, false);
        initComponents(myFragment);
        initAdapter();
        initData();
        return myFragment;
    }

    private void initComponents(View myFragment) {
        LinearLayoutManager llManager;
        activity = getActivity();

        ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        rvProducts = (RecyclerView) myFragment.findViewById(R.id.rv_products);
        llManager = new LinearLayoutManager(activity);

        rvProducts.setLayoutManager(llManager);
        rvProducts.setHasFixedSize(true);

        tvTitle = (TextView) myFragment.findViewById(R.id.tvTitle);
        progressBar = (ProgressBar) myFragment.findViewById(R.id.progressBar);
        swipeRefresh = (SwipeRefreshLayout) myFragment.findViewById(R.id.swipe_container);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listProducts = null;
                initData();
            }
        });
    }

    private void initAdapter() {
        adapter = new ProductsAdapter(activity, listProducts);
        rvProducts.setAdapter(adapter);

        tvTitle.setVisibility(View.VISIBLE);
        rvProducts.setVisibility(View.VISIBLE);
    }

    private void initData() {

        if(listProducts == null) {
            listProducts = new ArrayList<>();

            // check if you are connected or not
            if (Utils.isConnected(activity)) {
                productTask = new GetProductsTask().execute(getString(R.string.service_products));
            } else {
                Toast.makeText(activity, activity.getString(R.string.msg_connection), Toast.LENGTH_LONG).show();
                swipeRefresh.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                tvTitle.setText(getString(R.string.msg_connection));
            }
        }
        else {
            updateAdapter();
        }
    }

    private void updateAdapter() {
        adapter.setData(listProducts);
        adapter.notifyDataSetChanged();

        if(progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.GONE);

        swipeRefresh.setRefreshing(false);
    }

    private class GetProductsTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Log.i("MainActivityFragment", "Getting data...");
        }

        @Override
        protected String doInBackground(String... urls) {
            return Utils.GET(getString(R.string.service_products));
        }

        @Override
        protected void onPostExecute(String result) {

            progressBar.setVisibility(View.GONE);
            swipeRefresh.setRefreshing(false);

            if(result != null) {
                parseData(result);
            }
            else {
                Toast.makeText(activity, activity.getString(R.string.no_service), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void parseData(String result) {
        try {
            JSONObject objResult = new JSONObject(result);

            if(objResult.has("products")) {
                JSONArray aProducts = objResult.getJSONArray("products");

                for(int i=0; i<aProducts.length(); i++) {

                    JSONObject objProduct = aProducts.getJSONObject(i);

                    int id = 0;
                    String productTitle = "";
                    double productPrice = 0.0;
                    int productAvailability = 0;
                    String productImage = "";

                    if(objProduct.has("id"))
                        id = objProduct.getInt("id");
                    if(objProduct.has("title"))
                        productTitle = objProduct.getString("title");
                    if(objProduct.has("price"))
                        productPrice = objProduct.getDouble("price");
                    if(objProduct.has("availability"))
                        productAvailability = objProduct.getInt("availability");
                    if(objProduct.has("img"))
                        productImage = objProduct.getString("img");

                    listProducts.add(new ProductBean(id, productTitle, productPrice, productAvailability, productImage));
                }

                if(aProducts.length() == 0)
                    tvTitle.setText(getString(R.string.no_data));

                updateAdapter();

            }
            else {
                tvTitle.setText(getString(R.string.data_error));
            }

        }catch (JSONException jsonException){
            Toast.makeText(activity, activity.getString(R.string.data_error), Toast.LENGTH_LONG).show();
            tvTitle.setText(getString(R.string.data_error));
        }
    }

    public ArrayList<ProductBean> getAlProducts() {
        return listProducts;
    }

    public void onProductSold(int productId) {

        ProductBean productSold;

        for(int i=0; i<listProducts.size(); i++) {
            if(listProducts.get(i).getId() == productId) {
                listProducts.get(i).sellItem();
                Log.i(listProducts.get(i).getTitle(), "#" + listProducts.get(i).getAvailability());
                //updateAdapter();
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(productTask != null) {
            productTask.cancel(true);
        }
    }
}
