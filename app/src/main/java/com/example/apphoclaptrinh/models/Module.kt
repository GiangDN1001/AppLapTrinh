package com.example.apphoclaptrinh.models

data class Module(
    val module_id: Int,       // ID của module
    val module_title: String, // Tiêu đề của module
    val lessons: List<Lesson> // Danh sách bài học trong module
)
