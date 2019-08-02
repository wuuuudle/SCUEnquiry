package com.wuuuudle.scuenquiry;

import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wuuuudle.URP.Captcha;
import com.wuuuudle.URP.Login;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViewById(R.id.Login).setOnClickListener(v ->
        {
            final String zjh = ((TextView) findViewById(R.id.zjh)).getText().toString();
            final String mm = ((TextView) findViewById(R.id.mm)).getText().toString();
            if (zjh.equals("") || mm.equals(""))
            {
                Toast.makeText(getApplicationContext(), "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() ->
            {
                try (Captcha captcha = new Captcha(getApplicationContext()))
                {
                    if (Login.loginWithZM(zjh, mm, captcha) != null)
                    {
                        //页面跳转
                        LoginActivity.this.setResult(1);
                        LoginActivity.this.finish();
                    } else
                    {
                        //错误提示
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "账号或密码错误", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (IOException e)
                {
                    Toast.makeText(getApplicationContext(), "载入验证码识别模型错误", Toast.LENGTH_LONG).show();
                }
            }).start();
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (Login.loginWithZM() == null)
            {
                Toast.makeText(getApplicationContext(), "请登录", Toast.LENGTH_SHORT).show();
                return true;
            } else
            {
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
