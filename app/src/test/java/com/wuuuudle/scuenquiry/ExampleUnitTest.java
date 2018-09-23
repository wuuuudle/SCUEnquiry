package com.wuuuudle.scuenquiry;

import android.util.Log;

import com.wuuuudle.URP.*;

import org.junit.Test;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.*;

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
        Login login=Login.loginWithZM("2017141463221", "244132");
        if(login == null)
        {
            System.out.println("登录错误");
            return;
        }
        callback backdata = login.getClassInformation();
        Data data = backdata.getDateList()[0];
        SelectCourse[] courses = data.getSelectCourseList();
        for(SelectCourse temp:courses)
        {
            System.out.println(temp.getAttendClassTeacher()+" "+temp.getCourseName());
        }
    }
}