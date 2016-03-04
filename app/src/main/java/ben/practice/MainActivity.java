package ben.practice;

import android.app.Fragment;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ben.practice.fragment.Fragment1;
import ben.practice.fragment.Fragment2;
import ben.practice.fragment.Fragment3;
import ben.practice.listener.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnFragmentInteractionListener{

    private ImageView icon_home_practice, icon_home_discovery, icon_home_misc;
    private TextView home_practice, home_discovery, home_misc;
    private RelativeLayout one, two, three;
    private FragmentManager fragmentManager;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getData();
    }

    private void initView() {
        icon_home_practice = (ImageView) findViewById(R.id.icon_home_practice);
        icon_home_discovery = (ImageView) findViewById(R.id.icon_home_discovery);
        icon_home_misc = (ImageView) findViewById(R.id.icon_home_misc);
        home_practice = (TextView) findViewById(R.id.home_practice);
        home_discovery = (TextView) findViewById(R.id.home_discovery);
        home_misc = (TextView) findViewById(R.id.home_misc);
        one = (RelativeLayout) findViewById(R.id.one);
        two = (RelativeLayout) findViewById(R.id.two);
        three = (RelativeLayout) findViewById(R.id.three);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        setChoiceItem(0);
    }


    private void getData(){

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                setChoiceItem(0);
                break;
            case R.id.two:
                setChoiceItem(1);
                break;
            case R.id.three:
                setChoiceItem(2);
                break;
        }
    }

    // 隐藏所有的Fragment,避免fragment混乱
    private void hideFragments(FragmentTransaction transaction) {
        if (fragment1 != null) {
            transaction.hide(fragment1);
        }
        if (fragment2 != null) {
            transaction.hide(fragment2);
        }
        if (fragment3 != null) {
            transaction.hide(fragment3);

        }

    }

    private void setChoiceItem(int index) {
        clearChoice();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
                home_practice.setTextColor(getResources().getColor(R.color.colorPrimary));
                icon_home_practice.setImageResource(R.mipmap.icon_home_practice_checked);
                if (fragment1 == null) {
                    fragment1 = new Fragment1();
                    fragmentTransaction.add(R.id.content, fragment1);
                } else {
                    fragmentTransaction.show(fragment1);
                }
                break;
            case 1:
                home_discovery.setTextColor(getResources().getColor(R.color.colorPrimary));
                icon_home_discovery.setImageResource(R.mipmap.icon_home_discovery_checked);
                if (fragment2 == null) {
                    fragment2 = new Fragment2();
                    fragmentTransaction.add(R.id.content, fragment2);
                } else {
                    fragmentTransaction.show(fragment2);
                }
                break;
            case 2:
                home_misc.setTextColor(getResources().getColor(R.color.colorPrimary));
                icon_home_misc.setImageResource(R.mipmap.icon_home_misc_checked);
                if (fragment3 == null) {
                    fragment3 = new Fragment3();
                    fragmentTransaction.add(R.id.content, fragment3);
                } else {
                    fragmentTransaction.show(fragment3);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void clearChoice() {
        home_practice.setTextColor(getResources().getColor(R.color.gray));
        icon_home_practice.setImageResource(R.mipmap.icon_home_practice);

        home_discovery.setTextColor(getResources().getColor(R.color.gray));
        icon_home_discovery.setImageResource(R.mipmap.icon_home_discovery);

        home_misc.setTextColor(getResources().getColor(R.color.gray));
        icon_home_misc.setImageResource(R.mipmap.icon_home_misc);
    }

    long mMillis;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mMillis > 2000) {
                mMillis = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "再次点击返回键，将退出程序",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else {
                try {
                    android.os.Process.killProcess(android.os.Process.myPid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
