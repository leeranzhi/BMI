package com.demo.bmi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sctdroid.app.uikit.CurveView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewActivity extends AppCompatActivity {
    List<BMI> dataList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        CurveView curveView = (CurveView) findViewById(R.id.curve_view);
        //初始化本地数据
        initFromLocal();
        //开始绘制折线图
        curveView.setAdapter(new CurveView.Adapter() {
            String text = "吾生有涯，而知也无涯";

            /**
             * @return 点的数量
             */
            @Override
            public int getCount() {
                return 7;
            }

            /**
             * level 是 y 轴高度，在 minLevel 和 maxLevel 之间
             * @param position
             * @return 返回当前 position 的 level
             */
            @Override
            public int getLevel(int position) {
                Log.d("ViewActivtiy","查看当前"+position);
                return (int)(dataList.get(position).getBmi());
            }

            /**
             * @return y 轴下限
             */
            @Override
            public int getMinLevel() {
                return 15;
            }

            /**
             * @return y 轴上限
             */
            @Override
            public int getMaxLevel() {
                return 29;
            }
            /**
             * 设置点上的文字，每个mark是一个，可同时设置点的 8 个方向的文字
             * 注意: Gravity 应使用 CurveView.Gravity 类
             *
             * @param position
             * @return
             */
            @Override
            public Set<CurveView.Mark> onCreateMarks(int position) {
                Set<CurveView.Mark> marks = new HashSet<CurveView.Mark>();
                CurveView.Mark mark = new CurveView.Mark(getLevel(position) + "°", CurveView.Gravity.BOTTOM | CurveView.Gravity.CENTER_HORIZONTAL, 0, 20, 0, 0);
                CurveView.Mark mark1 = new CurveView.Mark(getLevel(position) + "°", CurveView.Gravity.START | CurveView.Gravity.CENTER_HORIZONTAL, 0, 0, 0, 20);
//                marks.add(mark);
                marks.add(mark1);
                return marks;
            }

            @Override
            public String getXAxisText(int i) {
                return text.substring(i, i + 1);
            }
        });
    }

    private void initFromLocal() {
        MyDatabase dbHelper = new MyDatabase(this, "Bmi.db", null, 1);
        //从数据库中取数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Bmi", null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据
                float bmi = cursor.getFloat(cursor.getColumnIndex("bmi"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                BMI bmi1 = new BMI();
                bmi1.setBmi(bmi);
                bmi1.setTime(time);
                dataList.add(bmi1);
            } while (cursor.moveToNext());
        }
        //关闭cursor对象
        cursor.close();
    }
}
