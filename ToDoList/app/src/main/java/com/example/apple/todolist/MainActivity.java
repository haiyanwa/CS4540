package com.example.apple.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.apple.todolist.data.Contract;
import com.example.apple.todolist.data.DBHelper;

public class MainActivity extends AppCompatActivity implements AddToDoFragment.OnDialogCloseListener, UpdateToDoFragment.OnUpdateDialogCloseListener, ToDoListAdapter.CheckboxClickListener{

    private RecyclerView rv;
    private FloatingActionButton button;
    private com.example.apple.todolist.data.DBHelper helper;
    private Cursor cursor;
    private SQLiteDatabase db;
    ToDoListAdapter adapter;
    private final String TAG = "mainactivity";
    private Toast mToast;
    private String defaultCategory = "Personal";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "oncreate called in main activity");
        button = (FloatingActionButton) findViewById(R.id.addToDo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AddToDoFragment frag = new AddToDoFragment();
                frag.show(fm, "addtodofragment");
            }
        });

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    //add menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //responding to menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if(menuItemThatWasSelected == R.id.categoryPersonal){
            Context context = MainActivity.this;
            cursor = getSelectedCategoryItems(db,"Personal");
            //check if any data returned to the cursor, if not show toast
            if(cursor.getCount() >= 1){
                //refresh with selected data
                refreshUI(cursor, this);
            }else{
                String message = "No todo item in this category";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
        else if(menuItemThatWasSelected == R.id.categoryWork){
            Context context = MainActivity.this;
            cursor = getSelectedCategoryItems(db,"Work");
            if(cursor.getCount() >= 1){
                refreshUI(cursor, this);
            }else{
                String message = "No todo item in this category";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }

        }
        else if(menuItemThatWasSelected == R.id.categorySchool){
            Context context = MainActivity.this;
            cursor = getSelectedCategoryItems(db,"School");
            if(cursor.getCount() >= 1){
                refreshUI(cursor, this);
            }else{
                String message = "No todo item in this category";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }

        }else if(menuItemThatWasSelected == R.id.categoryHobbie){
            Context context = MainActivity.this;
            cursor = getSelectedCategoryItems(db,"Hobbie");
            if(cursor.getCount() >= 1){
                refreshUI(cursor, this);
            }else{
                String message = "No todo item in this category";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }

        }
        return true;
    }

    private boolean refreshUI(Cursor cursor, ToDoListAdapter.CheckboxClickListener chkListerner){

        ToDoListAdapter mAdapter = new ToDoListAdapter(cursor, new ToDoListAdapter.ItemClickListener() {

            @Override
            public void onItemClick(int pos, String description, String duedate, long id, String category) {
                Log.d(TAG, "item click id: " + id);
                String[] dateInfo = duedate.split("-");
                int year = Integer.parseInt(dateInfo[0].replaceAll("\\s",""));
                int month = Integer.parseInt(dateInfo[1].replaceAll("\\s",""));
                int day = Integer.parseInt(dateInfo[2].replaceAll("\\s",""));

                FragmentManager fm = getSupportFragmentManager();

                UpdateToDoFragment frag = UpdateToDoFragment.newInstance(year, month, day, description, id, category);
                frag.show(fm, "updatetodofragment");
            }
        }, chkListerner);

        rv.setAdapter(mAdapter);
        return true;
    }

    //show only selected category items
    public void showSelectedItems(String categoryName){

    }

    //Show toast when checkbox for "done" is clicked
    public void markAsDone(View view){

        Toast.makeText(this,"this is done",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) db.close();
        if (cursor != null) cursor.close();
    }

    @Override
    protected void onStart() {
        super.onStart();

        helper = new DBHelper(this);
        db = helper.getWritableDatabase();
        cursor = getAllItems(db);

        adapter = new ToDoListAdapter(cursor, new ToDoListAdapter.ItemClickListener() {

            @Override
            public void onItemClick(int pos, String description, String duedate, long id, String category) {
                Log.d(TAG, "item click id: " + id);
                String[] dateInfo = duedate.split("-");
                int year = Integer.parseInt(dateInfo[0].replaceAll("\\s",""));
                int month = Integer.parseInt(dateInfo[1].replaceAll("\\s",""));
                int day = Integer.parseInt(dateInfo[2].replaceAll("\\s",""));

                FragmentManager fm = getSupportFragmentManager();

                UpdateToDoFragment frag = UpdateToDoFragment.newInstance(year, month, day, description, id, category);
                frag.show(fm, "updatetodofragment");
            }
        }, this);

        rv.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                long id = (long) viewHolder.itemView.getTag();
                Log.d(TAG, "passing id: " + id);
                removeToDo(db, id);
                adapter.swapCursor(getAllItems(db));
            }
        }).attachToRecyclerView(rv);
    }

    @Override
    public void onCheckboxClick(long id, boolean isChecked) {
        if(mToast != null){
            mToast.cancel();
        }
        //String toastMessage = "Item #" + id + "clicked. ";
        String toastMessage = "Marked as done";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();
        updateStatus(db,id,isChecked);
    }

    @Override
    public void closeDialog(int year, int month, int day, String description, String category) {
        Log.d(TAG, "closeDialog " + category);
        addToDo(db, description, formatDate(year, month, day), category);
        cursor = getAllItems(db);
        adapter.swapCursor(cursor);
    }

    public String formatDate(int year, int month, int day) {
        return String.format("%04d-%02d-%02d", year, month + 1, day);
    }

    private Cursor getAllItems(SQLiteDatabase db) {
        return db.query(
                Contract.TABLE_TODO.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE
        );
    }

    private Cursor getSelectedCategoryItems(SQLiteDatabase db, String categoryName) {
        String whereClause = "category = ?";
        String whereArgs[] = {categoryName};
        return db.query(
                Contract.TABLE_TODO.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE
        );
    }

    private long addToDo(SQLiteDatabase db, String description, String duedate, String category) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION, description);
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE, duedate);
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_DONE, 0);
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY, category);
        return db.insert(Contract.TABLE_TODO.TABLE_NAME, null, cv);
    }

    private boolean removeToDo(SQLiteDatabase db, long id) {
        Log.d(TAG, "deleting id: " + id);
        return db.delete(Contract.TABLE_TODO.TABLE_NAME, Contract.TABLE_TODO._ID + "=" + id, null) > 0;
    }


    private int updateToDo(SQLiteDatabase db, int year, int month, int day, String description, long id, String category){

        String duedate = formatDate(year, month - 1, day);

        ContentValues cv = new ContentValues();
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION, description);
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE, duedate);
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY, category);

        return db.update(Contract.TABLE_TODO.TABLE_NAME, cv, Contract.TABLE_TODO._ID + "=" + id, null);
    }

    private int updateStatus(SQLiteDatabase db, long id, boolean isChecked){
        ContentValues cv = new ContentValues();
        if(isChecked == false){
            cv.put(Contract.TABLE_TODO.COLUMN_NAME_DONE, "1");
        }else{
            cv.put(Contract.TABLE_TODO.COLUMN_NAME_DONE, "0");
        }

        return db.update(Contract.TABLE_TODO.TABLE_NAME, cv, Contract.TABLE_TODO._ID + "=" + id, null);
    }
    //add category
    @Override
    public void closeUpdateDialog(int year, int month, int day, String description, long id, String category) {
        updateToDo(db, year, month, day, description, id, category);
        adapter.swapCursor(getAllItems(db));
    }
}
