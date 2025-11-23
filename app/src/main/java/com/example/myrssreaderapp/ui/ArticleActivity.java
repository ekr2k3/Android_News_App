package com.example.myrssreaderapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.DataHelper.DatabaseHelper;
import com.example.myrssreaderapp.adapter.CommentAdapter;
import com.example.myrssreaderapp.models.ArticleItem;
import com.example.myrssreaderapp.R;
import com.example.myrssreaderapp.adapter.ArticleAdapter;
import com.example.myrssreaderapp.models.BookmarkItem;
import com.example.myrssreaderapp.models.Comment;

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

    // Day 7
    private RecyclerView rvComments;
    private EditText edtNewComment;
    private Button btnPostComment;
    private CommentAdapter commentAdapter;
    private DatabaseHelper dbHelper;

    // End Day 7
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArticleAdapter adapter;
    List<ArticleItem> articleItems = new ArrayList<>();

    // Day 6
    private ImageButton btnSaveArticle;
    private boolean isSaved = false;
//    private DatabaseHelper dbHelper = new DatabaseHelper(this);

    // Cái này là từ NewsAdapter chuyển qua hặc từ BookMarkAdapter chuyển qua
//        intent.putExtra("userId", currentUserId);
//        intent.putExtra("url", it.getLink());
//        intent.putExtra("title", it.getTitle());
//        intent.putExtra("description", it.getDescription());
//        intent.putExtra("imageUrl", imageUrl);

    private int currentUserId;
    String title;
    String description;
    String imageUrl;
    String articleUrl;
    // End Day 6

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        dbHelper = new DatabaseHelper(this);



        //Day 6
        currentUserId = getIntent().getIntExtra("userId", -1);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        imageUrl = getIntent().getStringExtra("imageUrl");
        articleUrl = getIntent().getStringExtra("url");


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

        BookmarkItem bookmark = new BookmarkItem(articleUrl, currentUserId, title, description, imageUrl);

        isSaved = dbHelper.isBookmarked(currentUserId, articleUrl);

        updateBookmarkIcon();

        btnSaveArticle.setOnClickListener(v -> {
            if(isSaved){
                dbHelper.deleteBookmark(bookmark.getUserId(), bookmark.getUrl());
                isSaved = false;
            } else {
                // In thử ra để kiểm tra giá trị của currentUserId
                String currentUserIdString = String.valueOf(bookmark.getUserId());
                Toast.makeText(this,"Gia tri id user tu ArticleActivity" + currentUserIdString,Toast.LENGTH_SHORT).show();
                dbHelper.addBookmark(bookmark.getUserId(), bookmark.getUrl(), bookmark.getTitle(), bookmark.getDescription(), bookmark.getImageUrl());
                isSaved = true;
            }
            updateBookmarkIcon();
        });
        //End day 6


        // Day 7
        rvComments = findViewById(R.id.rvComments);
        edtNewComment = findViewById(R.id.edtNewComment);
        btnPostComment = findViewById(R.id.btnPostComment);

//        dbHelper = new DatabaseHelper(this); Đẩy leen trên

        // lấy articleUrl từ Intent
        articleUrl = getIntent().getStringExtra("url");

        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(new ArrayList<>());
        rvComments.setAdapter(commentAdapter);

        loadComments();

        btnPostComment.setOnClickListener(v -> {
            String content = edtNewComment.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy email nào đó — nếu bạn có user login thì lấy email user
//            String email = "guest@example.com";

            String email = dbHelper.getEmailById(currentUserId);
            Comment comment = new Comment(articleUrl, email, content);
            long id = dbHelper.addComment(comment);
            if (id > 0) {
                comment.setIdComment((int) id);
                commentAdapter.addComment(comment);
                rvComments.scrollToPosition(0);
                edtNewComment.setText("");
                Toast.makeText(this, "Đã gửi bình luận", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gửi thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        // End day 7
    }

    // Day 7
    private void loadComments() {
        List<Comment> list = dbHelper.getCommentsByArticle(articleUrl);
        commentAdapter.setComments(list);
    }
    // End Day 7
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