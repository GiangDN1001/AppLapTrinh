package com.example.apphoclaptrinh.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoclaptrinh.R
import com.example.apphoclaptrinh.adapters.ExerciseAdapter
import com.example.apphoclaptrinh.adapters.OptionAdapter
import com.example.apphoclaptrinh.models.Course
import com.example.apphoclaptrinh.models.Exercise
import com.example.apphoclaptrinh.network.RetrofitClient
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call


class ExerciseActivity : AppCompatActivity() {
    private lateinit var questionText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var exercises: List<Exercise> // Danh sách bài tập
    private var selectedAnswer: String? = null // Lưu đáp án người dùng chọn
    private var currentQuestionIndex: Int = 0 // Chỉ số của câu hỏi hiện tại
    private lateinit var optionAdapter: OptionAdapter
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exercise)
        val exit_icon = findViewById<ImageView>(R.id.exit_icon)

        // Lấy dữ liệu từ Intent
        val courseId = intent.getIntExtra("courseId", 0)
        val lessonId = intent.getIntExtra("lessonId", 0)

        // Khởi tạo các view
        questionText = findViewById(R.id.questionText)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        btnBack = findViewById(R.id.btnBack)
        btnNext = findViewById(R.id.btnNext)

        // Lấy danh sách bài tập từ Intent
        exercises = intent.getParcelableArrayListExtra("exercises") ?: return

        // Hiển thị câu hỏi ban đầu
        displayQuestion(currentQuestionIndex)

        exit_icon.setOnClickListener {
            val intent = Intent(this, LessonActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Nút Back
        btnBack.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                displayQuestion(currentQuestionIndex)
            }
        }

        // Nút Next
        btnNext.setOnClickListener {
            if (currentQuestionIndex < exercises.size - 1) {
                currentQuestionIndex++
                displayQuestion(currentQuestionIndex)
            }
        }
    }

    // Hàm hiển thị câu hỏi và các đáp án
    private fun displayQuestion(index: Int) {
        val currentExercise = exercises[index]
        questionText.text = currentExercise.question
        optionAdapter = OptionAdapter(
            currentExercise.options,
            { selectedOption ->
                selectedAnswer = selectedOption.text
                if (selectedAnswer == currentExercise.correct_answer) {

                } else {

                }

                if (index == exercises.size - 1) {
                    btnNext.text = "Bài học tiếp theo"
                    btnNext.setOnClickListener {
                        if (currentQuestionIndex < exercises.size - 1) {
                            currentQuestionIndex++
                            displayQuestion(currentQuestionIndex)
                        } else {
                            val intent = Intent(this, LessonActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                }
            },
            currentExercise.correct_answer
        )

        // Đặt adapter cho RecyclerView
        recyclerView.adapter = optionAdapter

        // Vô hiệu hóa nút Back khi ở câu hỏi đầu tiên
        btnBack.isEnabled = index > 0

        // Nếu chưa đến câu hỏi cuối cùng, hiển thị nút Next bình thường
        if (index < exercises.size - 1) {
            btnNext.text = "Next"
            btnNext.setOnClickListener {
                if (currentQuestionIndex < exercises.size - 1) {
                    currentQuestionIndex++
                    displayQuestion(currentQuestionIndex)
                }
            }
        }
    }



    // Hàm gọi API để lấy dữ liệu bài tập (nếu cần)
    private fun getExercises(courseId: Int, lessonId: Int, recyclerView: RecyclerView) {
        RetrofitClient.apiService.getCourseDetails(courseId).enqueue(object : Callback<Course> {
            override fun onResponse(call: Call<Course>, response: Response<Course>) {
                if (response.isSuccessful) {
                    val course = response.body()
                    val exercises = course?.content
                        ?.flatMap { it.lessons }
                        ?.find { it.id == lessonId }
                        ?.exercises

                    if (!exercises.isNullOrEmpty()) {
                        val adapter = ExerciseAdapter(exercises)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@ExerciseActivity, "Không có bài tập nào!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ExerciseActivity, "Lỗi: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Course>, t: Throwable) {
                Log.e("API Error", "Lỗi khi gọi API: ${t.message}")
                Toast.makeText(this@ExerciseActivity, "Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}