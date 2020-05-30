package com.abc.smartshopper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.abc.smartshopper.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    public static List<Product> historyList = new ArrayList<>();
    ProductAdapter adapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.history_list);
        adapter = new ProductAdapter(historyList, this);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = historyList.get(position);
                Intent detailIntent = new Intent(HistoryActivity.this, DetailActivity.class);
                detailIntent.putExtra("PRODUCT", product);
                startActivity(detailIntent);
            }
        });
    }

    public static void addToHistory(Product product) {
        if(!historyList.contains(product))
            historyList.add(product);
    }

}
