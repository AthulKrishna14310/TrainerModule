package com.integrals.trainermodule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageView extends AppCompatActivity {
    private String url;
    private AppCompatImageView imageView;
    private ProgressBar progressBar;
    private ImageButton refreshButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        getSupportActionBar().hide();
        url=getIntent().getStringExtra("url");
        imageView=findViewById(R.id.imageview);
        progressBar=findViewById(R.id.progress_circular);
        refreshButton=findViewById(R.id.refresh);
        progressBar.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPicasso();
        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.INVISIBLE);
                refreshButton.setVisibility(View.INVISIBLE);
                loadPicasso();
            }
        });
    }

    private void loadGlide() {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(ImageView.this)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        refreshButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        refreshButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(imageView);
    }
   private void loadPicasso(){
       progressBar.setVisibility(View.VISIBLE);
       Picasso.get().load(url).fetch(new Callback() {
           @Override
           public void onSuccess() {
               Picasso.get().load(url).into(imageView);
               refreshButton.setVisibility(View.VISIBLE);
               progressBar.setVisibility(View.INVISIBLE);

           }

           @Override
           public void onError(Exception e) {
               refreshButton.setVisibility(View.VISIBLE);
               progressBar.setVisibility(View.INVISIBLE);

           }
       });
   }
}