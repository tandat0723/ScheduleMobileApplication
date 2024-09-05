package com.example.personalcalendarmanagement.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.personalcalendarmanagement.R;
import com.example.personalcalendarmanagement.Utils.Utils;
import com.example.personalcalendarmanagement.data.MyDatabase;
import com.example.personalcalendarmanagement.data.Roles;
import com.example.personalcalendarmanagement.data.User;

import java.util.List;

public class CustomAdapterAdmin extends ArrayAdapter<User> {
    private MyDatabase myDatabase;
    private List<User> userList;
    private Context context;

    public CustomAdapterAdmin(Context context, List<User> userList, MyDatabase myDatabase) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
        this.myDatabase = myDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_admin, parent, false);
        }

        TextView tvFullName = convertView.findViewById(R.id.txtFullName);
        TextView tvUsername = convertView.findViewById(R.id.txtUsername);
        TextView tvPassword = convertView.findViewById(R.id.txtPassword);
        TextView tvRoleName = convertView.findViewById(R.id.txtRoleName);

        User item = userList.get(position);
        tvFullName.setText(item.getName());
        tvUsername.setText(item.getUsername());
        tvPassword.setText("********");
        tvRoleName.setText(item.getRole_id().getName());

        convertView.setOnClickListener(view -> {
            PopupMenu menu = new PopupMenu(context, view);

            menu.inflate(R.menu.admin_item_menu);
            menu.setOnMenuItemClickListener(itemMenu -> {
                switch (itemMenu.getItemId()) {
                    case R.id.item_update:
                        if (item.getRole_id().equals(Roles.Administrator)){
                            Toast.makeText(context, "Không thể chỉnh sửa Administrator", Toast.LENGTH_SHORT).show();
                        } else {
                            updateUserAdmin(item);
                        }
                        return true;
                    case R.id.item_delete:
                        if (item.getRole_id().equals(Roles.Administrator)) {
                            Toast.makeText(context, "Không thể xóa Administrator", Toast.LENGTH_SHORT).show();
                        } else {
                            deleteUserAdmin(item);
                        }
                        return true;
                    default:
                        return false;
                }
            });
            menu.show();
        });

        return convertView;
    }

    private void updateUserAdmin(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_user, null);
        builder.setView(dialogView);

        EditText edtFullName = dialogView.findViewById(R.id.edtFullName);
        EditText edtUsername = dialogView.findViewById(R.id.edtUsername);
        EditText edtPassword = dialogView.findViewById(R.id.edtPassword);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinnerRole);

        edtFullName.setText(user.getName());
        edtUsername.setText(user.getUsername());
        edtPassword.setText("");

        SharedPreferences preferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int currentUserId = preferences.getInt("user_id", -1);
        User currentUser = myDatabase.getUserById(currentUserId);
        Roles[] availableRoles;

        if (currentUser.getRole_id() == Roles.Administrator) {
            availableRoles = new Roles[]{Roles.User, Roles.Admin};
        } else if (currentUser.getRole_id() == Roles.Admin) {
            availableRoles = new Roles[]{user.getRole_id()};
            spinnerRole.setEnabled(false);
        } else {
            availableRoles = new Roles[]{};
        }

        ArrayAdapter<Roles> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, availableRoles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        int role = adapter.getPosition(user.getRole_id());
        spinnerRole.setSelection(role);

        builder.setPositiveButton("Cập nhật", (dialogInterface, i) -> {
            String fullName = edtFullName.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            Roles selectedRole = (Roles) spinnerRole.getSelectedItem();

            if (fullName.isEmpty() || username.isEmpty()) {
                Toast.makeText(context, "Thông tin không để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                password = user.getPassword();
            } else {
                password = Utils.hashPassword(password);
            }

            user.setName(fullName);
            user.setUsername(username);
            user.setPassword(password);
            user.setRole_id(selectedRole);

            myDatabase.updateUser(user);
            notifyDataSetChanged();
            Toast.makeText(context, "Cập nhật tài khoản thành công", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void deleteUserAdmin(User user) {
        new AlertDialog.Builder(context).setTitle("Xóa tài khoản").setMessage("Bạn có chắc chắn muốn xóa tài khoản này không?")
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                    myDatabase = new MyDatabase(context);
                    myDatabase.deleteUser(user.getUser_id());
                    userList.remove(user);
                    notifyDataSetChanged();
                }).setNegativeButton(android.R.string.no, null).show();
    }

}
