package com.example.tah.ui.task;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tah.R;
import com.example.tah.models.Task;
import com.example.tah.models.TaskViewModel;
import com.example.tah.ui.main.AddAndDetailsActivity;

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
        final TextView descriptionTV;
        final CheckBox checkBox;
        private final TaskViewModel viewModel;

        Task task;

        public ViewHolder(View view, TaskViewModel viewModel) {
            super(view);
            this.viewModel = viewModel;
            nameTV = (TextView) view.findViewById(R.id.item_name);
            descriptionTV  = (TextView) view.findViewById(R.id.description);
            checkBox = (CheckBox) view.findViewById(R.id.check_box);
        }

        public void bind(int position) {
            task = tasks.get(position);
            nameTV.setText(task.getName());
            checkBox.setChecked(false);

            if(task.getDescription().isEmpty()){
                descriptionTV.setVisibility(View.GONE);
            }else{
                descriptionTV.setVisibility(View.VISIBLE);
                descriptionTV.setText(task.getDescription());
            }

            setOnClickListeners();

            checkBox.setVisibility(viewModel.getCheckBoxVisibility().getValue());
        }

        private void setOnClickListeners() {
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddAndDetailsActivity.class);
                intent.putExtra("fragmentId", Task.Companion.getDetailsView());
                intent.putExtra("taskId", task.getId());
                intent.putExtra("name", task.getName());
                intent.putExtra("description", task.getDescription());
                context.startActivity(intent);
            });

            itemView.setOnLongClickListener(v -> {
                int visibility = checkBox.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                viewModel.setCheckBoxVisibility(visibility);

                if(visibility == View.GONE){
                    checkedTasks.clear();
                    viewModel.addToCheckedItems(checkedTasks);
                }

                return true;
            });

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