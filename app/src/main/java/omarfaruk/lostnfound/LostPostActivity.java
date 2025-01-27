package omarfaruk.lostnfound;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class LostPostActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText Name;
    private EditText Description;
    private EditText Date;
    private EditText Location;
    private EditText Mobile;
    private String userEmail;
    private ImageView SelectedImage;
    private Button btn_imageSelect;
    private Button btn_submitPost;
    private DBhelper dbhelper;
    private byte[] imageByteArray;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private Spinner spinnerCategories;
    String selectedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_post);



        userEmail = getIntent().getStringExtra("userEmail");
        Log.d("LostPostActivity", "Received userEmail: " + userEmail);
        Name = findViewById(R.id.et_post_name);
        Description = findViewById(R.id.et_post_description);
        Date = findViewById(R.id.et_date);
        Mobile = findViewById(R.id.et_mbl);
        Location = findViewById(R.id.et_post_location);
        SelectedImage = findViewById(R.id.iv_selectedImage);
        btn_imageSelect = findViewById(R.id.btn_uploadimage);
        btn_submitPost = findViewById(R.id.btn_lost_post_submit);
        spinnerCategories = findViewById(R.id.catagory_selector);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });








        dbhelper = new DBhelper(this);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    SelectedImage.setImageBitmap(imageBitmap);
                    imageByteArray = bitmapToByteArray(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_imageSelect.setOnClickListener(view -> showImageSelectionDialog());

        btn_submitPost.setOnClickListener(view -> submitLostPost());


    }

    private void showImageSelectionDialog() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        imagePickerLauncher.launch(pickIntent);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void submitLostPost() {
        String cat = selectedItem;
        String name = Name.getText().toString();
        String description = Description.getText().toString();
        String date = Date.getText().toString();
        String location = Location.getText().toString();
        String mobile = Mobile.getText().toString();


        if (name.isEmpty() || date.isEmpty() || location.isEmpty() ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageByteArray == null) {
            Bitmap defaultImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
            imageByteArray = bitmapToByteArray(defaultImageBitmap);
        }

        Log.d("LostPostActivity", "userEmail before insertion: " + userEmail);
        boolean insert = dbhelper.insertLostItem(userEmail, cat, name, mobile, description, date, location, imageByteArray);

        if(insert){
            Toast.makeText(this, "lost Posted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LostPostActivity.this, LandingPageActivity.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
        }

    }
}
