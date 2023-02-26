package com.example.yoklamauygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String className;
    private String subjectName;
    private int position;

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems=new ArrayList();
    private DbHelper dbHelper;
    private long cID;

    private MyCalendar calendar;

    private TextView subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        calendar=new MyCalendar();
        dbHelper=new DbHelper(this);

        Intent intent=getIntent();
        className=intent.getStringExtra("className");
        subjectName=intent.getStringExtra("subjectName");
        position=intent.getIntExtra("position",-1);
        cID=intent.getLongExtra("cID",-1);

        setToolbar();
        loadData();

        recyclerView=findViewById(R.id.recyler_student);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> changeStatus(position));
        loadStatusData();
    }

    private void loadData() {
        Cursor cursor=dbHelper.getStudentTable(cID);
        studentItems.clear();

        while(cursor.moveToNext())
        {
            @SuppressLint("Range") long sID=cursor.getLong(cursor.getColumnIndex(DbHelper.S_ID));
            @SuppressLint("Range") int studentID=cursor.getInt(cursor.getColumnIndex(DbHelper.STUDENT_ID_KEY));
            @SuppressLint("Range") String studentName=cursor.getString(cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sID,studentID,studentName));
        }
        cursor.close();
    }

    private void changeStatus(int position){
        String status = studentItems.get(position).getStatus();

        if(status.equals("Gelmedi")) status = "Geldi";
        else status = "Gelmedi";

        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar(){
        toolbar=findViewById(R.id.toolBar);
        TextView title=toolbar.findViewById(R.id.tb_title);
        subtitle=toolbar.findViewById(R.id.tb_subtitle);
        ImageButton back=toolbar.findViewById(R.id.iconBack);
        ImageButton save=toolbar.findViewById(R.id.iconSave);

        save.setOnClickListener(v->saveStatus());


        title.setText(className);
        subtitle.setText(subjectName + " - " +calendar.getDate());

        back.setOnClickListener(v->onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem -> onMenuItemClick(menuItem));
    }

    private void saveStatus() {
        for (StudentItem studentItem : studentItems){
            String status=studentItem.getStatus();
            if(status!="Gelmedi") status = "Geldi";
            long value = dbHelper.addStatus(studentItem.getsID(),cID,calendar.getDate(),status);

            if(value==-1) dbHelper.updateStatus(studentItem.getsID(),calendar.getDate(),status);
        }
    }

    private void loadStatusData(){
        for (StudentItem studentItem : studentItems){
            String status=dbHelper.getStatus(studentItem.getsID(),calendar.getDate());
            if(status!=null)  studentItem.setStatus(status);
            else studentItem.setStatus("");


        }
        adapter.notifyDataSetChanged();
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.add_student){
            showAddStudentDialog();
        }
        else if(menuItem.getItemId()==R.id.show_calendar){
            showCalendar();
        }
        else if(menuItem.getItemId()==R.id.show_attendance_sheet){
            openSheetList();
        }
        return true;
    }

    private void openSheetList() {

        long[] idArray=new long[studentItems.size()];
        String[] studentNameArray=new String[studentItems.size()];
        int[] studentIDArray=new int[studentItems.size()];

        for(int i=0;i<idArray.length;i++)
            idArray[i]=studentItems.get(i).getsID();

        for (int i=0;i<studentIDArray.length;i++)
            studentIDArray[i]=studentItems.get(i).getStudentID();


        for (int i=0;i<studentNameArray.length;i++)
            studentNameArray[i]=studentItems.get(i).getStudentName();


        Intent intent=new Intent(this,SheetListActivity.class);
        intent.putExtra("cID",cID);
        intent.putExtra("idArray",idArray);
        intent.putExtra("studentIDArray",studentIDArray);
        intent.putExtra("studentNameArray",studentNameArray);
        startActivity(intent);
    }

    private void showCalendar() {

        calendar.show(getSupportFragmentManager(),"");
        calendar.setOnCalendarOnClickListener(this::OnCalendarOnClicked);
    }

    private void OnCalendarOnClicked(int year, int month, int day) {  // Toolbar üzerinden tarihi set etmek
        calendar.setDate(year,month,day);
        subtitle.setText(subjectName + " | " + calendar.getDate());
        loadStatusData();
    }

    private void showAddStudentDialog() {
        MyDialog myDialog=new MyDialog();
        myDialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        myDialog.setListener((studentID,studentName)->addStudent(studentID,studentName));
    }

    private void addStudent(String studentID_string, String studentName) {
        int studentID=Integer.parseInt(studentID_string);
        long sID= dbHelper.addStudent(cID,studentID,studentName);
        StudentItem studentItem=new StudentItem(sID,studentID,studentName);
        studentItems.add(studentItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) { // Güncelle veya silme butonu basıldığında yapılacak işlem
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());

        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog myDialog=new MyDialog(studentItems.get(position).getStudentID(),studentItems.get(position).getStudentName());
        myDialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        myDialog.setListener((studentID_string,studentName)->updateStudent(position,studentName));
    }

    private void updateStudent(int position, String studentName) {
        dbHelper.updateStudent(studentItems.get(position).getsID(),studentName);
        studentItems.get(position).setStudentName(studentName);
        adapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getStudentID());
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
    }
}