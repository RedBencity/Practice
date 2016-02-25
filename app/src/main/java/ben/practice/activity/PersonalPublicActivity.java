package ben.practice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ben.practice.R;

public class PersonalPublicActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView head_title;
    private RelativeLayout two_area;
    private TextView one_text,two_text;
    private EditText one_edit,two_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_public);
        initView();
        getData();
    }

    private void getData() {

        Intent intent = getIntent();
        if (intent.getStringExtra("style").equals("昵称")) {
            head_title.setText("修改昵称");
        } else if (intent.getStringExtra("style").equals("修改密码")) {
            head_title.setText("修改密码");
            two_area.setVisibility(View.VISIBLE);
            one_text.setText("原始密码：");
            two_text.setText("新的密码：");
            one_edit.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            two_edit.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            one_edit.setHint("请输入原始密码");
            two_edit.setHint("请输入新密码");

        }
    }

    private void initView() {
        setToolbar();
        two_area = (RelativeLayout)findViewById(R.id.two_area);
        one_text = (TextView)findViewById(R.id.one_text);
        two_text = (TextView)findViewById(R.id.two_text);
        one_edit = (EditText)findViewById(R.id.one_edit);
        two_edit = (EditText)findViewById(R.id.two_edit);


    }

    private void setToolbar() {
        View view = findViewById(R.id.public_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        head_title = (TextView) view.findViewById(R.id.head_title);
//        ImageView head_right_btn = (ImageView)view.findViewById(R.id.head_right_btn);
//        head_right_btn.setVisibility(View.VISIBLE);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setContentInsetsRelative(0, 0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
            }
        });
    }

}
