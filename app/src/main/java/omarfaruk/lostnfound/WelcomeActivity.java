package omarfaruk.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button btn_login = findViewById(R.id.btn_login);
        Button btn_signup = findViewById(R.id.btn_signup);

        btn_login.setOnClickListener(v ->{
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btn_signup.setOnClickListener(v ->{
            Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
            startActivity(intent);
        });

    }
}