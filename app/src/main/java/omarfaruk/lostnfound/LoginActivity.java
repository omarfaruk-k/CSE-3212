package omarfaruk.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        EditText et_loginEmail = findViewById(R.id.et_loginEmail);
        EditText et_loginPass = findViewById(R.id.et_loginPass);
        Button btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(v ->{
            String email = et_loginEmail.getText().toString();
            String password = et_loginPass.getText().toString();

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all information!", Toast.LENGTH_SHORT).show();
            }
            else {
                
                DBhelper db = new DBhelper(LoginActivity.this);
                boolean result = db.checkUser(email, password);
                
                if(result){
                    Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
                    intent.putExtra("userEmail", email);

                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }


            }
            
        });
    }
}