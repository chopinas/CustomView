package com.example.chopin.customview.viewpagerad;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chopin.customview.R;

import java.util.ArrayList;

public class ViewPagerAd extends AppCompatActivity {

    private ViewPager viewPager;
    private int[] img={R.drawable.aaaaaaaa,R.drawable.b,R.drawable.c
                        ,R.drawable.d,R.drawable.e};
    private String[] texts={"aaaaaaaa","bbbbbbb","ccccccc","dddddddd","fffffffff"};
    private TextView textView;
    private ArrayList<ImageView> imageViewArrayList=new ArrayList<>();
    private View pointView;
    private LinearLayout ll_point_layout;
    private Boolean isloop=true;


    @Override
    protected void onDestroy() {
        isloop=false;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_ad);

        initView();

        initData();

        initControll();

        //开启轮询播放
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isloop){
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initControll() {
        //设置第一个圆点为白色
        ll_point_layout.getChildAt(0).setEnabled(true);
        textView.setText(texts[0]);
        viewPager.setAdapter(new MyViewPagerAdapter(imageViewArrayList));

        //设置当前viewpager位置
        viewPager.setCurrentItem(2000);
    }

    private void initData() {
        LinearLayout.LayoutParams layoutParams = null;

        ImageView imageView = null;
        for (int i=0;i<img.length;i++){
            assert imageView != null;
            imageView=new ImageView(this);
            imageView.setImageResource(img[i]);
            imageViewArrayList.add(imageView);

            pointView=new View(this);
            pointView.setBackgroundResource(R.drawable.point_selector);
            layoutParams=new LinearLayout.LayoutParams(15,15);
            if (i!=0)
                layoutParams.leftMargin=20;
            pointView.setEnabled(false);
            ll_point_layout.addView(pointView,layoutParams);
        }
    }

    private void initView() {
        viewPager=findViewById(R.id.viewpager);
        textView=findViewById(R.id.tv_desc);

        ll_point_layout=findViewById(R.id.textlayout);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position=position%5;

                for(int i=0;i<img.length;i++){
                    if (position==i){
                        ll_point_layout.getChildAt(position).setEnabled(true);}
                    else{
                        ll_point_layout.getChildAt(i).setEnabled(false);}
                }




                textView.setText(texts[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

class MyViewPagerAdapter extends PagerAdapter{

    ArrayList<ImageView> arrayList;

    MyViewPagerAdapter(ArrayList<ImageView> list){
        this.arrayList=list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView iv=arrayList.get(position%5);
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
