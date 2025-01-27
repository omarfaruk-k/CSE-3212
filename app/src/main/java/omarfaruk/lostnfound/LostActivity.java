package omarfaruk.lostnfound;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DBhelper dbhelper;
    private TextView nopost;
private String um;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);



        nopost = findViewById(R.id.nopost);
        String userEmail = getIntent().getStringExtra("userEmail");
        Log.d("LostActivity", "Received userEmail: " + userEmail);
        recyclerView = findViewById(R.id.lost_list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbhelper = new DBhelper(this);

        displayItems();

        Button btn_lost_post_create = findViewById(R.id.btn_lost_post_create);

        btn_lost_post_create.setOnClickListener(v -> {
            Intent intent = new Intent(LostActivity.this, LostPostActivity.class);
            intent.putExtra("userEmail", userEmail);
            Log.d("LostActivity", "userEmail sent to LostPostActivity: " + userEmail);
            startActivity(intent);
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        displayItems();
    }

    private void displayItems() {
        Cursor cursor = dbhelper.getAllFoundItems();
        if (cursor != null && cursor.getCount() > 0) {
            FoundItemAdapter adapter = new FoundItemAdapter(this, cursor);
            recyclerView.setAdapter(adapter);
        } else {
            nopost.setVisibility(View.VISIBLE);
        }
    }
}
