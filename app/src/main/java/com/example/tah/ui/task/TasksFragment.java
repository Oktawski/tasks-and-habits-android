package com.example.tah.ui.task;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tah.R;
import com.example.tah.models.Task;
import com.example.tah.utilities.State;
import com.example.tah.viewModels.TaskViewModel;

import java.util.ArrayList;
import java.util.List;


public class TasksFragment extends Fragment {

    private TaskViewModel viewModel;
    private List<Task> tasks;
    private TaskRecyclerViewAdapter adapter;
    private ImageView deleteIcon;



    public static TasksFragment newInstance(int columnCount) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        tasks = new ArrayList<>();

        deleteIcon = view.findViewById(R.id.delete_icon);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new TaskRecyclerViewAdapter(tasks, requireActivity(), viewModel);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getItemsLD().observe(getViewLifecycleOwner(), taskList ->
                adapter.update(taskList));

        viewModel.getCheckBoxVisibility().observe(getViewLifecycleOwner(), integer -> {
            adapter.notifyDataSetChanged();
        });

        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if(state.getStatus().equals(State.Status.REMOVED)){
                Toast.makeText(requireActivity(), state.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.clearCheckedItems();
        adapter.notifyDataSetChanged();
    }
}