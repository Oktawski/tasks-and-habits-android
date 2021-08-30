package com.example.tah.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.tah.R
import com.example.tah.databinding.ActivityMainBinding
import com.example.tah.models.Habit
import com.example.tah.models.Task
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.TaskViewModel
import com.example.tah.viewModels.TodoViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity
    : AppCompatActivity(R.layout.activity_main),
    ViewInitializable
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var addAndDetailsIntent: Intent
    private val taskViewModel: TaskViewModel by viewModels()
    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        addAndDetailsIntent = Intent(this, AddAndDetailsActivity::class.java)
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        setContentView(binding.root)

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(sectionsPagerAdapter.getTabTitleId(position))
        }.attach()

        Log.i("MainActivityK", "onCreate: ${taskViewModel.hashCode()}")

        initOnClickListeners()
        initViewModelObservables()
    }

    override fun initOnClickListeners() {
        binding.fabAdd.setOnClickListener { startActivity(addAndDetailsIntent) }

        binding.deleteIcon.setOnClickListener {
            when(binding.viewPager.currentItem) {
                0 -> taskViewModel.deleteSelected()
                1 -> todoViewModel.deleteSelected()

            }
        }
    }

    override fun initViewModelObservables() {
        taskViewModel.state.observe(this) {
            if(it.status == State.Status.REMOVED) {
                taskViewModel.setCheckBoxVisibility(View.GONE)
                taskViewModel.checkedItemsLD.value = emptyList()
            }
        }
    }

    fun setDeleteIconVisibility(@LayoutRes visibility: Int) {
        binding.deleteIcon.visibility = visibility
    }

    private val onPageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                addAndDetailsIntent.putExtra("fragmentId", sectionsPagerAdapter.getTabTitleId(position))
                var layoutRes: Int? = null

                // Hide soft keyboard
                val view: View? = currentFocus
                if(view != null) {
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }

                when(position) {
                    0 -> {
                        binding.fabAdd.show()
                        layoutRes = Task.getAddView()
                    }
                    1 -> {
                        binding.fabAdd.show()
                        layoutRes = Habit.getAddView()
                        taskViewModel.setCheckBoxVisibility(View.GONE)
                    }
                }

                if(layoutRes != null) addAndDetailsIntent.putExtra("fragmentId", layoutRes)
            }
        }

}
