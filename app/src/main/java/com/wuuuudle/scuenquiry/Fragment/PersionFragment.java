package com.wuuuudle.scuenquiry.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuuuudle.URP.Login;
import com.wuuuudle.scuenquiry.LoginActivity;
import com.wuuuudle.scuenquiry.MainActivity;
import com.wuuuudle.scuenquiry.R;
import com.wuuuudle.scuenquiry.parseCallback;

public class PersionFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.personfragment, container, false);
        TextView textView = view.findViewById(R.id.week);
        textView.setText(String.valueOf("第" + (parseCallback.getCurrentWeek() + 1)) + "周");

        view.findViewById(R.id.LogOut).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Login.LoginOut();
                MainActivity.toLogin();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
}
