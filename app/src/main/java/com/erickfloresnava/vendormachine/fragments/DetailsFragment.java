package com.erickfloresnava.vendormachine.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.erickfloresnava.vendormachine.R;
import com.erickfloresnava.vendormachine.beans.ProductBean;
import com.erickfloresnava.vendormachine.interfaces.VendorMachine;
import com.erickfloresnava.vendormachine.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by erickfloresnava on 1/11/16.
 */
public class DetailsFragment extends Fragment {

    private Activity activity;
    private ProgressBar progressBar;
    private ImageView imgIcon;
    private TextView tvTitle;
    private TextView tvPrice;
    private TextView tvPaid;
    private TextView tvAvailability;
    private Button btnCurrency1;
    private Button btnCurrency2;
    private Button btnCurrency3;
    private Button btnCurrency4;
    private Button btnCurrency5;
    private Button btnCurrency6;
    private Button btnCancel;
    private Button btnConfirm;
    private ProductBean productData;
    private double moneyPaid = 0;
    private OnStockChangedListener mCallback;

    public DetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragment = inflater.inflate(R.layout.fragment_details, container, false);

        initComponents(myFragment);
        initData();

        return myFragment;
    }

    private void initComponents(View myFragment) {
        activity = getActivity();

        ((AppCompatActivity)activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) myFragment.findViewById(R.id.progressBar);
        imgIcon = (ImageView) myFragment.findViewById(R.id.imgIcon);
        tvTitle = (TextView) myFragment.findViewById(R.id.tvTitle);
        tvPrice = (TextView) myFragment.findViewById(R.id.tvPrice);
        tvPaid = (TextView) myFragment.findViewById(R.id.tvPaid);
        tvAvailability = (TextView) myFragment.findViewById(R.id.tvAvailability);

        btnCurrency1 = (Button) myFragment.findViewById(R.id.btnCurrency1);
        btnCurrency2 = (Button) myFragment.findViewById(R.id.btnCurrency2);
        btnCurrency3 = (Button) myFragment.findViewById(R.id.btnCurrency3);
        btnCurrency4 = (Button) myFragment.findViewById(R.id.btnCurrency4);
        btnCurrency5 = (Button) myFragment.findViewById(R.id.btnCurrency5);
        btnCurrency6 = (Button) myFragment.findViewById(R.id.btnCurrency6);
        btnCancel = (Button) myFragment.findViewById(R.id.btnCancel);
        btnConfirm = (Button) myFragment.findViewById(R.id.btnConfirm);

        btnCurrency1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment(VendorMachine.currency[5]);
            }
        });
        btnCurrency2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment(VendorMachine.currency[4]);
            }
        });
        btnCurrency3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment(VendorMachine.currency[3]);
            }
        });
        btnCurrency4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment(VendorMachine.currency[2]);
            }
        });
        btnCurrency5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment(VendorMachine.currency[1]);
            }
        });
        btnCurrency6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment(VendorMachine.currency[0]);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPayment();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPayment();
            }
        });
    }

    private void initData() {
        Bundle bundle = getArguments();
        productData = (ProductBean) bundle.getSerializable("productData");

        progressBar.setVisibility(View.GONE);

        tvTitle.setText(productData.getTitle());
        tvPrice.setText(Double.toString(productData.getPrice()));
        tvAvailability.setText(" " + Integer.toString(productData.getAvailability()));
        tvPaid.setText("0.0");

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(productData.getImg(), imgIcon);
    }

    private void makePayment(Double money) {
        if(moneyPaid < productData.getPrice()) {
            moneyPaid = Utils.round((moneyPaid + money), 2);
            tvPaid.setText("" + moneyPaid);
        }
        else {
            Toast.makeText(activity, activity.getString(R.string.priceReached), Toast.LENGTH_LONG).show();
        }
    }

    private void cancelPayment() {
        if(moneyPaid > 0) {
            showMoneyBack(getMoneyBack(moneyPaid));
            Toast.makeText(activity, activity.getString(R.string.purchaseCanceled), Toast.LENGTH_LONG).show();
            moneyPaid = 0.0;
            tvPaid.setText("" + moneyPaid);
        }
        else {
            Toast.makeText(activity, activity.getString(R.string.purchaseCanceled), Toast.LENGTH_LONG).show();
            getFragmentManager().popBackStack();
        }
    }

    private void confirmPayment() {
        if(moneyPaid >= productData.getPrice()) {
            double moneyBack = Utils.round(moneyPaid - productData.getPrice(), 2);
            showMoneyBack(getMoneyBack(moneyBack));

            Toast.makeText(activity, activity.getString(R.string.purchaseConfirmed), Toast.LENGTH_LONG).show();
            moneyPaid = 0.0;
            tvPaid.setText("" + moneyPaid);

            mCallback.onProductSold(productData.getId());
        }
        else {
            double missingMoney = Utils.round(productData.getPrice() - moneyPaid, 2);
            Toast.makeText(activity, activity.getString(R.string.priceNotReached) + " " + activity.getString(R.string.missingMoney) + " $" + missingMoney, Toast.LENGTH_LONG).show();
        }
    }

    private String getMoneyBack(Double moneyBack) {
        String message = "";
        double total = moneyBack;

        for(int i=0; i<VendorMachine.currency.length; i++) {
            int numCurrency = (int)(moneyBack / VendorMachine.currency[i]);
            moneyBack = Utils.round(moneyBack % VendorMachine.currency[i], 2);

            if(numCurrency != 0) {
                String currency;
                if(VendorMachine.currency[i] >= 1) {
                    currency = "" + Utils.round(VendorMachine.currency[i], 0);
                    message = message + "Denomination: $" + currency + " - Number: " + numCurrency + "\n";
                }
                else {
                    currency = "" + Utils.round((VendorMachine.currency[i] * 100), 0);
                    message = message + "Denomination: " + currency + "¢ - Number: " + numCurrency + "\n";
                }
            }
        }

        message = message + "\n" + "Total: $" + total;

        return message;
    }

    private void showMoneyBack(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                getFragmentManager().popBackStack();
            }
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface OnStockChangedListener {
        public void onProductSold(int productId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnStockChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStockChangedListener");
        }
    }
}
