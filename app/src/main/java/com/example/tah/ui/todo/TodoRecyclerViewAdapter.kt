package com.example.tah.ui.todo

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.tah.databinding.ItemTodoBinding
import com.example.tah.models.Todo
import com.example.tah.viewModels.TodoViewModel

class TodoRecyclerViewAdapter(
        private val context: Context,
        private var todos: List<Todo>,
        private val viewModel: TodoViewModel
) : RecyclerView.Adapter<TodoRecyclerViewAdapter.ViewHolder>() {

    fun update(todos: List<Todo>){
        this.todos = todos
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTodoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    inner class ViewHolder(
            private val binding: ItemTodoBinding,
            private val viewModel: TodoViewModel
    ) : RecyclerView.ViewHolder(binding.root){

        var todo: Todo? = null

        fun bind(todo: Todo){
            this.todo = todo

            with(binding){
                name.text = todo.name
                okIcon.visibility = if(todo.isComplete) View.VISIBLE else View.GONE
            }
            initOnClickListeners()
        }

        private fun initOnClickListeners(){
            itemView.setOnClickListener {
                todo?.isComplete = !todo?.isComplete!!
                viewModel.update(todo!!)
            }

            itemView.setOnLongClickListener {
                showEditDialog()
                return@setOnLongClickListener true
            }
        }

        private fun showEditDialog() {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            val input = EditText(context)
            input.hint = "Edit name"
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK") { _, _ ->
                todo?.name = input.text.toString()
                viewModel.update(todo!!)
            }

            builder.setNeutralButton("DELETE") { _, _ ->
                viewModel.delete(todo!!)
            }

            builder.setNegativeButton("CANCEL") { dialog, _ ->
                dialog.cancel()
            }


            builder.show()
        }
    }
}