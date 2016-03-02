package ben.practice.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ben.practice.MainActivity;
import ben.practice.R;
import ben.practice.utils.BitmapUtil;
import ben.practice.utils.NetUtil;

import ben.practice.utils.Util;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import android.os.Handler.Callback;

public class LoginActivity extends AppCompatActivity {

    private ImageView login_img_photo;
    private ImageView login_img_progress;
    private LinearLayout login_layout_welcome;
    private View login_view_line;
    private EditText login_edit_username;
    private EditText login_edit_password;
    private ImageView login_img_slide;
    private Button login_btn_clear_username;
    private Button login_btn_clear_password;
    private Button login_btn_register;
    private Button login_btn_find_password;

    private Handler hander;
    private boolean slidingBack;
    private int slidingMinX, slidingMaxX, lastX;

    private String isSuccess = "false";
    public static final int PASSWORD_MIN_LENGTH = 1;
    public static final int LOGIN_SUCCESS = 0; // 登录成功
    public static final int LOGIN_FAILED = 1; // 登录失败
    public static final int LOGIN_SLIDER_TIP = 2; // 登录页面滑块向左自动滑动
    public static final int LOGIN_PHOTO_ROTATE_TIP = 3; // 登录页面加载图片转动

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SMSSDK.initSDK(this, "fc574ead4d5c", "f5114ca3eb1938aab29b1f3c919b7fa3");
        initView();
        initData();
    }

    protected void onDestroy() {
        if (ready) {
            // 销毁回调监听接口
            SMSSDK.unregisterAllEventHandler();
        }
        super.onDestroy();
    }

    private void initView() {
        login_img_photo = (ImageView) findViewById(R.id.login_img_photo);
        login_img_progress = (ImageView) findViewById(R.id.login_img_progress);
        login_layout_welcome = (LinearLayout) findViewById(R.id.login_layout_welcome);
        login_view_line = findViewById(R.id.login_view_line);
        login_edit_username = (EditText) findViewById(R.id.login_edit_username);
        login_edit_password = (EditText) findViewById(R.id.login_edit_password);
        login_img_slide = (ImageView) findViewById(R.id.login_img_slide);
        login_btn_clear_username = (Button) findViewById(R.id.login_btn_clear_username);
        login_btn_clear_password = (Button) findViewById(R.id.login_btn_clear_password);
        login_btn_register = (Button) findViewById(R.id.login_btn_register);
        login_btn_find_password = (Button) findViewById(R.id.login_btn_find_password);
        slidingBack = false;
        lastX = 0;
        slidingMinX = 0;
        slidingMaxX = 0;

        preferences = getSharedPreferences("constants", MODE_PRIVATE);
        editor = preferences.edit();


    }



    private void initData() {
        hander = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case LOGIN_SUCCESS:
                        Map<String, String> mExtra = null;
                        break;
                    case LOGIN_FAILED:
                        stopLogin();
                        break;
                    case LOGIN_SLIDER_TIP:
                        login_img_slide.layout(lastX, login_img_slide.getTop(), lastX + login_img_slide.getWidth(),
                                login_img_slide.getTop() + login_img_slide.getHeight());
                        break;
                    case LOGIN_PHOTO_ROTATE_TIP:
                        login_img_photo.setImageBitmap((Bitmap) message.obj);
                        break;

                }
            }
        };

        login_edit_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() >= 1) {
                    login_btn_clear_username.setVisibility(View.VISIBLE);
                } else {
                    login_btn_clear_username.setVisibility(View.GONE);
                }

                initWidgetForCanLogin();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        login_edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() >= 1) {
                    login_btn_clear_password.setVisibility(View.VISIBLE);
                } else if (s.length() == 0 && login_btn_clear_password.getVisibility() == View.VISIBLE) {
                    login_btn_clear_password.setVisibility(View.GONE);
                }
                initWidgetForCanLogin();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        login_btn_clear_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_edit_username.setText("");
                login_edit_password.setText("");

            }
        });

        login_btn_clear_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_edit_password.setText("");
            }
        });

        login_img_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canLogin()) {
                }
            }
        });

        login_img_slide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Util.colseKeybord(login_edit_username, LoginActivity.this);
                Util.colseKeybord(login_edit_password, LoginActivity.this);
                if (canLogin() && !slidingBack) {
                    if (slidingMaxX == 0) {
                        slidingMinX = login_view_line.getLeft() - login_img_slide.getWidth() / 2;
                        slidingMaxX = login_view_line.getRight() - login_img_slide.getWidth() / 2;
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastX = (int) event.getRawX();
                        case MotionEvent.ACTION_MOVE:
                            int liX = (int) event.getRawX();
                            if (liX > slidingMaxX) {
                                liX = slidingMaxX;
                            } else if (liX < slidingMinX) {
                                liX = slidingMinX;
                            }
                            if (liX != lastX) {
                                login_img_slide.layout(liX, login_img_slide.getTop(), liX + login_img_slide.getWidth(),
                                        login_img_slide.getTop() + login_img_slide.getHeight());
                                lastX = liX;
                                if (lastX == slidingMaxX) {
                                    checkUser();
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if ((int) event.getRawX() < slidingMaxX) {
                                slideBack();
                            }
                            break;
                    }
                }
                return false;
            }
        });

        login_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
//                      解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
//                          提交用户信息
                            registerUser(phone);
                        }
                    }
                });
                ready = true;
                registerPage.show(LoginActivity.this);
            }
        });

        login_btn_find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
