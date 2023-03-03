package com.example.assign06_6306021621138

import android.util.Log
import android.widget.Toast

class Helper() {

    fun randomString(length: Int = 6): String {
        var str = "abcdefghijklmnopqrstuvwxyzABCD@$#*123456789"
        var value = ""
        for (i in 1..length) {
            value += str.random()
        }
        return value
    }

}