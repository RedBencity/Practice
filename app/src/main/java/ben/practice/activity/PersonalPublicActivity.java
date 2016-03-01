package ben.practice.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import ben.practice.R;
import ben.practice.utils.NetUtil;
import ben.practice.utils.Util;

public class PersonalPublicActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView head_title;
    private RelativeLayout two_area;
    private TextView one_text, two_text;
    private EditText one_edit, two_edit;
    private Button personal_public_btn;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


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
            personal_public_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateNickname();
                    setResult(0x123);
                    finish();
                }
            });
        } else if (intent.getStringExtra("style").equals("修改密码")) {
            head_title.setText("修改密码");
            two_area.setVisibility(View.VISIBLE);
            one_text.setText("原始密码：");
            two_text.setText("新的密码：");
            one_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            two_edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            one_edit.setHint("请输入原始密码");
            two_edit.setHint("请输入新密码");
            personal_public_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePassword();

                }
            });
        }
    }

    private void initView() {
        setToolbar();
        two_area = (RelativeLayout) findViewById(R.id.two_area);
        one_text = (TextView) findViewById(R.id.one_text);
        two_text = (TextView) findViewById(R.id.two_text);
        one_edit = (EditText) findViewById(R.id.one_edit);
        two_edit = (EditText) findViewById(R.id.two_edit);
        personal_public_btn = (Button) findViewById(R.id.personal_public_btn);
        preferences = getSharedPreferences("constants", MODE_PRIVATE);
        editor = preferences.edit();


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

    private void updateNickname() {
        String url = NetUtil.URL + "/servlet/PersonalPublicServer";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                editor.putString("nickname", one_edit.getText().toString());
                editor.commit();
                Util.setToast(PersonalPublicActivity.this, "更改成功！!");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.setToast(PersonalPublicActivity.this, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "updateNickname");
                map.put("phone", preferences.getString("phone", "defalut"));
                map.put("nickname",one_edit.getText().toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void updatePassword(){
        String url = NetUtil.URL + "/servlet/PersonalPublicServer";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                if (Boolean.parseBoolean(response)) {
                    Util.setToast(PersonalPublicActivity.this, "更改成功！!");
                    setResult(0x124);
                    finish();
                }else {
                    Util.setToast(PersonalPublicActivity.this, "原密码不对！!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.setToast(PersonalPublicActivity.this, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "updatePassword");
                map.put("phone", preferences.getString("phone", "defalut"));
                map.put("oldPassword",one_edit.getText().toString());
                map.put("newPassword",two_edit.getText().toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}
