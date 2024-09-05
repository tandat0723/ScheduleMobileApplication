package com.example.personalcalendarmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.personalcalendarmanagement.R;
import com.example.personalcalendarmanagement.data.MyDatabase;
import com.example.personalcalendarmanagement.data.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomAdapterScheduleHistory extends ArrayAdapter<Schedule> {
    private Context context;
    private List<Schedule> list;
    private MyDatabase myDatabase;

    public CustomAdapterScheduleHistory(Context context, List<Schedule> list, MyDatabase myDatabase) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
        this.myDatabase = myDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_add_schedule, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.txtTitle);
        TextView tvDescription = convertView.findViewById(R.id.txtDescription);
        TextView tvDay = convertView.findViewById(R.id.txtDayOfWeek);
        TextView tvMonth = convertView.findViewById(R.id.txtDay);
        TextView tvYear = convertView.findViewById(R.id.txtMonth);
        TextView tvTime = convertView.findViewById(R.id.txtTime);

        Schedule item = list.get(position);
        tvTitle.setText(item.getTitle());
        tvDescription.setText(item.getDescription());
        tvTime.setText(item.getTime());
        formatDate(item.getDate(), tvDay, tvMonth, tvYear);

        return convertView;
    }

    private void formatDate(String stringDate, TextView day, TextView month, TextView year) {
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date date = input.parse(stringDate);
            if (date != null) {
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
                day.setText(dayFormat.format(date));
                month.setText(monthFormat.format(date));
                year.setText(yearFormat.format(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            day.setText("");
            month.setText("");
            year.setText("");
        }
    }
}
