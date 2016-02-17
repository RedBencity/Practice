package ben.practice.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ben.practice.MainActivity;
import ben.practice.R;
import ben.practice.activity.PersonalActivity;
import ben.practice.activity.PublicActivity;
import ben.practice.activity.RankActivity;
import ben.practice.utils.PhotoDialog;
import ben.practice.utils.Util;

/**
 * Created by Administrator on 2016/1/18 0018.
 */
public class ListAdapter extends BaseAdapter {
    private int[] icons;
    private String[] item_names;
    private Activity activity;

    public ListAdapter(Activity activity, int[] icons, String[] item_names) {
        this.activity = activity;
        this.icons = icons;
        this.item_names = item_names;
    }

    public ListAdapter(Activity activity, String[] item_names) {
        this.activity = activity;
        this.item_names = item_names;
    }


    @Override
    public int getCount() {
        return item_names.length;
    }

    @Override
    public Object getItem(int position) {
        return item_names[position];
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
            textView.setText(item_names[position]);
        } else if (activity instanceof RankActivity) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_rank, null);
            TextView rank_position = (TextView) convertView.findViewById(R.id.rank_position);
            ImageView rank_photo = (ImageView) convertView.findViewById(R.id.rank_photo);
            TextView rank_username = (TextView) convertView.findViewById(R.id.rank_username);
            TextView rank_count = (TextView) convertView.findViewById(R.id.rank_count);
            rank_position.setText(position + 1 + "");
            rank_photo.setImageResource(icons[position]);
            rank_username.setText(item_names[position]);
        } else if (activity instanceof PersonalActivity) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_personal, null);
            RelativeLayout item_area = (RelativeLayout) convertView.findViewById(R.id.item_area);
            TextView item_name = (TextView) convertView.findViewById(R.id.personal_style);
            TextView item_text = (TextView) convertView.findViewById(R.id.personal_text);
            item_name.setText(item_names[position]);
            if (item_names[position].equals("头像")) {
                item_text.setText("");
                item_text.setBackgroundResource(R.mipmap.icon_default_avatar);
                item_area.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final PhotoDialog photoDialog = new PhotoDialog(activity);
                        photoDialog.setTopBtnOnClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
                                PersonalActivity personalActivity = (PersonalActivity) activity;
                                personalActivity.setCameraImage();
                                photoDialog.dismiss();
                            }
                        });
                        photoDialog.setBottomBtnOnClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

                                PersonalActivity personalActivity = (PersonalActivity) activity;
                                if (mIsKitKat) {
                                    personalActivity.selectImageUriAfterKikat();
                                } else {
                                    personalActivity.cropImageUri();
                                }
                                photoDialog.dismiss();
                            }
                        });

                        photoDialog.show();
                    }
                });
            } else if (item_names[position].equals("修改密码")) {
                item_text.setText("");
            } else if (item_names[position].equals("昵称")) {
                item_area.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PersonalActivity personalActivity = (PersonalActivity) activity;
                        Intent intent = new Intent(personalActivity, PublicActivity.class);
                        personalActivity.startActivityForResult(intent, 0x123);
                    }
                });

            }
        }
        return convertView;
    }


}
