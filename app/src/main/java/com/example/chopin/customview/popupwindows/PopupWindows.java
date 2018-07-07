package com.example.chopin.customview.popupwindows;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chopin.customview.R;

import java.util.ArrayList;
import java.util.BitSet;

public class PopupWindows extends AppCompatActivity implements View.OnClickListener {

    private EditText et_input;
    private ListView listView;
    private ArrayList<String> tests=new ArrayList<>();
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_windows);

        initView();
    }

    private void initView() {
        ImageButton iv_drop = findViewById(R.id.ib_dropdown);
        et_input=findViewById(R.id.et_input);
        iv_drop.setOnClickListener(this);
        initListView();
    }

    @Override
    public void onClick(View v) {
        showPupwindow();
    }

    private void showPupwindow() {
        popupWindow=new PopupWindow(listView,et_input.getWidth(),600);
        listView.setAdapter(new ListAdater(tests,popupWindow));
        et_input.setText(tests.get(0));

        //设置可获取焦点
        popupWindow.setFocusable(true);
        //设置外部点击隐藏
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(et_input,0,0);
    }

    private void initListView() {
        listView=new ListView(this);
        for (int i=0;i<50;i++){
            tests.add("1008"+i);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_input.setText(tests.get(position));
                popupWindow.dismiss();
            }
        });
    }
}

class ListAdater extends BaseAdapter{
    ArrayList<String> test;
    PopupWindow popupWindow;

    ListAdater(ArrayList<String> test,PopupWindow popupWindow){
        this.test=test;
        this.popupWindow=popupWindow;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(parent.getContext(),R.layout.pup_item,null);
        }
        TextView tv_number=convertView.findViewById(R.id.tv_number);
        tv_number.setText(test.get(position));
        ImageButton imageButton=convertView.findViewById(R.id.ib_delete);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.remove(position);
                notifyDataSetChanged();
                if (test.size()==0){
                    popupWindow.dismiss();
                }
            }
        });
        return convertView;
    }
}