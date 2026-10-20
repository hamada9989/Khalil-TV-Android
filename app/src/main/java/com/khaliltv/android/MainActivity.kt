package com.khaliltv.android

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // تثبيت الشاشة بالوضع الأفقي (Landscape)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContent {
            KhalilTvTheme {
                MainTvScreen()
            }
        }
    }
}

@Composable
fun KhalilTvTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFE50914), // أحمر سينمائي فاخر
            secondary = Color(0xFF1F1F1F),
            background = Color(0xFF0F0F14)
        ),
        content = content
    )
}

@Composable
fun MainTvScreen() {
    // خلفية داكنة متدرجة فخمة
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF141523),
            Color(0xFF07080E)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // الهيدر الرئيسي / اللوجو والعنوان
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KHALIL TV",
                    color = Color(0xFFE50914),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "البث المباشر 🔴",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // منطقة مشغل الفيديو بستايل زجاجي وأطراف مضيئة
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1A1C29))
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFE50914), Color(0xFF3F51B5))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📺 مشغل الوسائط جاهز لعرض البث",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // شريط سفلي لمسارات القنوات والخيارات
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TvButton(text = "القناة الرئيسية", modifier = Modifier.weight(1f))
                TvButton(text = "قائمة المشاهدة", modifier = Modifier.weight(1f))
                TvButton(text = "الإعدادات", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun TvButton(text: String, modifier: Modifier = Modifier) {
    Button(
        onClick = { },
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF252836),
            contentColor = Color.White
        )
    ) {
        Text(text = text, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}
