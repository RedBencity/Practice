package ben.practice.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;

import ben.practice.R;
import ben.practice.activity.SubjectActivity;
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
    private int direction =0;
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
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewpager_head_message, null);
            TextView textView = (TextView) view.findViewById(R.id.aaa);
            textView.setText(i + 1 + "");
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
            margin.setLayoutParams(new ViewGroup.LayoutParams(20,20));
            smallDots_area.addView(smallDots[i]);
            smallDots_area.addView(margin);
        }
        viewPager.setAdapter(new QuestionAdapter());
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
        viewPager.setCurrentItem(pageCount*10);
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
                    intent.putExtra("subject_name",subject_name[position]);
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
            return 200;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
//            Log.i("destroyItem", position + 1 + "");
           // ((ViewPager) container).removeView(pageViews.get(position % pageViews.size()));
        }

        //每次滑动的时候生成的组件
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            Log.i("instantiateItem", position + 1 + "");
//            ((ViewPager) container).addView(pageViews.get((position % pageViews.size())));
            ViewGroup parent = (ViewGroup) pageViews.get((position % pageViews.size())).getParent();
            if (parent != null ) {
                parent.removeAllViews();
            }
            container.addView(pageViews.get((position % pageViews.size())));

            return pageViews.get((position % pageViews.size()));
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
            if (arg0==1){
                  lastPosition =currentPosition;
            }else if (arg0==2){

            }else if(arg0==0){
               if(lastPosition >currentPosition){
//                   Log.i("onPageScrolled","left");
                   direction=1;
               }else if (lastPosition <currentPosition){
//                   Log.i("onPageScrolled","right");
                   direction =2;
               }
            }

        }
        int currentPosition =0;
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
            currentPosition =arg0;
            for (int i = 0; i < smallDots.length; i++) {
                smallDots[arg0%smallDots.length]
                        .setBackgroundResource(R.mipmap.page_indicator);
                if (arg0%smallDots.length != i) {
                    smallDots[i]
                            .setBackgroundResource(R.mipmap.page_indicator_focused);
                }
            }

        }
    }
}
