package com.abc.smartshopper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.abc.smartshopper.model.SliderImage;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.abc.smartshopper.app.AppController;
import com.abc.smartshopper.model.Product;
import com.abc.smartshopper.model.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    TextView productNameDetail, productRatingDetail, productPriceDetail;
    public String DETAIL_URL;
    public String IMAGES_URL = "https://price-api.datayuge.com/api/v1/compare/images?api_key=470mAe1LTHkheoqKgXOv073koZvkYUucceh&id=";
    public String API_KEY = "api_key=470mAe1LTHkheoqKgXOv073koZvkYUucceh";
    public String connector = "&";

    public List<Store> storeList = new ArrayList<>();
    private StoreAdapter storeAdapter;
    private ListView listView;

    public String[] storesArray = {"amazon", "flipkart", "snapdeal", "ebay", "paytm", "croma", "yebhi", "indiatimes", "homeshop18", "naaptol", "infibeam", "tatacliq", "shopclues", "paytmmall", "gadgets360", "mi", "2gud"};


    public List<SliderImage> sliderImageList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private ViewPager viewPager;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        listView = findViewById(R.id.store_list_view);
        storeAdapter = new StoreAdapter(storeList, this);
        listView.setAdapter(storeAdapter);

        viewPager = findViewById(R.id.viewpager);
        imageAdapter = new ImageAdapter(sliderImageList, this);
        viewPager.setAdapter(imageAdapter);

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Store store = (Store) storeAdapter.getItem(position);
                Uri storeUri = Uri.parse(store.getStoreUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, storeUri);
                startActivity(websiteIntent);
            }
        });


        //getting and storing data from the list item clicked (SearchActivity)
        Product product = (Product) getIntent().getExtras().getSerializable("PRODUCT");
        String productName = product.getProductName();
        String productPrice = String.valueOf(product.getProductPrice());
        String productRating = String.valueOf(product.getProductRating());
        String productLink = product.getProductLink();
        String productId = product.getProductId();
        Log.d("PRODUCT ID", productId);
        Log.d("LINK DETAIL", productLink);

        // constructing the FINAL_URL for details of product by concatenating api key
        DETAIL_URL = productLink.concat(connector).concat(API_KEY);
        Log.d("DETAIL_URL", DETAIL_URL);

        // constructing the FINAL_URL for images of product by concatenating Product id
        //IMAGES_URL = IMAGES_URL.concat(connector).concat(productId);
        IMAGES_URL = IMAGES_URL.concat(productId);
        Log.d("IMAGE_URL", IMAGES_URL);
        // initializing TextViews
        productNameDetail = findViewById(R.id.product_name_detail);
        productPriceDetail = findViewById(R.id.product_price_detail);
        productRatingDetail = findViewById(R.id.product_rating_detail);

        // setting values for TextViews
        productNameDetail.setText(productName);
        productPriceDetail.setText(productPrice);
        productRatingDetail.setText(productRating);

        // run query for external details
        extractProductDetails();

    }


    private void extractProductDetails() {


        JsonObjectRequest detailRequest = new JsonObjectRequest(Request.Method.GET, DETAIL_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Detail Response", response.toString());

                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray stores = data.getJSONArray("stores");

                            for (int i = 0; i < stores.length(); i++){
                                // Populating the ArrayList of Store objects
                                JSONObject currentObject = stores.getJSONObject(i);
                                Store store = new Store();

                                JSONObject storeObject = currentObject.optJSONObject(storesArray[i]);
                                if (storeObject != null){
                                    store.setStoreName(storeObject.getString("product_store"));
                                    store.setStoreUrl(storeObject.getString("product_store_url"));
                                    store.setStorePrice(storeObject.getString("product_price"));
                                    Log.d("CURRENT STORE: ", store.getStoreName());
                                    storeList.add(store);
                                }

                            }

                            // Populating ViewPager for images
                            SliderImage sliderImage = new SliderImage();
                            JSONArray images = data.getJSONArray("product_images");
                            for (int i = 0; i < images.length(); i++){
                                String imageUrl = images.getString(i);
                                sliderImage.setImagesUrl(imageUrl, i);
                                sliderImageList.add(sliderImage);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        storeAdapter.notifyDataSetChanged();
                        imageAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR", "Error:  " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(detailRequest);
    }
}
