package com.texttrans.translator.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.texttrans.translator.R;
import com.texttrans.translator.app_data.HomingActivity;


public class SplashActivity extends AppCompatActivity {

    String f212gm;
    int f213i = 0;
    private SharedPreferences f214sp;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_splsh);
        setStoreToken(getResources().getString(R.string.app_name));
        goNext();
    }

    private void setStoreToken(String str) {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), 0);
        this.f214sp = sharedPreferences;
        String string = sharedPreferences.getString("gm", "");
        this.f212gm = string;
        if (this.f213i == 0 && string.equals("")) {
            SharedPreferences.Editor edit = this.f214sp.edit();
            edit.putString("gm", "0");
            edit.apply();
            this.f212gm = this.f214sp.getString("gm", "");
        }

    }



    private void goNext() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, HomingActivity.class));
                SplashActivity.this.finish();
            }
        }, 2000);
    }



}
