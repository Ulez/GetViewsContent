package com.ulez.getviewscontent;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt).setOnClickListener(this);
        startService(new Intent(this, MyAccessibilityService.class));
//        Log.e("lcy","startService");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                break;
        }
    }
}
