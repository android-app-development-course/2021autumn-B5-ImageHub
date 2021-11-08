package com.hyosakura.imagehub

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.hyosakura.imagehub.ui.theme.ImageHubTheme
import com.hyosakura.imagehub.util.AppDatabase
import com.hyosakura.imagehub.util.DataBaseUtil

class MainActivity : ComponentActivity() {
    val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(applicationContext)
        setContent {
            ImageHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

fun init(context : Context) {
    DataBaseUtil.db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "imagehub"
    ).build()
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageHubTheme {
        Greeting("Android")
    }
}