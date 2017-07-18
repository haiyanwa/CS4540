package com.example.apple.todolist;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.apple.todolist.data.Contract;

/**
 * Created by mark on 7/4/17.
 */

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ItemHolder> {

    private Cursor cursor;
    private ItemClickListener listener;
    private CheckboxClickListener checkboxListener;
    private String TAG = "todolistadapter";

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public interface ItemClickListener {
        //add category to ItemClickListener interface
        void onItemClick(int pos, String description, String duedate, long id, String category);
    }
    public interface CheckboxClickListener {
        void onCheckboxClick(long id, boolean isChecked);
    }


    public ToDoListAdapter(Cursor cursor, ItemClickListener listener, CheckboxClickListener checkboxListerner) {
        this.cursor = cursor;
        this.listener = listener;
        this.checkboxListener = checkboxListerner;
    }

    public void swapCursor(Cursor newCursor){
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView descr;
        TextView due;
        String duedate;
        String description;
        TextView category;
        String categoryName;
        CheckBox statusCheckbox;
        long id;
        boolean isChecked;

        ItemHolder(View view) {
            super(view);
            descr = (TextView) view.findViewById(R.id.description);
            due = (TextView) view.findViewById(R.id.dueDate);
            category = (TextView) view.findViewById(R.id.category);
            statusCheckbox = (CheckBox) view.findViewById(R.id.mark_as_done);
            view.setOnClickListener(this);
        }

        public void bind(final ItemHolder holder, int pos) {

            cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_TODO._ID));
            Log.d(TAG, "deleting id: " + id);

            duedate = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE));
            description = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION));
            //get category name from the cursor
            categoryName = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY));
            Log.d(TAG, "select : " + description + " date " + duedate + " category " + categoryName);

            descr.setText(description);
            due.setText(duedate);
            //show category in the view
            category.setText(categoryName);

            //check if this item is "done", 0: false 1: true, if it's true then check the checkbox
            if(cursor.getInt(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DONE)) == 0){
                //not done yet
                statusCheckbox.setChecked(false);
                isChecked = false;
            }else{
                //done, check the checkbox
                statusCheckbox.setChecked(true);
                isChecked = true;
            }
            holder.statusCheckbox.setText("Done");
            holder.statusCheckbox.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    checkboxListener.onCheckboxClick(id, isChecked);
                }
            });
            holder.itemView.setTag(id);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            //add category name to click listener
            listener.onItemClick(pos, description, duedate, id, categoryName);
        }
    }

}