//                      解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
//                          提交用户信息
                            updateUserPassword(phone);
                        }
                    }
                });
                ready = true;
                registerPage.show(LoginActivity.this);
            }
        });
    }

    // 提交用户信息
    private void registerUser(String phone) {
            checkUserBySMS(phone);
    }


    // 触摸登录界面收回键盘
    public boolean onTouchEvent(android.view.MotionEvent event) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {
            return false;
        }
    }

    // 滑块向会自动滑动
    private void slideBack() {
        new Thread() {
            @Override
            public void run() {
                slidingBack = true;
                while (lastX > slidingMinX) {
                    lastX -= 5;
                    if (lastX < slidingMinX) {
                        lastX = slidingMinX;
                    }
                    Message loMsg = new Message();
                    loMsg.what = LOGIN_SLIDER_TIP;
                    hander.sendMessage(loMsg);

                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                slidingBack = false;
            }

        }.start();
    }

    //开始登录
    private void startLogin() {

        if (isSuccess.equals("true")) {
//        if (true) {
            getServerPhoto();
            Animation loAnimRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
            Animation loAnimScale = AnimationUtils.loadAnimation(this, R.anim.login_photo_scale_small);

            login_img_progress.setVisibility(View.VISIBLE);
            login_img_progress.startAnimation(loAnimRotate);
            login_img_photo.startAnimation(loAnimScale);
            loAnimRotate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
//                    Util.USERNAME = login_edit_username.getText().toString();
//                    Map<String, String> putExtra = new HashMap<String, String>();
//                    putExtra.put("username", login_edit_username.getText().toString());
//                    Bitmap bm = ((BitmapDrawable) login_img_photo.getDrawable()).getBitmap();
//                    putExtra.put("photo", Util.convertBitmapToString(bm));
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    editor.putString("phone", login_edit_username.getText().toString());
                    editor.putString("nickname","ben");
                    editor.commit();
                    finish();
                    startActivity(intent);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            login_img_slide.setVisibility(View.GONE);
            login_view_line.setVisibility(View.GONE);
            login_edit_username.setVisibility(View.GONE);
            login_edit_password.setVisibility(View.GONE);
            login_btn_register.setVisibility(View.GONE);
            login_btn_find_password.setVisibility(View.GONE);
            login_btn_clear_username.setVisibility(View.GONE);
            login_btn_clear_password.setVisibility(View.GONE);
            login_layout_welcome.setVisibility(View.VISIBLE);
        } else {
            Util.setToast(LoginActivity.this, "用户或密码错误！");
            slideBack();
        }

    }

    private void stopLogin() {
        Animation loAnimScale = AnimationUtils.loadAnimation(this,
                R.anim.login_photo_scale_big);
        LinearInterpolator loLin = new LinearInterpolator();
        loAnimScale.setInterpolator(loLin);
        login_img_progress.clearAnimation();
        login_img_progress.setVisibility(View.GONE);
        login_img_photo.clearAnimation();
        login_img_photo.startAnimation(loAnimScale);

        login_img_slide.setVisibility(View.VISIBLE);
        login_view_line.setVisibility(View.VISIBLE);
        login_edit_username.setVisibility(View.VISIBLE);
        login_edit_password.setVisibility(View.VISIBLE);
        login_btn_clear_username.setVisibility(View.VISIBLE);
        login_btn_clear_password.setVisibility(View.VISIBLE);
        login_btn_register.setVisibility(View.VISIBLE);
        login_btn_find_password.setVisibility(View.VISIBLE);
        login_layout_welcome.setVisibility(View.GONE);
    }


    // 根据是否可以登录，初始化相关控件
    private void initWidgetForCanLogin() {

        if (canLogin()) {
            login_img_slide.setImageResource(R.mipmap.ic_arrow_circle_right);
        } else {
            login_img_slide.setImageResource(R.mipmap.ic_ask_circle);
        }


    }

    // 判断当前用户输入是否合法，是否可以登录
    private boolean canLogin() {
        Editable loginUsername = login_edit_username.getText();
        Editable loginPassword = login_edit_password.getText();
        return !Util.isStrEmpty(loginUsername) && loginPassword.length() >= PASSWORD_MIN_LENGTH;
    }

    //检查用户密码
    private void checkUser() {
        Util.println(LoginActivity.this, login_edit_username.getText().toString());
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetUtil.URL + "/servlet/LoginServer";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.println(LoginActivity.this,response);
                isSuccess = response;
                startLogin();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                slideBack();
                Util.setToast(LoginActivity.this, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType","login");
                map.put("phone", login_edit_username.getText().toString());
                map.put("password", login_edit_password.getText().toString());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void updateUserPassword(final String phone){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetUtil.URL + "/servlet/LoginServer";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Util.setToast(LoginActivity.this, "修改成功！默认密码 123456 !",5000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                slideBack();
                Util.setToast(LoginActivity.this, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType","find_password");
                map.put("phone", phone);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    //手机短信验证登录
    private void checkUserBySMS(final String phone) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = NetUtil.URL + "/servlet/LoginServer";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                if (Boolean.parseBoolean(response)){
                    Util.setToast(LoginActivity.this, "您已注册过，直接登录！");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    editor.putString("phone", phone);
                    editor.putString("nickname","ben");
                    editor.commit();
                    finish();
                    startActivity(intent);
                }else {
                    Util.setToast(LoginActivity.this, "注册成功！请尽快修改密码！");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    editor.putString("phone", phone);
                    editor.putString("nickname","ben");
                    editor.commit();
                    finish();
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                slideBack();
                Util.setToast(LoginActivity.this, "服务器异常!");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("requestType","register");
                map.put("phone", phone);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }



    //获取服务器上头像
    private void getServerPhoto() {
        String url = NetUtil.URL + "/photo/" + login_edit_username.getText().toString() + ".png";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                login_img_photo.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Util.setToast(LoginActivity.this, "获取头像失败！");
                login_img_photo.setImageResource(R.mipmap.icon_default_avatar);
            }
        });
        requestQueue.add(imageRequest);
    }
}
