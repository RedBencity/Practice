package ben.practice.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import ben.practice.R;

public class ResultActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView head_title;
    private int[] results;
    private  int[] right_result;
    private GridView result_gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();
        getData();
    }

    private void initView(){
        setToolbar();
        result_gridview = (GridView)findViewById(R.id.result_gridview);
        right_result = new int[]{1,2,3,4,1,2,3,4,1,2};
    }

    private void getData(){
        Intent intent = getIntent();
        results = intent.getIntArrayExtra("results");
        setGridView();

    }

    private void setToolbar(){
        View view = findViewById(R.id.result_toolbar);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsRelative(0, 0);
        head_title = (TextView)view.findViewById(R.id.head_title);
        head_title.setText("练习报告");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setGridView(){
        result_gridview.setAdapter(new ResultAdapter());
    }

    class ResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return results.length;
        }

        @Override
        public Object getItem(int position) {
            return results[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(ResultActivity.this).inflate(R.layout.item_result, null);
            TextView result_position = (TextView) convertView.findViewById(R.id.result_position);
            result_position.setText((position + 1) + "");
           if(results[position]==right_result[position]){
               result_position.setBackgroundResource(R.mipmap.answer_btn_right);
               result_position.setTextColor(getResources().getColor(R.color.white));
           }else if (results[position]==0){
               result_position.setBackgroundResource(R.mipmap.answer_btn_cant_be_answered);
               result_position.setTextColor(getResources().getColor(R.color.colorPrimary));
           }else if (results[position]!=right_result[position]){
               result_position.setBackgroundResource(R.mipmap.answer_btn_wrong);
               result_position.setTextColor(getResources().getColor(R.color.white));
           }

            return convertView;
        }
    }
}
