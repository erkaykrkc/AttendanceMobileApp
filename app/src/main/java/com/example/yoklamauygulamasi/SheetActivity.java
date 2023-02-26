package com.example.yoklamauygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);

        showTable();
    }

    private void showTable() {
        DbHelper dbHelper=new DbHelper(this);

        TableLayout tableLayout=findViewById(R.id.tableLayout);


        long[] idArray=getIntent().getLongArrayExtra("idArray");
        int[] studentIDArray=getIntent().getIntArrayExtra("studentIDArray");
        String[] studentNameArray=getIntent().getStringArrayExtra("studentNameArray");
        String month=getIntent().getStringExtra("month");

        // Gelen ay bilgisinin içerisinde yer alan gün sayısını atadık.
        int DAY_IN_MONTH = getDayInMonth(month);

        //ROW SETUP
        int rowSize=idArray.length+1;
        TableRow[] rows=new TableRow[rowSize];

        TextView[] studentID_tvs=new TextView[rowSize];
        TextView[] studentName_tvs=new TextView[rowSize];
        TextView[][] status_tvs=new TextView[rowSize][DAY_IN_MONTH + 1];

        for(int i=0;i<rowSize;i++)
        {
            studentID_tvs[i]=new TextView(this);
            studentName_tvs[i]=new TextView(this);
            for(int j=1;j<=DAY_IN_MONTH;j++)
            {
                status_tvs[i][j]=new TextView(this);
            }
        }

        // HEADER
        studentID_tvs[0].setText("Öğrenci Numarası");
        studentID_tvs[0].setTypeface(studentID_tvs[0].getTypeface(), Typeface.BOLD);
        studentName_tvs[0].setText("Öğrenci Adı ve Soyadı");
        studentName_tvs[0].setTypeface(studentName_tvs[0].getTypeface(), Typeface.BOLD);
        for(int i=1;i<=DAY_IN_MONTH;i++)
        {
            status_tvs[0][i].setText(String.valueOf(i));
            status_tvs[0][i].setTypeface(status_tvs[0][i].getTypeface(), Typeface.BOLD);
        }

        // Bilgilerin textviewlara yerleştirildiği kısım burası.
        for (int i=1;i<rowSize;i++){
            studentID_tvs[i].setText(String.valueOf(studentIDArray[i-1]));
            studentName_tvs[i].setText(studentNameArray[i-1]);

            for(int j=1;j<=DAY_IN_MONTH;j++)
            {
                //01.09.2023
                String day=String.valueOf(j);
                if(day.length()==1) day="0"+day;
                String date=day+"."+month;
                String status=dbHelper.getStatus(idArray[i-1],date); //01.09.2023
                status_tvs[i][j].setText(status);
            }
        }

        for(int i=0;i<rowSize;i++) {
            rows[i]=new TableRow(this);
        }

        for(int i=0;i<rowSize;i++)
        {
            studentID_tvs[i].setPadding(16,16,16,16);
            studentName_tvs[i].setPadding(16,16,16,16);

            rows[i].addView(studentID_tvs[i]);
            rows[i].addView(studentName_tvs[i]);

            int counter=0;
            for(int j=1;j<=DAY_IN_MONTH;j++)
            {
                status_tvs[i][j].setPadding(90,90,90,90);

                rows[i].addView(status_tvs[i][j]);

                if(i+1<rowSize) {
                    TextView txtView = status_tvs[i + 1][j];
                    String str = txtView.getText().toString();

                    if (str.equals("Gelmedi")) {
                        counter = counter + 1;
                    }
                }
            }
            tableLayout.addView(rows[i]);

            if (counter>=4 ){
                rows[i+1].setBackgroundColor(Color.parseColor("#FF5252"));
            }
        }

        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);
    }

    private int getDayInMonth(String month) {
        int monthIndex = Integer.parseInt(month.substring(0,2));
        //System.out.println(monthIndex);
        int year = Integer.parseInt(month.substring(4));
        //System.out.println(year);
        //System.out.println("Bu sayfaya gelince");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthIndex);
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}