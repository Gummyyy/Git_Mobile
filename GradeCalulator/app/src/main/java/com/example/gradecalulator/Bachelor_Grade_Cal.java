package com.example.gradecalulator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import static android.provider.BaseColumns._ID;
import static com.example.gradecalulator.Constants.GRADE;
import static com.example.gradecalulator.Constants.SUBJECT;
//import static com.example.gradecalulator.Constants.TABLE_NAME;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class Bachelor_Grade_Cal extends AppCompatActivity {
    private Button Change;
    private TextView CGPA;
    private Spinner SubjectSpinner;
    private Spinner GradeSpinner;
    private Button add_butt;
    private Button remove_butt;
    private TextView add_view;
    private DatabaseHelper helper;
    private ArrayList<Subject> Subject_List = new ArrayList<Subject>();
    private String TABLE_NAME;
    private TextView nav_header;
    private TextView nav_sub;
    private NavigationView navbar;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bachelor__grade__cal);
        init();

        TABLE_NAME = getIntent().getStringExtra("table_name");
        SQLiteDatabase db = helper.getWritableDatabase();
        cursor = helper.getInfo(TABLE_NAME);
        cursor.moveToFirst();
        init_nav(cursor);
        String[] bachlor_set = getResources().getStringArray(R.array.Bachlor_subject);
        ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, bachlor_set);
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
                Intent intent = new Intent( Bachlor_Grade.this, MainActivity.class);
                startActivity(intent);
            }
        });**/




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
            // builder.append(id).append("    ");
            builder.append(subject);
            builder.append("\t\t").append(grade).append("\n\n\t\t");
        }
        //System.out.println(sum);
        GPA = sum/count;
        System.out.println(String.format("%.2f",GPA));
        CGPA.setText(String.format("%.2f",GPA));
        add_view.setText(builder);
    }
    private void init_nav(Cursor user){
        navbar = (NavigationView) findViewById(R.id.nav_view);

        View inflatedView = getLayoutInflater().inflate(R.layout.nav_header_main, null);
        nav_sub = (TextView) inflatedView.findViewById(R.id.nav_sub) ;
        nav_header = (TextView) inflatedView.findViewById(R.id.nav_header) ;
        nav_sub.setText(user.getString(cursor.getColumnIndex("Email")));
        nav_header.setText(user.getString(cursor.getColumnIndex("FName")));
        navbar.addHeaderView(inflatedView);

        navbar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                boolean c =false;
                Intent i;
                switch(id)
                {
                    default:
                        c = true;
                    case R.id.Subject_List:
                        i = new Intent( Bachelor_Grade_Cal.this, Subject_List_B.class);
                      //  i.putExtra();
                        startActivity(i);
                        break;
                    case R.id.Grade_Cal:
                        break;
                    case R.id.LogOut:
                        i = new Intent( Bachelor_Grade_Cal.this, MainActivity.class);
                        //  i.putExtra();
                        startActivity(i);
                        break;
                }
                return c;
            }
        });
    }

    private void init(){
       // Change = (Button)findViewById(R.id.Change);
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
        Subject_List.add(new Subject("SCBI109 Integrated Biology (3)",3));
        Subject_List.add(new Subject("SHSS103 Man and Society (2)",2));
        Subject_List.add(new Subject("SHSS116 Comparative Culture (2)",2));
        Subject_List.add(new Subject("SCCH100 Integrated Chemistry (3)",3));
        Subject_List.add(new Subject("ITCS320 Discrete Structures (3)",3));
        Subject_List.add(new Subject("ITCS211 Introduction to Digital Systems (3)",3));
        Subject_List.add(new Subject("ITCS375 Advanced Mathematics I for Computer Science (3)",3));
        Subject_List.add(new Subject("ITCS161 Physical Science and Computation (3)",3));
        Subject_List.add(new Subject("ITGE101 Problem Solving Techniques (2)",2));
        Subject_List.add(new Subject("ITCS208 Object Oriented Programming (3)",3));
        Subject_List.add(new Subject("ITCS200 Fundamentals of Programming (3)",3));
        Subject_List.add(new Subject("ITCS302 Technical English II (2)",2));
        Subject_List.add(new Subject("ITCS301 Technical English I (2)",2));
        Subject_List.add(new Subject("ITCS125 Applied Statistics for Computing (3)",3));
        Subject_List.add(new Subject("SPGE102 Yoga for Health (1)",1));
        Subject_List.add(new Subject("ITLG182 Reading Skills (2)",2));
        Subject_List.add(new Subject("ITLG181 Public Speaking and Presentation (2)",2));
        Subject_List.add(new Subject("ITCS306 Numerical Methods (3)",3));
        Subject_List.add(new Subject("ITCS323 Computer Data Communication (3)",3));
        Subject_List.add(new Subject("ITID276 Management (2)",2));
        Subject_List.add(new Subject("ITCS381 Introduction to Multimedia Systems (3)",3));
        Subject_List.add(new Subject("ITCS210 Web Programming (3)",3));
        Subject_List.add(new Subject("ITCS343 Principles of Operating Systems (3)",3));
        Subject_List.add(new Subject("ITCS222 Computer Organization and Architecture (3)",3));
        Subject_List.add(new Subject("ITCS411 Database Management Systems (3)",3));
        Subject_List.add(new Subject("ITCS321 Data Structures and Algorithm Analysis (3)",3));
        Subject_List.add(new Subject("ITGE301 Communication Strategies in Professional Life (2)",2));
        Subject_List.add(new Subject("ITCS159 Software Lab for Basic Scientific Problem Solving (1)",1));
        Subject_List.add(new Subject("ITCS335 Introduction to E-business Systems (3)",3));
        Subject_List.add(new Subject("ITLG281 Business Writing (2)",2));
        Subject_List.add(new Subject("ITLG282 Academic Writing (2)",2));
        Subject_List.add(new Subject("ITCS371 Introduction to Software Engineering (3)",3));
        Subject_List.add(new Subject("ITCS424 Wireless and Mobile Computing (3)",3));
        Subject_List.add(new Subject("ITCS414 Information Storage and Retrieval (3)",3));
        Subject_List.add(new Subject("ITCS461 Computer and Communication Security (3)",3));
        Subject_List.add(new Subject("ITCS420 Computer Networks (3)",3));
        Subject_List.add(new Subject("ITCS336 Human Computer Interface (3)",3));
        Subject_List.add(new Subject("ITCS451 Artificial Intelligence (3)",3));
        Subject_List.add(new Subject("ITCS391 Computer Network Lab (1)",1));
        Subject_List.add(new Subject("ITCS443 Distributed and Parallel Systems (3)",3));
        Subject_List.add(new Subject("ITCS361 Management Information Systems (3)",3));
        Subject_List.add(new Subject("ITCS402 Computer and Business Ethics (3)",3));
        Subject_List.add(new Subject("ITID277 Digital Marketing (2)",2));
        Subject_List.add(new Subject("ITCS499 Senior Project (6)",6));








/**
 String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
 db.execSQL(DROP_USER_TABLE);

 db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT NOT NULL, " + GRADE + " TEXT NOT NULL);");**/
        // db.execSQL("CREATE UNIQUE INDEX Bachlor ON " + TABLE_NAME + " (" + SUBJECT +");");
    }


}
