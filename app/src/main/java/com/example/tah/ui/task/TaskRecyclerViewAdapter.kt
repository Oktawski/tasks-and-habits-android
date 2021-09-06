package com.example.tah.ui.task

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tah.databinding.ItemTaskBinding
import com.example.tah.models.Task
import com.example.tah.ui.main.AddAndDetailsActivity
import com.example.tah.viewModels.TaskViewModel

class TaskRecyclerViewAdapter(
    private val context: Context,
    private val viewModel: TaskViewModel,
    private val checkedTasks: MutableList<Int> = mutableListOf()
) : RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size


    inner class ViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root){

        private lateinit var task: Task

        fun bind(task: Task){
            this.task = task

            with(binding){
                itemName.text = task.name
                checkBox.isChecked = false
                typeImage.setImageResource(task.imageResource)

                if(task.description.isNullOrEmpty()){
                    description.visibility = View.GONE
                }
                else{
                    description.visibility = View.VISIBLE
                    description.text = task.description
                }

                checkBox.visibility = viewModel.getCheckBoxVisibility().value!!
            }
            setOnClickListeners()
        }

        private fun setOnClickListeners() {
            itemView.setOnClickListener {
                val intent = Intent(context, AddAndDetailsActivity::class.java)
                intent.putExtra("fragmentId", Task.getDetailsView())
                intent.putExtra("taskId", task.taskId)
                intent.putExtra("name", task.name)
                intent.putExtra("description", task.description)
                context.startActivity(intent)
            }

            itemView.setOnLongClickListener {
                val visibility: Int = if(binding.checkBox.visibility == View.VISIBLE)
                    View.GONE else View.VISIBLE

                viewModel.setCheckBoxVisibility(visibility)

                if(visibility == View.GONE){
                    checkedTasks.clear()
                    viewModel.addToCheckedItems(emptyList())
                }

                return@setOnLongClickListener true
            }

            binding.checkBox.setOnClickListener {
                if(binding.checkBox.isChecked){
                    checkedTasks.add(task.taskId!!)
                }else{
                    checkedTasks.remove(task.taskId!!)
                }
                viewModel.addToCheckedItems(checkedTasks)
            }
        }
    }

}
