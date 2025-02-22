package com.example.apphoclaptrinh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import com.example.apphoclaptrinh.activities.MainActivity
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ChatBotActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    // UI components
    lateinit var txtResponse: TextView
    lateinit var idTVQuestion: TextView
    lateinit var etQuestion: TextInputEditText

    // Cờ để theo dõi trạng thái request
    private var isRequestInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        // Initialize UI components
        etQuestion = findViewById(R.id.etQuestion)
        idTVQuestion = findViewById(R.id.idTVQuestion)
        txtResponse = findViewById(R.id.txtResponse)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val exit_icon = toolbar.findViewById<ImageView>(R.id.exit_icon)
        exit_icon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Handle the send action on the keyboard
        etQuestion.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val question = etQuestion.text.toString().trim()
                if (question.isNotEmpty()) {
                    if (!isRequestInProgress) {
                        txtResponse.text = "Please wait..."
                        isRequestInProgress = true
                        getResponse(question) { response ->
                            runOnUiThread {
                                txtResponse.text = response
                                isRequestInProgress = false
                            }
                        }
                    } else {
                        Toast.makeText(this, "Please wait for the current request to finish.", Toast.LENGTH_SHORT).show()
                    }
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getResponse(question: String, callback: (String) -> Unit) {
        idTVQuestion.text = question
        etQuestion.setText("")

        val apiKey = "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"
        val url = "https://api.openai.com/v1/chat/completions"

        // Request body for gpt-4o-mini
        val requestBody = """
            {
                "model": "gpt-4o-mini",
                "store": true,
                "messages": [
                    {
                        "role": "user",
                        "content": "$question"
                    }
                ]
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error", "API failed", e)
                callback("Error: ${e.message}")
                isRequestInProgress = false
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (response.code == 429) {
                    callback("Error: Too many requests. Please wait a moment and try again.")
                    isRequestInProgress = false
                    return
                }

                if (body != null) {
                    try {
                        val jsonObject = JSONObject(body)
                        if (jsonObject.has("choices")) {
                            val choicesArray = jsonObject.getJSONArray("choices")
                            val messageObject = choicesArray.getJSONObject(0).getJSONObject("message")
                            val content = messageObject.getString("content")
                            callback(content.trim())
                        } else {
                            callback("No choices found. Here's the raw response:\n$body")
                        }
                    } catch (e: Exception) {
                        Log.e("error", "Parsing error", e)
                        callback("Error parsing the response: ${e.message}")
                    }
                } else {
                    callback("Empty response body.")
                }
                isRequestInProgress = false
            }
        })
    }


}
