package com.kcmap.frame.ui;

/**
 * Created by lizhiwei on 2018/11/26.
 */


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kcmap.frame.work.AnyResponse;

import java.util.List;
import java.util.Map;


public class CustomListAdapter extends SimpleAdapter {

    private int[] colors = new int[] { 0x30FF0000, 0x300000FF };

    private Context ctx;

    private AnyResponse delegate;

    public CustomListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {

        super(context, data, resource, from, to);
        this.ctx=context;
        this.delegate=(AnyResponse)ctx;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        int colorPos = position % colors.length;
        if (colorPos == 1)
            view.setBackgroundColor(Color.argb(250, 255, 255, 255));
        else
            view.setBackgroundColor(Color.argb(250, 232, 241, 251));

        TextView rowid=(TextView)((ViewGroup) view).getChildAt(0);

        final int id=Integer.valueOf(rowid.getText().toString());

        int count=((ViewGroup)view).getChildCount();

        TextView lsc=(TextView)((ViewGroup) view).getChildAt(count-1);

        lsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.ResponseSelectRow(id);
            }
        });

        return view;
    }
}
