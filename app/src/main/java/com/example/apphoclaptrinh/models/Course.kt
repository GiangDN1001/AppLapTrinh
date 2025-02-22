package com.example.apphoclaptrinh.models



data class Course(
    val id: Int,               // ID của khóa học
    val title: String,         // Tên khóa học
    val slug: String,          // Đường dẫn slug
    val description: String,   // Mô tả
    val image_url: String,     // Đường dẫn hình ảnh
    val content: List<Module>  // Danh sách module trong khóa học
)



