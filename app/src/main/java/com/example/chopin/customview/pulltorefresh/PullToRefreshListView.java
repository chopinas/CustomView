package com.example.chopin.customview.pulltorefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chopin.customview.R;

import java.text.SimpleDateFormat;

public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener{

    private View headView,footView;
    private int headHeight,footerHeight;//获取下拉框的高度
    public static final int PULL_TO_REFRESH = 0;// 下拉刷新
    public static final int RELEASE_REFRESH = 1;// 释放刷新
    public static final int REFRESHING = 2; // 刷新中
    private int currentState = PULL_TO_REFRESH; // 当前刷新模式
    private ImageView arrow;
    private ProgressBar pb;
    private boolean isLoadingMore=false;     //是否加载更多
    private TextView tv_fresh_state,tv_lasttime;
    private RotateAnimation rotateUpAnim,rotateDownAnim;
    private OnRefreshListner listner;

    public PullToRefreshListView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        initHeadView();
        initFooterView();
        initAnimation();

        setOnScrollListener(this);
    }

    private void initFooterView() {
        footView=View.inflate(getContext(),R.layout.pull_to_refresh_footerview,null );
        footView.measure(0,0);
        footerHeight=footView.getMeasuredHeight();
        footView.setPadding(0,0,0,-footerHeight);
        addFooterView(footView);
    }

    private void initHeadView() {
        headView= View.inflate(getContext(), R.layout.pull_to_refresh_headview,null);
        arrow=headView.findViewById(R.id.iv_pull_arrow);
        pb=headView.findViewById(R.id.pull_pb);
        tv_fresh_state=headView.findViewById(R.id.tv_pull_refreshState);
        tv_lasttime=headView.findViewById(R.id.tv_pull_refreshtime);
        //隐藏头布局
        headView.measure(0,0);
        headHeight=headView.getMeasuredHeight();
        headView.setPadding(0,-headHeight,0,0);
        addHeaderView(headView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
            float downY = 0,moveY;
        //判断滑动距离，给Header设置paddingTop
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY=ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY=ev.getY();

                int offset= (int) (moveY-downY);
                int paddingTop =(- headHeight + offset);
                if (paddingTop>200)
                    paddingTop=200;
                headView.setPadding(0, paddingTop, 0, 0);
                if(paddingTop >= 0 && currentState != RELEASE_REFRESH){// 头布局完全显示
                    // 切换成释放刷新模式
                    currentState = RELEASE_REFRESH;
                    updateHeader(); // 根据最新的状态值更新头布局内容
                }else if(paddingTop < 0 && currentState != PULL_TO_REFRESH){ // 头布局不完全显示
                    // 切换成下拉刷新模式
                    currentState = PULL_TO_REFRESH;
                    updateHeader(); // 根据最新的状态值更新头布局内容
                }
                break;
            case MotionEvent.ACTION_UP:
                if(currentState == PULL_TO_REFRESH){
//			- paddingTop < 0 不完全显示, 恢复
                    headView.setPadding(0, -headHeight, 0, 0);
                }else if(currentState == RELEASE_REFRESH){
//			- paddingTop >= 0 完全显示, 执行正在刷新...
                    headView.setPadding(0, 0, 0, 0);
                    currentState = REFRESHING;
                    updateHeader();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 初始化头布局的动画
     */
    private void initAnimation() {
        // 向上转, 围绕着自己的中心, 逆时针旋转0 -> -180.
        rotateUpAnim = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUpAnim.setDuration(300);
        rotateUpAnim.setFillAfter(true); // 动画停留在结束位置

        // 向下转, 围绕着自己的中心, 逆时针旋转 -180 -> -360
        rotateDownAnim = new RotateAnimation(-180f, -360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnim.setDuration(300);
        rotateDownAnim.setFillAfter(true); // 动画停留在结束位置

    }

    /**
     * 根据状态更新头布局内容
     */
    private void updateHeader() {
        switch (currentState) {
            case PULL_TO_REFRESH: // 切换回下拉刷新
                // 做动画, 改标题
                arrow.startAnimation(rotateDownAnim);
                tv_fresh_state.setText("下拉刷新");

                break;
            case RELEASE_REFRESH: // 切换成释放刷新
                // 做动画, 改标题
                arrow.startAnimation(rotateUpAnim);
                tv_fresh_state.setText("释放刷新");

                break;
            case REFRESHING: // 刷新中...
                arrow.clearAnimation();
                arrow.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);
                tv_fresh_state.setText("正在刷新中...");

                if(listner != null){
                    listner.onRefresh(); // 通知调用者, 让其到网络加载更多数据.

                }
                break;
            default:
                break;
        }
    }

    /**
     * 刷新结束，恢复界面效果
     */
    @SuppressLint("SetTextI18n")
    public void onRefreshComlete(){

        if (isLoadingMore){
            footView.setPadding(0,0,0,-footerHeight);
            isLoadingMore=false;
        }else {
            currentState=PULL_TO_REFRESH;
            tv_fresh_state.setText("下拉刷新");
            headView.setPadding(0,-headHeight,0,0);
            pb.setVisibility(View.INVISIBLE);
            arrow.setVisibility(View.VISIBLE);

            tv_lasttime.setText("最后刷新时间："+getTime());
        }

    }

    public String getTime(){
        long currentTimeMillis = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(currentTimeMillis);
    }


    /**
     *  public static int SCROLL_STATE_IDLE=0;//空闲
        public static int SCROLL_STATE_TOUCH_SCROLL=1;//触摸滑动
        public static int SCROLL_STATE_FLING=2;//滑翔
     */

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if(isLoadingMore){
            return;
        }


        if(scrollState==SCROLL_STATE_IDLE&&getLastVisiblePosition()>=(getCount()-1)){
             footView.setPadding(0,0,0,0);
             isLoadingMore=true;
             setSelection(getCount());//跳转到最后一条，使其显示出加载更多

            if(listner!=null){
                listner.loadMore();
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public interface OnRefreshListner{
        void onRefresh();
        void loadMore();
    }

    public void setRefreshListner(OnRefreshListner listner){
        this.listner=listner;
    }
}
