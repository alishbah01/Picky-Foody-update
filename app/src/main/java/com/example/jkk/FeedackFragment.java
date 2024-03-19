package com.example.jkk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FeedackFragment extends Fragment {

    private EditText feedbackEditText;
    private RatingBar ratingBar;
    private FirebaseFirestore firestore;
    private MediaRecorder mediaRecorder;
    private String outputFile;
    private boolean isRecording = false;
    private static final int REQUEST_PERMISSION_CODE = 100;
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feedack, container, false);

        feedbackEditText = rootView.findViewById(R.id.feedbackEditText);
        ratingBar = rootView.findViewById(R.id.ratingBar);
        firestore = FirebaseFirestore.getInstance();
        Button submitFeedbackButton = rootView.findViewById(R.id.submitFeedbackButton);
        ImageButton recordButton = rootView.findViewById(R.id.voiceRecorderIcon);
        Button playButton = rootView.findViewById(R.id.playAudioButton);

        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRecording();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecording();
            }
        });

        // Check and request audio recording permission if not granted
        if (!checkPermission()) {
            requestPermission();
        }

        return rootView;
    }

    private boolean checkPermission() {
        int resultRecord = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
        int resultStorage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return resultRecord == PackageManager.PERMISSION_GRANTED && resultStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start recording
                startRecording();
            } else {
                // Permission denied, inform the user
                Toast.makeText(getActivity(), "Permission denied to record audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toggleRecording() {
        if (!isRecording) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                startRecording();
            }
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        resetMediaRecorder();
        outputFile = getActivity().getFilesDir().getAbsolutePath() + "/recording.3gp";

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFile);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(getActivity(), "Recording started", Toast.LENGTH_SHORT).show();
                isRecording = true;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed to start recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            // External storage not available or not writable
            Toast.makeText(getActivity(), "External storage is not available or writable", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                Toast.makeText(getActivity(), "Recording stopped", Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed to stop recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resetMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void playRecording() {
        if (outputFile != null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(outputFile);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Toast.makeText(getActivity(), "Playing recording", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed to play recording: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "No recording to play", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitFeedback() {
        String feedbackText = feedbackEditText.getText().toString().trim();
        float ratingValue = ratingBar.getRating(); // Get the rating value

        if (feedbackText.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your feedback", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload audio file to Firebase Storage
        if (outputFile != null) {
            // Log the file path
            Log.d("FilePath", "File Path: " + outputFile);

            Uri file = Uri.fromFile(new File(outputFile));
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("audio").child(file.getLastPathSegment());
            UploadTask uploadTask = storageRef.putFile(file);
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                Toast.makeText(getActivity(), "Failed to upload audio: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UploadError", "Failed to upload audio: " + exception.getMessage());
            }).addOnSuccessListener(taskSnapshot -> {
                // Task completed successfully, get the download URL
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Save feedback, rating, and audio URL to Firestore
                    Map<String, Object> feedbackData = new HashMap<>();
                    feedbackData.put("feedback", feedbackText);
                    feedbackData.put("rating", ratingValue);
                    feedbackData.put("audioUrl", uri.toString()); // Add download URL to Firestore

                    // Save feedback document to Firestore
                    firestore.collection("feedbacks").add(feedbackData)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(getActivity(), "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                                feedbackEditText.setText(""); // Clear EditText after submission
                                ratingBar.setRating(0); // Reset rating
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getActivity(), "Failed to submit feedback to Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("FirestoreError", "Failed to submit feedback to Firestore: " + e.getMessage());
                            });
                }).addOnFailureListener(exception -> {
                    // Handle any errors getting the download URL
                    Toast.makeText(getActivity(), "Failed to get audio download URL: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("DownloadURLError", "Failed to get audio download URL: " + exception.getMessage());
                });
            });
        } else {
            // No audio file recorded
            Toast.makeText(getActivity(), "No audio recorded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
