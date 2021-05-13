package com.example.tah;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tah.models.Habit;
import com.example.tah.models.Task;
import com.example.tah.ui.main.AddAndDetailsActivity;
import com.example.tah.ui.main.SectionsPagerAdapter;
import com.example.tah.utilities.ViewInits;
import com.example.tah.viewModels.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements ViewInits {
    
    private TaskViewModel taskViewModel;
    private ImageView deleteIcon;
    private FloatingActionButton fabAdd;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabs;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        intent = new Intent(this, AddAndDetailsActivity.class);

        viewPager.registerOnPageChangeCallback(onPageChangeCallback);

        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(getResources().getString(sectionsPagerAdapter.getTabTitleId(position)));
        }).attach();

        initOnClickListeners();
        initViewModelObservables();
    }


    public void initViews(){
        sectionsPagerAdapter = new SectionsPagerAdapter(this);
        tabs = findViewById(R.id.tabs);
        fabAdd = findViewById(R.id.fab_add);
        deleteIcon = findViewById(R.id.delete_icon);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
    }

    @Override
    public void initOnClickListeners(){
        fabAdd.setOnClickListener(v -> startActivity(intent));

        deleteIcon.setOnClickListener(v -> taskViewModel.deleteSelected());
    }

    @Override
    public void initViewModelObservables(){
        taskViewModel.itemsLD.observe(this, items -> {
            if(items.size() > 0){
                createNotificationChannel();
                initNotifications();
            }
        });

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

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "ReminderChannel";
            String description = "Channel Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("testNot", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void initNotifications(){
        Intent intent = new Intent(MainActivity.this, NotificationBroadcast.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long currentTime = System.currentTimeMillis();
        long tenSecondsInMillis = 1000 * 10;
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                currentTime + tenSecondsInMillis,
                pendingIntent);

    }

    private final ViewPager2.OnPageChangeCallback onPageChangeCallback
            = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            intent.putExtra("fragmentId", sectionsPagerAdapter.getTabTitleId(position));
            Integer layoutRes = null;
            switch(position) {
                case 0:
                    fabAdd.show();
                    layoutRes = Task.Companion.getAddView();
                    break;
                case 1:
                    fabAdd.show();
                    layoutRes = Habit.Companion.getAddView();
                    break;
                case 2:
                    fabAdd.hide();
                    break;
            }
            if(layoutRes != null) intent.putExtra("fragmentId", layoutRes);
        }
    };
}