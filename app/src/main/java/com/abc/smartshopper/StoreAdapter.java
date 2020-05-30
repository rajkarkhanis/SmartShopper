package com.abc.smartshopper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.abc.smartshopper.app.AppController;
import com.abc.smartshopper.model.Store;

import java.util.List;

public class StoreAdapter extends BaseAdapter {

    private List<Store> storeList;
    private Activity activity;
    private LayoutInflater layoutInflater;
    Context context;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public StoreAdapter(List<Store> storeList, Activity activity) {
        this.storeList = storeList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int i) {
        return storeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = layoutInflater.inflate(R.layout.store_list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();


         final Store s = storeList.get(i);

        TextView storeName = view.findViewById(R.id.store_name);
        storeName.setText(s.getStoreName());

        TextView storePrice = view.findViewById(R.id.store_price);
        storePrice.setText(s.getStorePrice());

        /*Button storeLink = view.findViewById(R.id.store_link_btn);
        storeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storeUrl = s.getStoreUrl();
                Log.d("STORE URL", storeUrl);
                Uri uri = Uri.parse(storeUrl);
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(websiteIntent);
            }
        }); */




        return view;
    }
}
