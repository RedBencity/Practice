package ben.practice.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import ben.practice.R;
import ben.practice.activity.AlarmClockActivity;
import ben.practice.activity.RankActivity;
import ben.practice.adapter.ListAdapter;
import ben.practice.listener.OnFragmentInteractionListener;
import ben.practice.utils.Util;


public class Fragment2 extends Fragment {
    private int[] icons;
    private String[] item_name;
    private OnFragmentInteractionListener mListener;
    private ListView listView;

    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        icons = new int[]{R.mipmap.discovery_rank, R.mipmap.discovery_reminder};
        item_name = new String[]{"排行榜", "提醒"};
        listView.setAdapter(new ListAdapter(getActivity(), icons, item_name));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (position == 0) {
                    intent = new Intent(getActivity(), RankActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    intent = new Intent(getActivity(), AlarmClockActivity.class);
                    startActivity(intent);
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
