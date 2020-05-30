package com.abc.smartshopper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.abc.smartshopper.app.AppController;
import com.abc.smartshopper.model.Product;

import java.util.List;

public    class ProductAdapter extends BaseAdapter {

    private List<Product> productList;
    private Activity activity;
    private LayoutInflater inflater;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ProductAdapter(List<Product> productList, Activity activity) {
        this.productList = productList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return productList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.product_list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbnail =  view.findViewById(R.id.thumbnail);
        TextView title = view.findViewById(R.id.title);
        TextView rating = view.findViewById(R.id.rating);
        TextView price = view.findViewById(R.id.price);
        Button saveButton = view.findViewById(R.id.save_button);

        // getting product data for the row
        Product p = productList.get(i);

        //thumbnail image
        thumbnail.setImageUrl(p.getProductImage(), imageLoader);

        //title
        title.setText(p.getProductName());

        //rating
        rating.setText("Rating: " + String.valueOf(p.getProductRating()));

        //price
        price.setText(String.valueOf(p.getProductPrice()));

        //call a method
        saveButton

        return view;
    }


}
