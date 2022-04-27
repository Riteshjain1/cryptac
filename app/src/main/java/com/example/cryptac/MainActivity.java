package com.example.cryptac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

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

class item{
    String ID;
    String sym;
    Double percent;
    Double value;
    item(String ID, String sym, Double percent, Double value){
        this.ID = ID;
        this.sym = sym;
        this.percent = percent;
        this.value = value;
    }
}

public class MainActivity extends AppCompatActivity {
    item topGain, topLoss;
    ArrayList<item> a = new ArrayList<>(); //arraylist of all items
    String url1 = "https://www.cryptologos.cc/logos/";
    String url2 = "-logo.png";
    BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CurrentTrendsActivity.class);
                startActivity(intent);
            }
        });

        populateArray();
        while(a.size() < 5);
        String[] url3 = new String[5];

        for(int i=0; i<5; i++){
            String str = a.get(i).ID+"-" + a.get(i).sym.toLowerCase();
            url3[i] = str;
        }
        topGain = a.get(0);
        topLoss = a.get(0);
        while(a.size()!=100);
        for(int i=0; i<a.size(); i++){

            if(a.get(i).percent > topGain.percent){
                topGain = a.get(i);
            }
            if(a.get(i).percent < topLoss.percent){
                topLoss = a.get(i);
            }
        }

        String str = topGain.ID+"-" + topGain.sym.toLowerCase();
        TextView coinNA = findViewById(R.id.coinNameA);
        TextView coinVA = findViewById(R.id.coinValueA);
        ImageView imageView = findViewById(R.id.imageViewA);
        Picasso.get().load(url1+str+url2).placeholder(R.drawable.ic_baseline_image_24).resize(200, 200)
                .centerCrop().into(imageView);
        coinNA.setText(topGain.sym); coinVA.setText("$" + topGain.value);

        str = topLoss.ID+"-" + topLoss.sym.toLowerCase();
        TextView coinNB = findViewById(R.id.coinNameB);
        TextView coinVB = findViewById(R.id.coinValueB);
        imageView = findViewById(R.id.imageViewB);
        Picasso.get().load(url1+str+url2).placeholder(R.drawable.ic_baseline_image_24).resize(200, 200)
                .centerCrop().into(imageView);
        coinNB.setText(topLoss.sym); coinVB.setText("$" + topLoss.value);

        TextView coinN1 = findViewById(R.id.coinName1);
        TextView coinV1 = findViewById(R.id.coinValue1);
        imageView = findViewById(R.id.imageView1);
        Picasso.get().load(url1+url3[0]+url2).placeholder(R.drawable.ic_baseline_image_24).resize(170, 170)
                .centerCrop().into(imageView);
        coinN1.setText(a.get(0).sym); coinV1.setText("$" + a.get(0).value);

        TextView coinN2 = findViewById(R.id.coinName2);
        TextView coinV2 = findViewById(R.id.coinValue2);
        imageView = findViewById(R.id.imageView2);
        Picasso.get().load(url1+url3[1]+url2).placeholder(R.drawable.ic_baseline_image_24).resize(170, 170)
                .centerCrop().into(imageView);
        coinN2.setText(a.get(1).sym); coinV2.setText("$" + a.get(1).value);


        TextView coinN3 = findViewById(R.id.coinName3);
        TextView coinV3 = findViewById(R.id.coinValue3);
        imageView = findViewById(R.id.imageView3);
        Picasso.get().load(url1+url3[2]+url2).placeholder(R.drawable.ic_baseline_image_24).resize(170, 170)
                .centerCrop().into(imageView);
        coinN3.setText(a.get(2).sym); coinV3.setText("$" + a.get(2).value);

        TextView coinN4 = findViewById(R.id.coinName4);
        TextView coinV4 = findViewById(R.id.coinValue4);
        imageView = findViewById(R.id.imageView4);
        Picasso.get().load(url1+url3[3]+url2).placeholder(R.drawable.ic_baseline_image_24).resize(170, 170)
                .centerCrop().into(imageView);
        coinN4.setText(a.get(3).sym); coinV4.setText("$" + a.get(3).value);

        TextView coinN5 = findViewById(R.id.coinName5);
        TextView coinV5 = findViewById(R.id.coinValue5);
        imageView = findViewById(R.id.imageView5);
        Picasso.get().load(url1+url3[4]+url2).placeholder(R.drawable.ic_baseline_image_24).resize(170, 170)
                .centerCrop().into(imageView);
        coinN5.setText(a.get(4).sym); coinV5.setText("$" + a.get(4).value);

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setSelectedItemId(R.id.page_2);
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.page_1:
                        startActivity(new Intent(getApplicationContext(), CalculateActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.page_2:
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

    void populateArray(){
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
                    JSONObject obj;
                    try {
                        obj = new JSONObject(myResponse);
                        JSONArray arr = obj.getJSONArray("data");
                        while(arr.length() == 0);
                        for(int i = 0;i < arr.length();i++){
                            Double val = Double.parseDouble(arr.getJSONObject(i).getString("priceUsd"));
                            double val2 = (double) Math.round(val * 100) / 100;
                            item it = new item(arr.getJSONObject(i).getString("id"),
                                    arr.getJSONObject(i).getString("symbol"),
                                    Double.parseDouble(arr.getJSONObject(i).getString("changePercent24Hr")),
                                    val2);
                            a.add(it);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}