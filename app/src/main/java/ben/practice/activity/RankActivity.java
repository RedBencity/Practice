package ben.practice.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
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
import ben.practice.adapter.ListAdapter;
import ben.practice.utils.NetUtil;
import ben.practice.utils.Util;

public class RankActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView rank_listview;
    private SharedPreferences preferences;
    private String[] nicknames;
    private String[] counts;
    private String[] phones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        preferences = getSharedPreferences("constants", MODE_PRIVATE);
    }

    private void getData(){
        rank_listview = (ListView)findViewById(R.id.rank_listview);
        getRank(this);
    }

    private void setToolbar() {
        View view = findViewById(R.id.rank_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("排行榜");
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

    private void getRank(final Activity activity ) {
        String url = NetUtil.URL + "/servlet/QuestionServer";
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                String[] strings = response.split("@");
                phones = new String[strings.length];
                nicknames = new String[strings.length];
                counts = new String[strings.length];
                for(int i=0;i< strings.length;i++){
                    String[] strs = strings[i].split("#");
                    phones[i] = strs[0];
                    nicknames[i] = strs[1];
                    counts[i] = strs[2];
                }
                rank_listview.setAdapter(new ListAdapter(RankActivity.this,phones,nicknames,counts));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.setToast(activity, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "getRank");
                map.put("phone", preferences.getString("phone", "defalut"));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }



}
