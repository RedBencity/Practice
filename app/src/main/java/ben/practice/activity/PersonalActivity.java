package ben.practice.activity;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;

import ben.practice.MainActivity;
import ben.practice.R;
import ben.practice.adapter.ListAdapter;
import ben.practice.utils.Util;

public class PersonalActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView personal_listview;
    private String[] itemNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        initView();
        getData();
    }

    private void initView() {
        setToolbar();
        personal_listview = (ListView) findViewById(R.id.personal_listview);
        itemNames = new String[]{"头像", "昵称", "账号信息", "修改密码"};
    }

    private void getData() {
        personal_listview.setAdapter(new ListAdapter(PersonalActivity.this, itemNames));
        createFile();


    }

    private void setToolbar() {
        View view = findViewById(R.id.personal_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView head_title = (TextView) view.findViewById(R.id.head_title);
        head_title.setText("个人");
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.selector_bar_back);
        toolbar.setContentInsetsRelative(0, 0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
            }
        });
    }


    //保存图片本地路径
    public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath()
            + "/account/";
    public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "icon_cache/";
    private static final String IMGPATH = ACCOUNT_DIR + ACCOUNT_MAINTRANCE_ICON_CACHE;

    private static final String IMAGE_FILE_NAME = "faceImage.jpeg";
    private static final String TMP_IMAGE_FILE_NAME = "tmp_faceImage.jpeg";

    //常量定义
    public static final int TAKE_A_PICTURE = 10;
    public static final int SELECT_A_PICTURE = 20;
    public static final int SET_PICTURE = 30;
    public static final int SET_ALBUM_PICTURE_KITKAT = 40;
    public static final int SELECET_A_PICTURE_AFTER_KIKAT = 50;
    private String mAlbumPicturePath = null;

    File fileone = null;
    File filetwo = null;

    //版本比较：是否是4.4及以上版本
    final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_A_PICTURE) {
            if (resultCode == RESULT_OK && null != data) {
                //				Log.i("zou", "4.4以下的");
                Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH,
                        TMP_IMAGE_FILE_NAME)));

                setPhoto(bitmap);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(PersonalActivity.this, "取消头像设置", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SELECET_A_PICTURE_AFTER_KIKAT) {
            if (resultCode == RESULT_OK && null != data) {
//				Log.i("zou", "4.4以上的");
                mAlbumPicturePath = getPath(getApplicationContext(), data.getData());
                cropImageUriAfterKikat(Uri.fromFile(new File(mAlbumPicturePath)));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(PersonalActivity.this, "取消头像设置", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SET_ALBUM_PICTURE_KITKAT) {
            Log.i("zou", "4.4以上上的 RESULT_OK");

            Bitmap bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
            setPhoto(bitmap);

//			Log.i("zou", "4.4以上上的 RESULT_OK");
//			Bitmap bitmap = data.getParcelableExtra("data");
//			mAcountHeadIcon.setImageBitmap(bitmap);
        } else if (requestCode == TAKE_A_PICTURE) {
            Log.i("zou", "TAKE_A_PICTURE-resultCode:" + resultCode);
            if (resultCode == RESULT_OK) {
                cameraCropImageUri(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
            } else {
                Toast.makeText(PersonalActivity.this, "取消头像设置", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SET_PICTURE) {
            //拍照的设置头像  不考虑版本
            //			Log.i("zou", "SET_PICTURE-resultCode:" + resultCode);
            Bitmap bitmap = null;
            //			if (mIsKitKat) {  //高版本
            //				if (null != data) {
            //					bitmap = data.getParcelableExtra("data");
            //					showLoading();
            //					mAccountControl.resetGoUserIcon(bitmap2byte(bitmap), this);
            //				} else {  //高版本不能通过“data”获取到图片数据的就用uri
            //					if (resultCode == RESULT_OK) {
            //						bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
            //						showLoading();
            //						mAccountControl.resetGoUserIcon(bitmap2byte(bitmap), this);
            //					}
            //				}
            //			} else {  //低版本
            if (resultCode == RESULT_OK && null != data) {
                bitmap = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
                setPhoto(bitmap);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(PersonalActivity.this, "取消头像设置", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PersonalActivity.this, "设置头像失败", Toast.LENGTH_SHORT).show();
            }
            //			}
        } else if (requestCode == 0x123){
            Toast.makeText(PersonalActivity.this, "aaaaaaa", Toast.LENGTH_SHORT).show();
        }
    }

    private void setPhoto(Bitmap bitmap) {
        View view = personal_listview.getChildAt(0);
        TextView textView = (TextView) view.findViewById(R.id.personal_text);
        textView.setBackgroundDrawable(new BitmapDrawable(toRoundBitmap(bitmap)));
    }

    private void createFile() {
        File directory = new File(ACCOUNT_DIR);
        File imagepath = new File(IMGPATH);
        if (!directory.exists()) {
            Log.i("zou", "directory.mkdir()");
            directory.mkdir();
        }
        if (!imagepath.exists()) {
            Log.i("zou", "imagepath.mkdir()");
            imagepath.mkdir();
        }

        fileone = new File(IMGPATH, IMAGE_FILE_NAME);
        filetwo = new File(IMGPATH, TMP_IMAGE_FILE_NAME);

        try {
            if (!fileone.exists() && !filetwo.exists()) {
                fileone.createNewFile();
                filetwo.createNewFile();
            }
        } catch (Exception e) {
        }
    }

    public void setCameraImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
        startActivityForResult(intent, TAKE_A_PICTURE);
        Log.i("zou", "TAKE_A_PICTURE");
    }


    /**
     * <br>功能简述:裁剪图片方法实现---------------------- 相册
     * <br>功能详细描述:
     * <br>注意:
     */
    public void cropImageUri() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", Util.getImageWidth(PersonalActivity.this, R.mipmap.icon_default_avatar) * Util.getScreendensity(PersonalActivity.this));
        intent.putExtra("outputY", Util.getImageHeight(PersonalActivity.this, R.mipmap.icon_default_avatar) * Util.getScreendensity(PersonalActivity.this));
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, SELECT_A_PICTURE);
    }


    /**
     * <br>功能简述:4.4以上裁剪图片方法实现---------------------- 相册
     * <br>功能详细描述:
     * <br>注意:
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void selectImageUriAfterKikat() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
    }

    /**
     * <br>功能简述:裁剪图片方法实现----------------------相机
     * <br>功能详细描述:
     * <br>注意:
     *
     * @param uri
     */
    private void cameraCropImageUri(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/jpeg");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", Util.getImageWidth(PersonalActivity.this, R.mipmap.icon_default_avatar) * Util.getScreendensity(PersonalActivity.this));
        intent.putExtra("outputY", Util.getImageHeight(PersonalActivity.this, R.mipmap.icon_default_avatar) * Util.getScreendensity(PersonalActivity.this));
        intent.putExtra("scale", true);
        //		if (mIsKitKat) {
        //			intent.putExtra("return-data", true);
        //			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //		} else {
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //		}
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, SET_PICTURE);
    }

    /**
     * <br>功能简述: 4.4及以上改动版裁剪图片方法实现 --------------------相机
     * <br>功能详细描述:
     * <br>注意:
     *
     * @param uri
     */
    private void cropImageUriAfterKikat(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/jpeg");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", Util.getImageWidth(PersonalActivity.this, R.mipmap.icon_default_avatar) * Util.getScreendensity(PersonalActivity.this));
        intent.putExtra("outputY", Util.getImageHeight(PersonalActivity.this, R.mipmap.icon_default_avatar) * Util.getScreendensity(PersonalActivity.this));
        intent.putExtra("scale", true);
        //		intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
    }

    /**
     * <br>功能简述:
     * <br>功能详细描述:
     * <br>注意:
     *
     * @param uri
     * @return
     */
    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * <br>功能简述:4.4及以上获取图片的方法
     * <br>功能详细描述:
     * <br>注意:
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public Bitmap toRoundBitmap(Bitmap bitmap) {
        //圆形图片宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //正方形的边长
        int r = 0;
        //取最短边做边长
        if (width > height) {
            r = height;
        } else {
            r = width;
        }
        //构建一个bitmap
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        //设置边缘光滑，去掉锯齿
        paint.setAntiAlias(true);
        //宽高相等，即正方形
        RectF rect = new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, paint);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, paint);
        //返回已经绘画好的backgroundBmp
        return backgroundBmp;
    }
}
