package com.example.apphoclaptrinh.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.apphoclaptrinh.ChatBotActivity
import com.example.apphoclaptrinh.R
import com.example.apphoclaptrinh.models.Lesson
import com.google.android.material.bottomnavigation.BottomNavigationView

class LessonDetailActivity : AppCompatActivity() {
    private lateinit var buttonStartExercise: Button
    private lateinit var currentLesson: Lesson
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_detail)
        val exit_icon = findViewById<ImageView>(R.id.exit_icon)
        currentLesson = intent.getParcelableExtra("lesson") ?: return

        buttonStartExercise = findViewById(R.id.button_start_exercise)
        // Nhận dữ liệu từ Intent
        val lesson = intent.getParcelableExtra<Lesson>("lesson")

        // Gắn dữ liệu vào UI
        val lessonTitle = findViewById<TextView>(R.id.lessonTitle)
        val lessonDescription = findViewById<TextView>(R.id.lessonDescription)

        lesson?.let {
            lessonTitle.text = it.title
            lessonDescription.text = it.description
        }

        exit_icon.setOnClickListener {
            val intent = Intent(this, LessonActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonStartExercise.setOnClickListener {
            // Chuyển sang ExerciseActivity và truyền dữ liệu bài học
            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra("lessonId", currentLesson.id)  // Truyền ID bài học
            intent.putParcelableArrayListExtra("exercises", ArrayList(currentLesson.exercises))  // Truyền danh sách bài tập
            startActivity(intent)
        }

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
    }
}


