package com.example.dev.smartcook;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class examine extends AppCompatActivity {
    public Button start_baking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine);
        start_baking = (Button) findViewById(R.id.start_baking);
        start_baking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent baking = new Intent(examine.this, show_session.class);
                startActivity(baking);
            }
        });
    }
}
