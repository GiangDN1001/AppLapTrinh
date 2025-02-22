package com.example.apphoclaptrinh.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoclaptrinh.R
import com.example.apphoclaptrinh.models.Lesson

class LessonAdapter(
    private var lessons: List<Lesson>, // Danh sách các bài học
    private val onClick: (Lesson) -> Unit // Sự kiện khi nhấn vào bài học
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    // Cập nhật danh sách bài học và thông báo cho adapter
    fun setLessons(newLessons: List<Lesson>) {
        lessons = newLessons
        notifyDataSetChanged()
    }

    // Tạo ViewHolder cho mỗi item trong danh sách
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
        return LessonViewHolder(view)
    }

    // Liên kết dữ liệu với ViewHolder
    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.bind(lesson, onClick) // Truyền dữ liệu bài học và sự kiện click
    }

    // Trả về số lượng bài học trong danh sách
    override fun getItemCount(): Int = lessons.size

    // ViewHolder để xử lý các item trong RecyclerView
    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lessonTitle: TextView = itemView.findViewById(R.id.lesson_title)
        private val lessonDuration: TextView = itemView.findViewById(R.id.lesson_duration)

        // Liên kết dữ liệu bài học vào view và xử lý sự kiện nhấn
        fun bind(lesson: Lesson, onClick: (Lesson) -> Unit) {
            lessonTitle.text = lesson.title
            lessonDuration.text = lesson.duration
            itemView.setOnClickListener { onClick(lesson) }
        }
    }
}
