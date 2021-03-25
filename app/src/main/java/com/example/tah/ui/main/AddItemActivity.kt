package com.example.tah.ui.main

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.tah.R
import com.example.tah.ui.task.TaskAddFragment

class AddItemActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val backArrow = findViewById<ImageButton>(R.id.back_arrow)

        supportFragmentManager.beginTransaction()
                .replace(R.id.add_fragment_container, TaskAddFragment())
                .commit()


        backArrow.setOnClickListener{
            finish()
        }
    }
}