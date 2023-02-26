package com.example.yoklamauygulamasi;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{


    ArrayList<StudentItem> studentsItems;
    Context context;

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener=onItemClickListener;
    }
    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems) {
        this.studentsItems = studentItems;
        this.context=context;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder  implements View.OnCreateContextMenuListener{  //Inner class tanımlama
        TextView studentID;
        TextView studentName;
        TextView status;

        CardView cardView;

        public StudentViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            studentID=itemView.findViewById(R.id.tv_studentID);
            studentName=itemView.findViewById(R.id.tv_studentName);
            status=itemView.findViewById(R.id.status);
            cardView=itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"Güncelle");
            menu.add(getAdapterPosition(),1,0,"Sil");
        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return new StudentViewHolder(itemView,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.studentID.setText(studentsItems.get(position).getStudentID()+"");
        holder.studentName.setText(studentsItems.get(position).getStudentName());
        holder.status.setText(studentsItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    private int getColor(int position){
        String status=studentsItems.get(position).getStatus();
        if (status.equals("Gelmedi"))
        {
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context,R.color.noCome)));
        }
        else if(status.equals("Geldi"))
        {
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context,R.color.come)));
        }

        return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context,R.color.white)));
    }

    @Override
    public int getItemCount() {
        return studentsItems.size();
    }
}
