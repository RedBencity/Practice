package ben.practice.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ben.practice.R;
import ben.practice.entity.Message;
import ben.practice.utils.Util;

public class MessageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView message_listview;
    private List<Message> messages;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        preferences = getSharedPreferences("messages", MODE_PRIVATE);
        editor = preferences.edit();
        message_listview = (ListView) findViewById(R.id.message_listview);

    }

    private void getData() {

        messages = new ArrayList<Message>();
        Message message;
        int size = preferences.getInt("size", 0);
        Util.println(MessageActivity.this,size);
        if (size != 0) {
            for (int i = size; i >0; i--) {
                message = new Message();
                message.setId(preferences.getString("id_" + i, "null"));
                message.setTitle(preferences.getString("title_" + i, "null"));
                message.setTime(preferences.getString("time_" + i, "null"));
                message.setContent(preferences.getString("content_" + i, "null"));
                messages.add(message);
            }
        }
        message_listview.setAdapter(new MessageAdapter());
        message_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private void setToolbar() {
        View view = findViewById(R.id.message_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("我的信息");
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

    class MessageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(MessageActivity.this).inflate(R.layout.item_message, null);
            TextView message_title = (TextView) convertView.findViewById(R.id.message_title);
            TextView message_time = (TextView) convertView.findViewById(R.id.message_time);
            TextView message_content = (TextView) convertView.findViewById(R.id.message_contente);
            message_title.setText(messages.get(position).getTitle());
            message_time.setText(messages.get(position).getTime());
            message_content.setText(messages.get(position).getContent());
            return convertView;
        }
    }
}
