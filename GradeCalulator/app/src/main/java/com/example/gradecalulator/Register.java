package com.example.gradecalulator;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;
import static com.example.gradecalulator.Constants.GRADE;
import static com.example.gradecalulator.Constants.SUBJECT;
//import static com.example.gradecalulator.Constants.TABLE_NAME;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String TABLE_NAME;
    private DatabaseHelper db;
    private EditText fname, lname, username, pass, cpass, email, phone;
    private Spinner program;
    private Button register, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        fname = (EditText)findViewById(R.id.regFName);
        lname = (EditText)findViewById(R.id.regLName);
        username = (EditText)findViewById(R.id.regUsername);
        pass = (EditText)findViewById(R.id.regPass);
        cpass = (EditText)findViewById(R.id.regCPass);
        email = (EditText)findViewById(R.id.regEmail);
        phone = (EditText)findViewById(R.id.regPhone);
        program = (Spinner)findViewById(R.id.regProgram);
        register = (Button)findViewById(R.id.regReg);
        login = (Button)findViewById(R.id.regBack);

        // add value to spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.program, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        program.setAdapter(adapter);
        program.setOnItemSelectedListener(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = username.getText().toString();
                String s2 = pass.getText().toString();
                String s3 = cpass.getText().toString();
                String s4 = email.getText().toString();
                String s5 = fname.getText().toString();
                String s6 = lname.getText().toString();
                String s7 = phone.getText().toString();
                String s8 = program.getSelectedItem().toString();

                if(s1.equals("")||s2.equals("")||s3.equals("")||s4.equals("")||s5.equals("")||s6.equals("")||s7.equals("")){
                    //Toast.makeText(getApplicationContext(), "Field are empty", Toast.LENGTH_SHORT).show();
                    openDialog7();
                }
                else{
                    if(s2.equals(s3)){
                        Boolean userCheck = db.checkUser(s1);
                        Boolean emailCheck = db.checkEmail(s4);

                        if(userCheck == true){
                            if(emailCheck == true) {
                                Boolean insert = db.insert(s1, s2, s4, s5, s6, s7,s8);
                                //add grade list db
                                TABLE_NAME = s1;
                                SQLiteDatabase dbex = db.getWritableDatabase();
                                dbex.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT NOT NULL, " + GRADE + " TEXT NOT NULL);");
                                dbex.execSQL("CREATE UNIQUE INDEX idx_"+ s1 +" ON " + TABLE_NAME + " (" + SUBJECT +");");


                                if (insert == true) {
                                    //Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    openDialog1();
                                }
                            }
                            else{
                                //Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                                openDialog2();
                            }
                        }
                        else{
                            //Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                            openDialog3();
                        }
                    }
                    else //Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                        openDialog4();
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public void openDialog1() {
        Dialog1 dialog = new Dialog1();
        dialog.show(getSupportFragmentManager(), "register dialog");
    }

    public void openDialog2() {
        Dialog2 dialog = new Dialog2();
        dialog.show(getSupportFragmentManager(), "email error");
    }

    public void openDialog3() {
        Dialog3 dialog = new Dialog3();
        dialog.show(getSupportFragmentManager(), "username error");
    }

    public void openDialog4() {
        Dialog4 dialog = new Dialog4();
        dialog.show(getSupportFragmentManager(), "password error");
    }

    public void openDialog7() {
        Dialog7 dialog = new Dialog7();
        dialog.show(getSupportFragmentManager(), "password error");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
