package com.afrobiz.afrobizfind;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class View_Image extends AppCompatActivity
{
    @BindView(R.id.photoview)
    PhotoView photoview;

    @BindView(R.id.Ivback)
    ImageView Ivback;

    String path = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_view_image);

        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            path = b.getString("path");

            if(path != null)
            {
                Glide.with(View_Image.this).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).into(photoview);
            }
        }
    }
    @OnClick(R.id.Ivback)
    public void onBack()
    {
        onBackPressed();
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}

