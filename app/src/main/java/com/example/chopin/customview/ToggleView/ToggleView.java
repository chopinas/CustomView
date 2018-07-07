package com.example.chopin.customview.ToggleView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 */
public class ToggleView extends View {
    private Bitmap switchBackgBitmap;
    private Bitmap slideBackgBitmap;
    Paint paint;

    private int slideButtonResource;
    private boolean switchState=false;
    private int switchBackgroundResource;
    private boolean isTuchMode=false;
    private float currtentX;
    boolean currentState=false;
    private OnSwitchStateUpdateListener onSwitchStateUpdateListener;

    /**
     * 用于代码创建控件
     * @param context
     */
    public ToggleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint=new Paint();
    }

    /**
     * 用于xml中使用，指定自定义属性
     * @param context
     */
    public ToggleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        String namespace="http://schemas.android.com/apk/res/com.example.chopin.customview";
        int switchBackgroundResource=attrs.getAttributeResourceValue(namespace,"switch_background",-1);
        int slideButtonResource = attrs.getAttributeResourceValue(namespace , "slide_button", -1);
        switchState=attrs.getAttributeBooleanValue(namespace,"switch_state",false);

        this.setSwitchBackgroundResource(switchBackgroundResource);
        this.setSlideButtonResource(slideButtonResource);
    }
    /**
     * 用于xml中使用，指定自定义属性，指定了样式
     * @param context
     */
    public ToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setSwitchBackgroundResource(int switchBackgroundResource) {
        this.switchBackgroundResource = switchBackgroundResource;
        switchBackgBitmap=BitmapFactory.decodeResource(getResources(),switchBackgroundResource);
    }

    public void setSlideButtonResource(int slideButtonResource) {
        this.slideButtonResource = slideButtonResource;
        slideBackgBitmap=BitmapFactory.decodeResource(getResources(),slideButtonResource);
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTuchMode=true;
                currtentX=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currtentX=event.getX();
                if(currtentX<0){
                    currtentX=0;
                }
                if(currtentX>switchBackgBitmap.getWidth()-slideBackgBitmap.getWidth()){
                    currtentX=switchBackgBitmap.getWidth()-slideBackgBitmap.getWidth();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                currtentX=event.getX();
                if(currtentX<switchBackgBitmap.getWidth()/2-slideBackgBitmap.getWidth()/2)
                    currentState=false;
                if (currtentX>switchBackgBitmap.getWidth()/2-slideBackgBitmap.getWidth()/2)
                    currentState=true;
                if (currentState!=switchState&&onSwitchStateUpdateListener!=null){
                    switchState=currentState;
                    onSwitchStateUpdateListener.onStateUpdate(currentState);
                }
                invalidate();
                isTuchMode=false;
                 break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(switchBackgBitmap.getWidth(),switchBackgBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(switchBackgBitmap,0,0,paint);
        if(isTuchMode){
            canvas.drawBitmap(slideBackgBitmap,currtentX,0,paint);
        }else {
            if (switchState){
                canvas.drawBitmap(slideBackgBitmap,switchBackgBitmap.getWidth()-slideBackgBitmap.getWidth()
                        ,0,paint);
            }else{
                canvas.drawBitmap(slideBackgBitmap,0,0,paint);
            }
        }
    }

    public void setSwitchStateUpdateListener(OnSwitchStateUpdateListener onSwitchStateUpdateListener) {
          this.onSwitchStateUpdateListener=onSwitchStateUpdateListener;
    }

    public interface OnSwitchStateUpdateListener{
        void onStateUpdate(boolean state);
    }

}
