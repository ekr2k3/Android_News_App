package com.example.myrssreaderapp.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrssreaderapp.R;
import com.example.myrssreaderapp.adapter.BookmarkAdapter;
import com.example.myrssreaderapp.models.BookmarkItem;
import com.example.myrssreaderapp.sql.BookmarkManager;

import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookmarkAdapter adapter;
    private BookmarkManager bookmarkManager;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);


        currentUserId = getIntent().getIntExtra("userId", -1);


        // In thử ra để kiểm tra giá trị của currentUserId
        String currentUserIdString = String.valueOf(currentUserId);
        Toast.makeText(this,"Form BorkMarkActivity" + currentUserIdString,Toast.LENGTH_SHORT).show();



        if(currentUserId == -1){ finish(); return; }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookmarkManager = new BookmarkManager(this);
        List<BookmarkItem> list = bookmarkManager.getBookmarksForUser(currentUserId);

        if(list.size() > 0){
            System.out.println(list.get(0));
        }
        else{
            System.out.println("Khong co du lieu"); //  ở đây có lỗi
        }
        // Adapter
        adapter = new BookmarkAdapter(list, this, bookmarkManager);
        recyclerView.setAdapter(adapter);
    }
}

