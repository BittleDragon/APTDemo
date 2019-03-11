package com.rxt.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.rxt.anolib.OnClick;
import com.rxt.onclickbindtool.OnClickBindTool;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnClickBindTool.bind(this);
    }

    @OnClick({R.id.tv_hello_world, R.id.btn_apt})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hello_world :
                Toast.makeText(this, "hello world", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_apt :
                Toast.makeText(this, "apt_binding success", Toast.LENGTH_SHORT).show();
                break;
            default :
                break;
        }
    }
}
