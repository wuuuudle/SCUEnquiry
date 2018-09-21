package com.wuuuudle.scuenquiry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wuuuudle.URP.Data;
import com.wuuuudle.URP.Login;
import com.wuuuudle.URP.SelectCourse;
import com.wuuuudle.URP.TimeAndPlace;
import com.wuuuudle.URP.callback;


public class MainActivity extends AppCompatActivity
{

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.navigation_class:
                    Toast.makeText(getApplicationContext(), "课表", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_setting:
                    Toast.makeText(getApplicationContext(), "个人中心", Toast.LENGTH_SHORT).show();
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
                    //((EditText)findViewById(R.id.editText2)).setText((String)msg.obj);
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                callback backdata = Login.loginWithZM().getClassInformation();
                Data data = backdata.getDateList()[0];
                SelectCourse[] courses = data.getSelectCourseList();
                Message message = new Message();
                message.what = 1;
                String send = "";
                for (SelectCourse temp : courses)
                {
                    Log.e("E", temp.getAttendClassTeacher() + " " + temp.getCourseName());
                    String add = "";
                    for (TimeAndPlace tt : temp.getTimeAndPlaceList())
                    {
                        add += tt.getTeachingBuildingName() + tt.getClassroomName() + " ";
                    }

                    send += add + temp.getCourseName() + "\n";
                }

                message.obj = send;
                handler.sendMessage(message);
            }
        }).start();

    }

    //从数据库读取登录状态
    public void initLoginState()
    {

    }
}
