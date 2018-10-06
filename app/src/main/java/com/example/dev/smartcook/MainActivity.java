package com.example.dev.smartcook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public Button start_heating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_heating = (Button) findViewById(R.id.start_heating);
        start_heating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent heating = new Intent(MainActivity.this, examine.class);
                startActivity(heating);
            }
        });
    }


}
