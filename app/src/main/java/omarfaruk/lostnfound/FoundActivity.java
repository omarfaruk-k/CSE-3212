package omarfaruk.lostnfound;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FoundActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DBhelper dbhelper;
    private TextView nopost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);


        String userEmail = getIntent().getStringExtra("userEmail");
        nopost = findViewById(R.id.nopost);
        recyclerView = findViewById(R.id.list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbhelper = new DBhelper(this);

        displayItems();

        Button btn_found_post_create = findViewById(R.id.btn_found_post_create);

        btn_found_post_create.setOnClickListener(v ->{
            Intent intent = new Intent(FoundActivity.this, FoundPostActivity.class);
            intent.putExtra("userEmail", userEmail);
            Log.d("FoundActivity", "userEmail sent to LostPostActivity: " + userEmail);
            startActivity(intent);
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        displayItems();
    }
    private void displayItems() {
        Cursor cursor = dbhelper.getAllLostItems();
        if (cursor != null && cursor.getCount() > 0) {
            LostItemAdapter adapter = new LostItemAdapter(this, cursor);
            recyclerView.setAdapter(adapter);
        } else {
            nopost.setVisibility(View.VISIBLE);
        }
    }



}
