package com.erickfloresnava.vendormachine.utils;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by erickfloresnava on 1/11/16.
 */
public class Utils {

    public static boolean isConnected(Activity context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static String GET(String url){

        String result = "";

        if(!url.contains("dummy")) {

            InputStream inputStream = null;
            try {

                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = null;

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
        }
        else {
            result = getDummyData();
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public static String getDummyData() {
        String result = "";
        JSONObject jsonResult = new JSONObject();

        try {
            JSONArray products = new JSONArray();

            JSONObject product1 = new JSONObject();
            product1.put("id", "1");
            product1.put("title", "Water");
            product1.put("price", 3.5);
            product1.put("availability", 15);
            product1.put("img", "http://testweb.com.mx/icons/bottle24_by_freepik.png");

            JSONObject product2 = new JSONObject();
            product2.put("id", "2");
            product2.put("title", "Phone");
            product2.put("price", 199);
            product2.put("availability", 3);
            product2.put("img", "http://testweb.com.mx/icons/cell12_by_freepik.png");

            JSONObject product3 = new JSONObject();
            product3.put("id", "3");
            product3.put("title", "USB Wire");
            product3.put("price", 5);
            product3.put("availability", 5);
            product3.put("img", "http://testweb.com.mx/icons/connection95_by_freepik.png");

            JSONObject product4 = new JSONObject();
            product4.put("id", "4");
            product4.put("title", "Headphones");
            product4.put("price", 99);
            product4.put("availability", 6);
            product4.put("img", "http://testweb.com.mx/icons/headphones13_by_freepik.png");

            JSONObject product5 = new JSONObject();
            product5.put("id", "5");
            product5.put("title", "Toilet Paper");
            product5.put("price", 0.5);
            product5.put("availability", 10);
            product5.put("img", "http://testweb.com.mx/icons/paper69_by_freepik.png");

            JSONObject product6 = new JSONObject();
            product6.put("id", "6");
            product6.put("title", "Pizza");
            product6.put("price", 4.99);
            product6.put("availability", 8);
            product6.put("img", "http://testweb.com.mx/icons/pizza3_by_freepik.png");

            products.put(product1);
            products.put(product2);
            products.put(product3);
            products.put(product4);
            products.put(product5);
            products.put(product6);

            jsonResult.put("products", products);
            result = jsonResult.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
