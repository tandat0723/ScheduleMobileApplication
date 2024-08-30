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

public class CustomAdapterSchedule extends ArrayAdapter<Schedule> {
    private Context context;
    private List<Schedule> listSchedule;
    private MyDatabase myDatabase;
    private int resource;

    public CustomAdapterSchedule(Context context, int resource, List<Schedule> listSchedule) {
        super(context, 0, listSchedule);
        this.context = context;
        this.listSchedule = listSchedule;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvTitle = convertView.findViewById(R.id.txtTitle);
        TextView tvDescription = convertView.findViewById(R.id.txtDescription);
        TextView tvDayOfWeek = convertView.findViewById(R.id.txtDayOfWeek);
        TextView tvDay = convertView.findViewById(R.id.txtDay);
        TextView tvMonth = convertView.findViewById(R.id.txtMonth);
        TextView tvTime = convertView.findViewById(R.id.txtTime);

        Schedule item = listSchedule.get(position);
        tvTitle.setText(item.getTitle());
        tvDescription.setText(item.getDescription());
        formatDate(item.getDate(), tvDayOfWeek, tvDay, tvMonth);
        tvTime.setText(item.getTime());

        return convertView;
    }

    private void formatDate(String stringDate, TextView dayOfWeek, TextView day, TextView month) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());

        try {
            Date date = input.parse(stringDate);
            assert date != null;
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
