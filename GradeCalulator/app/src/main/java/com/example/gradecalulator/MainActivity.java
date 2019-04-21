package com.example.gradecalulator;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
// link to login page
public class MainActivity extends AppCompatActivity {
    //init  private variable
    private DatabaseHelper db;
    private EditText username, password;
    private Button login, register;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //link javascript with xml component;
        db = new DatabaseHelper(this);
        SQLiteDatabase dbl = db.getWritableDatabase();

       //dbl.execSQL("drop table if exists user");
        //dbl.execSQL("create table user(Fname text, Lname text, username text primary key, password text, Email text, Phone text)");
      // dbl.execSQL("create table user(FName text, LName text, username text primary key, password text, Email text, Phone text, Program text)");
        username = (EditText)findViewById(R.id.loginUsername);
        password = (EditText)findViewById(R.id.loginPass);
        login = (Button)findViewById(R.id.loginLogin);
        // set button action
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = username.getText().toString();
                String s2 = password.getText().toString();
                // check login valid
                Boolean checklogin = db.toLogin(s1, s2);
                if(checklogin == true)
                {
                    //Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    openDialog5();
                    // move to next grade cal page and send variable
                    cursor = db.getInfo(s1);
                    cursor.moveToFirst();
                    System.out.println(cursor.getColumnCount());
                    if(cursor.getString(cursor.getColumnIndex("Program")).equals("Bachelor Degree")){
                        Intent i = new Intent(MainActivity.this, Bachelor_Grade_Cal.class);
                        i.putExtra("table_name",s1);
                        startActivity(i);
                    }
                    else if(cursor.getString(cursor.getColumnIndex("Program")).equals("Master in Cyber Security")){
                        Intent i = new Intent(MainActivity.this, Master_Grade_Cal.class);
                        i.putExtra("table_name",s1);
                        startActivity(i);
                    }

                }
                else
                    //Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                    openDialog6();
            }
        });

        register = (Button)findViewById(R.id.loginReg);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });
    }

    public void openDialog5() {
        Dialog5 dialog = new Dialog5();
        dialog.show(getSupportFragmentManager(), "login dialog");
    }

    public void openDialog6() {
        Dialog6 dialog = new Dialog6();
        dialog.show(getSupportFragmentManager(), "login dialog");
    }
}
