package com.example.yoklamauygulamasi;

import  android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {

    public static final String CLASS_ADD_DIALOG="addClass";
    public static final String CLASS_UPDATE_DIALOG="updateClass";

    public static final String STUDENT_ADD_DIALOG="addStudent";
    public static final String STUDENT_UPDATE_DIALOG = "updateStudent";


    OnClickListener listener;

    public interface OnClickListener{
        void onClick(String text1, String text2);
    }
    public void setListener(OnClickListener listener)
    {
        this.listener=listener;
    }
    private int studentID;
    private String studentName;
    public MyDialog(int studentID, String studentName) {
        this.studentID=studentID;
        this.studentName=studentName;
    }

    public MyDialog() {

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog=null;

        if(getTag().equals(CLASS_ADD_DIALOG)) dialog=getAddClassDialog();
        if(getTag().equals(STUDENT_ADD_DIALOG)) dialog=getAddStudentDialog();
        if(getTag().equals(CLASS_UPDATE_DIALOG)) dialog=getUpdateClassDialog();
        if(getTag().equals(STUDENT_UPDATE_DIALOG)) dialog=getUpdateStudentDialog();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // dialog penceresini transparent yapma

        return dialog;
    }

    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()); // Ekrana gelen pop-up penceresi
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView title=view.findViewById(R.id.titleDialog);
        title.setText("Öğrenci güncelle");

        EditText edtStudentID=view.findViewById(R.id.edt01);
        EditText edtStudentName=view.findViewById(R.id.edt02);

        edtStudentID.setHint("Öğrenci numarasını giriniz.");
        edtStudentName.setHint("Öğrenci Adı ve Soyadını giriniz.");
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnAdd=view.findViewById(R.id.btn_add);
        btnAdd.setText("Güncelle");
        edtStudentID.setText(studentID+"");
        edtStudentID.setEnabled(false);
        edtStudentName.setText(studentName);
        btnCancel.setOnClickListener(v-> alertDialog.dismiss());
        btnAdd.setOnClickListener(v-> {
            String className=edtStudentID.getText().toString();
            String subjectName = edtStudentName.getText().toString();
            listener.onClick(className,subjectName);
            alertDialog.dismiss();
        });
        return alertDialog;
    }

    private Dialog getUpdateClassDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()); // Ekrana pop-up penceresi
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView title=view.findViewById(R.id.titleDialog);
        title.setText("Bu sınıfı güncelle");

        EditText edtClass=view.findViewById(R.id.edt01);
        EditText edtSubject=view.findViewById(R.id.edt02);

        edtClass.setHint("Sınıf adını giriniz.");
        edtSubject.setHint("Ders adını giriniz.");
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnAdd=view.findViewById(R.id.btn_add);
        btnAdd.setText("Güncelle");


        btnCancel.setOnClickListener(v-> alertDialog.dismiss());
        btnAdd.setOnClickListener(v-> {
            String className=edtClass.getText().toString();
            String subjectName = edtSubject.getText().toString();
            listener.onClick(className,subjectName);
            alertDialog.dismiss();
        });
        return alertDialog;
    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()); // Ekrana pop-up penceresini
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView title=view.findViewById(R.id.titleDialog);
        title.setText("Yeni bir sınıf ekle");

        EditText edtClass=view.findViewById(R.id.edt01);
        EditText edtSubject=view.findViewById(R.id.edt02);

        edtClass.setHint("Sınıf adını giriniz.");
        edtSubject.setHint("Ders adını giriniz.");
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnAdd=view.findViewById(R.id.btn_add);

        btnCancel.setOnClickListener(v-> alertDialog.dismiss());
        btnAdd.setOnClickListener(v-> {
            String className=edtClass.getText().toString();
            String subjectName = edtSubject.getText().toString();
            listener.onClick(className,subjectName); // OnClickListener arayüzündeki onClick() metodu çağrılır ve className ve subjectName değişkenleri bu metoda iletilir.
            alertDialog.dismiss();
        });
        return alertDialog;
    }
    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()); // Ekrana pop-up penceresi
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView title=view.findViewById(R.id.titleDialog);
        title.setText("Yeni bir öğrenci ekle");

        EditText edtstudentID=view.findViewById(R.id.edt01);
        EditText edtstudentName=view.findViewById(R.id.edt02);
        edtstudentID.setHint("Öğrenci Numarasını giriniz.");
        edtstudentName.setHint("Öğrenci Adını ve Soyadını giriniz.");

        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnAdd=view.findViewById(R.id.btn_add);

        btnCancel.setOnClickListener(v-> alertDialog.dismiss());
        btnAdd.setOnClickListener(v-> {
            String studentID=edtstudentID.getText().toString();
            String studentName = edtstudentName.getText().toString();
            listener.onClick(studentID,studentName);
            alertDialog.dismiss();
        });
        return alertDialog;
    }

}
