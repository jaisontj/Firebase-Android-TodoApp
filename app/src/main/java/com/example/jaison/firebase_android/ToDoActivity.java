package com.example.jaison.firebase_android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoActivity extends BaseActivity {

    private static String FIREBASE_TABLENAME = "todo";
    private static String TAG = "ToDoActivity";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ToDoRecyclerViewAdapter adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference(FIREBASE_TABLENAME);

    public static void startActivity(Activity startingActivity) {
        startingActivity.startActivity(new Intent(startingActivity,ToDoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ToDoRecyclerViewAdapter(new ToDoRecyclerViewAdapter.InteractionListener() {
            @Override
            public void onToDoClicked(DataSnapshot snapshot, final int position) {
                final ToDo toDo = snapshot.getValue(ToDo.class);
                Log.d(TAG,"On Clicked - "+toDo.getDescription() + " - "+toDo.getIs_complete());
                toDo.toggleCompletion();
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("is_complete",toDo.getIs_complete());
                ref.child(snapshot.getKey()).updateChildren(updateData, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            adapter.updateData(position, toDo);
                            Log.d(TAG,"On Clicked - "+toDo.getDescription() + " - "+toDo.getIs_complete() + " - Successful");
                        } else {
                            showErrorAlert("Could not complete the ToDo. Please try again", null);
                        }
                    }
                });
            }

            @Override
            public void onToDoLongPressed(DataSnapshot snapshot, final int position) {
                ref.child(snapshot.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                        } else {
                            showErrorAlert("Could not delete. Please try again",null);
                        }
                    }
                });
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG,"OnDataChange");
                List<DataSnapshot> data = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ToDo toDo = snapshot.getValue(ToDo.class);
                    Log.d(TAG,"OnDataChange - "+toDo.getDescription() + " - "+toDo.getIs_complete());
                    data.add(snapshot);
                }
                adapter.setData(data);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                showErrorAlert(error.getMessage(),null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTodo:
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                alert.setMessage("Describe your task");
                alert.setTitle("Create new task");
                alert.setView(edittext);
                alert.setPositiveButton("Add Todo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String todoName = edittext.getText().toString();
                        ToDo toDo = new ToDo(todoName);
                        String key = ref.push().getKey();
                        ref.updateChildren(toDo.toFirebaseObject(key));
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
                return true;
            case R.id.signOut:
                AlertDialog.Builder signOutAlert = new AlertDialog.Builder(this);
                signOutAlert.setTitle("Sign Out");
                signOutAlert.setMessage("Are you sure you want to sign out?");
                signOutAlert.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                signOutAlert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        ToDoActivity.this.finish();
                    }
                });
                signOutAlert.show();
                return true;
        }
        return false;
    }
}
