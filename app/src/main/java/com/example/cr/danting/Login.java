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
        setContentView(R.layout.login_layout);      //加载布局文件
        imageView = (ImageView) findViewById(R.id.music);   //加载ImageView控件
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
                String password_text = Login.this.password.getText().toString();
                int i=check(username_text, password_text,dataBaseHelper);

                if (i==1) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);

                } else if(i==0){
                    Toast.makeText(getApplicationContext(), "请先注册", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "用户名或密码输入错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }


    public int check(String username, String password,DataBaseHelper d) {
        username=username.replace(" ", "");
        password=password.replace(" ", "");


        if (isEmpty(username) || isEmpty(password)){
            return 0;//未注册
        }
        else{

            SQLiteDatabase db=d.getReadableDatabase();
            String sql = "select * from User";
            Cursor cursor = db.rawQuery(sql,null);
            int p=2;
            while(cursor.moveToNext()){
                if(username.equals(cursor.getString(1))&&password.equals(cursor.getString(2)))
                {
                    p=1;
//                    System.out.println(username+" "+password);
//                    System.out.println(cursor.getInt(0)+" "+cursor.getString(1)+" "+cursor.getString(2));
//                    System.out.println(username.equals(cursor.getString(1))+" "+password.equals(cursor.getString(2)));
                    return p;
                }
            }
            cursor.close();
            return p;

        }

    }
    public static boolean isEmpty( String input )
    {
        if ( input == null || "".equals( input ) )
            return true;

        for ( int i = 0; i < input.length(); i++ )
        {
            char c = input.charAt( i );
            if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
            {
                return false;
            }
        }
        return true;
    }


}
