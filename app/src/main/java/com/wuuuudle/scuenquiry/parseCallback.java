package com.wuuuudle.scuenquiry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wuuuudle.URP.SelectCourse;
import com.wuuuudle.URP.TimeAndPlace;

import java.util.ArrayList;
import java.util.Random;
import com.wuuuudle.URP.callback;

public class parseCallback
{
    private static int[] week = new int[]{0, 3, 4, 5, 6, 7, 8, 2};
    private static long startMilles = 1535817600000l;

    private Random random = new Random();
    private callback callback;

    public parseCallback(callback data)
    {
        callback = data;
    }

    public ArrayList<TextView> getTextView(final Context context)
    {
        int week = getCurrentWeek();
        ArrayList<TextView> ret = new ArrayList<>();
        SelectCourse[] course = callback.getDateList()[0].getSelectCourseList();
        for (SelectCourse temp : course)
        {
            final SelectCourse t1 = temp;
            int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            final String descripe = temp.getCourseName() + "@";

            for (TimeAndPlace temp2 : temp.getTimeAndPlaceList())
            {
                final TimeAndPlace t2 = temp2;
                if(t2.getClassWeek().charAt(week) == '0')
                    continue;
                TextView textView = parseCallback.getTextView(temp2.getClassDay(), temp2.getClassSessions(), temp2.getContinuingSession(), descripe + temp2.getCampusName() + temp2.getTeachingBuildingName() + temp2.getClassroomName(), color, context);
                textView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("coureNumber",t1.getId().getCoureNumber());
                        intent.putExtra("coureSequenceNumber",t1.getId().getCoureSequenceNumber());
                        intent.putExtra("courseName",t1.getCourseName());
                        intent.putExtra("attendClassTeacher",t1.getAttendClassTeacher());
                        intent.putExtra("unit",String.valueOf(t1.getUnit()));
                        intent.putExtra("examTypeName", t1.getExamTypeName());

                        context.startActivity(intent);
                    }
                });

                ret.add(textView);
            }
        }
        return ret;
    }

    public static int getCurrentWeek()
    {
        long currentTime = System.currentTimeMillis();
        return (int)(currentTime-startMilles)/(7*24*60*60*1000);
    }

    private static TextView getTextView(int classDay, int classSessions, int continuingSession, String Description, int color, Context context)
    {
        TextView textView = new TextView(context);
        textView.setText(Description);

        android.support.v7.widget.GridLayout.Spec columnSpec = android.support.v7.widget.GridLayout.spec(week[classDay], 1, 1);
        android.support.v7.widget.GridLayout.Spec rowSpec = android.support.v7.widget.GridLayout.spec(classSessions, continuingSession, continuingSession);
        android.support.v7.widget.GridLayout.LayoutParams layoutParams = new android.support.v7.widget.GridLayout.LayoutParams(rowSpec, columnSpec);

        textView.setLayoutParams(layoutParams);
        textView.setBackgroundResource(R.drawable.text_view_border);
        textView.setGravity(Gravity.CENTER);
        textView.setWidth(-2);
        textView.setHeight(-2);

        //textView.setBackgroundColor(color);
        textView.setTextSize(8);

        return textView;
    }
}
