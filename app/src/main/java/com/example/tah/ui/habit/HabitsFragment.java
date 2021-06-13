package com.example.tah.ui.habit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tah.R;
import com.example.tah.models.Habit;
import com.example.tah.utilities.ViewInits;
import com.example.tah.viewModels.HabitViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HabitsFragment extends Fragment implements ViewInits {

    private HabitViewModel viewModel;
    private List<Habit> habits;
    private HabitRecyclerViewAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        habits = new ArrayList<>();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new HabitRecyclerViewAdapter(habits);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModelObservables();
    }

    @Override
    public void initOnClickListeners() {
    }

    @Override
    public void initViewModelObservables() {
        viewModel.itemsLD.observe(getViewLifecycleOwner(), items -> adapter.update(items));
    }
}