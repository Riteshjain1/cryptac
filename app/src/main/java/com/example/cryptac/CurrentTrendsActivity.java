package com.example.cryptac;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CurrentTrendsActivity extends AppCompatActivity{

    private Button btn;
    private SearchableSpinner searchableSpinner1;
    private SearchableSpinner searchableSpinner2;
    private List<String> coinSymbols = new ArrayList<String>();
    private List<String> coins = new ArrayList<String>();
    private List<String> currencySymbols = new ArrayList<String>();
    private List<String> currency = new ArrayList<String>();
    private ImageButton backButton;
    private String setCoinSym;
    private String setCoin;
    private String setCurrency;
    private String setCurrencySym;
    private String coinNamesStr;
    private String coinSymbolsStr;
    private String currSymStr;
    private String currNamesStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_trends);
        coinNamesStr = getString(R.string.coin_names);
        coinSymbolsStr = getString(R.string.coin_symbols);
        currNamesStr = getString(R.string.currency);
        currSymStr = getString(R.string.currency_symbols);

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
        currencySymbols.add(0, "Search currencies");

        searchableSpinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<String> sp1ArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                coinSymbols);
        searchableSpinner1.setAdapter(sp1ArrayAdapter);
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
        searchableSpinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> sp2ArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                currencySymbols);
        searchableSpinner2.setAdapter(sp2ArrayAdapter);
        searchableSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if(pos != 0){
                    setCurrencySym = currencySymbols.get(pos);
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
                intent.putExtra("currencySym", setCurrencySym);
                intent.putExtra("coinSymbol",setCoinSym);
                intent.putExtra("currency",setCurrency);
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

}