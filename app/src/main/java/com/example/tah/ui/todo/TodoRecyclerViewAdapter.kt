package com.example.tah.ui.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tah.R
import com.example.tah.models.Todo
import com.example.tah.viewModels.TodoViewModel

class TodoRecyclerViewAdapter(
        private var todos: List<Todo>,
        private val context: Context,
        private val viewModel: TodoViewModel
): RecyclerView.Adapter<TodoRecyclerViewAdapter.ViewHolder>() {

    fun update(todos: List<Todo>){
        this.todos = todos
        this.notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoRecyclerViewAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(context, view, viewModel)
    }

    override fun onBindViewHolder(holder: TodoRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    class ViewHolder(
            private val context: Context,
            itemView: View,
            private val viewModel: TodoViewModel
    ): RecyclerView.ViewHolder(itemView){

        private val name: TextView = itemView.findViewById(R.id.name)
        private val okIcon: ImageButton = itemView.findViewById(R.id.ok_icon)

        var todo: Todo? = null

        fun bind(todo: Todo){
            this.todo = todo
            this.name.text = todo.name

            if(this.todo!!.isComplete){
                okIcon.visibility = View.VISIBLE
            }
            else
                okIcon.visibility = View.GONE

            initOnClickListeners()
        }

        private fun initOnClickListeners(){
            itemView.setOnClickListener {
                todo?.isComplete = !todo?.isComplete!!

                viewModel.update(todo!!)
            }
        }
    }
}