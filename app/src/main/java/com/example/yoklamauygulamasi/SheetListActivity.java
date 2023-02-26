package com.example.yoklamauygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {
    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList<String> listItems=new ArrayList();
    private long cID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        cID=getIntent().getLongExtra("cID",-1);

        loadListItems();
        sheetList=findViewById(R.id.sheetList);
        adapter=new ArrayAdapter(this,R.layout.sheet_list,R.id.date_list_item,listItems);
        sheetList.setAdapter(adapter);

        sheetList.setOnItemClickListener((parent,view,position,id)->openSheetActivity(position));
    }

    private void openSheetActivity(int position) {
        long[] idArray=getIntent().getLongArrayExtra("idArray");
        int[] studentIDArray=getIntent().getIntArrayExtra("studentIDArray");
        String[] studentNameArray=getIntent().getStringArrayExtra("studentNameArray");
        Intent intent = new Intent(this,SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("studentIDArray",studentIDArray);
        intent.putExtra("studentNameArray",studentNameArray);
        intent.putExtra("month",listItems.get(position));
        startActivity(intent);
    }

    private void loadListItems() {
        Cursor cursor= new DbHelper(this).getDistinctMonths(cID);

        while(cursor.moveToNext())
        {
            @SuppressLint("Range") String date=cursor.getString(cursor.getColumnIndex(DbHelper.DATE_KEY));
            listItems.add(date.substring(3)); //09.02.23
        }
    }
}