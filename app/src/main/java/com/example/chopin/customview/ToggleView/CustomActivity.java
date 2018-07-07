package com.example.chopin.customview.ToggleView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chopin.customview.R;

public class CustomActivity extends AppCompatActivity {
    ToggleView toggleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        toggleView=findViewById(R.id.toggleview);

//        toggleView.setSwitchBackgroundResource(R.drawable.switch_background);
//        toggleView.setSlideButtonResource(R.drawable.slide_button);
//        toggleView.setSwitchState(false);
        toggleView.setSwitchStateUpdateListener(new ToggleView.OnSwitchStateUpdateListener() {
            @Override
            public void onStateUpdate(boolean state) {
                Toast.makeText(getApplicationContext(),"state:"+state,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
