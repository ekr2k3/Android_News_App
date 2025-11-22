package com.example.myrssreaderapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.ApiClient;
import com.example.myrssreaderapp.R;
import com.example.myrssreaderapp.RssService;
import com.example.myrssreaderapp.adapter.NewsAdapter;
import com.example.myrssreaderapp.models.Item;
import com.example.myrssreaderapp.models.RssFeed;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    List<Item> items = new ArrayList<>();

    private static final String BASE_URL = "https://vnexpress.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        // Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer Toggle (hamburger menu)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Day 6
        int currentUserId = getIntent().getIntExtra("userId", -1);
        if(currentUserId == -1){
            // Chưa đăng nhập → quay về LoginActivity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // kết thúc MainActivity hiện tại
            return; // thoát hàm
        }
        //End Day 6


//        adapter = new NewsAdapter(this, items);
        adapter = new NewsAdapter(this, items, currentUserId);
        recyclerView.setAdapter(adapter);

//        loadRss();
        // RSS mặc định khi ở trang tin tức (luôn cố định)
        String x = "https://vnexpress.net/rss/tin-moi-nhat.rss";
        loadRss(x);


        
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

//    private void loadRss(){
//        RssService service = ApiClient.getClient(BASE_URL).create(RssService.class);
//
//        service.getFeed().enqueue(new Callback<RssFeed>() {
//            @Override
//            public void onResponse(Call<RssFeed> call, Response<RssFeed> response){
//                if(response.isSuccessful() && response.body()!=null){
//                    items.clear();
//                    items.addAll(response.body().getChannel().getItems());
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RssFeed> call, Throwable t){
//                Toast.makeText(MainActivity.this,
//                        "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
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
            Toast.makeText(MainActivity.this,
                    "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

//        switch(id){
//            case R.id.nav_home:
//                Toast.makeText(this,"Trang chủ",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.nav_category:
//                Toast.makeText(this,"Chuyên mục",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.nav_search:
//                Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.nav_bookmark:
//                Toast.makeText(this,"Bookmark",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.nav_settings:
//                Toast.makeText(this,"Cài đặt",Toast.LENGTH_SHORT).show();
//                break;
//        }
        if (id == R.id.nav_home) {
            Toast.makeText(this,"Trang chủ",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_category) {
            int currentUserId = getIntent().getIntExtra("userId", -1);
            if(currentUserId == -1){
                // Chưa đăng nhập → quay về LoginActivity
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish(); // kết thúc MainActivity hiện tại
                return true; // thoát hàm
            }
            Intent intent = new Intent(this, CategoryActivity.class);
            intent.putExtra("userId", currentUserId);
            startActivity(intent);
        } else if (id == R.id.nav_search) {

            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            int currentUserId = getIntent().getIntExtra("userId", -1);
            if(currentUserId == -1){
                // Chưa đăng nhập → quay về LoginActivity
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish(); // kết thúc MainActivity hiện tại
                return true; // thoát hàm
            }

            intent.putExtra("items", new Gson().toJson(items));  // CHUYỂN LIST SANG DẠNG JSON
            startActivity(intent);
        } else if (id == R.id.nav_bookmark) {
//            Toast.makeText(this,"Bookmark",Toast.LENGTH_SHORT).show();

            int currentUserId = getIntent().getIntExtra("userId", -1);
            if(currentUserId == -1){
                // Chưa đăng nhập → quay về LoginActivity
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish(); // kết thúc MainActivity hiện tại
                return true; // thoát hàm
            }
            Intent intent = new Intent(this, BookmarkActivity.class);
            intent.putExtra("userId", currentUserId);


            // In thử ra để kiểm tra giá trị của currentUserId
            String currentUserIdString = String.valueOf(currentUserId);
            Toast.makeText(this,currentUserIdString,Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this,"Cài đặt",Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
