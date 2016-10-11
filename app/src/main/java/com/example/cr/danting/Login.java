package com.example.cr.danting;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by cr on 2016/10/10.
 */

public class Login extends Activity {
    private EditText user;
    private EditText password;
    private Button login;
    private Button register;
    private ImageView imageView;
    private DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        imageView=(ImageView)findViewById(R.id.music);
        imageView.setImageResource(R.drawable.music);


        dataBaseHelper = new DataBaseHelper(this, "ZhangHu.db", null, 1);
        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_text = Login.this.user.getText().toString();
                String password_text = Login.this.user.getText().toString();

                if (check(username_text, password_text)) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(),"请先注册",Toast.LENGTH_SHORT).show();

                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
    }




    public boolean check(String username, String password) {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        String sql = "select * from User where username=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;

        }

        return false;
    }



}
