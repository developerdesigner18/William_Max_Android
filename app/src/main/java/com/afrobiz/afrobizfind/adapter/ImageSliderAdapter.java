package com.afrobiz.afrobizfind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.View_Image;
import com.afrobiz.afrobizfind.ui.modal.Event;
import com.afrobiz.afrobizfind.ui.modal.EventImage;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends PagerAdapter
{
    Context context;
    List  listImage;
    boolean isEventImage  = false;
//    private List<View> viewList;
    LayoutInflater inflater;

    public ImageSliderAdapter(Context context, List listImage1 , boolean isEventImages1) {
        this.context = context;
        this.isEventImage = isEventImages1;

        if (isEventImage == true)
        {
            this.listImage = (List<EventImage>) listImage1;
        }
        else
        {
            this.listImage = (List<Image>) listImage1;
        }

//        viewList = new ArrayList<>();

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return listImage.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view)
    {
        collection.removeView((View) view);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position)
    {
        View imageLayout = inflater.inflate(R.layout.image_slider_view, view, false);

        assert imageLayout != null;
        final PhotoView photoview = (PhotoView) imageLayout.findViewById(R.id.photoview);

        String path = null;

        if(isEventImage == true)
        {
            EventImage objEvent = (EventImage) listImage.get(position);
            path = objEvent.getImagepath();
        }
        else
        {
            Image objEvent = (Image) listImage.get(position);
            path = objEvent.getImagepath();
        }

        if(path != null)
        {
            Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).into(photoview);
        }

        view.addView(imageLayout, 0);

        return imageLayout;
    }

//    void setData(@Nullable List<View> list)
//    {
//        this.viewList.clear();
//
//        if (list != null && !list.isEmpty())
//        {
//            this.viewList.addAll(list);
//        }
//
//        notifyDataSetChanged();
//    }

//    @NonNull
//    List<View> getData()
//    {
//        if (viewList == null)
//        {
//            viewList = new ArrayList<>();
//        }
//
//        return viewList;
//    }
}
