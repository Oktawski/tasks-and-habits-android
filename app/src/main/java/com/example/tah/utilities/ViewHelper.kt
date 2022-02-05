package com.example.tah.utilities

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

interface ViewHelper {

    fun showErrorMessages(vararg view: View){
        for(v in view){
            if(v is TextInputEditText && v.text.isNullOrEmpty()){
                v.error = "Cannot be blank"
            }
            else if(v is TextInputLayout) {
                v.editText?.error = "Cannot be blank"
            }
        }
    }

    fun toggleEditText(isEnabled: Boolean, vararg editText: EditText) {
        for (e in editText) {
            e.isEnabled = isEnabled
        }
    }

    fun toggleEditText(vararg editText: EditText) {
        for (e in editText) {
            e.isEnabled = !e.isEnabled
        }
    }

}