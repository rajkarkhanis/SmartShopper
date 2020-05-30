package com.abc.smartshopper.model;

import java.util.ArrayList;
import java.util.List;

public    class SliderImage   {
    public List<String> imagesUrl = new ArrayList<>();

    public String getImagesUrl(int position) {
        return imagesUrl.get(position);
    }

    public void setImagesUrl(String url, int position) {
        imagesUrl.add(position, url);
    }
}
