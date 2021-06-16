package com.afrobiz.afrobizfind;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.afrobiz.afrobizfind.adapter.ImageSliderAdapter;
import com.afrobiz.afrobizfind.ui.modal.EventImage;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.rd.pageindicatorview.view.PageIndicatorView;
import com.rd.pageindicatorview.view.animation.AnimationType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageSliderActivity extends AppCompatActivity
{
    ViewPager pager;
    ImageSliderAdapter adapter ;
    List listImages;
    PageIndicatorView pageIndicatorView;
    int currentposition = 0 ;
    boolean eventImages = false;
    @BindView(R.id.Ivback)
    ImageView Ivback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activtiy_image_slider);

        ButterKnife.bind(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            currentposition = b.getInt("position" , 0 );

            eventImages = b.getBoolean("event" , false);

            if(eventImages == true)
            {
                listImages = (List<EventImage>) b.getSerializable("imgList");
            }
            else
            {
                listImages= (List<Image>) b.getSerializable("imgList");
            }

        }
        adapter = new ImageSliderAdapter(ImageSliderActivity.this, listImages , eventImages);

        pageIndicatorView.setCount(listImages.size());
        pageIndicatorView.setSelection(currentposition);

        pageIndicatorView.setAnimationType(AnimationType.WORM);

        pager.setAdapter(adapter);
        pager.setCurrentItem(currentposition);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                pager.setCurrentItem(currentposition);
            }
        }, 100);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {}

            @Override
            public void onPageSelected(int position)
            {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
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
