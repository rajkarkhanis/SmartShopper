package com.abc.smartshopper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abc.smartshopper.app.AppController;
import com.abc.smartshopper.model.Product;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    public String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String BASE_URL = "https://price-api.datayuge.com/api/v1/compare/search?api_key=470mAe1LTHkheoqKgXOv073koZvkYUucceh&product=";
    public String FINAL_URL;
    public TextView textView;
    public EditText editText;
    public Button searchButton;
    public ListView listView;
    public ProductAdapter adapter;
    public List<Product> productList = new ArrayList<>();
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override


            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }

            }
        };

        searchButton = findViewById(R.id.search_button);
        editText = findViewById(R.id.edit_text);
        listView = findViewById(R.id.list);
        adapter = new ProductAdapter(productList, this);
        listView.setAdapter(adapter);

        progressBar = findViewById(R.id.progressBar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeaderView = (View) navigationView.getHeaderView(0);
        textView = mHeaderView.findViewById(R.id.textViewEmail);
        textView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        navigationView.setNavigationItemSelectedListener(this);


        // onClickListener for searchButton
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText.getText().toString();
                FINAL_URL = BASE_URL.concat(query);

                progressBar.setVisibility(View.VISIBLE);
                // Volley Request
                extractProducts(); 
            }
        });

        // onItemClickListener for listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Product product = productList.get(i);

                // Add the item clicked to HistoryList
                HistoryActivity.addToHistory(product);

                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                detailIntent.putExtra("PRODUCT", product);
                startActivity(detailIntent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        if (id == R.id.nav_viewing_history)
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void extractProducts() {

        if(productList != null)
            productList.clear();
        Log.d(LOG_TAG, "COUNT:  " + adapter.getCount());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FINAL_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, response.toString());

                progressBar.setVisibility(View.GONE);

                //parsing JSON
                try {
                    JSONArray data = response.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++){
                        JSONObject jsonObject = data.getJSONObject(i);
                        Product product = new Product();
                        product.setProductName(jsonObject.getString("product_title"));
                        product.setProductPrice(jsonObject.getInt("product_lowest_price"));
                        product.setProductRating(jsonObject.getDouble("product_rating"));
                        product.setProductImage(jsonObject.getString("product_image"));
                        product.setProductLink(jsonObject.getString("product_link"));
                        product.setProductId(jsonObject.getString("product_id"));
                        productList.add(product);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
                Log.d(LOG_TAG, "COUNT:  " + adapter.getCount());

                if(productList.isEmpty())
                    Toast.makeText(MainActivity.this, "No Products Found", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, "Error:  " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    public void addToWishlist(){
        if(user == null){

        }

    }
}
