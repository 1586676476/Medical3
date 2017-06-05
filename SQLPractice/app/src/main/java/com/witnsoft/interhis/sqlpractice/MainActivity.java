package com.witnsoft.interhis.sqlpractice;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button button;
    private ArrayList<CityModel> list;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button= (Button) findViewById(R.id.btn);
        DBManager dbManager=new DBManager(this);
        dbManager.openDateBase();
        dbManager.closeDatabase();
        database=SQLiteDatabase.openOrCreateDatabase(DBManager.)
    }

    private ArrayList<CityModel> getCityNames(){
        ArrayList<CityModel> name=new ArrayList<CityModel>();
        Cursor cursor=database.rawQuery("SELECE*FROM T_city ORDER BY CityName",null);
        for (int i = 0; i < cursor.getCount(); i++) {
            CityModel cityModel=new CityModel();
            cityModel.setCityName(cursor.getString(cursor.getColumnIndex("AllNameSort")));
            cityModel.setNameSort(cursor.getString(cursor.getColumnIndex("CityName")));
            name.add(cityModel);
        }
        cursor.close();
        Log.e(TAG, "getCityNames: "+name );
        return name;
    }
}
