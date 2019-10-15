package com.example.surfaceview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void bt1(View view){

        Intent intent = new Intent(MainActivity.this,Main2Activity.class);

        startActivity(intent);
    }

    public void bt2(View view){

        Intent intent = new Intent(MainActivity.this,OpenGLActivity.class);

        startActivity(intent);
    }

}
