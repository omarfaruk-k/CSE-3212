package omarfaruk.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class LandingPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private DBhelper dbHelper;
    private String userEmail;
    private TextView tvOngoingPosts;
    private TextView tvInstructions;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        mAuth = FirebaseAuth.getInstance();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null || !currentUser.isEmailVerified()) {

            Intent intent = new Intent(LandingPageActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }


        userEmail = getIntent().getStringExtra("userEmail");
        if (userEmail == null || userEmail.isEmpty()) {
            userEmail = currentUser.getEmail();
        }


        Button btn_found = findViewById(R.id.btn_found);
        Button btn_lost = findViewById(R.id.btn_lost);
        ImageButton btn_logout = findViewById(R.id.btn_logout);
        tvOngoingPosts = findViewById(R.id.tvOngoingPosts);
        tvInstructions = findViewById(R.id.tvInstructions);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbHelper = new DBhelper(this);
        loadUserPosts();


        btn_logout.setOnClickListener(v -> {

            mAuth.signOut();


            Toast.makeText(LandingPageActivity.this,
                    "Logged out successfully",
                    Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(LandingPageActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        btn_found.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPageActivity.this, FoundActivity.class);
            intent.putExtra("userEmail", userEmail);
            Log.d("LandingPageActivity", "userEmail sent to FoundActivity: " + userEmail);
            startActivity(intent);
        });


        btn_lost.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPageActivity.this, LostActivity.class);
            intent.putExtra("userEmail", userEmail);
            Log.d("LandingPageActivity", "userEmail sent to LostActivity: " + userEmail);
            startActivity(intent);
        });
    }

    private void loadUserPosts() {
        List<Post> posts = dbHelper.getPostsByUserEmail(userEmail);
        adapter = new PostAdapter(this, posts);
        adapter.setOnItemClickListener(post -> {
            Intent intent = new Intent(LandingPageActivity.this, UserPostDetails.class);
            intent.putExtra("post_id", post.getId());
            intent.putExtra("post_type", post.getType());
            intent.putExtra("item_name", post.getItemName());
            intent.putExtra("category", post.getCategory());
            intent.putExtra("date", post.getDate());
            intent.putExtra("image", post.getImage());
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);


        if (posts.isEmpty()) {
            tvOngoingPosts.setVisibility(View.GONE);
            tvInstructions.setVisibility(View.GONE);
        } else {
            tvOngoingPosts.setVisibility(View.VISIBLE);
            tvInstructions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            loadUserPosts();
        } else {

            Intent intent = new Intent(LandingPageActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }
}