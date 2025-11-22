package com.example.myrssreaderapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myrssreaderapp.R;

public class CategoryActivity extends AppCompatActivity {

    ListView listCategories;

    String[] categories = {
            "Thời sự",
            "Thế giới",
            "Khoa học - Công nghệ",
            "Giáo dục",
            "Sức khỏe",
            "Giải trí"
    };

    String[] rssLinks = {
            "https://vnexpress.net/rss/thoi-su.rss",
            "https://vnexpress.net/rss/the-gioi.rss",
            "https://vnexpress.net/rss/khoa-hoc.rss",
            "https://vnexpress.net/rss/giao-duc.rss",
            "https://vnexpress.net/rss/suc-khoe.rss",
            "https://vnexpress.net/rss/giai-tri.rss"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listCategories = findViewById(R.id.listCategories);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                categories
        );

        listCategories.setAdapter(adapter);

        listCategories.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(CategoryActivity.this, NewsByCategoryActivity.class);
            intent.putExtra("rss_url", rssLinks[position]);
            intent.putExtra("title", categories[position]);
            startActivity(intent);
        });
    }
}