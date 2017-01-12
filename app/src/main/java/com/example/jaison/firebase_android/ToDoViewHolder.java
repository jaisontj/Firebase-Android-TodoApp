package com.example.jaison.firebase_android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jaison on 11/01/17.
 */

public class ToDoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.description)
    TextView description;

    public ToDoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
