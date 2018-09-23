package com.wuuuudle.scuenquiry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.wuuuudle.URP.Login;
import com.wuuuudle.URP.callback;
import com.wuuuudle.scuenquiry.Fragment.ClassFragment;
import com.wuuuudle.scuenquiry.Fragment.PersionFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    private ViewPager viewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.navigation_class:
                    viewPager.setCurrentItem(0, true);
                    return true;
                case R.id.navigation_setting:
                    viewPager.setCurrentItem(1, true);
                    return true;
            }
            return false;
        }
    };
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    android.support.v7.widget.GridLayout gridLayout = findViewById(R.id.gridlayout);
                    callback data = (callback) msg.obj;
                    parseCallback parse = new parseCallback(data);
                    ArrayList<TextView> list = parse.getTextView(getApplicationContext());
                    for(TextView temp: list)
                    {
                        gridLayout.addView(temp);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Login.loginWithZM() == null)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        initview();


        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                callback backdata = Login.loginWithZM().getClassInformation();
                Message message = new Message();
                message.what = 1;
                message.obj = backdata;
                handler.sendMessage(message);
            }
        }).start();

    }
    public void initview()
    {

        viewPager = findViewById(R.id.viewpager);
        final ArrayList<Fragment> fgLists = new ArrayList<>(2);
        fgLists.add(new ClassFragment());
        fgLists.add(new PersionFragment());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(int i)
            {
                return fgLists.get(i);
            }

            @Override
            public int getCount()
            {
                return 2;
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i1)
            {

            }

            @Override
            public void onPageSelected(int i)
            {
                navigation.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i)
            {

            }
        });

    }


    //从数据库读取登录状态
    public void initLoginState()
    {

    }
}
