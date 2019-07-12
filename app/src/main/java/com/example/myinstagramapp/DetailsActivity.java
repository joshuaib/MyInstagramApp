package com.example.myinstagramapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myinstagramapp.model.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;


public class DetailsActivity extends AppCompatActivity {
    Post post;
    ImageView detailImage;
    ImageView detailHeart;
    ImageView detailBookmark;
    ImageView detailIcon;
    ImageView detailComment;
    ImageView detailShare;
    TextView detailTime;
    TextView detailDescription;
    TextView detailUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailImage = findViewById(R.id.detailImage);
        detailDescription = findViewById(R.id.detailDescription);
        detailUsername = findViewById(R.id.detailUsername);
        detailTime = findViewById(R.id.detailTime);
        detailImage = findViewById(R.id.detailImage);
        detailHeart = findViewById(R.id.detailHeart);
        detailBookmark = findViewById(R.id.detailBookmark);
        detailIcon = findViewById(R.id.detailIcon);
        detailComment = findViewById(R.id.detailComment);
        detailShare = findViewById(R.id.detailShare);
        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        detailDescription.setText(post.getUser().getUsername() + " : " + post.getDescription());
        detailUsername.setText(post.getUser().getUsername());
        detailTime.setText(post.getCreatedAt().toString());

        ParseFile img = post.getImage();
        String imgUrl = "";
        if (img != null) {
            imgUrl = img.getUrl();
        }
        Glide.with(this)
                .load(imgUrl)
                .into(detailImage);


        try {
            Log.d("DetailsActivity", "Saved file");
            String profileImage = post.getUser().getParseFile("profileImage").getUrl();
//            if (profileImage != null) {
                Glide.with(this)
                        .load(profileImage)
                        .into(detailIcon);
//            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

}
