package com.example.dev.smartcook;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dev.smartcook.model.Data;
import com.example.dev.smartcook.model.Feed;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class examine extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.0.94/";
    String T1, T2, T3, T4;
    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    TextView right_up, right_down, left_up, left_down;

    public Button start_baking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine);

        mHandler = new Handler();
        startRepeatingTask();

        start_baking = (Button) findViewById(R.id.start_baking);



                start_baking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent baking = new Intent(examine.this, show_session.class);
                        startActivity(baking);
                    }
                });
    }

    protected void update_temperature_values(){
        String[] temperature_values;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ArduinoAPI arduinoAPI = retrofit.create(ArduinoAPI.class);
        Call<Feed> call = arduinoAPI.getData();
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.d("TAG", "onResponse: Server Response: " + response.toString());
                Log.d("TAG", "onResponse: received information: " + response.body().toString());
                T1 = response.body().getData().getT1();
                T2 = response.body().getData().getT2();
                T3 = response.body().getData().getT3();
                T4 = response.body().getData().getT4();

                right_down = findViewById(R.id.right_down);
                left_down =  findViewById(R.id.left_down);
                right_up =  findViewById(R.id.right_up);
                left_up = findViewById(R.id.left_up);

                right_down.setText(T1);
                left_down.setText(T2);
                right_up.setText(T3);
                left_up.setText(T4);


                Log.d("TAG", "onResponse: \n" + "Temperatury: " + "T1: " + T1 + " T2: " + T2 +
                        " T3: " + T3 +  " T4: " + T4);
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e("TAG", "onFailure: Something went wrong: " + t.getMessage());
                Toast.makeText(examine.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                update_temperature_values(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

}
