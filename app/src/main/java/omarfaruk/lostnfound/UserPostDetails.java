package omarfaruk.lostnfound;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserPostDetails extends AppCompatActivity {
    private Spinner categorySpinner;
    private EditText etPostName, etPostDescription, etDate, etPostLocation, etMobile;
    private ImageView ivSelectedImage;
    private DBhelper dbHelper;
    private int postId;
    private String postType;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_details);

        dbHelper = new DBhelper(this);


        categorySpinner = findViewById(R.id.catagory_selector);
        etPostName = findViewById(R.id.et_post_name);
        etPostDescription = findViewById(R.id.et_post_description);
        etDate = findViewById(R.id.et_date);
        etPostLocation = findViewById(R.id.et_post_location);
        etMobile = findViewById(R.id.et_mbl);
        ivSelectedImage = findViewById(R.id.iv_selectedImage);


        Intent intent = getIntent();
        postId = intent.getIntExtra("post_id", -1);
        postType = intent.getStringExtra("post_type");
        userEmail = intent.getStringExtra("user_email");


        loadPostDetails();


        findViewById(R.id.btn_update).setOnClickListener(v -> updatePost());
        findViewById(R.id.btn_delete).setOnClickListener(v -> deletePost());
        findViewById(R.id.btn_okay).setOnClickListener(v -> finish());
        findViewById(R.id.btn_uploadimage).setOnClickListener(v -> uploadImage());
    }

    private void loadPostDetails() {
        Post post = dbHelper.getPostById(postId, postType);
        if (post != null) {
            etPostName.setText(post.getItemName());
            etPostDescription.setText(post.getDescription());
            etDate.setText(post.getDate());
            etPostLocation.setText(post.getLocation());
            etMobile.setText(post.getMobile());


            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.categories_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
            int spinnerPosition = adapter.getPosition(post.getCategory());
            categorySpinner.setSelection(spinnerPosition);


            byte[] imageBytes = post.getImage();
            if (imageBytes != null && imageBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                ivSelectedImage.setImageBitmap(bitmap);
            } else {

//                ivSelectedImage.setImageResource(R.drawable.noimage);

                 ivSelectedImage.setVisibility(View.GONE);
            }

            setTitle(postType.equals("found") ? "Found Item Details" : "Lost Item Details");

            // Optionally, you can set the post type in a TextView if you have one
            // TextView tvPostType = findViewById(R.id.tv_post_type);
            // tvPostType.setText(postType.equals("found") ? "Found Item" : "Lost Item");
        } else {
            Toast.makeText(this, "Failed to load post details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updatePost() {
        String name = etPostName.getText().toString();
        String description = etPostDescription.getText().toString();
        String date = etDate.getText().toString();
        String location = etPostLocation.getText().toString();
        String mobile = etMobile.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();

        boolean success = dbHelper.updatePost(postId, postType, name, description, date, location, mobile, category);
        if (success) {
            Toast.makeText(this, "Post updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update post", Toast.LENGTH_SHORT).show();
        }
    }

    private void deletePost() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean success = dbHelper.deletePost(postId, postType);
                    if (success) {
                        Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to delete post", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void uploadImage() {
        // Implement image upload functionality
        // This could involve opening a gallery intent and handling the result
    }
}