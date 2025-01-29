package omarfaruk.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        EditText et_name = findViewById(R.id.et_name);
        EditText et_signupEmail = findViewById(R.id.et_signupEmail);
        EditText et_signupPass = findViewById(R.id.et_signupPass);
        EditText et_signupConfirmPass = findViewById(R.id.et_signupConfirmPass);
        Button btn_signupSignup = findViewById(R.id.btn_signupSignup);

        btn_signupSignup.setOnClickListener(v -> {
            String name = et_name.getText().toString().trim();
            String email = et_signupEmail.getText().toString().trim();
            String pass = et_signupPass.getText().toString();
            String confirmPass = et_signupConfirmPass.getText().toString();

            if (name.length() < 3) {
                Toast.makeText(this, "Please Insert A valid Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.isEmpty()) {
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }


            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            mAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(emailTask -> {
                                        if (emailTask.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this,
                                                    "Signup successful! Please check your email for verification.",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(SignupActivity.this, WelcomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SignupActivity.this,
                                                    "Failed to send verification email.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SignupActivity.this,
                                    "Signup failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}