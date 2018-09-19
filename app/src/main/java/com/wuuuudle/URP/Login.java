package com.wuuuudle.URP;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login
{
    public String cookies = null;
    private static Login login=null;
    public static Login loginWithZM(String zjh, String mm)
    {
        Login log = new Login ();
        if(log.login(zjh, mm))
        {
            login=log;
        }
        return login;
    }

    public static Login loginWithZM()
    {
        return login;
    }

    public boolean login(String zjh, String mm)
    {
        initJSESSIONID();
        String ur = "http://202.115.47.141/j_spring_security_check";
        HttpURLConnection conn;
        URL url;
        try
        {
            url = new URL(ur);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Host", "202.115.47.141");
            conn.setRequestProperty("Content-Length", "59");
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("Origin", "http://202.115.47.141");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.setRequestProperty("Referer","http://202.115.47.141/login");
            conn.setRequestProperty("Accept-Encoding","gzip, deflate");
            conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
            conn.setRequestProperty("Cookie",cookies);
            conn.setRequestProperty("Connection","close");

            conn.connect();
            String params = String.format("j_username=%s&j_password=%s&j_captcha1=error", zjh, mm);
            //params = "j_username=2017141463221&j_password=244132&j_captcha1=error";
            OutputStream out = conn.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            out.close();
            String tString = getStringFromInputStream(conn.getInputStream());
            conn.disconnect();
            if (!tString.equals("The URL has moved <a href=\"http://202.115.47.141/index.jsp\">here</a>"))
                return false;
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    public boolean login(String cookies)
    {
        this.cookies = cookies;
        String ret = ViewPage("http://202.115.47.141/index.jsp");
        if(ret.matches("正在跳转"))
            return false;
        return true;
    }

    public void initJSESSIONID()
    {
        if (cookies != null)
            return;
        String temps = null;
        String ur = "http://202.115.47.141";
        HttpURLConnection conn;
        URL url;
        try
        {
            url = new URL(ur);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Host", "202.115.47.141");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
            conn.connect();
            String temp = conn.getHeaderField("Set-Cookie");
            String s[] = temp.split(";");
            temps = s[0];
            conn.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        cookies = temps;
    }

    public String ViewPage(String ur)
    {
        URL url;
        HttpURLConnection conn;
        try
        {
            url = new URL(ur);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(100000);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Cookie", cookies);
            conn.setRequestProperty("Host", "zhjw.scu.edu.cn");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");

            conn.connect();
            String ret = getStringFromInputStream(conn.getInputStream());
            conn.disconnect();
            return ret;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public callback getClassInformation()
    {
        URL url;
        HttpURLConnection conn;
        try
        {
            url = new URL("http://202.115.47.141/student/courseSelect/thisSemesterCurriculum/ajaxStudentSchedule/callback");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(100000);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Cookie", cookies);
            conn.setRequestProperty("Host", "zhjw.scu.edu.cn");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");

            conn.connect();
            String ret = getStringFromInputStream(conn.getInputStream());
            conn.disconnect();
            return callback.FormatData(ret);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static String getStringFromInputStream(InputStream is) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf8"));
        String line = "";
        String result = "";
        while ((line = br.readLine()) != null)
        {
            result += line;
        }
        return result;
    }
}
