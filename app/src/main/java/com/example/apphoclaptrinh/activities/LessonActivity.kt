package com.example.apphoclaptrinh.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoclaptrinh.ChatBotActivity
import com.example.apphoclaptrinh.R
import com.example.apphoclaptrinh.adapters.LessonAdapter
import com.example.apphoclaptrinh.models.Course
import com.example.apphoclaptrinh.models.Lesson
import com.example.apphoclaptrinh.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LessonActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var lessonAdapter: LessonAdapter
    private var courseId: Int = 0
    private var lessons: List<Lesson> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lesson)
        recyclerView = findViewById(R.id.lesson_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val exit_icon = findViewById<ImageView>(R.id.exit_icon)
        exit_icon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        courseId = intent.getIntExtra("courseId", 0)
        Log.d("LessonActivity", "courseId: $courseId")

        if (courseId == 0) {
            Toast.makeText(this, "Invalid course ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Khởi tạo adapter
        lessonAdapter = LessonAdapter(lessons) { selectedLesson ->
            val intent = Intent(this, LessonDetailActivity::class.java)
            intent.putExtra("lesson", selectedLesson) // Truyền đối tượng Lesson
            startActivity(intent)
        }

        recyclerView.adapter = lessonAdapter

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
        getCourseDetails(courseId)
    }

    private fun getCourseDetails(courseId: Int) {
        RetrofitClient.apiService.getCourseDetails(courseId).enqueue(object : Callback<Course> {
            override fun onResponse(call: Call<Course>, response: Response<Course>) {
                Log.d("LessonActivity", "API called with courseId: $courseId")

                if (response.isSuccessful) {
                    val course = response.body()
                    if (course != null) {
                        Log.d("LessonActivity", "Course: ${course.title}")
                        lessons = course.content.flatMap { it.lessons } // Gán giá trị cho lessons
                        lessonAdapter.setLessons(lessons) // Cập nhật danh sách bài học vào adapter
                    } else {
                        Log.e("LessonActivity", "No course found for courseId $courseId")
                    }
                } else {
                    Log.e("LessonActivity", "API Error: Code ${response.code()}, Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Course>, t: Throwable) {
                Log.e("LessonActivity", "API Failure: ${t.message}")
            }
        })
    }
}

