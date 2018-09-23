package com.wuuuudle.scuenquiry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classdetails);
        Intent intent = getIntent();

        ((TextView)findViewById(R.id.coureNumber)).setText("课程号:"+intent.getStringExtra("coureNumber"));
        ((TextView)findViewById(R.id.coureSequenceNumber)).setText("课序号:"+intent.getStringExtra("coureSequenceNumber"));
        ((TextView)findViewById(R.id.courseName)).setText("课程名:"+intent.getStringExtra("courseName"));
        ((TextView)findViewById(R.id.attendClassTeacher)).setText("教师:"+intent.getStringExtra("attendClassTeacher"));
        ((TextView)findViewById(R.id.unit)).setText("学分:"+intent.getStringExtra("unit"));
        ((TextView)findViewById(R.id.examTypeName)).setText("考试类型:"+intent.getStringExtra("examTypeName"));
    }
}
