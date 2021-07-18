package com.shashank.mentalhealth.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.shashank.mentalhealth.Adapters.NewsAdapter;
import com.shashank.mentalhealth.R;
import com.shashank.mentalhealth.models.news;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        ListView newsView = findViewById(R.id.newsView);
        ProgressBar progressBar = findViewById(R.id.loading);
        SwipeRefreshLayout refresh = findViewById(R.id.refresh);
        progressBar.setVisibility(View.VISIBLE);
        newsView.animate().alpha(0.0f);
        ArrayList<news> newsArrayList = new ArrayList<>();
        NewsApiClient newsApiClient = new NewsApiClient("46ef00c8abd44573afd74929552f32f5");
// /v2/everything
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("Mental Health")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
//                        int total = response.getTotalResults();
                        int total = response.getArticles().size();
                        new Thread(() -> {
                            for (int  i = 0 ; i < total ; i++){
                                Article res = response.getArticles().get(i);
                                newsArrayList.add(new news(res.getTitle(), res.getDescription(), res.getUrlToImage(), res.getUrl()));
                            }
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                NewsAdapter adapter = new NewsAdapter(NewsActivity.this,newsArrayList);
                                newsView.setAdapter(adapter);
                                newsView.animate().alpha(1.0f).setDuration(1500);
                            });
                        }).start();

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsArrayList.clear();
                newsApiClient.getEverything(
                        new EverythingRequest.Builder()
                                .q("Mental Health")
                                .build(),
                        new NewsApiClient.ArticlesResponseCallback() {
                            @Override
                            public void onSuccess(ArticleResponse response) {
//                        int total = response.getTotalResults();
                                int total = response.getArticles().size();
                                new Thread(() -> {
                                    for (int  i = 0 ; i < total ; i++){
                                        Article res = response.getArticles().get(i);
                                        newsArrayList.add(new news(res.getTitle(), res.getDescription(), res.getUrlToImage(), res.getUrl()));
                                    }
                                    runOnUiThread(() -> {
                                        progressBar.setVisibility(View.GONE);
                                        NewsAdapter adapter = new NewsAdapter(NewsActivity.this,newsArrayList);
                                        newsView.setAdapter(adapter);
                                        newsView.animate().alpha(1.0f).setDuration(1500);
                                        refresh.setRefreshing(false);
                                    });
                                }).start();

                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                System.out.println(throwable.getMessage());
                            }
                        }
                );
            }
        });

        newsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                news n = (news) newsView.getAdapter().getItem(position);
                String url = n.getUrl();
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabColorSchemeParams darkParams = new CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(ContextCompat.getColor(NewsActivity.this, R.color.colorPrimary))
                        .build();
                builder.setDefaultColorSchemeParams(darkParams);
                CustomTabsIntent customTabsIntent = builder.build();
                try {
                    customTabsIntent.intent.setPackage("com.android.chrome");
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    customTabsIntent.launchUrl(NewsActivity.this, Uri.parse(url));
                }
            }
        });
    }
}