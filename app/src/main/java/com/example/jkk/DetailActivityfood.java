package com.example.jkk;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailActivityfood extends AppCompatActivity {

    private EditText specialInstructionsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activityfood);

        specialInstructionsEditText = findViewById(R.id.foodSpecialInstructionsEditText);

        Intent intent = getIntent();
        String foodIngredients = intent.getStringExtra("ingredients");
        String foodDescription = intent.getStringExtra("FoodDescription");

        TextView description = findViewById(R.id.foodDescriptionTextView);
        description.setText(foodDescription);
        TextView ingredients = findViewById(R.id.foodIngredientTextView);
        ingredients.setText(foodIngredients);
    }

    // Method to start voice input
    public void startVoiceInput(View view) {
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {
                Toast.makeText(DetailActivityfood.this, "Speech recognition failed. Please try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> voiceResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (voiceResults != null && !voiceResults.isEmpty()) {
                    String recognizedSpeech = voiceResults.get(0);
                    specialInstructionsEditText.setText(recognizedSpeech);
                } else {
                    Toast.makeText(DetailActivityfood.this, "No voice input recognized. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        speechRecognizer.startListening(intent);
    }

    // Method to handle submit button click
    public void submit(View view) {
        String specialInstructions = specialInstructionsEditText.getText().toString();
        // Perform the desired action with special instructions, e.g., send to server or save
        System.out.println("Special Instructions: " + specialInstructions);
    }
}
