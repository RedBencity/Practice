package ben.practice.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import ben.practice.R;

//答题卡
public class AnswersActivity extends AppCompatActivity {

    private GridView answers_gridview;
    private int question_position_resultCode = 0x123;
    private Toolbar toolbar;
    private TextView submit_result_btn;
    private int[] results;
    private String point_name;
    private int[] right_results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        initView();
        getData();
    }

    private void initView() {
        answers_gridview = (GridView) findViewById(R.id.answers_gridview);
        setToolbar();
        submit_result_btn = (TextView) findViewById(R.id.submit_result_btn);
    }

    private void getData() {
        final Intent intent = getIntent();
        results = intent.getIntArrayExtra("results");
        point_name = intent.getStringExtra("point_name");
        if (intent.getIntArrayExtra("right_results") != null) {
            right_results = intent.getIntArrayExtra("right_results");
            System.out.println(right_results);
        }
        setGridView();
        submit_result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AnswersActivity.this, ResultActivity.class);
                intent1.putExtra("results", results);
                intent1.putExtra("point_name", point_name);
                QuestionActivity.instance.finish();
                startActivity(intent1);
                finish();
            }
        });
        if (right_results!=null){
            submit_result_btn.setVisibility(View.INVISIBLE);
        }
    }

    private void setToolbar() {
        View view = findViewById(R.id.answers_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsRelative(0, 0);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("答题卡");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
            }
        });
    }

    private void setGridView() {
        answers_gridview.setAdapter(new AnswersAdapter());
    }

    class AnswersAdapter extends BaseAdapter {

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
            convertView = LayoutInflater.from(AnswersActivity.this).inflate(R.layout.item_answers, null);
//            ImageView answers_position_bg = (ImageView) convertView.findViewById(R.id.answers_position_bg);
            TextView answers_position = (TextView) convertView.findViewById(R.id.answers_position);
            answers_position.setText((position + 1) + "");
            if (results[position] != 0) {
                answers_position.setBackgroundResource(R.mipmap.answer_btn_answered);
                answers_position.setTextColor(getResources().getColor(R.color.white));
            }

            if (right_results != null) {

                if (results[position] == right_results[position]) {
                    answers_position.setTextColor(getResources().getColor(R.color.white));
                    answers_position.setBackgroundResource(R.mipmap.answer_btn_right);
                } else if (results[position] != right_results[position] && results[position] != 0) {
                    answers_position.setTextColor(getResources().getColor(R.color.white));
                    answers_position.setBackgroundResource(R.mipmap.answer_btn_wrong);
                }


            }


            answers_position.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    intent.putExtra("question_position", position);
                    setResult(question_position_resultCode, intent);
                    AnswersActivity.this.finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);

                }

            });
            return convertView;
        }
    }
}
