package com.example.apphoclaptrinh.network


import com.example.apphoclaptrinh.models.Course

import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Path

interface ApiService {
    @GET("free_courses")
    fun getCourses(): Call<List<Course>> // Lấy tất cả khóa học

    @GET("free_courses/{courseId}")
    fun getCourseDetails(@Path("courseId") courseId: Int): Call<Course> // Lấy chi tiết khóa học bao gồm các module

}
