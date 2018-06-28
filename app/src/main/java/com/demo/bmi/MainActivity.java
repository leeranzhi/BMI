package com.demo.bmi;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    float bmi = 0;
    String suggestion = null;
    EditText shengao;
    EditText tizhong;
    TextView BMI_show;
    private MyDatabase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shengao = (EditText) findViewById(R.id.edit_shengao);
        tizhong = (EditText) findViewById(R.id.edit_tizhong);
        BMI_show = (TextView) findViewById(R.id.BMI_show);
        Button send = (Button) findViewById(R.id.bt_send);
        Button save = (Button) findViewById(R.id.bt_save);
        Button view = (Button) findViewById(R.id.bt_view);
        send.setOnClickListener(this);
        save.setOnClickListener(this);
        view.setOnClickListener(this);
        //初始化
        dbHelper = new MyDatabase(this, "Bmi.db", null, 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_send: {
                //隐藏软键盘

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String str1 = shengao.getText().toString();
                String str2 = tizhong.getText().toString();
                if (str1.equals("") || str2.equals("")) {
                    Toast.makeText(MainActivity.this, "请输入完整后提交", Toast.LENGTH_SHORT).show();
                    return;
                }
                float shengaoNumber = Float.valueOf(str1);
                float tizhongNumber = Float.valueOf(str2);
                bmi = tizhongNumber / ((shengaoNumber * shengaoNumber) / (100 * 100));
                requestBMI(bmi);
                break;
            }
            //保存数据
            case R.id.bt_save: {
                Time t = new Time();
                t.setToNow(); // 取得系统时间。
                int year = t.year;
                int month = t.month + 1;
                int day = t.monthDay;
                int hour = t.hour; // 0-23
                int minute = t.minute;
                int second = t.second;
                String nowTime = year + "年" + month + "月" + day +
                        "日" + hour + ":" + minute + ":" + second;
                Log.d("MainActivity.this",
                        "当前时间" + t.year + "年" + t.month + "月" + t.monthDay + "日"
                                + t.hour + "时" + t.minute + "分");
                Toast.makeText(MainActivity.this, nowTime, Toast.LENGTH_SHORT).show();
                //创建数据库
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //添加数据
                ContentValues values = new ContentValues();
                //开始组装数据
                values.put("bmi", bmi);
                values.put("time", nowTime);
                db.insert("Bmi", null, values);
                Toast.makeText(MainActivity.this, "数据添加成功！！",
                        Toast.LENGTH_SHORT).show();
                break;
            }
            //查看历史趋势图
            case R.id.bt_view: {
                Intent intent=new Intent(MainActivity.this,ViewActivity.class);
                startActivity(intent);

                break;
            }

            default:
                break;
        }
    }

    private void requestBMI(float bmi) {
        if (bmi <= 18) {
            suggestion = "\tBMI值" + bmi + "\n\t偏瘦体质";
            BMI_show.setText(suggestion);
        }
        if (bmi >= 18.5 && bmi <= 23.9) {
            suggestion = "\tBMI值" + bmi + "\n\t正常体质";
            BMI_show.setText(suggestion);
        }
        if (bmi >= 24 && bmi <= 27.9) {
            suggestion = "\tBMI值" + bmi + "\n\t偏胖体质";
            BMI_show.setText(suggestion);
        }
        if (bmi >= 28) {
            suggestion = "\tBMI值" + bmi + "\n\t肥胖体质";
            BMI_show.setText(suggestion);
        }

    }
}
