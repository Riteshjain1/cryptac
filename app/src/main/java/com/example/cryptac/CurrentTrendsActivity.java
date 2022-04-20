package com.example.cryptac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CurrentTrendsActivity extends AppCompatActivity{

    private Button btn;
    private SearchableSpinner searchableSpinner1;
    private SearchableSpinner searchableSpinner2;
    private ArrayList<String> coinSymbols = new ArrayList<String>();
    private ArrayList<String> coins = new ArrayList<String>();
    private ArrayList<String> currency = new ArrayList<String>();
    private ImageButton backButton;
    private String setCoinSym;
    private String setCoin;
    private String setCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_trends);

        //spinner 1
        coins.add("");
        coinSymbols.add("Search Cryptocurrencies");
        populateCoins();
        while(coinSymbols.size() == 1){};
        searchableSpinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<String> sp1ArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                coinSymbols);
        searchableSpinner1.setAdapter(sp1ArrayAdapter);
        searchableSpinner1 = findViewById(R.id.spinner1);
        searchableSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos != 0){
                    setCoinSym = coinSymbols.get(pos);
                    setCoin = coins.get(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinner 2
        currency.add("Search currencies");
        populateCurrency();
        while(currency.size() == 1){};
        searchableSpinner2 = findViewById(R.id.spinner2);
        searchableSpinner2.setTitle("Search Cryptocurrencies");
        searchableSpinner2.setPositiveButton("OK");
        ArrayAdapter<String> sp2ArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                currency);
        searchableSpinner2.setAdapter(sp2ArrayAdapter);
        searchableSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if(pos != 0){
                    setCurrency = currency.get(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //button code here
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RedirectionPage.class);
                intent.putExtra("coin",setCoin);
                intent.putExtra("currency", setCurrency);
                intent.putExtra("coinSymbol",setCoinSym);
                startActivity(intent);
            }
        });

        //backButton code
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void populateCoins(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.coincap.io/v2/assets")
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
                        JSONArray arr = obj.getJSONArray("data");
                        int i;
                        for(i = 0; i<arr.length(); i++){
                            String coin_sym = arr.getJSONObject(i).getString("symbol");
                            String coin = arr.getJSONObject(i).getString("id");
                            coins.add(coin);
                            coinSymbols.add(coin_sym);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void populateCurrency(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.coincap.io/v2/rates")
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
                        JSONArray arr = obj.getJSONArray("data");
                        int i;
                        for(i = 0; i<arr.length(); i++){
                            String cur_sym = arr.getJSONObject(i).getString("symbol");
                            currency.add(cur_sym);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}