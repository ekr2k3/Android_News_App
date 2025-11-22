package com.example.myrssreaderapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myrssreaderapp.R;
import com.example.myrssreaderapp.adapter.NewsAdapter;
import com.example.myrssreaderapp.models.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;

    List<Item> originalList = new ArrayList<>();
    List<Item> filteredList = new ArrayList<>();

    NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.searchRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(this, filteredList, getIntent().getIntExtra("userId", -1));
        recyclerView.setAdapter(adapter);

        // Nhận dữ liệu từ MainActivity
        String json = getIntent().getStringExtra("items");
        Type type = new TypeToken<List<Item>>(){}.getType();
        originalList = new Gson().fromJson(json, type);

        searchView.setIconified(false);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterLocal(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterLocal(newText);
                return true;
            }
        });
    }

    private void filterLocal(String keyword) {

        filteredList.clear();

        if (keyword == null || keyword.trim().isEmpty()) {
            adapter.notifyDataSetChanged();
            return;
        }

        keyword = keyword.toLowerCase();

        for (Item item : originalList) {
            if (item.getTitle().toLowerCase().contains(keyword)) {
                filteredList.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
