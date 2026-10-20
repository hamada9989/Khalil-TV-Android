package com.khaliltv.android

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember { mutableStateOf(false) }

                    if (!isLoggedIn) {
                        LoginScreen(onLoginSuccess = { isLoggedIn = true })
                    } else {
                        HomeScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    SideEffect {
        (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(340.dp)
        ) {
            Text(
                text = "Khalil TV",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (errorMsg.isNotEmpty()) {
                Text(text = errorMsg, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("اسم المستخدم") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("كلمة المرور") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (username.isEmpty() || password.isEmpty()) {
                        errorMsg = "يرجى تعبئة جميع الحقول"
                        return@Button
                    }
                    loading = true
                    errorMsg = ""
                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(username).get()
                        .addOnSuccessListener { doc ->
                            loading = false
                            if (doc.exists()) {
                                val dbPass = doc.getString("password")
                                val isActive = doc.getBoolean("isActive") ?: false
                                val expiry = doc.getLong("expiryDate") ?: 0L
                                val now = System.currentTimeMillis()

                                if (!isActive) {
                                    errorMsg = "الحساب موقوف من الإدارة"
                                } else if (now > expiry) {
                                    errorMsg = "انتهت صلاحية الاشتراك السنوي"
                                } else if (dbPass == password) {
                                    onLoginSuccess()
                                } else {
                                    errorMsg = "كلمة المرور غير صحيحة"
                                }
                            } else {
                                errorMsg = "المستخدم غير موجود"
                            }
                        }
                        .addOnFailureListener {
                            loading = false
                            errorMsg = "خطأ في الاتصال بالشبكة"
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text(if (loading) "جاري التحقق..." else "تسجيل الدخول")
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("أهلاً بك في Khalil TV - القنوات والأفلام والمسلسلات")
    }
}
