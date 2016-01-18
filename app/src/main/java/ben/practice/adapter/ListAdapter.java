package ben.practice.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ben.practice.MainActivity;
import ben.practice.R;
import ben.practice.activity.RankActivity;

/**
 * Created by Administrator on 2016/1/18 0018.
 */
public class ListAdapter extends BaseAdapter {
    private int[] icons;
    private String[] item_name;
    private Activity activity;

    public ListAdapter(Activity activity, int[] icons, String[] item_name) {
        this.activity = activity;
        this.icons = icons;
        this.item_name = item_name;
    }

    @Override
    public int getCount() {
        return item_name.length;
    }

    @Override
    public Object getItem(int position) {
        return item_name[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (activity instanceof MainActivity) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_style, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
            TextView textView = (TextView) convertView.findViewById(R.id.item_name);
            imageView.setImageResource(icons[position]);
            textView.setText(item_name[position]);
        }else if (activity instanceof RankActivity){
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_rank, null);
            TextView rank_position = (TextView) convertView.findViewById(R.id.rank_position);
            ImageView rank_photo = (ImageView) convertView.findViewById(R.id.rank_photo);
            TextView rank_username = (TextView) convertView.findViewById(R.id.rank_username);
            TextView rank_count = (TextView) convertView.findViewById(R.id.rank_count);
            rank_position.setText(position+1+"");
            rank_photo.setImageResource(icons[position]);
            rank_username.setText(item_name[position]);
        }
        return convertView;
    }


}
