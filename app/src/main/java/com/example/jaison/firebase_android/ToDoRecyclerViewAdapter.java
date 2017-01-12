package com.example.jaison.firebase_android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jaison on 11/01/17.
 */

public class ToDoRecyclerViewAdapter extends RecyclerView.Adapter<ToDoViewHolder> {

    public interface InteractionListener {
        void onToDoClicked(DataSnapshot snapshot, int position);
        void onToDoLongPressed(DataSnapshot snapshot, int position);
    }

    List<DataSnapshot> data = new ArrayList<>();
    InteractionListener listener;

    public ToDoRecyclerViewAdapter(InteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_todo,parent,false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToDoViewHolder holder, final int position) {
        ToDo todo = data.get(position).getValue(ToDo.class);
        holder.description.setText(todo.getDescription());
        holder.checkbox.setChecked(todo.getIs_complete());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onToDoClicked(data.get(position), position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onToDoLongPressed(data.get(position), position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(int position, ToDo todo) {
        DataSnapshot snapshot = data.get(position);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
//        this.data.remove(position);
        notifyItemRemoved(position);
    }

    public void setData(List<DataSnapshot> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(DataSnapshot toDo) {
        this.data.add(toDo);
        notifyItemInserted(this.data.size());
    }


}
