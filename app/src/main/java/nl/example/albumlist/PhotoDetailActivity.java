package nl.example.albumlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Intent intent = getIntent();
        final String titlePhoto = intent.getStringExtra("titlePhoto");
        final String imageUrl = intent.getStringExtra("imageUrl");

        TextView photoTitleTextView = findViewById(R.id.photo_title_text_view);
        ImageView photoImageView = findViewById(R.id.photo_image_view);

        photoTitleTextView.setText(titlePhoto);
        Picasso.get().load(imageUrl).into(photoImageView);
    }
}
