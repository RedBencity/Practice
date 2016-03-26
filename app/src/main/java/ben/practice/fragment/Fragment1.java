package ben.practice.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ben.practice.R;
import ben.practice.activity.SubjectActivity;
import ben.practice.activity.HistoryActivity;
import ben.practice.listener.OnFragmentInteractionListener;


public class Fragment1 extends Fragment {


    private OnFragmentInteractionListener mListener;
    private GridView gridView;
    private ViewPager viewPager;
    private ArrayList<View> pageViews;
    private ImageView[] smallDots;
    private ViewGroup smallDots_area;
    private ImageView smallDot;
    private int pageCount = 3;
    private int direction = 0;
    private boolean isrunning = true;

    final private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        gridView = (GridView) view.findViewById(R.id.subject_gridview);
        viewPager = (ViewPager) view.findViewById(R.id.head_message);
        smallDots_area = (ViewGroup) view.findViewById(R.id.small_dots);
        pageViews = new ArrayList<View>();
        setViewPager();
        setGridView();
        return view;
    }

    private void setViewPager() {
        for (int i = 0; i < pageCount; i++) {
            View view = null;
            if (i == 0) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.viewpager_head_message1, null);
                DateFormat df = new SimpleDateFormat("MMM-dd", Locale.ENGLISH);
                String[] date = df.format(new Date()).split("-");
                TextView month = (TextView) view.findViewById(R.id.month);
                TextView day = (TextView) view.findViewById(R.id.day);
                TextView wisdom = (TextView) view.findViewById(R.id.wisdom);
                month.setText(date[0]);
                day.setText(date[1]);
                wisdom.setText("少壮不努力，老大徒伤悲！");
            } else if (i == 1) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.viewpager_head_message2, null);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HistoryActivity.class);
                        startActivity(intent);
                    }
                });
            } else if (i == 2) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.viewpager_head_message3, null);
            }
            pageViews.add(view);
        }
        smallDots = new ImageView[pageViews.size()];
        for (int i = 0; i < pageViews.size(); i++) {
            smallDot = new ImageView(getActivity());
            smallDot.setLayoutParams(new ViewGroup.LayoutParams(15, 15));
            smallDot.setPadding(25, 25, 25, 25);
            smallDots[i] = smallDot;
            if (i == 0) {
                //默认选中第一张图片
                smallDots[i].setBackgroundResource(R.mipmap.page_indicator);
            } else {
                smallDots[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            }
            ImageView margin = new ImageView(getActivity());
            margin.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            smallDots_area.addView(smallDots[i]);
            smallDots_area.addView(margin);
        }

        viewPager.setAdapter(new QuestionAdapter());
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
        viewPager.setCurrentItem(pageCount * 10);

    }


    private void setGridView() {
        gridView.setAdapter(new SubjectAdapter());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        isrunning = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isrunning = true;
        new Thread() {
            @Override
            public void run() {
                while (isrunning) {
                    try {
                        sleep(4000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isrunning = false;
    }

    class SubjectAdapter extends BaseAdapter {

        private int[] pic_normal = {
                R.mipmap.icon_subject_yuwen, R.mipmap.icon_subject_shuxue, R.mipmap.icon_subject_yingyu
        };
        private int[] pic_pressed = {
                R.mipmap.icon_subject_yuwen_pressed, R.mipmap.icon_subject_shuxue_pressed, R.mipmap.icon_subject_yingyu_pressed
        };
        private String[] subject_name = {"语文", "数学", "英语"};

        @Override
        public int getCount() {
            return pic_normal.length;
        }

        @Override
        public Object getItem(int position) {
            return pic_normal[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_subject, null);
            ImageView imageView;
            TextView textView;
            imageView = (ImageView) convertView.findViewById(R.id.subject_pic);
            // imageView.setImageResource(pic_normal[position]);
            imageView.setBackgroundDrawable(newSelector(pic_normal[position], pic_pressed[position]));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("image", subject_name[position]);
                    Intent intent = new Intent(getActivity(), SubjectActivity.class);
                    intent.putExtra("subject_name", subject_name[position]);
                    startActivity(intent);

                }
            });
            textView = (TextView) convertView.findViewById(R.id.subject_name);
            textView.setText(subject_name[position]);
            return convertView;
        }
    }

    private StateListDrawable newSelector(int idNormal, int idPressed) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : getContext().getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : getContext().getResources().getDrawable(idPressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);

        stateListDrawable.addState(new int[]{}, normal);
        return stateListDrawable;
    }

    class QuestionAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (pageCount == 2) {
                return pageViews.size();
            } else if (pageCount >= 3) {
                return 200;
            } else {
                return 1;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
//            Log.i("isViewFromObject", (view == object)+" ");
            return view == object;
//            return true;
        }


        //向右先destroy后instant，向左先instant后destroy
        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
//            Log.i("destroyItem", position + 1 + "");
            if (pageCount == 2) {
                ((ViewPager) container).removeView(pageViews.get(position % pageViews.size()));
            }

        }

        ArrayList<View> check = new ArrayList<View>();

        //每次滑动的时候生成的组件
        //自己维护item，确保左右滑动不会出BUG
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            Log.i("instantiateItem", position + 1 + "");
            if (pageCount == 3) {
                if (direction == 1) {
                    check.remove(pageViews.get((position + 3) % pageViews.size()));
                    ((ViewPager) container).removeView(pageViews.get((position + 3) % pageViews.size()));
                }
                if (direction == 2) {
                    check.remove(pageViews.get((position - 3) % pageViews.size()));
                    ((ViewPager) container).removeView(pageViews.get((position - 3) % pageViews.size()));
                }
                if (!check.contains(pageViews.get(position % pageViews.size()))) {
                    check.add(pageViews.get(position % pageViews.size()));
                    ((ViewPager) container).addView(pageViews.get(position % pageViews.size()));
//                    Log.i("instantiateItem", "----------------------------------------------");
                }
                return pageViews.get(position % pageViews.size());
            } else if (pageCount == 2) {
                check.add(pageViews.get(position % pageViews.size()));
                ((ViewPager) container).addView(pageViews.get(position % pageViews.size()));
//                Log.i("instantiateItem", "----------------------------------------------");
            }
            return pageViews.get((position) % pageViews.size());
        }
    }

    /**
     * 指引页面改监听器
     */
    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        int lastPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            // Log.i("StateChanged",arg0+"");
            if (arg0 == 1) {
                lastPosition = currentPosition;
            } else if (arg0 == 2) {

            } else if (arg0 == 0) {
                if (lastPosition > currentPosition) {
//                   Log.i("onPageScrolled","left");
                    direction = 1;
                } else if (lastPosition < currentPosition) {
//                   Log.i("onPageScrolled","right");
                    direction = 2;
                }
            }

        }

        int currentPosition = 0;

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
            //  Log.i("onPageScrolled",arg0+"");
//            if(mark==arg0){
//                Log.i("onPageScrolled","right");
//            }else{
//                Log.i("onPageScrolled","left");
//            }
        }

        //在指引页面更改事件监听器(GuidePageChangeListener)中要确保在切换页面时下面的圆点图片也跟着改变
        @Override
        public void onPageSelected(int arg0) {
            // Log.i("onPageSelected",arg0+"");
            currentPosition = arg0;
            for (int i = 0; i < smallDots.length; i++) {
                smallDots[arg0 % smallDots.length]
                        .setBackgroundResource(R.mipmap.page_indicator);
                if (arg0 % smallDots.length != i) {
                    smallDots[i]
                            .setBackgroundResource(R.mipmap.page_indicator_focused);
                }
            }

        }
    }
}
