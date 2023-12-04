package com.Xyliteee.titable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SomethingWrong extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_something_wrong);
        SharedPreferences sharedPreferences = this.getSharedPreferences("courseData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("JsonString");                                                             //重置旧的Json表单数据
        editor.apply();
    }

    public void onBackPressed() {
        Intent intent1 = new Intent(SomethingWrong.this, MainActivity.class);
        startActivity(intent1);
    }

}