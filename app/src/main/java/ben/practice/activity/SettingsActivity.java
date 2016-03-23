package ben.practice.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ben.practice.MainActivity;
import ben.practice.R;
import ben.practice.entity.Message;
import ben.practice.utils.NetUtil;
import ben.practice.utils.Util;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView settings_listview;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        preferences = getSharedPreferences("constants", MODE_PRIVATE);
        editor = preferences.edit();
        settings_listview = (ListView) findViewById(R.id.settings_listview);
        items = new String[]{"检测更新", "关于"};

    }

    private void getData() {
        settings_listview.setAdapter(new SettingsAdapter());
        settings_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.settings_title);
                Intent intent ;
                if (textView.getText().equals(items[0])) {
                    versionUpdate();
                } else if (textView.getText().equals(items[1])) {
                   intent = new Intent(SettingsActivity.this,AboutActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setToolbar() {
        View view = findViewById(R.id.settings_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("我的设置");
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

    private void versionUpdate() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetUtil.URL + "/servlet/SettingsServer";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                if (response.equals("update")) {
                    Util.setToast(SettingsActivity.this, "开始更新！");
                    new Thread() {
                        public void run() {
                            try {
                                File file = getFileFromServer(NetUtil.downloadApk_URL);
                                Intent intent = new Intent();
                                //执行动作
                                intent.setAction(Intent.ACTION_VIEW);
                                //执行的数据类型
                                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else if (response.equals("no")) {
                    Util.setToast(SettingsActivity.this, "已是最新版本！");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.setToast(SettingsActivity.this, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "versionUpdate");
                map.put("code",Util.getAppVersionName(SettingsActivity.this));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public File getFileFromServer(String path) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            Util.println(SettingsActivity.this, conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            Util.println(SettingsActivity.this, Environment.getExternalStorageDirectory());
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    class SettingsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(SettingsActivity.this).inflate(R.layout.item_settings, null);
            TextView settings_title = (TextView) convertView.findViewById(R.id.settings_title);
            settings_title.setText(items[position]);
            return convertView;
        }
    }
}
