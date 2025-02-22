package com.example.apphoclaptrinh.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apphoclaptrinh.R
import com.example.apphoclaptrinh.models.Course

class CourseAdapter : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private var courseList = mutableListOf<Course>()
    private var onCourseClick: ((Course) -> Unit)? = null
    fun setCourses(courses: List<Course>) {
        courseList.clear()
        courseList.addAll(courses)
        notifyDataSetChanged()
    }

    fun setOnCourseClickListener(listener: (Course) -> Unit) {
        onCourseClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.titleTextView.text = course.title
        holder.descriptionTextView.text = course.description
        Glide.with(holder.itemView).load(course.image_url).into(holder.courseImageView)
        holder.itemView.setOnClickListener {
            onCourseClick?.invoke(course)  // Gọi callback khi bấm vào khóa học
        }
    }

    override fun getItemCount(): Int = courseList.size

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.course_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.course_description)
        val courseImageView: ImageView = itemView.findViewById(R.id.course_image)
    }
}