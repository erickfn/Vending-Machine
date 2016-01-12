package com.erickfloresnava.vendormachine;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.erickfloresnava.vendormachine.R;
import com.erickfloresnava.vendormachine.fragments.DetailsFragment;
import com.erickfloresnava.vendormachine.fragments.MainActivityFragment;

/**
 * Created by erickfloresnava on 1/11/16.
 */
public class MainActivity extends AppCompatActivity implements DetailsFragment.OnStockChangedListener{

    private MainActivityFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actiobar_bg)));

        if(savedInstanceState == null) {
            FragmentActivity fragment = (FragmentActivity) this;
            FragmentTransaction transaction = fragment.getSupportFragmentManager().beginTransaction();

            // Create new fragment and transaction
            mainFragment = new MainActivityFragment();
            transaction.replace(R.id.fragment, mainFragment, MainActivityFragment.FRAGMENT_TAG);

            // Commit the transaction
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.getSupportFragmentManager().popBackStack();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onProductSold(int productId) {
        if (mainFragment != null) {
            mainFragment.onProductSold(productId);
        }
    }
}
