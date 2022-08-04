package com.example.taskmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.taskmanagement.R;
import com.example.taskmanagement.model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnLongClickListener{

        public TextView taskName;
        public TextView taskStaff;
        public TextView taskStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            taskName = (TextView) itemView.findViewById(R.id.taskName);
            taskStaff = (TextView) itemView.findViewById(R.id.taskStaff);
            taskStatus = (TextView) itemView.findViewById(R.id.taskStatus);

            itemView.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View view) {

            currentPos = getAdapterPosition();

            return false;
        }

    }

    private List<Task> mListData;   // list of task objects
    private Context mContext;       // activity context
    private int currentPos;         //current selected position.

    public TaskAdapter(Context context, List<Task> listData){
        mListData = listData;
        mContext = context;
    }

    private Context getmContext(){return mContext;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the single item layout
        View view = inflater.inflate(R.layout.task_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // bind data to the view holder
        Task m = mListData.get(position);
        holder.taskName.setText(m.getTaskName());
        holder.taskStaff.setText(m.getStaffName());
        holder.taskStatus.setText(m.getStatus());
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public Task getSelectedItem() {
        if(currentPos>=0 && mListData!=null && currentPos<mListData.size()) {
            return mListData.get(currentPos);
        }
        return null;
    }


}
