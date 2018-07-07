package com.example.chopin.customview.youku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.chopin.customview.R;
import com.example.chopin.customview.youku.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    ImageButton ib_home,ib_menu;
    RelativeLayout rl_level1,rl_level2,rl_level3;
    boolean islevel1Display,islevel2Display,islevel3Display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youkumenu);
        initView();

        ib_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(islevel2Display){
                        if (islevel3Display){
                            AnimationUtils.rotateOutAnim(rl_level3,0);
                            islevel3Display=false;
                            AnimationUtils.rotateOutAnim(rl_level2,200);
                        }else {
                            AnimationUtils.rotateOutAnim(rl_level2,0);

                        }
                        islevel2Display=false;
                    }else {
                        AnimationUtils.rotateInAnim(rl_level2,0);
                        AnimationUtils.rotateInAnim(rl_level3,200);
                        islevel2Display=true;
                        islevel3Display=true;
                    }
            }
        });

        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(islevel3Display){
                    AnimationUtils.rotateOutAnim(rl_level3,0);
                    islevel3Display=false;
                }else {
                    AnimationUtils.rotateInAnim(rl_level3,0);
                    islevel3Display=true;
                }
            }
        });
    }

    private void initView() {
        ib_home=findViewById(R.id.btn_home);
        ib_menu=findViewById(R.id.btn_mune);
        rl_level1=findViewById(R.id.rl_level1);
        rl_level2=findViewById(R.id.rl_level2);
        rl_level3=findViewById(R.id.rl_level3);
        islevel1Display=true;
        islevel2Display=true;
        islevel3Display=true;
    }
}
