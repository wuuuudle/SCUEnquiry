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
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.wuuuudle.URP.Login;
import com.wuuuudle.URP.callback;
import com.wuuuudle.scuenquiry.Fragment.ClassFragment;
import com.wuuuudle.scuenquiry.Fragment.PersionFragment;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
{
    private ViewPager viewPager;
    public ArrayList<TextView> list;

    private static MainActivity context;
    private static callback data;
    private static ArrayList<Pair<String, String>> planCode;
    private static Map<String, callback> callData;
    private static int weekLength;

    private Map<String, String> planCodeMap;

    private LoadingDialog ld;
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
            final Spinner planCode = context.findViewById(R.id.planCode);
            final Spinner weeks = context.findViewById(R.id.weeks);
            switch (msg.what)
            {
                case 1:
                {
                    try
                    {
                        for (TextView textView : mainActivity.list)
                        {
                            gridLayout.removeView(textView);
                        }
                    } catch (Exception e)//context.list为空的异常处理
                    {

                    }
                    mainActivity.viewPager.setCurrentItem(0, true);
                    if (msg.obj == null) break;
                    callback backdata = (callback) msg.obj;
                    parseCallback parse = new parseCallback(backdata);
                    mainActivity.list = parse.getTextView(mainActivity.getApplicationContext(), weeks.getSelectedItemPosition() - 1);
                    for (TextView temp : mainActivity.list)
                    {
                        gridLayout.addView(temp);
                    }
                    data = backdata;
                }
                break;
                case 2:
                {
                    //获取课表,学期,周次
                    new Thread(() ->
                    {
                        try
                        {
                            context.handler.sendEmptyMessage(4);
                            context.callData = new HashMap<>();
                            callback backdata = Login.loginWithZM().getClassInformation();

                            Message message = new Message();
                            message.what = 6;
                            message.obj = backdata.getWeekLength();
                            context.handler.sendMessage(message);


                            ArrayList<Pair<String, String>> arrayList = Login.loginWithZM().getPlanCode();
                            context.planCodeMap = new HashMap<>();

                            context.callData.put(arrayList.get(0).first, backdata);
                            for (Pair<String, String> temp : arrayList)
                            {
                                context.planCodeMap.put(temp.first, temp.second);
                            }
                            message = new Message();
                            message.what = 3;
                            message.obj = arrayList;
                            context.handler.sendMessage(message);
                            context.handler.sendEmptyMessage(5);
                        } catch (NullPointerException e)
                        {

                        }

                    }).start();
                }
                break;
                case 3:
                {
                    List<String> list = new ArrayList<>();
                    ArrayList<Pair<String, String>> arrayList = (ArrayList<Pair<String, String>>) msg.obj;
                    for (Pair<String, String> temp : arrayList) list.add(temp.first);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, R.id.planCodeItem, list);
                    planCode.setAdapter(adapter);
                    planCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            callback data = null;
                            if (context.callData.containsKey(planCode.getSelectedItem()))
                            {
                                data = context.callData.get(planCode.getSelectedItem());
                                Message message = new Message();
                                message.what = 1;
                                message.obj = data;
                                context.handler.sendMessage(message);
                            } else
                            {
                                new Thread(() ->
                                {
                                    context.handler.sendEmptyMessage(4);

                                    callback data1 = Login.loginWithZM().getClassInformation(context.planCodeMap.get(planCode.getSelectedItem()));
                                    context.callData.put((String) planCode.getSelectedItem(), data1);
                                    Message message = new Message();
                                    message.what = 6;
                                    message.obj = data1.getWeekLength();
                                    context.handler.sendMessage(message);

                                    message = new Message();
                                    message.what = 1;
                                    message.obj = data1;
                                    context.handler.sendMessage(message);

                                    context.handler.sendEmptyMessage(5);
                                }).start();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });

                    MainActivity.planCode = arrayList;
                }
                break;
                case 4:
                {
                    context.ld = new LoadingDialog(context);
                    context.ld.setLoadingText("加载中")
                            .setSuccessText("加载成功")//显示加载成功时的文字
                            .setInterceptBack(true)
                            .setLoadSpeed(LoadingDialog.Speed.SPEED_TWO)
                            .closeSuccessAnim()
                            .show();
                }
                break;
                case 5:
                {
                    context.ld.loadSuccess();
                }
                break;
                case 6:
                {
                    List<String> list_weeks = new ArrayList<>();
                    list_weeks.add("总览");
                    for (int i = 1; i <= (Integer) msg.obj; i++)
                        list_weeks.add(String.valueOf(i) + "周");
                    ArrayAdapter<String> adapter_weeks = new ArrayAdapter<String>(context, R.layout.spinner_item, R.id.planCodeItem, list_weeks);
                    weeks.setAdapter(adapter_weeks);
                    weeks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            Message message = new Message();
                            message.what = 1;
                            message.obj = context.callData.get(planCode.getSelectedItem());
                            context.handler.sendMessage(message);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {
                        }
                    });
                    weekLength = (Integer) msg.obj;
                    weeks.setSelection(parseCallback.getCurrentWeek() + 1);
                }
                break;
            }
        }
    }

    public MyHandler handler = new MyHandler(this);

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
            new Thread(() ->
            {
                while (findViewById(R.id.gridlayout) == null)
                {
                    try
                    {
                        Thread.sleep(1);
                    } catch (InterruptedException e)
                    {
                    }
                }
                Message message = new Message();
                message.what = 1;
                message.obj = data;
                handler.sendMessage(message);

                planCodeMap = new HashMap<>();
                for (Pair<String, String> temp : planCode)
                {
                    planCodeMap.put(temp.first, temp.second);
                }
                message = new Message();
                message.what = 3;
                message.obj = planCode;
                handler.sendMessage(message);

                message = new Message();
                message.what = 6;
                message.obj = weekLength;
                handler.sendMessage(message);
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
