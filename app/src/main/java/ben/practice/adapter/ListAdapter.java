package ben.practice.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import ben.practice.MainActivity;
import ben.practice.R;
import ben.practice.activity.PersonalActivity;
import ben.practice.activity.RankActivity;
import ben.practice.utils.BitmapUtil;
import ben.practice.utils.DiskLruCache;
import ben.practice.utils.NetUtil;
import ben.practice.utils.PhotoDialog;
import ben.practice.utils.Util;

/**
 * Created by Administrator on 2016/1/18 0018.
 */
public class ListAdapter extends BaseAdapter {
    private int[] icons;
    private String[] item_names;
    private Activity activity;
    private SharedPreferences preferences;
    private ImageLoader imageLoader;
    private DiskLruCache mDiskLruCache;
    private String[] counts;
    private String[] phones;

    public ListAdapter(Activity activity, int[] icons, String[] item_names) {
        this.activity = activity;
        this.icons = icons;
        this.item_names = item_names;
    }

    public ListAdapter(Activity activity, String[] item_names) {
        this.activity = activity;
        this.item_names = item_names;
        if (activity instanceof RankActivity) {
            try {
                File cacheDir = Util.getDiskCacheDir(activity, "bitmap");
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                mDiskLruCache = DiskLruCache.open(cacheDir, Util.getAppVersion(activity), 1, 10 * 1024 * 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ListAdapter(Activity activity, String[] phones, String[] item_names, String[] counts) {
        this.activity = activity;
        this.phones = phones;
        this.item_names = item_names;
        this.counts = counts;
        if (activity instanceof RankActivity) {
            try {
                File cacheDir = Util.getDiskCacheDir(activity, "bitmap");
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                mDiskLruCache = DiskLruCache.open(cacheDir, Util.getAppVersion(activity), 1, 10 * 1024 * 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (activity instanceof MainActivity) {
            preferences = activity.getSharedPreferences("constants", activity.MODE_PRIVATE);
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_style, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
            TextView textView = (TextView) convertView.findViewById(R.id.item_name);
            imageView.setImageResource(icons[position]);

            textView.setText(item_names[position]);
            if (icons[position] == R.mipmap.icon_default_avatar) {
//                Util.println(activity, Util.getViewHeight(imageView) + "  " + Util.getViewWidth(imageView));
                imageView.setAdjustViewBounds(true);
                imageView.setMaxHeight(Util.getViewHeight(imageView));
                imageView.setMaxWidth(Util.getViewWidth(imageView));
                String path = Util.getPhotoPath(activity, preferences.getString("phone", "default"));
                if (Util.isFile(path)) {
                    Bitmap bm = BitmapFactory.decodeFile(path);
                    imageView.setImageBitmap(bm);
                    bm = null;
                } else {
                    getServerPhoto(activity, imageView);

                }
            }
        } else if (activity instanceof RankActivity) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_rank, null);
            TextView rank_position = (TextView) convertView.findViewById(R.id.rank_position);
            ImageView rank_photo = (ImageView) convertView.findViewById(R.id.rank_photo);
            TextView rank_username = (TextView) convertView.findViewById(R.id.rank_username);
            TextView rank_count = (TextView) convertView.findViewById(R.id.rank_count);
            rank_position.setText(position + 1 + "");
            rank_username.setText(item_names[position]);
            rank_count.setText(counts[position]);
            rank_photo.setAdjustViewBounds(true);
            rank_photo.setMaxHeight(Util.getViewHeight(rank_photo));
            rank_photo.setMaxWidth(Util.getViewWidth(rank_photo));
            String url = NetUtil.URL + "/photo/" + phones[position] + ".png";
            getPhotoFromServer(url, rank_photo);

        } else if (activity instanceof PersonalActivity) {
            preferences = activity.getSharedPreferences("constants", activity.MODE_PRIVATE);
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_personal, null);
            RelativeLayout item_area = (RelativeLayout) convertView.findViewById(R.id.item_area);
            TextView item_name = (TextView) convertView.findViewById(R.id.personal_style);
            TextView item_text = (TextView) convertView.findViewById(R.id.personal_text);
            ImageView personal_arrow_right = (ImageView) convertView.findViewById(R.id.personal_arrow_right);

            item_name.setText(item_names[position]);
            if (item_names[position].equals("头像")) {
                item_text.setText("");
                item_text.setBackgroundResource(R.mipmap.icon_default_avatar);
                item_text.getLayoutParams().height = Util.getViewHeight(item_text);
                item_text.getLayoutParams().width = Util.getViewWidth(item_text);
                String path = Util.getPhotoPath(activity, preferences.getString("phone", "default"));
                if (Util.isFile(path)) {
                    Bitmap bm = BitmapFactory.decodeFile(path);
                    Drawable drawable = new BitmapDrawable(bm);
                    item_text.setBackgroundDrawable(drawable);
                    bm = null;
                } else {

                    getServerPhoto(activity, item_text);

                }
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
                item_area.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PersonalActivity personalActivity = (PersonalActivity) activity;
                        Intent intent = new Intent(personalActivity, ben.practice.activity.PersonalPublicActivity.class);
                        intent.putExtra("style", item_names[position]);
                        personalActivity.startActivity(intent);
                    }
                });
            } else if (item_names[position].equals("昵称")) {
                getNicknameFromServer(activity, item_text);
                item_area.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PersonalActivity personalActivity = (PersonalActivity) activity;
                        Intent intent = new Intent(personalActivity, ben.practice.activity.PersonalPublicActivity.class);
                        intent.putExtra("style", item_names[position]);
                        personalActivity.startActivityForResult(intent, 0x123);
                    }
                });

            } else if (item_names[position].equals("账号信息")) {
                personal_arrow_right.setVisibility(View.INVISIBLE);
                item_text.setText(preferences.getString("phone", "defalut"));
            }
        }
        return convertView;
    }


    private void getNicknameFromServer(final Activity activity, final TextView textView) {
        String url = NetUtil.URL + "/servlet/MainServer";
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                textView.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.setToast(activity, "服务器异常!");
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

    //获取服务器上头像
    private void getServerPhoto(final Activity activity, final TextView textView) {
        String url = NetUtil.URL + "/photo/" + preferences.getString("phone", "defalut") + ".png";
        final RequestQueue requestQueue = Volley.newRequestQueue(activity);

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Drawable drawable = new BitmapDrawable(response);
                textView.setBackgroundDrawable(drawable);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Util.setToast(activity, "获取头像失败！");
                textView.setBackgroundResource(R.mipmap.icon_default_avatar);
            }
        });
        requestQueue.add(imageRequest);
    }

    //获取服务器上头像
    private void getServerPhoto(final Activity activity, final ImageView imageView) {
        String url = NetUtil.URL + "/photo/" + preferences.getString("phone", "defalut") + ".png";
        final RequestQueue requestQueue = Volley.newRequestQueue(activity);

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);

            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Util.setToast(activity, "获取头像失败！");
                imageView.setBackgroundResource(R.mipmap.icon_default_avatar);
            }
        });
        requestQueue.add(imageRequest);
    }


    //异步下载图片
    class DownImageAsyncTask extends AsyncTask<String, String, Bitmap> {

        ImageView imageView;

        public DownImageAsyncTask() {

        }

        public DownImageAsyncTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String imageUrl = params[0];
                String key = Util.hashKeyForDisk(imageUrl);
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                Bitmap bitmap = null;
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (Util.downloadUrlToStream(imageUrl, outputStream)) {
                        editor.commit();
                        DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                        if (snapShot != null) {
                            InputStream is = snapShot.getInputStream(0);
                            bitmap = BitmapFactory.decodeStream(is);
                            return bitmap;
                        }
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            notifyDataSetChanged();
        }
    }

    private void getPhotoFromServer(String url, ImageView imageView) {
        //System.out.println(url);
//        ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.gallery_item_card_image,R.drawable.lighthouse,R.drawable.lighthouse);
//        imageLoader.get(url, listener);
        try {
            String imageUrl = url;
            String key = Util.hashKeyForDisk(imageUrl);
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageView.setImageBitmap(bitmap);
                notifyDataSetChanged();
            } else {
                DownImageAsyncTask downImageAsyncTask = new DownImageAsyncTask(imageView);
                downImageAsyncTask.execute(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
