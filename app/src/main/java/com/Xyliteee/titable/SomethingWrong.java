package com.Xyliteee.titable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class SomethingWrong extends AppCompatActivity {
    public TextView somethingWrongBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_something_wrong);
        somethingWrongBox = findViewById(R.id.textView2);
        Intent intent = getIntent();
        String errorString = intent.getStringExtra("ErrorString");
        somethingWrongBox.setText(errorString);
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