package omarfaruk.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


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

            DBhelper dbhelper = new DBhelper(SignupActivity.this);
            boolean insert = dbhelper.insertUser(name, email, pass);
            if (insert) {
                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Signup unsuccessful, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}