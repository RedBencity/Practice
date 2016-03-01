package ben.practice.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import ben.practice.MainActivity;
import ben.practice.R;
import ben.practice.activity.PersonalActivity;
import ben.practice.activity.RankActivity;
import ben.practice.adapter.ListAdapter;
import ben.practice.listener.OnFragmentInteractionListener;
import ben.practice.utils.BitmapUtil;
import ben.practice.utils.NetUtil;
import ben.practice.utils.Util;


public class Fragment3 extends Fragment {

    private int[] icons;
    private String[] item_name;
    private ListView listView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private OnFragmentInteractionListener mListener;
    private String nickname;
    private ListAdapter listAdapter;

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
        preferences = getActivity().getSharedPreferences("constants", getActivity().MODE_PRIVATE);
        editor = preferences.edit();
        item_name = new String[]{nickname, "错题、收藏", "做题统计", "我的消息", "设置"};
        listAdapter = new ListAdapter(getActivity(), icons, item_name);

        getNicknameFromServer();

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
    public void onResume() {
        super.onResume();
        item_name[0] = preferences.getString("nickname", "default");
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void getNicknameFromServer() {
        String url = NetUtil.URL + "/servlet/MainServer";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//               Util.println("Fragment3 "+response);
                Util.println(this,response);
                nickname = response;
                editor.putString("nickname",nickname);
                editor.commit();
                listView.setAdapter(listAdapter);
                item_name[0] = nickname;
                listAdapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent;
                        if (position == 0) {
                            intent = new Intent(getActivity(), PersonalActivity.class);
                            startActivity(intent);
                        } else if (position == 1) {
                            Util.println("1");
                        }
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.setToast(getActivity(), "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType", "nickname");
                map.put("phone", preferences.getString("phone", "defalut"));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }


}
