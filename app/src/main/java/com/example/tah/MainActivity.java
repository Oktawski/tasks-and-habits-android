package com.example.tah;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tah.models.Task;
import com.example.tah.utilities.ViewInits;
import com.example.tah.viewModels.TaskViewModel;
import com.example.tah.ui.main.AddAndDetailsActivity;
import com.example.tah.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements ViewInits {
    
    private TaskViewModel taskViewModel;
    private ImageView deleteIcon;
    private FloatingActionButton fabAdd;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        fabAdd = findViewById(R.id.fab_add);
        deleteIcon = findViewById(R.id.delete_icon);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        intent = new Intent(this, AddAndDetailsActivity.class);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                intent.putExtra("fragmentId", sectionsPagerAdapter.getTabTitleId(position));
                int layoutRes;
                switch(position){
                    case 1:
                        //layoutRes = Habit.Companion.getAddView();
                        //break;
                        layoutRes = 42;
                        break;
                    default:
                        layoutRes = Task.Companion.getAddView();
                        break;
                }
                intent.putExtra("fragmentId", layoutRes);
            }
        });

        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(getResources().getString(sectionsPagerAdapter.getTabTitleId(position)));
        }).attach();

        initOnClickListeners();
        initViewModelObservables();
    }

    @Override
    public void initOnClickListeners(){
        // temp
        fabAdd.setOnClickListener(v -> {
            // TODO new item activity/fragment, Task or Habit
            startActivity(intent);

        });

        deleteIcon.setOnClickListener(v -> {
            taskViewModel.deleteSelected();
        });
    }

    @Override
    public void initViewModelObservables(){
        taskViewModel.getCheckedItemsLD().observe(this, checkedTasks -> {
            if(checkedTasks.isEmpty()){
                deleteIcon.setVisibility(View.GONE);
            }
            else{
                deleteIcon.setVisibility(View.VISIBLE);
            }
        });

        taskViewModel.state.observe(this, state -> {
            switch(state.getStatus()){
                case REMOVED:
                    taskViewModel.setCheckBoxVisibility(View.GONE);
                    taskViewModel.getCheckedItemsLD().setValue(Collections.emptyList());
                    break;
            }
        });
    }
}