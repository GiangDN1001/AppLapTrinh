package com.example.apphoclaptrinh.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apphoclaptrinh.R
import com.example.apphoclaptrinh.models.AppDatabase
import com.example.apphoclaptrinh.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var edtUsername: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        db = AppDatabase.getDatabase(this)

        edtUsername = findViewById(R.id.edtSignUpUsername)
        edtEmail = findViewById(R.id.edtSignUpEmail)
        edtPassword = findViewById(R.id.edtSignUpPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val existingUser = db.userDao().getUserByEmail(email)
                    if (existingUser == null) {
                        db.userDao().insertUser(User(username = username, email = email, password = password))
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SignUpActivity, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SignUpActivity, "Email đã tồn tại!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
