package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.afrobiz.afrobizfind.ImageSliderActivity;
import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.View_Image;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;


public class SlidingImage_Adapter extends PagerAdapter
{
    private List<Image> IMAGES;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImage_Adapter(Context context, List<Image> IMAGES)
    {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position)
    {
        View imageLayout = inflater.inflate(R.layout.slider_images, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);

        Glide.with(context).load(IMAGES.get(position).getImagepath()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(IMAGES.get(position).getImagepath() != null)
                {
//                    Intent i_image = new Intent(context , ImageSliderActivity.class);
//                    i_image.putExtra("path",IMAGES.get(position).getImagepath());
//                    context.startActivity(i_image);

                    Intent i_image = new Intent(context, ImageSliderActivity.class);
                    i_image.putExtra("position", position);
                    i_image.putExtra("imgList", (Serializable) IMAGES);
                    i_image.putExtra("event", false);
                    context.startActivity(i_image);
                }
            }
        });

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
