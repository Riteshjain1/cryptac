package com.example.cryptac;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalculateActivity extends AppCompatActivity{

    private List<String> coinSymbols = new ArrayList<String>();
    private List<String> coins = new ArrayList<String>();
    private List<String> currencySymbols = new ArrayList<String>();
    private List<String> currency = new ArrayList<String>();
    private SearchableSpinner searchableSpinner1;
    private SearchableSpinner searchableSpinner2;
    private String setCoinSym;
    private String setCurrencySym;
    private String setCoin;
    private String setCurrency;
    private Button Nextbtn;
    private BottomNavigationView bottom_navigation;
    float val = 0;
    double point = 0;
    TextView txt;
    EditText value;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        String coinNamesStr = getString(R.string.coin_names);
        String coinSymbolsStr = getString(R.string.coin_symbols);
        String currNamesStr = getString(R.string.currency);
        String currSymStr = getString(R.string.currency_symbols);

        String str[] = coinSymbolsStr.split(",");
        coinSymbols.addAll(Arrays.asList(str));
        str = coinNamesStr.split(",");
        coins.addAll(Arrays.asList(str));
        str = currNamesStr.split(",");
        currency.addAll(Arrays.asList(str));
        str = currSymStr.split(",");
        currencySymbols.addAll(Arrays.asList(str));

        coins.add(0, "");
        coinSymbols.add(0, "Search Cryptocurrencies");
        currency.add(0, "");
        currencySymbols.add(0, "Search Currencies");

        searchableSpinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<String> sp1ArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                coinSymbols);
        searchableSpinner1.setAdapter(sp1ArrayAdapter);
        searchableSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos >= 0){
                    setCoinSym = coinSymbols.get(pos);
                    setCoin = coins.get(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinner 2
        searchableSpinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> sp2ArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                currencySymbols);
        searchableSpinner2.setAdapter(sp2ArrayAdapter);
        searchableSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if(pos >= 0){
                    setCurrencySym = currencySymbols.get(pos);
                    setCurrency = currency.get(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Nextbtn = findViewById(R.id.btn);
        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard((Button)view);
                value = findViewById(R.id.num);
                if(value.getText().toString().trim().length() == 0){
                    Toast.makeText(CalculateActivity.this, "Enter Valid Inputs", Toast.LENGTH_SHORT).show();
                    return;
                }
                double number = Double.parseDouble(String.valueOf(value.getText()));
                if(setCoinSym.equals("Search Cryptocurrencies") || setCurrencySym.equals("Search Currencies") || number < 0){
                    Toast.makeText(CalculateActivity.this, "Enter Valid Inputs", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = "https://api.coincap.io/v2/assets/"+setCoin;
                try {
                    getEqualUSD();
                    Thread.sleep(300);
                }catch(Exception e){
                }
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                Request request = new Request.Builder().url(url).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {

                            final String myResponse = response.body().string();
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(myResponse);
                                JSONObject myData = obj.getJSONObject("data");
                                Double price = Double.parseDouble(myData.getString("priceUsd"));
                                point = number * (price/val);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            CalculateActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt = findViewById(R.id.txt);
                                    Formatter formatter = new Formatter();
                                    formatter.format("%.2f", point);
                                    txt.setText("The current value of "+ number + " "+ setCoinSym + " is "+ setCurrencySym+"  " + formatter.toString());
                                }
                            });
                        }
                    }
                });
            }
        });
        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setSelectedItemId(R.id.page_1);
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.page_1:
                        return true;
                    case R.id.page_2:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.page_3:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    public void getEqualUSD(){
        String url = "https://api.coincap.io/v2/rates/"+setCurrency;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    final String myResponse = response.body().string();
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(myResponse);
                        JSONObject myData = obj.getJSONObject("data");
                        val = Float.parseFloat(myData.getString("rateUsd"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch(Exception ignored) {
        }
    }
}