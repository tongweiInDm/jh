package com.tw.swaprefreshlistview;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "SwipeRefreshLayoutTest";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private LinearLayoutManager mLayoutManager;
    private Handler handler;
    private int lastVisibleItem;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        mSwipeRefreshWidget = findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorScheme(R.color.color1, R.color.color2,
                R.color.color3, R.color.color4);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        mRecyclerView = findViewById(android.R.id.list);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    if (!mSwipeRefreshWidget.isRefreshing()) {
                        Log.v(TAG, "setRefreshing true， trigger by bottom item.");
                        mSwipeRefreshWidget.setRefreshing(true);
                        // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshWidget.setRefreshing(false);
                                Log.v(TAG, "setRefreshing false， trigger by bottom item.");
                            }
                        }, 2000);
                        Toast.makeText(MainActivity.this, "滚到底了", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new SampleAdapter(this);
        mRecyclerView.setAdapter(adapter);

        // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
        handler.sendEmptyMessageDelayed(0, 3000);


    }

    @Override
    public void onRefresh() {
        Log.v(TAG, "onRefresh...");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshWidget.setRefreshing(false);
                Log.v(TAG, "setRefreshing false");
            }
        }, 2000);
    }
}
