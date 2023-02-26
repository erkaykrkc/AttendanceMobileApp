package com.example.yoklamauygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addBtn;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItem> classItems = new ArrayList<>();
    Toolbar toolbar;
    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        addBtn=findViewById(R.id.btnAdd);

        addBtn.setOnClickListener(v-> showDialog()); // Main sayfasındaki add butona tıklandığı an

        loadData();

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        classAdapter=new ClassAdapter(this,classItems);
        recyclerView.setAdapter(classAdapter);

        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));

        setToolbar();
    }

    private void loadData() { // Veritabanındaki verileri okuma ve yükleme işlevini gerçekleştirir.
        Cursor cursor=dbHelper.getClassTable();

        classItems.clear();
        while(cursor.moveToNext())
        {
            @SuppressLint("Range") long cID=cursor.getLong(cursor.getColumnIndex(DbHelper.C_ID));
            @SuppressLint("Range") String className=cursor.getString(cursor.getColumnIndex(DbHelper.CLASS_NAME_KEY));
            @SuppressLint("Range") String subjectName=cursor.getString(cursor.getColumnIndex(DbHelper.SUBJECT_NAME_KEY));

            classItems.add(new ClassItem(cID,className,subjectName));
        }
    }

    private void setToolbar(){
        toolbar=findViewById(R.id.toolBar);
        TextView title=toolbar.findViewById(R.id.tb_title);
        TextView subtitle=toolbar.findViewById(R.id.tb_subtitle);
        ImageButton back=toolbar.findViewById(R.id.iconBack);
        ImageButton save=toolbar.findViewById(R.id.iconSave);
        title.setText("Attendance App");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivity(int position) {  //Eklenen sınıfa tıklandığı anda yapılacak işlemler
        Intent intent=new Intent(this,StudentActivity.class);

        intent.putExtra("className",classItems.get(position).getClassName()); //Main Acitvity'de oluşturduğumuz verileri studentActivity'e taşımak için putExtra metodunu kullanıyoruz.
        intent.putExtra("subjectName",classItems.get(position).getSubjectName());
        intent.putExtra("position",position);
        intent.putExtra("cID",classItems.get(position).getClassID());
        startActivity(intent);
    }

    private void showDialog(){

        MyDialog dialog=new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_DIALOG);
        dialog.setListener((className,subjectName)->addClass(className,subjectName)); // Onclick metodu ile yolladığımız classname ve subjectname verilerimizi addClass metoduna iletmiş olduk.

    }

    private void addClass(String className,String subjectName){
        long classID=dbHelper.addClass(className,subjectName);
        ClassItem classItem=new ClassItem(classID,className,subjectName);
        classItems.add(classItem);
        classAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) { // Sınıf'a basılı tuttuktan sonra güncelle veya silme işlemini yapılacağı kısım
        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog myDialog= new MyDialog();
        myDialog.show(getSupportFragmentManager(),MyDialog.CLASS_UPDATE_DIALOG);
        myDialog.setListener((className,subjectName)->updateClass(position,className,subjectName));
    }

    private void updateClass(int position, String className, String subjectName) {
        dbHelper.updateClass(classItems.get(position).getClassID(),className,subjectName);
        classItems.get(position).setClassName(className);
        classItems.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);
    }

    private void deleteClass(int position) {
        dbHelper.deleteClass(classItems.get(position).getClassID());
        classItems.remove(position);
        classAdapter.notifyItemRemoved(position);
    }


}