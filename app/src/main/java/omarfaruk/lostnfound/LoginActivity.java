package omarfaruk.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(LoginActivity.this, LandingPageActivity.class));
            finish();
            return;
        }

        EditText et_loginEmail = findViewById(R.id.et_loginEmail);
        EditText et_loginPass = findViewById(R.id.et_loginPass);
        Button btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(v -> {
            String email = et_loginEmail.getText().toString().trim();
            String password = et_loginPass.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all information!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {

                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
                                intent.putExtra("userEmail", email);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Please verify your email first.",
                                        Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Invalid Email or Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}