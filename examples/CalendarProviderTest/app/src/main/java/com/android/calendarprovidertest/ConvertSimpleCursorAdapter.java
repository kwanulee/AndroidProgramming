package com.android.calendarprovidertest;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ConvertSimpleCursorAdapter extends SimpleCursorAdapter {

    public ConvertSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);

        TextView tv_dtstart = convertView.findViewById(R.id.tv_dtstart);
        String dtstart_timeMilles = tv_dtstart.getText().toString();

        TextView tv_dtend = convertView.findViewById(R.id.tv_dtend);
        String dtend_timeMilles = tv_dtend.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        String dtstart_dateformat = sdf.format(new java.util.Date (Long.valueOf(dtstart_timeMilles)));
        tv_dtstart.setText(dtstart_dateformat);
        String dtend_dateformat = sdf.format(new java.util.Date (Long.valueOf(dtend_timeMilles)));
        tv_dtend.setText(dtend_dateformat);

        return convertView;
    }
}
