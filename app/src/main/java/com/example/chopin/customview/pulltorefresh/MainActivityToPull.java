package com.example.chopin.customview.pulltorefresh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chopin.customview.R;

import java.util.ArrayList;

public class MainActivityToPull extends AppCompatActivity {
    PullToRefreshListView listView;
    ArrayList<String> test=new ArrayList<>();
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_to_pull);

        initView();

        initData();

        initAdapter();
    }

    private void initAdapter() {
        listView.setAdapter(adapter);
    }

    private void initData() {
        for(int i=0;i<50;i++){
            test.add("1008"+i);
        }
        adapter=new MyAdapter(test);
    }

    private void initView() {
        listView=findViewById(R.id.lv_pullListview);
        listView.setRefreshListner(new PullToRefreshListView.OnRefreshListner() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        test.add("new one");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                listView.onRefreshComlete();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void loadMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        test.add("加载更多");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                listView.onRefreshComlete();
                            }
                        });
                    }
                }).start();
            }
        });
    }
}

class MyAdapter extends BaseAdapter{
    ArrayList<String> test;

    MyAdapter(ArrayList<String> test){
        this.test=test;
    }

    @Override
    public int getCount() {
        return test.size();
    }

    @Override
    public Object getItem(int position) {
        return test.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= View.inflate(parent.getContext(),R.layout.item_pull_to_refresh,null);
        }
        TextView tv_info=convertView.findViewById(R.id.tv_refreshinfo);
        tv_info.setText("这是一条listView数据："+test.get(position));
        return convertView;
    }
}


