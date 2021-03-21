package com.example.tah.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tah.R;
import com.example.tah.ui.habit.HabitsFragment;
import com.example.tah.ui.task.TasksFragment;


public class SectionsPagerAdapter extends FragmentStateAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_tasks, R.string.tab_habits};

    public SectionsPagerAdapter(FragmentActivity fa){
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return TasksFragment.newInstance(1);
            default:
                return HabitsFragment.newInstance(1);
        }
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }

    public int getTabTitleId(int position){
        return TAB_TITLES[position];
    }
}