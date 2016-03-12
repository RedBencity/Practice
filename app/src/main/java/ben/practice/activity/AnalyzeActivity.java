package ben.practice.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ben.practice.R;
import ben.practice.entity.Question;

public class AnalyzeActivity extends AppCompatActivity implements View.OnClickListener {


    private ViewPager viewPager;
    private ArrayList<View> pageViews;
    private Toolbar toolbar;
    private ImageView bar_answers;
    private ImageView bar_scratch;
    private int[] results;
    private int answers_requestCode = 0x123;
    private int question_position_resultCode = 0x123;
    private String point_name;
    private int position;
    private int[] right_results;
    private String[] analyzes;
    private ArrayList<Question> questionArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        pageViews = new ArrayList<View>();
        viewPager = (ViewPager) findViewById(R.id.viewpager);


    }

    private void getData() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        results = intent.getIntArrayExtra("results");
        questionArrayList = intent.getParcelableArrayListExtra("questionArrayList");
        point_name = questionArrayList.get(0).getPoint();
        analyzes = new String[questionArrayList.size()];
        right_results = new int[questionArrayList.size()];
        for (int i = 0; i < questionArrayList.size(); i++) {
            analyzes[i] = questionArrayList.get(i).getAnalyze();
            if (questionArrayList.get(i).getAnswer().equals("A")) {
                right_results[i] = 1;
            } else if (questionArrayList.get(i).getAnswer().equals("B")) {
                right_results[i] = 2;
            } else if (questionArrayList.get(i).getAnswer().equals("C")) {
                right_results[i] = 3;
            } else if (questionArrayList.get(i).getAnswer().equals("D")) {
                right_results[i] = 4;
            }
        }
        setViewPager();
        viewPager.setCurrentItem(position);

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.bar_question_more));
        setSupportActionBar(toolbar);
        bar_scratch = (ImageView) findViewById(R.id.bar_scratch);
        bar_scratch.setOnClickListener(this);
        bar_answers = (ImageView) findViewById(R.id.bar_answers);
        bar_answers.setOnClickListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.collect_item:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == answers_requestCode && resultCode == question_position_resultCode) {
            int position = data.getIntExtra("question_position", 0);
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setViewPager() {
        for (int i = 0; i < results.length + 1; i++) {
            if (i != results.length) {
                View view = LayoutInflater.from(this).inflate(R.layout.viewpager_question_card, null);
                TextView question_position = (TextView) view.findViewById(R.id.question_position);
                TextView question_total = (TextView) view.findViewById(R.id.question_total);
                TextView paractice_style = (TextView) view.findViewById(R.id.practice_style);
                paractice_style.setText(point_name);
                question_position.setText(i + 1 + "");
                question_total.setText("/" + results.length);
                pageViews.add(view);
            } else if (i == results.length) {
                View view = LayoutInflater.from(this).inflate(R.layout.viewpager_analyze_return, null);
                Button analyze_return_analyze_btn = (Button) view.findViewById(R.id.analyze_return_analyze_btn);
                Button analyze_return_subject_btn = (Button) view.findViewById(R.id.analyze_return_subject_btn);
                analyze_return_analyze_btn.setOnClickListener(this);
                analyze_return_subject_btn.setOnClickListener(this);

                pageViews.add(view);
            }
        }
        viewPager.setAdapter(new QuestionAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_answers:
                if (currentPosition != results.length) {
                    Intent intent = new Intent(AnalyzeActivity.this, AnswersActivity.class);
                    intent.putExtra("results", results);
                    intent.putExtra("point_name", point_name);
                    intent.putParcelableArrayListExtra("questionArrayList_from_analyze",questionArrayList);
                    startActivityForResult(intent, answers_requestCode);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
                } else if (currentPosition == results.length) {

                }
                break;
            case R.id.bar_scratch:
                if (currentPosition != results.length) {
                    Intent intent = new Intent(AnalyzeActivity.this, ScratchActivity.class);
                    startActivity(intent);
                } else if (currentPosition == results.length) {

                }
                break;
            case R.id.analyze_return_analyze_btn:
                finish();
                break;
            case R.id.analyze_return_subject_btn:
                Intent intent = new Intent(AnalyzeActivity.this, SubjectActivity.class);
                intent.putExtra("subject_name",questionArrayList.get(0).getSubject());
                startActivity(intent);
                finish();
                break;

        }
    }

    private int currentPosition;

    class QuestionAdapter extends PagerAdapter {

        private TextView question_title, option_a, option_b, option_c, option_d;

        private LinearLayout analyze_area;
        private TextView analyze_text;
        private TextView option_a_content, option_b_content, option_c_content, option_d_content;


        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            ((ViewPager) container).removeView(pageViews.get(position));
        }

        //每次滑动的时候生成的组件
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(pageViews.get(position));
            return pageViews.get(position);
        }


        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentPosition = position;
            View view = (View) object;
            if (position != results.length) {
                bar_scratch.setImageResource(R.drawable.selector_bar_scratch);
                bar_answers.setImageResource(R.drawable.selector_bar_answers);
                question_title = (TextView) view.findViewById(R.id.question_title);
                option_a = (TextView) view.findViewById(R.id.option_a);
                option_b = (TextView) view.findViewById(R.id.option_b);
                option_c = (TextView) view.findViewById(R.id.option_c);
                option_d = (TextView) view.findViewById(R.id.option_d);
                analyze_area = (LinearLayout) view.findViewById(R.id.analyze_area);
                analyze_text = (TextView) view.findViewById(R.id.analyze_text);
                analyze_area.setVisibility(View.VISIBLE);
                option_a_content = (TextView) view.findViewById(R.id.option_a_content);
                option_b_content = (TextView) view.findViewById(R.id.option_b_content);
                option_c_content = (TextView) view.findViewById(R.id.option_c_content);
                option_d_content = (TextView) view.findViewById(R.id.option_d_content);
                question_title.setText(questionArrayList.get(position).getTitle());
                option_a_content.setText(questionArrayList.get(position).getA());
                option_b_content.setText(questionArrayList.get(position).getB());
                option_c_content.setText(questionArrayList.get(position).getC());
                option_d_content.setText(questionArrayList.get(position).getD());

                switch (right_results[position]) {
                    case 1:
                        option_a.setTextColor(getResources().getColor(R.color.white));
                        option_a.setBackgroundResource(R.mipmap.answer_btn_right);
                        break;
                    case 2:
                        option_b.setTextColor(getResources().getColor(R.color.white));
                        option_b.setBackgroundResource(R.mipmap.answer_btn_right);
                        break;
                    case 3:
                        option_c.setTextColor(getResources().getColor(R.color.white));
                        option_c.setBackgroundResource(R.mipmap.answer_btn_right);
                        break;
                    case 4:
                        option_d.setTextColor(getResources().getColor(R.color.white));
                        option_d.setBackgroundResource(R.mipmap.answer_btn_right);
                        break;

                }


                if (results[position] != right_results[position] && results[position] != 0) {
                    switch (results[position]) {
                        case 1:
                            option_a.setTextColor(getResources().getColor(R.color.white));
                            option_a.setBackgroundResource(R.mipmap.answer_btn_wrong);
                            break;
                        case 2:
                            option_b.setTextColor(getResources().getColor(R.color.white));
                            option_b.setBackgroundResource(R.mipmap.answer_btn_wrong);
                            break;
                        case 3:
                            option_c.setTextColor(getResources().getColor(R.color.white));
                            option_c.setBackgroundResource(R.mipmap.answer_btn_wrong);
                            break;
                        case 4:
                            option_d.setTextColor(getResources().getColor(R.color.white));
                            option_d.setBackgroundResource(R.mipmap.answer_btn_wrong);
                            break;
                    }
                }
                analyze_text.setText(analyzes[position]);

            } else if (position == results.length) {

                bar_scratch.setImageResource(R.mipmap.bar_scratch_disable);
                bar_answers.setImageResource(R.mipmap.bar_answers_disable);
            }
        }


    }


}
