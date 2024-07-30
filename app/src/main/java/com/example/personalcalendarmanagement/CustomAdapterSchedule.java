package com.example.personalcalendarmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.personalcalendarmanagement.item.ItemSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomAdapterSchedule extends BaseAdapter {
    private Context context;
    private List<ItemSchedule> list;
    private LayoutInflater inflater;

    public CustomAdapterSchedule(Context context, List<ItemSchedule> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.lv_item_add_schedule, viewGroup, false);
        }

        TextView tvTitle = view.findViewById(R.id.txtTitle);
        TextView tvDescription = view.findViewById(R.id.txtDescription);
        TextView tvDayOfWeek = view.findViewById(R.id.txtDayOfWeek);
        TextView tvDay = view.findViewById(R.id.txtDay);
        TextView tvMonth = view.findViewById(R.id.txtMonth);
        TextView tvTime = view.findViewById(R.id.txtTime);

        ItemSchedule item = list.get(i);
        tvTitle.setText(item.getTitle());
        tvDescription.setText(item.getDescription());
        formatDate(item.getDate(), tvDayOfWeek, tvDay, tvMonth);
        tvTime.setText(item.getTime());

        return view;
    }

    private void formatDate(String stringDate, TextView dayOfWeek, TextView day, TextView month) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());

        try {
            Date date = input.parse(stringDate);
            dayOfWeek.setText(dayOfWeekFormat.format(date));
            day.setText(dayFormat.format(date));
            month.setText(monthFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            dayOfWeek.setText("");
            day.setText("");
            month.setText("");
        }
    }
}
