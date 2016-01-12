package com.erickfloresnava.vendormachine.interfaces;

import android.view.View;

/**
 * Created by erickfloresnava on 1/11/16.
 */
public interface VendorMachine {

    double currency[] = {5.0, 1.0, 0.25, 0.1, 0.05, 0.01};

    void showProducts(View view);
    void sellProduct(int idProduct);
    void receivePayment(double money);
    double giveMoneyBack();
    void cancelOrder();
}
