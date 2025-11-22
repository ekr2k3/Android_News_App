package com.example.myrssreaderapp;

import com.example.myrssreaderapp.models.RssFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RssService {
    @GET
    Call<RssFeed> getFeed(@Url String url);
}