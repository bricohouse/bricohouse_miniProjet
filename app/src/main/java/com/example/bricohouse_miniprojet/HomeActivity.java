package com.example.bricohouse_miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }


    public void sign_in(View view) {
        final Intent intent=new Intent(HomeActivity.this,SingUPActivity.class);
        startActivity(intent);
    }

    public void sign_up(View view) {
        Intent intent=new Intent(HomeActivity.this,SingUPActivity.class);
        startActivity(intent);
    }
}
