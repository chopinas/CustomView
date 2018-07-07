package com.example.chopin.customview.youku;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

public class AnimationUtils {

    //旋转出去
    public static void rotateOutAnim(RelativeLayout layout,long Delaytime) {
        RotateAnimation ra= new RotateAnimation(0f,-180f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,1.0f);

        ra.setDuration(500);
        //停留在动画结束位置
        ra.setStartOffset(Delaytime);
        ra.setFillAfter(true);
        layout.startAnimation(ra);
    }

    //旋转进入
    public static void rotateInAnim(RelativeLayout layout,long Delaytime) {
        RotateAnimation ra= new RotateAnimation(-180f,0f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,1.0f);

        ra.setDuration(500);
        //停留在动画结束位置
        ra.setFillAfter(true);
        ra.setStartOffset(Delaytime);
        layout.startAnimation(ra);
    }


}
