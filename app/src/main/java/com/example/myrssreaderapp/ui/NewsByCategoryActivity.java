package com.example.myrssreaderapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.ApiClient;
import com.example.myrssreaderapp.R;
import com.example.myrssreaderapp.RssService;
import com.example.myrssreaderapp.adapter.NewsAdapter;
import com.example.myrssreaderapp.models.Item;
import com.example.myrssreaderapp.models.RssFeed;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsByCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NewsAdapter adapter;
    List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_by_category);

        String rssUrl = getIntent().getStringExtra("rss_url");
        String title = getIntent().getStringExtra("title");

        setTitle(title);

        recyclerView = findViewById(R.id.recyclerCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(this, items);
        recyclerView.setAdapter(adapter);

        loadRss(rssUrl);
    }

    private void loadRss(String url) {
        RssService service = ApiClient.getClient("https://vnexpress.net/").create(RssService.class);

        service.getFeed(url).enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                if (response.isSuccessful() && response.body() != null) {
                    items.clear();
                    items.addAll(response.body().getChannel().getItems());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RssFeed> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}