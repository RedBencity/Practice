package ben.practice.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ben.practice.R;
import ben.practice.entity.History;
import ben.practice.utils.NetUtil;
import ben.practice.utils.Util;

public class HistoryActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    private Toolbar toolbar;
    private ListView history_listview;
    private ArrayList<History> arrayList = new ArrayList<History>();
    private ArrayList<History> histories = new ArrayList<History>();
    private HistoryAdapter historyAdapter;
    private boolean isLoading = false;
    private int remaining_history;
    private View listview_foot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        history_listview = (ListView) findViewById(R.id.history_listview);
        listview_foot = View.inflate(HistoryActivity.this, R.layout.listview_foot, null);
    }

    private void getData() {
        getHistory(this);
    }

    private void setToolbar() {
        View view = findViewById(R.id.history_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("历史上的今天");
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

    private void getHistory(final Activity activity) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String url = NetUtil.getHistoryURL(Util.patchZero(month) + Util.patchZero(day));
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("\\n", "\n");
                try {
                    History history;
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(jsonObject.getString("msg"));
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    System.out.println(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        history = new History();
                        JSONObject object = jsonArray.getJSONObject(i);
                        history.setDate(object.getString("date"));
                        history.setEvent(object.getString("event"));
                        arrayList.add(history);
                    }
                    for (int i = 0; i < 5; i++) {
                        histories.add(arrayList.get(i));
                    }
                    remaining_history = arrayList.size() - histories.size();
                    history_listview.addFooterView(listview_foot);
                    historyAdapter = new HistoryAdapter();
                    history_listview.setAdapter(historyAdapter);
                    history_listview.setOnScrollListener(HistoryActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.setToast(activity, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(stringRequest);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
//        Util.println(HistoryActivity.this,totalItemCount+" "+firstVisibleItem+" "+visibleItemCount);
        if (totalItemCount <= firstVisibleItem + visibleItemCount + 1 && isLoading == false) {
            isLoading = true;
            if (histories.size() < arrayList.size()) {
                new HistoryTask().execute(histories.size());
            } else {
                Util.setToast(HistoryActivity.this, "到底了！");
                history_listview.removeFooterView(listview_foot);
            }
        }
    }

    public class HistoryTask extends AsyncTask<Integer, String, List<History>> {
        @Override
        protected List<History> doInBackground(Integer... mark) {

            List<History> historyList = new ArrayList<History>();
            int margin = 5;
            if (remaining_history - margin >= 0) {
                remaining_history -= margin;
            } else {
                margin = remaining_history;
                remaining_history -= margin;
            }
            System.out.println(remaining_history + "  " + mark[0] + " " + margin);
            for (int i = mark[0]; i < mark[0] + margin; i++) {
                historyList.add(arrayList.get(i));
            }
            return historyList;
        }

        @Override
        protected void onPostExecute(List<History> historyList) {
            if (historyList.size() > 0) {
                histories.addAll(historyList);
                historyAdapter.notifyDataSetChanged();
            }
            isLoading = false;
        }

    }


    class HistoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return histories.size();
        }

        @Override
        public Object getItem(int position) {
            return histories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(HistoryActivity.this).inflate(R.layout.item_history, null);
            TextView date = (TextView) convertView.findViewById(R.id.history_date);
            TextView event = (TextView) convertView.findViewById(R.id.history_event);
            String dateStr = histories.get(position).getDate();
            String year = dateStr.substring(0, 4);
            String month = dateStr.substring(4, 6);
            String day = dateStr.substring(6, 8);
            date.setText(year + "年" + month + "月" + day + "日");
            event.setText(histories.get(position).getEvent());
            return convertView;
        }
    }


}


