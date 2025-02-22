package com.example.apphoclaptrinh.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoclaptrinh.ChatBotActivity
import com.example.apphoclaptrinh.R
import com.example.apphoclaptrinh.adapters.CourseAdapter
import com.example.apphoclaptrinh.models.Course
import com.example.apphoclaptrinh.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var banner: ImageView
    private val imageList = arrayOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)
    private var currentImageIndex = 0
    private val handler = Handler()
    private lateinit var txtWelcome: TextView
    private val imageSwitcherRunnable = object : Runnable {
        override fun run() {
            banner.setImageResource(imageList[currentImageIndex])
            currentImageIndex = (currentImageIndex + 1) % imageList.size
            handler.postDelayed(this, 3000)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = Color.TRANSPARENT
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.course_list)
        recyclerView.layoutManager = LinearLayoutManager(this)


        courseAdapter = CourseAdapter()
        recyclerView.adapter = courseAdapter
        courseAdapter.setOnCourseClickListener { course ->
            val intent = Intent(this, LessonActivity::class.java)
            intent.putExtra("courseId", course.id)
            startActivity(intent)
        }

        txtWelcome = findViewById(R.id.txtWelcome)

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userName = sharedPreferences.getString("user_name", "User")

        // Hiển thị tên người dùng
        txtWelcome.text = "Welcome, $userName"

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.learn -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.chatbot -> {
                    // Chuyển sang ChatBotActivity
                    val intent = Intent(this, ChatBotActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        banner = findViewById(R.id.banner)
        handler.post(imageSwitcherRunnable)
        getCourses()
    }


    private fun getCourses() {
        RetrofitClient.apiService.getCourses().enqueue(object : Callback<List<Course>> {
            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                if (response.isSuccessful) {
                    val courses = response.body()
                    if (courses != null) {
                        courseAdapter.setCourses(courses)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to get data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(imageSwitcherRunnable) // Dừng chuyển đổi khi thoát activity
    }

}
