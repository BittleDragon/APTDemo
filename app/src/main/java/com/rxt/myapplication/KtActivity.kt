package com.rxt.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.rxt.anolib.OnClick
import com.rxt.onclickbindtool.OnClickBindTool

/**
 * Created by raoxuting on 2019/12/18.
 */
class KtActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OnClickBindTool.bind(this)
    }

    @OnClick(R.id.tv_hello_world, R.id.btn_apt)
    fun onViewClick(view: View) {
        when (view.id) {
            R.id.tv_hello_world -> Toast.makeText(this, "hello world", Toast.LENGTH_SHORT).show()
            R.id.btn_apt -> Toast.makeText(this, "apt_binding success", Toast.LENGTH_SHORT).show()
            else -> {
            }
        }
    }
}