package com.example.taskmanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.taskmanagement.R;
import com.example.taskmanagement.model.Task;

public class TaskStaffAdapter extends RecyclerView.Adapter<TaskStaffAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnLongClickListener{

        public TextView taskID;
        public TextView taskTitle;
        public TextView taskStatus;
        public TextView taskStaff;
        public TextView taskdue;


        public ViewHolder(View itemView) {
            super(itemView);

            taskID = (TextView) itemView.findViewById(R.id.IDtask);
            taskTitle = (TextView) itemView.findViewById(R.id.titleTask);
            taskStatus = (TextView) itemView.findViewById(R.id.statusTask);
            taskStaff = (TextView) itemView.findViewById(R.id.staffTask);
            taskdue = (TextView) itemView.findViewById(R.id.DueTask);



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

    public TaskStaffAdapter(Context context, List<Task> listData){
        mListData = listData;
        mContext = context;
    }

    private Context getmContext(){return mContext;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the single item layout
        View view = inflater.inflate(R.layout.activity_view_task, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // bind data to the view holder
        Task m = mListData.get(position);
        holder.taskID.setText(String.valueOf(m.getTaskID()));
        holder.taskTitle.setText(m.getTaskName());
        holder.taskStatus.setText(m.getStatus());
        holder.taskStaff.setText(m.getStaffName());
        holder.taskdue.setText((m.getTaskDue()));

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
