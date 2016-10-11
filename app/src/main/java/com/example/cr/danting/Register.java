package com.example.cr.danting;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by cr on 2016/10/10.
 */

public class Register extends Activity {
    private EditText register_user;
    private EditText register_password;
    private Button register_login;

    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        dataBaseHelper = new DataBaseHelper(this, "ZhangHu.db", null, 1);
        register_user = (EditText) findViewById(R.id.register_user);
        register_password = (EditText) findViewById(R.id.register_password);
        register_login = (Button) findViewById(R.id.register_login);

        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_text = Register.this.register_user.getText().toString();
                String password_text = Register.this.register_user.getText().toString();
                int c = zhuce(username_text, password_text);

                if (c==2) {
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    String message_success = "注册成功";
                    Toast.makeText(getApplicationContext(), message_success, Toast.LENGTH_SHORT).show();
                } else if (c==0) {
                    String message_error = "账号或密码不能为空，请重新输入";
                    Toast.makeText(getApplicationContext(), message_error, Toast.LENGTH_SHORT).show();
                }else {
                    String message_error = "该账号已存在，请重新输入";
                    Toast.makeText(getApplicationContext(), message_error, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    public int zhuce(String username, String password) {


        if ((username == null || username.length() <= 0) || (password == null || password.length() <= 0)) {
            return 0;

        } else {
            int i = 1;

            SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
            String sql = "select * from User where username=?";
            Cursor cursor = db.rawQuery(sql, new String[]{username});
            if (cursor.moveToFirst()) {
                cursor.close();
                i = 0;

            }

            if (i == 0) {
                return 1;

            } else {
                SQLiteDatabase d_b = dataBaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("username", username);
                values.put("password", password);
                d_b.insert("User", null, values);

                return 2;

            }
        }



    }

}
