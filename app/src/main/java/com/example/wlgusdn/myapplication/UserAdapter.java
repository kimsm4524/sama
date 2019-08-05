package com.example.wlgusdn.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    private ArrayList<UserData> UserData = new ArrayList<UserData>();
    LayoutInflater inflater = null;
    private int Count = 0;
    TextView tv;

    public UserAdapter(ArrayList<UserData> userdata)
    {

        UserData=userdata;
        Count=1;




    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.trade_user, parent, false);

        }

        //구매글들에 대한 정보들

        ImageView UserProfile = convertView.findViewById(R.id.User_Profile);
        TextView UserName =  convertView.findViewById(R.id.User_Name);
        TextView UserPhone =  convertView.findViewById(R.id.User_Phone);

        UserProfile.setImageBitmap(UserData.get(position).Profile);
        UserName.setText(UserData.get(position).Name);
        UserPhone.setText(UserData.get(position).Phone);

        UserPhone.setPaintFlags(UserPhone.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        return convertView;
    }
}
