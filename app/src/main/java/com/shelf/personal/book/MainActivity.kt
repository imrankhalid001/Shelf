package com.shelf.personal.book

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shelf.personal.book.navigation.ShelfNavigation
import com.shelf.personal.book.ui.theme.ShelfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShelfTheme {
                ShelfNavigation()
            }
        }
    }
}
