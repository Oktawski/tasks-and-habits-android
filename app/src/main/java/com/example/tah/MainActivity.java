package com.example.tah;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tah.models.Task;
import com.example.tah.models.TaskViewModel;
import com.example.tah.ui.main.AddAndDetailsActivity;
import com.example.tah.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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

        intent = new Intent(this, AddAndDetailsActivity.class);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                int layoutRes;
                switch(position){
                    case 1:
                        //layoutRes = Habit.Companion.getAddView();
                        //break;
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
    }

    @Override
    public void initViewModelObservables(){
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskViewModel.getCheckedItemsLD().observe(this, checkedTasks -> {
            if(checkedTasks.isEmpty()){
                deleteIcon.setVisibility(View.GONE);
            }
            else{
                deleteIcon.setVisibility(View.VISIBLE);
            }
        });
    }
}