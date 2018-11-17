package com.example.dev.smartcook;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dev.smartcook.model.Feed;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class show_session extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.0.95/";
    public String T1, T2, T3, T4, T1_raw, T2_raw, T3_raw, T4_raw;
    private int mInterval = 50000; // 5 seconds by default, can be changed later
    private int tInterval = 10000;
    private int vInterval = 60000;
    private Handler tHandler, mHandler, vHandler;
    TextView right_up, right_down, left_up, left_down;
    public String temperature, dish, temp_minutes, display_temp;
    public int minutes, temporary_temp;
    public int longer_time = 0;
    long startTime;
    public Button finish, add_5_min;
    public boolean is_finished = false;
    public boolean half_time;
    public boolean five_mins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        half_time = true;
        five_mins = true;
        temperature = getIntent().getStringExtra("temp");
        dish = getIntent().getStringExtra("dish");
        temp_minutes =  getIntent().getStringExtra("time");
        minutes = Integer.valueOf(temp_minutes.substring(0, temp_minutes.length()-4));

        setContentView(R.layout.activity_show_session);

        finish = findViewById(R.id.finish);
        add_5_min = findViewById(R.id.add_5_min);

        mHandler = new Handler();
        tHandler = new Handler();
        vHandler = new Handler();

        startRepeatingTaskTemperatures();
        startRepeatingTaskTime();
        startRepeatingTaskValues();
        TextView dish_temp = findViewById(R.id.dish_temp);
        String display_dish = dish + ": " + temperature;
        dish_temp.setText(display_dish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_finished = true;
            }
        });

        add_5_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (temporary_temp < minutes/2)
                   longer_time += 1;
            }
        });

    }

    protected void update_temperature_values(){
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
                T1_raw = response.body().getData().getT1();
                T2_raw = response.body().getData().getT2();
                T3_raw = response.body().getData().getT3();
                T4_raw = response.body().getData().getT4();

                T1 = response.body().getData().getT1() +  " °C";
                T2 = response.body().getData().getT2() +  " °C";
                T3 = response.body().getData().getT3() +  " °C";
                T4 = response.body().getData().getT4() +  " °C";

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
                Toast.makeText(show_session.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void is_temperature_incorrect() {
        float T1_raw_float, T2_raw_float, T3_raw_float, T4_raw_float;
        float temperature_float = Float.valueOf(temperature.substring(0, temperature.length() - 3));
        try {
            T1_raw_float = Float.valueOf(T1_raw);
            T2_raw_float = Float.valueOf(T1_raw);
            T3_raw_float = Float.valueOf(T1_raw);
            T4_raw_float = Float.valueOf(T1_raw);
        } catch (NullPointerException e) {
            T1_raw_float = temperature_float;
            T2_raw_float = temperature_float;
            T3_raw_float = temperature_float;
            T4_raw_float = temperature_float;
        }
        if ((Math.abs(T1_raw_float - temperature_float) > 10) || (Math.abs(T2_raw_float - temperature_float) > 10)
            || (Math.abs(T3_raw_float - temperature_float) > 10) || (Math.abs(T4_raw_float - temperature_float) > 10))
            openDialog("Pay attention!", "One of the temperature values is incorrect.");

    }

    protected void update_time_value() {
        TextView time_left = findViewById(R.id.time_left);
        if (is_finished) {
            String msg = "Finished";
            time_left.setText(msg);
            openDialog("Baking finished!", "Bon appetit!");

        }
        else {

            long actual_time = System.currentTimeMillis();
            int temp = (int) ((actual_time - startTime) / 60000);


            temporary_temp = minutes - temp + 5 * longer_time;
            if (temporary_temp == 1)
                is_finished = true;
            display_temp = temporary_temp + " minutes left";
            time_left.setText(display_temp);

            if (half_time) {
                if (temporary_temp == minutes / 2) {
                    half_time = false;
                    openDialog("Pay attention!", "A half of time has passed.");
                }

            }
            if (five_mins) {
                if (temporary_temp == 5) {
                    openDialog("Pay attention!", "5 minutes left! To lengthen - press the button below.");
                    five_mins = false;
                }

            }
        }
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
    Runnable tStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                update_time_value(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
               tHandler.postDelayed(tStatusChecker, tInterval);
            }
        }
    };

    public void openDialog(String title, String msg) {
        AlertDialog.Builder notification = new AlertDialog.Builder(show_session.this);
        notification.setCancelable(false);
        notification.setTitle(title);
        notification.setMessage(msg);

        notification.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        notification.show();
    }

    Runnable vStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                is_temperature_incorrect();

            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                vHandler.postDelayed(vStatusChecker, vInterval);
            }
        }
    };

    void startRepeatingTaskValues() {
        vStatusChecker.run();
    }

    void startRepeatingTaskTemperatures() {
        mStatusChecker.run();
    }
    void startRepeatingTaskTime() {
        tStatusChecker.run();
    }
}
