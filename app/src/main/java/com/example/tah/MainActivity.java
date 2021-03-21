package com.example.tah;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tah.models.Task;
import com.example.tah.models.TaskViewModel;
import com.example.tah.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    
    private int page;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        FloatingActionButton fab = findViewById(R.id.fab_add);
        ImageView deleteIcon = findViewById(R.id.delete_icon);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(getResources().getString(sectionsPagerAdapter.getTabTitleId(position)));
        }).attach();

        // temp
        fab.setOnClickListener(v -> {
            // TODO new item activity/fragment, Task or Habit
            taskViewModel.insert(new Task("DUMMY", "Desc", false));
        });

        taskViewModel.getCheckedTasksLD().observe(this, checkedTasks -> {
            if(checkedTasks.isEmpty()){
                deleteIcon.setVisibility(View.GONE);
            }
            else{
                deleteIcon.setVisibility(View.VISIBLE);
            }
        });


    }
}