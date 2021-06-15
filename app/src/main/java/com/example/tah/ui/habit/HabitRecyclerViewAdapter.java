package com.example.tah.ui.habit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tah.R;
import com.example.tah.models.Habit;
import com.example.tah.ui.main.AddAndDetailsActivity;
import com.example.tah.utilities.ViewHabitTime;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HabitRecyclerViewAdapter
        extends RecyclerView.Adapter<HabitRecyclerViewAdapter.ViewHolder>
        implements ViewHabitTime{

    private final List<Habit> habits;
    private final Context context;

    public HabitRecyclerViewAdapter(Context context, List<Habit> items) {
        this.context = context;
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

            Map<String, String> timeMap = getTimeStrings(habit.getSessionLength());

            String timeText = String.format(Locale.ENGLISH,
                    "Time left: %s:%s:%s",
                    timeMap.get("Hours"),
                    timeMap.get("Minutes"),
                    timeMap.get("Seconds"));

            name.setText(habit.getName());
            description.setText(habit.getDescription());
            time.setText(timeText);

            setOnClickListeners();
        }

        private void setOnClickListeners(){
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddAndDetailsActivity.class);
                intent.putExtra("fragmentId", Habit.Companion.getDetailsView());
                intent.putExtra("habitId", habit.getId());
                context.startActivity(intent);
            });
        }
    }
}