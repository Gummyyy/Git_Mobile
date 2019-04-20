package com.example.gradecalulator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import static com.example.gradecalulator.Constants._ID;
import static com.example.gradecalulator.Constants.SUBJECT;
import static com.example.gradecalulator.Constants.GRADE;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class Master_Grade_Cal extends AppCompatActivity {
    private TextView CGPA;
    private Button Change;
    private Spinner SubjectSpinner;
    private Spinner GradeSpinner;
    private Button add_butt;
    private Button remove_butt;
    private TextView add_view;
    private DatabaseHelper helper;
    private ArrayList<Subject> Subject_List = new ArrayList<Subject>();
    private String TABLE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master__grade__cal);
        init();

        TABLE_NAME = getIntent().getStringExtra("table_name");
        String[] master_set = getResources().getStringArray(R.array.master_subject);

        ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, master_set);
        SubjectSpinner.setAdapter(adapterSubject);

        String[] grade_list = getResources().getStringArray(R.array.Grade_List);
        ArrayAdapter<String> adapterGrade = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, grade_list);
        GradeSpinner.setAdapter(adapterGrade);


        //assign grade to db
        try {
            Cursor cursor = getAllGrades();
            showGrades(cursor);
            calGrades(cursor);
        } finally {
            helper.close();
        }

        add_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addGrade(SubjectSpinner.getSelectedItem().toString(), GradeSpinner.getSelectedItem().toString());
                    Cursor cursor = getAllGrades();
                    showGrades(cursor);
                    calGrades(cursor);
                } finally {
                    helper.close();
                }
            }
        });

        remove_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    removeGrade();
                    Cursor cursor = getAllGrades();
                    showGrades(cursor);
                    calGrades(cursor);
                } finally {
                    helper.close();
                }
            }
        });
