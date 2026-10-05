package com.example.greetingcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.greetingcard.ui.theme.GreetingCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GreetingCardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AffirmationsApp()
                }
            }
        }
    }
}

data class Affirmation(
    val text: String
)

val affirmations = listOf(
    Affirmation("I am strong."),
    Affirmation("I believe in myself."),
    Affirmation("I can accomplish my goals."),
    Affirmation("I am capable of great things."),
    Affirmation("I choose to be positive."),
    Affirmation("I am proud of myself."),
    Affirmation("I learn from my mistakes."),
    Affirmation("I deserve happiness."),
    Affirmation("I can overcome challenges."),
    Affirmation("I will have a great day.")
)

@Composable
fun AffirmationsApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Affirmations",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AffirmationList(affirmations)
    }
}

@Composable
fun AffirmationList(
    affirmationList: List<Affirmation>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(affirmationList) { affirmation ->
            AffirmationCard(affirmation)
        }
    }
}

@Composable
fun AffirmationCard(
    affirmation: Affirmation
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = affirmation.text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AffirmationsPreview() {
    GreetingCardTheme {
        AffirmationsApp()
    }
}