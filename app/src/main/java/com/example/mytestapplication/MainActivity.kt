package com.example.mytestapplication

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var todayTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ddddd.setOnClickListener{
            isTodayFirstLogin()
        }
        transparentStatusBar(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        saveExitTime(todayTime)
    }

    private fun isTodayFirstLogin() { //取
        val preferences =
            getSharedPreferences("LastLoginTime", Context.MODE_PRIVATE)
        val lastTime = preferences.getString("LoginTime", "2017-04-08")
        val df = SimpleDateFormat("yyyy-MM-dd") // 设置日期格式
        todayTime = df.format(Date()) // 获取当前的日期
        if (lastTime == todayTime) { //如果两个时间段相等
            Toast.makeText(this, "不是当日首次登陆", Toast.LENGTH_SHORT).show()
            Log.e("Time", lastTime)
        } else {
            Toast.makeText(this, "当日首次登陆送积分", Toast.LENGTH_SHORT).show()
            Log.e("date", lastTime)
            Log.e("todayDate", todayTime)
        }
    }

    private fun saveExitTime(extiLoginTime: String) {
        val editor =
            getSharedPreferences("LastLoginTime", Context.MODE_PRIVATE).edit()
        editor.putString("LoginTime", extiLoginTime)
        editor.apply()
    }

    private fun transparentStatusBar(activity: Activity) {
        val window: Window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //设置虚拟按键背景透明，同时该属性会实现沉浸式状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

}
