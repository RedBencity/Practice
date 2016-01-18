package ben.practice.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ben.practice.R;
import ben.practice.adapter.ListAdapter;
import ben.practice.listener.OnFragmentInteractionListener;
import ben.practice.utils.Util;


public class Fragment3 extends Fragment {

    private int[] icons;
    private String[] item_name;
    private ListView listView;

    private OnFragmentInteractionListener mListener;

    public Fragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        icons = new int[]{R.mipmap.icon_default_avatar, R.mipmap.discovery_marked_question, R.mipmap.discovery_answer_count, R.mipmap.icon_misc_message,
        R.mipmap.icon_misc_settings};
        item_name = new String[]{"red", "错题、收藏", "做题统计", "我的消息", "设置"};
        listView.setAdapter(new ListAdapter(getActivity(), icons, item_name));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Util.println("0");
                } else if (position == 1) {
                    Util.println("1");
                }
            }
        });
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
