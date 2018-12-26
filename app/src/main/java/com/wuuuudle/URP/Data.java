package com.wuuuudle.URP;

public class Data
{
    private String programPlanCode;
    private String programPlanName;
    private double totalUnits;
    private SelectCourse [] selectCourseList;
    private String courseCalendarList;

    public double getTotalUnits()
    {
        return totalUnits;
    }

    public String getCourseCalendarList()
    {
        return courseCalendarList;
    }

    public String getProgramPlanCode()
    {
        return programPlanCode;
    }

    public String getProgramPlanName()
    {
        return programPlanName;
    }

    public SelectCourse [] getSelectCourseList()
    {
        return selectCourseList;
    }

    public void setCourseCalendarList(String courseCalendarList)
    {
        this.courseCalendarList = courseCalendarList;
    }

    public void setProgramPlanCode(String programPlanCode)
    {
        this.programPlanCode = programPlanCode;
    }

    public void setProgramPlanName(String programPlanName)
    {
        this.programPlanName = programPlanName;
    }

    public void setSelectCourseList(SelectCourse [] selectCourseList)
    {
        this.selectCourseList = selectCourseList;
    }

    public void setTotalUnits(double totalUnits)
    {
        this.totalUnits = totalUnits;
    }
}