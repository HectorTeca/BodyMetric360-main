package com.ulagos.myapplication.tmb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ulagos.myapplication.R
import android.widget.Spinner

class postActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val spinner: Spinner = findViewById(R.id.sp_majors)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayListOf<String>())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val apiRequestTask = ApiRequestTask()

        lifecycleScope.launch {
            try {
                val data = apiRequestTask.executeAsync("https://api.example.com/data")
                val jsonArray = JSONArray(data)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val name = jsonObject.getString("name")
                    adapter.add(name)
                }

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }