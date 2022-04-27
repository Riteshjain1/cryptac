package com.example.cryptac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RedirectionPage extends AppCompatActivity {


    private TextView txt;
    private TextView co;
    private TextView curr;
    private ImageButton btn;
    double points[] = new double[364];
    private GraphView graphView;
    String currency = "";
    float val = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirection_page);

        btn = findViewById(R.id.backButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        co = findViewById(R.id.coin);
        curr = findViewById(R.id.currency);

        txt = findViewById(R.id.txt);

        String coin = intent.getStringExtra("coin");
        String currencySym = intent.getStringExtra("currencySym");
        String coinSymbol = intent.getStringExtra("coinSymbol");
        currency = intent.getStringExtra("currency");
        String url = "https://api.coincap.io/v2/assets/"+coin+"/history?interval=d1";
        co.setText(coinSymbol);
        curr.setText(currencySym);
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
                        JSONArray arr = obj.getJSONArray("data");

                        for(int i = 0; i < arr.length(); i++){
                            String  price = arr.getJSONObject(i).getString("priceUsd");
                            points[i] = Float.parseFloat(price)/val;
                        }

                        RedirectionPage.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                        txt.setText("Current value is: "+String.valueOf(points[arr.length()-1]));
                        graphView = findViewById(R.id.idGraphView);
                        DataPoint[] pts = new DataPoint[arr.length()];

                        for(int i = 0; i < arr.length(); i++){
                            pts[i] = new DataPoint(i, points[i]);
                        }

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(pts);

                        graphView.setTitle("");
                        graphView.setTitleColor(R.color.colorPrimary);
                        graphView.setTitleTextSize(15);
                        graphView.addSeries(series);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
    public void getEqualUSD(){
        String url = "https://api.coincap.io/v2/rates/"+currency;
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
}