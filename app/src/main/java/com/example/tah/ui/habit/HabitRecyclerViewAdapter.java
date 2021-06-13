package com.example.tah.ui.habit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tah.R;
import com.example.tah.models.Habit;

import java.util.List;

public class HabitRecyclerViewAdapter extends RecyclerView.Adapter<HabitRecyclerViewAdapter.ViewHolder> {

    private final List<Habit> habits;

    public HabitRecyclerViewAdapter(List<Habit> items) {
        habits = items;
    }

    public void update(List<Habit> habits){
        this.habits.clear();
        this.habits.addAll(habits);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(habits.get(position));
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView name;
        final TextView description;
        final TextView time;


        public Habit habit;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.item_name);
            description = view.findViewById(R.id.description);
            time = view.findViewById(R.id.time);
        }

        private void bind(Habit habit){
            this.habit = habit;

            name.setText(habit.getName());
            description.setText(habit.getDescription());
            time.setText(String.valueOf(habit.getSessionLength()));
        }
    }
}