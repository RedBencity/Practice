package ben.practice.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import ben.practice.R;
import ben.practice.listener.OnFragmentInteractionListener;


public class Fragment2 extends Fragment {

    private OnFragmentInteractionListener mListener;

    public Fragment2() {
        // Required empty public constructor
    }
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        textView = (TextView) view.findViewById(R.id.textview);
        String html = "<pre>   这位在科学界崭露头角的东方学生令居里夫人满心欣慰，对他也关爱有加。在施士元做实验时，居里夫人经常站在身边，反复提醒必须注意的事项，叫他不能用手去碰放射源，接近放射源时\n" +
                "要用铅盾挡住身体，要屏住呼吸，防止把放射气体吸入体内。居里夫人反复提醒，施士元有些不耐烦。后来才明白，原来在他来镭研究所之前，一位法国同学因没有注意安全事项而不幸丧生，居里夫\n" +
                "人为此痛心不已。得知实情后，居里夫人平时的叮嘱像东方母亲的叮咛一般暖遍施士元的身心。紧张的工作之余，居里夫人总会询问起这位远离故土的学生的生活情况，问他有没有困难，有些生活的\n" +
                "琐事也能为他想得很周详。</pre>\n" +
                "\n" +
                "<pre>   名师出高徒。1933年春天的一个阳光和煦的日子，在巴黎大学理学院的阶梯教室里，举行了施士元博士论文答辩会。居里夫人、P•拜冷和A•特比扬主持答辩，三位主考官都是诺贝尔奖的获得\n" +
                "者。施士元认真而自信地宣读论文，答辩委员会对施士元的答辩十分满意。从休息室出来，居里夫人高兴地宣布：“论文通过，很好。”恩师与爱徒的手紧紧相握。</pre> \n" +
                "\n" +
                " <pre>   第二天，居里夫人专门为施士元举行了香槟酒会。在镭研究所那块草坪上，居里夫人显得格外高兴。席间，居里夫人来到施士元身边，轻声问他是否愿意留下来工作。面对恩师满怀期望的眼\n" +
                "神，施士元沉默了，随后他说：“我们公费学习的期限是四年。”善解人意的居里夫人知道，这样一位才华横溢的东方青年心底有一个矢志报效祖国的夙愿。</pre> ";
        textView.setText(Html.fromHtml(html));
        return view;
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


}
