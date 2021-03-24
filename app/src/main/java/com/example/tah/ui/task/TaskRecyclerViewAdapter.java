package com.example.tah.ui.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tah.R;
import com.example.tah.models.Task;
import com.example.tah.models.TaskViewModel;

import java.util.ArrayList;
import java.util.List;


public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private final List<Task> tasks;
    private final List<Integer> checkedTasks;
    private final Context context;
    private final TaskViewModel viewModel;

    public TaskRecyclerViewAdapter(List<Task> items, Context context, TaskViewModel viewModel) {
        tasks = items;
        checkedTasks = new ArrayList<>();
        this.context = context;
        this.viewModel = viewModel;
    }

    public void update(List<Task> tasks){
        this.tasks.clear();
        this.tasks.addAll(tasks);
        this.notifyDataSetChanged();
    }

    public List<Integer> getCheckedTasks(){
        return checkedTasks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view, viewModel);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameTV;
        final CheckBox checkBox;
        private final TaskViewModel viewModel;

        Task task;

        public ViewHolder(View view, TaskViewModel viewModel) {
            super(view);
            this.viewModel = viewModel;
            nameTV = (TextView) view.findViewById(R.id.item_name);
            checkBox = (CheckBox) view.findViewById(R.id.item_check_box);
        }

        public void bind(int position) {
            task = tasks.get(position);
            nameTV.setText(task.getName());

            setOnClickListeners();
        }

        private void setOnClickListeners() {
            /* TODO item onClick -> details
                    item onLongClick -> context menu with delete/edit options
             */

            checkBox.setOnClickListener(v -> {
                if(checkBox.isChecked()){
                    checkedTasks.add(task.getId());
                }
                else{
                    checkedTasks.remove(task.getId());
                }
                viewModel.addToCheckedItems(checkedTasks);
            });
        }
    }
}