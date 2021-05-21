package com.example.tah.utilities

import android.view.View
import com.google.android.material.textfield.TextInputEditText

interface ViewHelper {

    fun showErrorMessages(vararg view: View){
        for(v in view){
            if(v is TextInputEditText && v.text.isNullOrEmpty()){
                v.error = "Cannot be blank"
            }
        }
    }
}