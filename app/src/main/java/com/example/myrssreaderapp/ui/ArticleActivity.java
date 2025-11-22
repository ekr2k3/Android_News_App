package com.example.myrssreaderapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.models.ArticleItem;
import com.example.myrssreaderapp.R;
import com.example.myrssreaderapp.adapter.ArticleAdapter;
import com.example.myrssreaderapp.models.BookmarkItem;
import com.example.myrssreaderapp.sql.BookmarkManager;

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

    // Day 6
    private ImageButton btnSaveArticle;
    private boolean isSaved = false;
    private String articleUrl;
    private BookmarkManager bookmarkManager;
    private int currentUserId;
    // End Day 6

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        //Day 6
//        currentUserId = getIntent().getIntExtra("userId", -1); // Cái này là từ MainActivity chuyển qua
//        if(currentUserId == -1){ finish(); return; }
        //End day 6

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArticleAdapter(this, articleItems);
        recyclerView.setAdapter(adapter);

        String url = getIntent().getStringExtra("url");
        if (url != null) loadArticle(url);

        btnSaveArticle = findViewById(R.id.btnSaveArticle);

        //Day 6
//        btnSaveArticle = findViewById(R.id.btnSaveArticle);
        articleUrl = getIntent().getStringExtra("url");
        bookmarkManager = new BookmarkManager(this);
        BookmarkItem bookmark = new BookmarkItem(articleUrl, currentUserId);

        isSaved = bookmarkManager.isBookmarked(bookmark);
        updateBookmarkIcon();

        btnSaveArticle.setOnClickListener(v -> {
            if(isSaved){
                bookmarkManager.removeBookmark(bookmark);
                isSaved = false;
            } else {
                bookmarkManager.addBookmark(bookmark);
                isSaved = true;
            }
            updateBookmarkIcon();
        });
        //End day 6
    }

    //Day 6
    private void updateBookmarkIcon(){
        if(isSaved) btnSaveArticle.setImageResource(R.drawable.ic_bookmark_filled);
        else btnSaveArticle.setImageResource(R.drawable.ic_bookmark_outline);
    }
    //end day 6
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