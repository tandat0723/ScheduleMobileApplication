package com.example.personalcalendarmanagement.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

    public CustomAdapterSchedule(Context context, List<Schedule> listSchedule, MyDatabase myDatabase) {
        super(context, 0, listSchedule);
        this.context = context;
        this.listSchedule = listSchedule;
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

        Schedule item = listSchedule.get(position);
        tvTitle.setText(item.getTitle());
        tvDescription.setText(item.getDescription());
        tvTime.setText(item.getTime());
        formatDate(item.getDate(), tvDay, tvMonth, tvYear);

        convertView.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.schedule_item_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menu_update:
                        updateSchedule(item);
                        return true;
                    case R.id.menu_delete:
                        deleteSchedule(item);
                        return true;
                    default:
                        return false;
                }
            });

            popupMenu.show();
        });

        return convertView;
    }

    private void updateSchedule(Schedule schedule) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_schedule, null);
        builder.setView(dialogView);

        EditText edtTitle = dialogView.findViewById(R.id.edtTitle);
        EditText edtDescription = dialogView.findViewById(R.id.edtDescription);
        EditText edtType = dialogView.findViewById(R.id.edtType);
        EditText edtDate = dialogView.findViewById(R.id.edtDate);
        EditText edtTime = dialogView.findViewById(R.id.edtTime);

        edtTitle.setText(schedule.getTitle());
        edtDescription.setText(schedule.getDescription());
        edtType.setText(schedule.getType());
        edtTime.setText(schedule.getTime());
        edtDate.setText(schedule.getDate());

        builder.setPositiveButton("Lưu", (dialogInterface, i) -> {
            String newTitle = edtTitle.getText().toString().trim();
            String newDescription = edtDescription.getText().toString().trim();
            String newDate = edtDate.getText().toString().trim();
            String newTime = edtTime.getText().toString().trim();
            String newType = edtType.getText().toString().trim();
            if (newTitle.isEmpty() || newDescription.isEmpty() || newDate.isEmpty() || newTime.isEmpty() || newType.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            schedule.setTitle(newTitle);
            schedule.setDescription(newDescription);
            schedule.setType(newType);
            schedule.setDate(newDate);
            schedule.setTime(newTime);

            myDatabase = new MyDatabase(context);
            myDatabase.updateSchedule(schedule);
            notifyDataSetChanged();
            Toast.makeText(context, R.string.noti_update_schedule, Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteSchedule(Schedule schedule) {
        new AlertDialog.Builder(context).setTitle("Xóa lịch trình").setMessage("Bạn có chắc chắn muốn xóa lịch trình này không?").setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
            myDatabase = new MyDatabase(context);
            myDatabase.deleteSchedule(schedule.getSchedule_id());
            listSchedule.remove(schedule);
            notifyDataSetChanged();
            Toast.makeText(context, "Lịch trình đã xóa thành công", Toast.LENGTH_SHORT).show();
        }).setNegativeButton(android.R.string.no, null).show();
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