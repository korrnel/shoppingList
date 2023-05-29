package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingList("Android")
                }
            }
        }
    }
}

@Composable
fun ShoppingList(name: String) {

Row {
    Image (
             painter = painterResource(id = R.drawable.image_part_001),
             "image",
             modifier = Modifier

                 .size(40.dp)
                 .clip(CircleShape)
    )
    Spacer(modifier = Modifier.size(10.dp,0.dp))
    Column {
        Text(text = "Hello $name!")
        Text("Gábriel")
    }
}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShoppingListTheme {
        ShoppingList("Kotlin")
    }
}