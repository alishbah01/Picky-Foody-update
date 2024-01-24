package com.example.jkk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterationActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        // Initialize Firebase Authentication
//        fAuth = FirebaseAuth.getInstance();
        // Initialize SharedPreferences editor
//        SharedPreferences sharedPreferences = getSharedPreferences("your_preference_name", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
    }

    public void login(View view) {
        startActivity(new Intent(RegisterationActivity.this, LoginActivity.class));
    }

    public void openNavigationDrawer(View view) {
        EditText fullNameEditText = findViewById(R.id.textView5);
        EditText passwordEditText = findViewById(R.id.textView9);
        EditText emailEditText = findViewById(R.id.textView8);
        String fullName = fullNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (!fullName.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
            // Check if the password is strong
            if (isStrongPassword(password)) {
                // Check if the full name already exists
                createNewUser(email, password, fullName);
            } else {
                Toast.makeText(this, "Password must contain both uppercase and lowercase characters", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isStrongPassword(String password) {
        // Implement your password strength validation here
        // Example: Check for uppercase, lowercase, and minimum length
        return password.length() >= 5;
    }

    public void createNewUser(String email, String password, String name) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fuser = fAuth.getCurrentUser();
                        if (fuser != null) {
                            fuser.sendEmailVerification().addOnCompleteListener(task1 ->

                                    Toast.makeText(this, "Register Successfully, Verify your Mail before login", Toast.LENGTH_SHORT).show());

                        }

                        String userID = fAuth.getCurrentUser() != null ? fAuth.getCurrentUser().getUid() : "";
                        Map<String, Object> user = new HashMap<>();
                        user.put("id", userID);
                        user.put("name", name);
                        user.put("email", email);
                        user.put("password", password);
                        user.put("role", "customer");

                        editor.putString("id", userID);
                        editor.putString("name", name);
                        editor.putString("role", "customer");
                        editor.putString("email", email);
                        editor.apply();

                        FirebaseFirestore.getInstance().collection("users").document(userID)
                                .set(user)


                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(this, "User added to Firestore", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Log.d("TAG", "Failed to add user to Firestore"));

                        Intent mainActivity = new Intent(this, LoginActivity.class);
                        this.startActivity(mainActivity);
                    } else {
                        Toast.makeText(this, "Error: " + (task.getException() != null ? task.getException().getMessage() : ""), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.d("onFailure", "Email not sent"));
    }
    
}
