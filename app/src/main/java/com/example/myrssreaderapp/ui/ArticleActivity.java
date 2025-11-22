package com.example.myrssreaderapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.models.ArticleItem;
import com.example.myrssreaderapp.R;
import com.example.myrssreaderapp.adapter.ArticleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArticleAdapter adapter;
    List<ArticleItem> articleItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArticleAdapter(this, articleItems);
        recyclerView.setAdapter(adapter);

        String url = getIntent().getStringExtra("url");
        if (url != null) loadArticle(url);
    }

    private void loadArticle(String url) {
        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(url).build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(ArticleActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful() || response.body() == null) {
                    runOnUiThread(() ->
                            Toast.makeText(ArticleActivity.this, "Failed to load article", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                String html = response.body().string();
                parseHtml(html);

                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }

    private void parseHtml(String html) {
        Document doc = Jsoup.parse(html);

        // VnExpress nội dung nằm trong <article>
        Element article = doc.selectFirst("article");

        if (article == null) {
            // fallback
            article = doc.body();
        }

        // Lấy tất cả text và ảnh theo thứ tự
        for (Element e : article.children()) {

            // Nếu là ảnh
            if (e.tagName().equals("figure")) {
                Element img = e.selectFirst("img[itemprop=contentUrl]");
                if (img != null) {
                    String src = img.attr("data-src");  // <--- lấy URL thật
                    if (src.isEmpty()) {
                        src = img.attr("src"); // fallback nếu không có data-src
                    }
                    if (!src.isEmpty()) {
                        articleItems.add(new ArticleItem(ArticleItem.TYPE_IMAGE, src));
                    }
                }
                continue;
            }

            // Nếu là đoạn text
            if (e.tagName().equals("p")) {
                String text = e.text().trim();
                if (!text.isEmpty()) {
                    articleItems.add(new ArticleItem(ArticleItem.TYPE_TEXT, text));
                }
            }
        }
    }
}