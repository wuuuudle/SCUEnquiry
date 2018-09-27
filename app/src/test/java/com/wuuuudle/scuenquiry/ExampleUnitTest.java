package com.wuuuudle.scuenquiry;

import com.wuuuudle.URP.Login;

import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
    @Test
    public void addition_isCorrect()
    {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void login_test()
    {
        String KCH, KXH;
        Login login = Login.loginWithZM("2017141463221", "244132");
        if (login == null)
        {
            System.out.println("登录错误");
            return;
        }
        System.out.println("登录成功");
        System.out.println(Login.loginWithZM().getXKInformation("104215020"));


    }

    @Test
    public void week()
    {
        System.out.println(parseCallback.getCurrentWeek());
    }
}