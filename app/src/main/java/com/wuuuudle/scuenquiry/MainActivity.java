package com.wuuuudle.scuenquiry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
    public ArrayList<TextView> list;
    private static MainActivity context;
    private static callback data;

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

    static class MyHandler extends Handler
    {
        private MainActivity mainActivity;

        MyHandler(MainActivity activity)
        {
            mainActivity = activity;
        }

        @Override
        public void handleMessage(Message msg)
        {
            android.support.v7.widget.GridLayout gridLayout = mainActivity.findViewById(R.id.gridlayout);
            switch (msg.what)
            {
                case 1:
                    mainActivity.viewPager.setCurrentItem(0, true);
                    callback backdata = (callback) msg.obj;
                    parseCallback parse = new parseCallback(backdata);
                    mainActivity.list = parse.getTextView(mainActivity.getApplicationContext());
                    for (TextView temp : mainActivity.list)
                    {
                        gridLayout.addView(temp);
                    }
                    data = backdata;
                    break;
                case 2:
                    try
                    {
                        for (TextView textView : mainActivity.list)
                        {
                            gridLayout.removeView(textView);
                        }
                    } catch (Exception e)//context.list为空的异常处理
                    {

                    }
                    //获取课表
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                callback backdata = Login.loginWithZM().getClassInformation();
                                Message message = new Message();
                                message.what = 1;
                                message.obj = backdata;
                                context.handler.sendMessage(message);
                            } catch (NullPointerException e)
                            {

                            }

                        }
                    }).start();
                    break;
            }
        }
    }

    MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        initview();
        if (Login.loginWithZM() == null)
        {
            toLogin();
        }
        if (data != null)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(500);//等待加载layout加载完成
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = data;
                    handler.sendMessage(message);
                }
            }).start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        switch (resultCode)
        {
            case 1:
                handler.sendEmptyMessage(2);
        }
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

    public static void toLogin()
    {
        //跳转至登录界面
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivityForResult(intent, 0);
    }

    //从数据库读取登录状态
    public void initLoginState()
    {

    }
}
