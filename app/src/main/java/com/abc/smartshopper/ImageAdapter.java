package com.abc.smartshopper;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abc.smartshopper.app.AppController;
import com.abc.smartshopper.model.SliderImage;
import com.android.volley.toolbox.ImageLoader;


import java.util.List;

public    class ImageAdapter extends PagerAdapter   {

    private List<SliderImage> sliderImagesList;
    private LayoutInflater layoutInflater;
    private Context context;
    private ImageLoader imageLoader;

    public ImageAdapter(List<SliderImage> sliderImagesList, Context context) {
        this.sliderImagesList = sliderImagesList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_layout, null);
        SliderImage sliderImage = sliderImagesList.get(position);

        ImageView imageView = view.findViewById(R.id.image);
        imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(sliderImage.getImagesUrl(position), ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