/**
        Change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent( Master_Grade_Cal.this, Bachlor_Grade.class);
                startActivity(intent);
            }
        });
**/




    }
    private void addGrade(String sub, String gra){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUBJECT, sub);
        values.put(GRADE, gra);
        db.execSQL("INSERT OR REPLACE INTO " +TABLE_NAME+ " (Subject,grade) VALUES('" + sub +"','" + gra+ "');");
    }
    private void removeGrade(){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.execSQL("DELETE FROM " +TABLE_NAME+ " WHERE "+ _ID +" = (SELECT MAX("+_ID+") FROM " + TABLE_NAME+");");
    }


    private static String[] COLUMNS = {_ID, SUBJECT, GRADE};
    private static String ORDER_BY = _ID + " ASC";

    private Cursor getAllGrades(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, ORDER_BY);

        return cursor;
    }

    private void calGrades(Cursor cursor){
        StringBuilder builder = new StringBuilder("");
        int count = 0;
        double sum = 0;
        double GPA = 4;
        while (cursor.moveToNext()){
            String grade = cursor.getString(2);
            //sum += Double.parseDouble(grade);
            count++;
            builder.append(count);
        }
        /** if(count != 0){
         GPA = sum/count;
         }else GPA = 0;
         builder.append(Double.toString(GPA));
         CGPA.setText(builder);**/
    }

    private void showGrades(Cursor cursor){
        StringBuilder builder = new StringBuilder("\t\t");
        int count = 0;
        double sum = 0;
        double GPA = 0;
        /**  for(Subject str: Subject_List){
         System.out.println(str.getSubject_detail());
         }**/

        while (cursor.moveToNext()){
            double temp_sum = 0;
            long id = cursor.getLong(0);
            String subject = cursor.getString(1);
            String grade = cursor.getString(2);

            for(Subject str: Subject_List){
                if(str.getSubject_detail().equals(subject)){
                    count += str.getCredit();
                    temp_sum = str.getCredit();
                }
            }
            System.out.println("credit :"+ temp_sum);
            //count ++;
            switch(grade){
                case "A": temp_sum *= 4; break;
                case "B+": temp_sum *= 3.5; break;
                case "B": temp_sum *= 3; break;
                case "C+": temp_sum *= 2.5; break;
                case "C": temp_sum *= 2; break;
                case "D+": temp_sum *= 1.5; break;
                case "D": temp_sum *= 1; break;
                case "F": temp_sum *= 0; break;
            }
            sum += temp_sum;
            // System.out.println("Credit = "+count);
            System.out.println("subject grade :"+ temp_sum);
            //  builder.append(id).append("    ");
            builder.append(subject);
            builder.append("\t\t").append(grade).append("\n\n\t\t");
        }
        //System.out.println(sum);
        GPA = sum/count;
        System.out.println(String.format("%.2f",GPA));
        CGPA.setText(String.format("%.2f",GPA));
        add_view.setText(builder);
    }

    private void init(){
        //Change = (Button)findViewById(R.id.Change);
        CGPA = (TextView)findViewById(R.id.Curr_GPA_Num);
        SubjectSpinner= (Spinner) findViewById(R.id.spinner_Subject);
        GradeSpinner = (Spinner) findViewById(R.id.spinner_Grade);
        add_butt = (Button)findViewById(R.id.add_button);
        remove_butt = (Button) findViewById(R.id.Remove_button);
        add_view =  (TextView)findViewById(R.id.add_view);
        helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();



        //crate subject and credit set

        // ArrayList<Subject> Subject_List = new ArrayList<Subject>();
        Subject_List.add(new Subject("ITCY511 Computer and Network Security (3)",3));
        Subject_List.add(new Subject("ITCY541 Digital Forensics Technologies and Techniques (3)",3));
        Subject_List.add(new Subject("ITCY513 Cyber Ethics and Law (2)",2));
        Subject_List.add(new Subject("ITCY512 Information Security Management (3)",3));
        Subject_List.add(new Subject("ITCY515 Research Methodology and Seminar in Cybersecurity and Information Assurance (1)",1));
        Subject_List.add(new Subject("ITCY531 System Hardening and Penetration Testing (3)",3));
        Subject_List.add(new Subject("ITCY571 Information Assurance and Risk Analysis (3)",3));
        Subject_List.add(new Subject("ITCY698 Thesis (3)",3));
        Subject_List.add(new Subject("ITCY698 Thesis (9)",9));
        Subject_List.add(new Subject("ITCY697 Research Project (6)",6));
        Subject_List.add(new Subject("ITCY514 Fraud Analysis and Detection (3)",3));
        Subject_List.add(new Subject("ITCY534 Reverse Engineering and Vulnerability Analysis (3)",3));
        Subject_List.add(new Subject("ITCY543 Network Forensics (3)",3));
        Subject_List.add(new Subject("ITCY544 Mobile Security (3)",3));
        Subject_List.add(new Subject("ITCY545 Cloud Security (3)",3));
        Subject_List.add(new Subject("ITCY561 Ethical Hacking (3)",3));
        Subject_List.add(new Subject("ITCY562 Intrusion Detection and Prevention (3)",3));
        Subject_List.add(new Subject("ITCY591 Special Topics in Cyber Security and Forensics (3)",3));
        Subject_List.add(new Subject("ITCY551 Application of Cryptography (3)",3));
        Subject_List.add(new Subject("ITCY552 Authentication Technology Management (3)",3));
        Subject_List.add(new Subject("ITCY553 Secure Software Design (3)",3));
        Subject_List.add(new Subject("ITCY572 Information and Social Networks Security (3)",3));
        Subject_List.add(new Subject("ITCY573 E-Services Security Management (3)",3));
        Subject_List.add(new Subject("ITCY581 Incident Response Management (3)",3));
        Subject_List.add(new Subject("ITCY592 Special Topics in Information Assurance (3)",3));





/**

 String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
 db.execSQL(DROP_USER_TABLE);

 db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT NOT NULL, " + GRADE + " TEXT NOT NULL);");**/
        //   db.execSQL("CREATE UNIQUE INDEX idx_GradeList_subject ON " + TABLE_NAME + " (" + SUBJECT +");");
    }


}
