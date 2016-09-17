package com.lalic.twmoments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.momentslist.TweetsBean;
import com.momentslist.TweetsListAdapter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.widgets.recycleviewwrap.PullRefresh;
import com.widgets.recycleviewwrap.PullableRecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PullableRecyclerView mMomentsList;
    private LinearLayoutManager mListManager;
    private TweetsListAdapter mAdapter;
    private PullRefresh mPullRefresh;

    private List<TweetsBean> mData;
    private List<TweetsBean> mCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageLoader(this);

        mCurrent = new ArrayList<>();
        if (initData()) {
            initView();
        } else {
            //空实现
        }
    }

    private boolean initData() {

        //读取静态数据
        InputStream is;
        try {
            is = getAssets().open("file.txt");
        } catch (IOException e) {
            Toast.makeText(this, "数据加载错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //FastJson解析数据
        try {
            mData = JSON.parseArray(sb.toString(), TweetsBean.class);
        } catch (Exception e) {
            Toast.makeText(this, "数据解析错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        List<TweetsBean> tmp = new ArrayList<>();
        for (TweetsBean tb : mData) {
            if (tb.getSender() == null || (tb.getContent() == null && tb.getImages() == null)) {

            } else {
                tmp.add(tb);
            }
        }
        mData = tmp;
        mCurrent.clear();
        for (int i = 0; i < 5; i++) {
            mCurrent.add(mData.get(i));
        }
        return true;
    }


    boolean loading = false;
    boolean hasMore=true;

    private void initView() {
        mMomentsList = (PullableRecyclerView) findViewById(R.id.moments_list);
        mPullRefresh = (PullRefresh) findViewById(R.id.pull_down);
        mPullRefresh.setIsAllowPullDown(mMomentsList);
        mPullRefresh.setOnPullDown(new PullRefresh.OnPullDownListener() {
            @Override
            public void onPullDown() {
                Toast.makeText(MainActivity.this, "刷新顶部5条", Toast.LENGTH_SHORT).show();
                mCurrent.clear();
                for (int i = 0; i < 5; i++) {
                    mCurrent.add(mData.get(i));
                }
                mAdapter.updateItems(mCurrent);
                hasMore=true;
//                mPullRefresh.notifyEnd();
            }
        });
        mListManager = new LinearLayoutManager(this);

        mMomentsList.setLayoutManager(mListManager);
        mMomentsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (!loading&&hasMore) {

                    loading=true;
                    int pos = mListManager.findLastCompletelyVisibleItemPosition();
                    if ((pos + 1) == mCurrent.size()) {
                        Log.e("Lalic","loading pos+1="+(pos+1)+"  mCurrent.size()="+mCurrent.size());
                        Toast.makeText(MainActivity.this, "自动加载5条", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < 5; i++) {
                            if (pos + 1 + i < mData.size()) {
                                mCurrent.add(mData.get(pos + 1 + i));
                            }else{
                                hasMore=false;
                                Toast.makeText(MainActivity.this, "没有更多", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        mAdapter.updateItems(mCurrent);
                    }
                    loading=false;
                }
            }
        });
        mAdapter = new TweetsListAdapter(this);
        mMomentsList.setAdapter(mAdapter);
        mAdapter.updateItems(mCurrent);
        View header = LayoutInflater.from(this).inflate(R.layout.header, null);

        View myImage = header.findViewById(R.id.my_avatar);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击我的头像", Toast.LENGTH_SHORT).show();
            }
        });
        View myProfile = header.findViewById(R.id.profile_image);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击moments顶部背景", Toast.LENGTH_SHORT).show();
            }
        });

        mMomentsList.addHeaderView(header);

    }

    public void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(2);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);

        config.memoryCache(new WeakMemoryCache());
        DisplayMetrics displyMetric = context.getResources().getDisplayMetrics();
        config.memoryCacheExtraOptions(displyMetric.widthPixels, displyMetric.heightPixels);
        config.memoryCacheSizePercentage(20);
        int memSize = getMemorySize();
        if (memSize > 90) {
            ImageSizeUtils.setMaxBitmapSize(new ImageSize(ImageSizeUtils.DEFAULT_MAX_BITMAP_DIMENSION * 7, ImageSizeUtils.DEFAULT_MAX_BITMAP_DIMENSION * 7));
        } else if (memSize > 120) {
            ImageSizeUtils.setMaxBitmapSize(new ImageSize(ImageSizeUtils.DEFAULT_MAX_BITMAP_DIMENSION * 10, ImageSizeUtils.DEFAULT_MAX_BITMAP_DIMENSION * 10));
        }
        ImageLoader.getInstance().init(config.build());
    }

    private int getMemorySize() {
        Context context = this;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        if (hasHoneycomb() && isLargeHeap(context)) {
            memoryClass = am.getLargeMemoryClass();
        }
        int memoryCacheSize = memoryClass;
        return memoryCacheSize;
    }

    private boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private boolean isLargeHeap(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
    }
}
