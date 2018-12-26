package com.wuuuudle.URP;

import com.alibaba.fastjson.*;

public class callback
{
    private int allUnits;
    private String xkxx;
    private Data[] dateList;

    public static callback FormatData(String jsonstring)
    {
        //System.out.println(jsonstring);
        return (callback) JSONObject.parseObject(jsonstring, callback.class);
    }

    public int getAllUnits()
    {
        return allUnits;
    }

    public String getXkxx()
    {
        return xkxx;
    }

    public Data[] getDateList()
    {
        return dateList;
    }

    public void setAllUnits(int allUnits)
    {
        this.allUnits = allUnits;
    }

    public void setXkxx(String xkxx)
    {
        this.xkxx = xkxx;
    }

    public void setDateList(Data[] dateList)
    {
        this.dateList = dateList;
    }

    public int getWeekLength()
    {
        return this.getDateList()[0].getSelectCourseList()[0].getTimeAndPlaceList()[0].getClassWeek().length();
    }


}