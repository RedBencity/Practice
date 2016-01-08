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

public class AnswersActivity extends AppCompatActivity {

    private GridView answers_gridview;
    private int questionCount;
    private boolean[] isAnswers;
    private int question_position_resultCode = 0x123;
    private Toolbar toolbar;

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
    }

    private void getData() {
        Intent intent = getIntent();
        questionCount = intent.getIntExtra("questionCount", 1);
        isAnswers = intent.getBooleanArrayExtra("is_answers");
        setGridView();
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
            }
        });
    }

    private void setGridView() {
        answers_gridview.setAdapter(new AnswersAdapter());
    }

    class AnswersAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return isAnswers.length;
        }

        @Override
        public Object getItem(int position) {
            return isAnswers[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(AnswersActivity.this).inflate(R.layout.item_answers, null);
            ImageView answers_position_bg = (ImageView) convertView.findViewById(R.id.answers_position_bg);
            TextView answers_position = (TextView) convertView.findViewById(R.id.answers_position);
            answers_position.setText((position + 1) + "");
            if (isAnswers[position]) {
                answers_position_bg.setImageResource(R.mipmap.answer_btn_answered);
                answers_position.setTextColor(getResources().getColor(R.color.white));
            }
            answers_position_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    intent.putExtra("question_position", position);
                    setResult(question_position_resultCode, intent);
                    AnswersActivity.this.finish();
                }
            });
            return convertView;
        }
    }
}
