package com.wuuuudle.URP;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Login
{
    public String cookies = null;
    private static Login login = null;

    public static Login loginWithZM(String zjh, String mm, Captcha captcha)
    {
        Login log = new Login();
        if (log.login(zjh, mm, captcha))
        {
            login = log;
        }
        return login;
    }

    public static Login loginWithZM()
    {
        return login;
    }

    public static void LoginOut()
    {
        login.cookies = null;
        login = null;
    }

    public boolean login(String zjh, String mm, Captcha captcha)
    {
        initJSESSIONID();
        String ur = "http://202.115.47.141/j_spring_security_check";
        String cap = captcha.OCR(this.getCaptcha());
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
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("Origin", "http://202.115.47.141");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.setRequestProperty("Referer", "http://202.115.47.141/login");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Cookie", cookies);
            conn.setRequestProperty("Connection", "close");

            conn.connect();
            String params = String.format("j_username=%s&j_password=%s&j_captcha=%s", zjh, mm, cap);
            //params = "j_username=2017141463221&j_password=244132&j_captcha1=error";
            OutputStream out = conn.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            out.close();
            String tString = getStringFromInputStream(conn.getInputStream());
            conn.getInputStream().close();
            conn.disconnect();
            if (!tString.equals("The URL has moved <a href=\"http://202.115.47.141/index.jsp\">here</a>"))
                return false;
        } catch (IOException e)
        {
            return false;
        }
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
            conn.getInputStream().close();
            conn.disconnect();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        cookies = temps;
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
            conn.getInputStream().close();
            conn.disconnect();
            return callback.FormatData(ret);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public callback getClassInformation(String planCode)
    {
        String ur = "http://202.115.47.141/student/courseSelect/thisSemesterCurriculum/ajaxStudentSchedule/callback";
        HttpURLConnection conn;
        URL url;
        try
        {
            url = new URL(ur);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);

            conn.setRequestProperty("Host", "202.115.47.141");
            //conn.setRequestProperty("Content-Length", "42");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Origin", "http://202.115.47.141");
            //conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            conn.setRequestProperty("Cookie", cookies);
            conn.setRequestProperty("Connection", "close");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Pragma", "no-cache");

            String params = String.format("&planCode=%s", planCode);

            OutputStream out = conn.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            out.close();
            conn.connect();
            String tString = getStringFromInputStream(conn.getInputStream());

            conn.getInputStream().close();
            conn.disconnect();
            return callback.FormatData(tString);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String ViewPage(String ur)
    {
        HttpURLConnection conn;
        URL url;
        try
        {
            url = new URL(ur);

            conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);

            conn.setRequestProperty("Host", "202.115.47.141");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "gzip, deflate");
            conn.setRequestProperty("Cookie", cookies);
            conn.setRequestProperty("Connection", "close");

            conn.connect();
            String tString = getStringFromInputStream(conn.getInputStream());
            conn.getInputStream().close();
            conn.disconnect();
            return tString;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Pair<String, String>> getPlanCode()
    {
        ArrayList<Pair<String, String>> arrayList = new ArrayList<>();
        String ss = this.ViewPage("http://202.115.47.141/student/courseSelect/calendarSemesterCurriculum/index");
        Document document = Jsoup.parse(ss);
        Elements s = document.selectFirst(".profile-info-value.col-xs-3").child(0).children();
        for (Element temp : s)
        {
            arrayList.add(new Pair<String, String>(temp.text(), temp.attr("value")));
        }
        return arrayList;
    }

    public Bitmap getCaptcha()
    {
        String ur = "http://202.115.47.141/img/captcha.jpg";
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
            conn.setRequestProperty("Cookie", cookies);
            conn.connect();

            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            conn.disconnect();
            return bitmap;
        } catch (IOException e)
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
