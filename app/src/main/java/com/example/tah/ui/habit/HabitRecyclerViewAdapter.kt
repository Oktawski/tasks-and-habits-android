package com.example.tah.ui.habit

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tah.databinding.ItemHabitBinding
import com.example.tah.models.Habit
import com.example.tah.ui.main.AddAndDetailsActivity
import com.example.tah.utilities.ViewHabitTime
import java.util.*

class HabitRecyclerViewAdapter(
    private val context: Context,
    private val habits: MutableList<Habit>)
: RecyclerView.Adapter<HabitRecyclerViewAdapter.ViewHolder>(),
    ViewHabitTime
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHabitBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(habits[position])

    override fun getItemCount(): Int = habits.size

    fun update(items: List<Habit>){
        habits.clear()
        habits.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemHabitBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var habit: Habit

        fun bind(habit: Habit){
            this.habit = habit

            val timeMap = getTimeStrings(habit.sessionLength)
            val timeText = String.format(Locale.ENGLISH,
                    "Time left: %s:%s:%s",
                    timeMap["Hours"],
                    timeMap["Minutes"],
                    timeMap["Seconds"])

            with(binding){
                itemName.text = habit.name
                time.text = timeText

                if (habit.description.isNullOrEmpty()){
                    description.visibility = View.GONE
                } else {
                    description.visibility = View.VISIBLE
                    description.text = habit.description
                }
            }
            setOnClickListeners()
        }

        private fun setOnClickListeners(){
            itemView.setOnClickListener {
                val intent = Intent(context, AddAndDetailsActivity::class.java)
                intent.putExtra("fragmentId", Habit.getDetailsView())
                intent.putExtra("habitId", habit.id)
                context.startActivity(intent)
            }
        }
    }
}