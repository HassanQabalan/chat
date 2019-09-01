package com.example.hasan.chat;

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

    public void btn_Start_Chat(View view) {
        Intent intent =new Intent(MainActivity.this,Activity_get_Data_Person.class);
        startActivity(intent);
    }
}
