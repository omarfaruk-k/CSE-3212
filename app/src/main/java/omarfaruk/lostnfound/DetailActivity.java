package omarfaruk.lostnfound;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DetailActivity extends AppCompatActivity {

    private DBhelper dbhelper;
    private int postId;
    private int lostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView detailName = findViewById(R.id.detail_name);
        TextView detailPostId = findViewById(R.id.detail_id);
        TextView detailCategory = findViewById(R.id.detail_cat);
        TextView detailLocation = findViewById(R.id.detail_location);
        TextView detailDate = findViewById(R.id.detail_date);
        TextView detailDescription = findViewById(R.id.detail_desc);
        ImageView detailImage = findViewById(R.id.detail_image);
        Button thatsIT = findViewById(R.id.btn_thats_it);

        dbhelper = new DBhelper(this);

        String name = getIntent().getStringExtra("item_name");
        String description = getIntent().getStringExtra("item_description");
        String category = getIntent().getStringExtra("item_category");
        String location = getIntent().getStringExtra("item_location");
        String date = getIntent().getStringExtra("item_date");
        postId = getIntent().getIntExtra("item_post_id", 0);
        lostId = getIntent().getIntExtra("item_lost_id", 0);
        byte[] imageBytes = getIntent().getByteArrayExtra("item_image");

        detailName.setText(name);
        detailPostId.setText(postId != 0 ? String.valueOf(postId) : String.valueOf(lostId));
        detailCategory.setText(category);
        detailLocation.setText(location);
        detailDate.setText(date);
        detailDescription.setText(description);

        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            detailImage.setImageBitmap(bitmap);
        }

        thatsIT.setOnClickListener(view -> showBottomSheet());
    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = LayoutInflater.from(this).inflate(
                R.layout.bottom_sheet_contact,
                findViewById(R.id.main), false
        );

        TextView tvPhoneNumber = bottomSheetView.findViewById(R.id.tv_phone_number);
        Button btnCall = bottomSheetView.findViewById(R.id.btn_call);


        String mobileNumber = postId != 0 ? dbhelper.getMobileNumberByPostID(postId) : dbhelper.getLostMobileNumberByLostID(lostId);
        if (mobileNumber != null) {
            tvPhoneNumber.setText("Phone Number: " + mobileNumber);
        } else {
            tvPhoneNumber.setText("Phone Number: Not available");
        }

        btnCall.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + mobileNumber));
            startActivity(callIntent);
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}
