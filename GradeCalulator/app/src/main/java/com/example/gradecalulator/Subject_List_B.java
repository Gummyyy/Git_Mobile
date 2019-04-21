package com.example.gradecalulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.gradecalulator.adapter.ExpandableListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Subject_List_B extends AppCompatActivity {
    // create private variable
    private ExpandableListView expandableListView;

    private ExpandableListViewAdapter expandableListViewAdapter;

    private List<String> listDataGroup;

    private HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject__list__b);

        // initializing the views
        initViews();

        // initializing the listeners
        initListeners();

        // initializing the objects
        initObjects();

        // preparing list data
        initListData();

    }


    /**
     * method to initialize the views
     */
    private void initViews() {

        expandableListView = findViewById(R.id.expandableListView);

    }

    /**
     * method to initialize the listeners
     */
    private void initListeners() {

        // ExpandableListView on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataGroup.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataGroup.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // ExpandableListView Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ExpandableListView Group collapsed listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed),
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * method to initialize the objects
     */
    private void initObjects() {

        // initializing the list of groups
        listDataGroup = new ArrayList<>();

        // initializing the list of child
        listDataChild = new HashMap<>();

        // initializing the adapter object
        expandableListViewAdapter = new ExpandableListViewAdapter(this, listDataGroup, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(expandableListViewAdapter);

    }

    /*
     * Preparing the list data
     *
     * Dummy Items
     */
    private void initListData() {


        // Adding metadata group data
        listDataGroup.add(getString(R.string.text_y1t1));
        listDataGroup.add(getString(R.string.text_y1t2));
        listDataGroup.add(getString(R.string.text_y2t1));
        listDataGroup.add(getString(R.string.text_y2t2));
        listDataGroup.add(getString(R.string.text_y3t1));
        listDataGroup.add(getString(R.string.text_y3t2));
        listDataGroup.add(getString(R.string.text_y4t1));
        listDataGroup.add(getString(R.string.text_y4t2));


        // array of strings
        String[] array;

        // add subject list in each semester to dict
        List<String> Bachelor1_1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Bachlor_subject1_1);
        for (String item : array) {
            Bachelor1_1.add(item);
        }
        listDataChild.put(listDataGroup.get(0),  Bachelor1_1);


        List<String> Bachelor1_2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Bachlor_subject1_2);
        for (String item : array) {
            Bachelor1_2.add(item);
        }
        listDataChild.put(listDataGroup.get(1), Bachelor1_2);

        List<String> Bachelor2_1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Bachlor_subject2_1);
        for (String item : array) {
            Bachelor2_1.add(item);
        }
        listDataChild.put(listDataGroup.get(2), Bachelor2_1);

        List<String> Bachelor2_2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Bachlor_subject2_2);
        for (String item : array) {
            Bachelor2_2.add(item);
        }
        listDataChild.put(listDataGroup.get(3), Bachelor2_2);

        List<String> Bachelor3_1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Bachlor_subject3_1);
        for (String item : array) {
            Bachelor3_1.add(item);
        }
        listDataChild.put(listDataGroup.get(4), Bachelor3_1);

        List<String> Bachelor3_2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Bachlor_subject3_2);
        for (String item : array) {
            Bachelor3_2.add(item);
        }
        listDataChild.put(listDataGroup.get(5), Bachelor3_2);

        List<String> Bachelor4_1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Bachlor_subject4_1);
        for (String item : array) {
            Bachelor4_1.add(item);
        }
        listDataChild.put(listDataGroup.get(6), Bachelor4_1);

        List<String> Bachelor4_2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Bachlor_subject4_2);
        for (String item : array) {
            Bachelor4_2.add(item);
        }
        listDataChild.put(listDataGroup.get(7), Bachelor4_2);


        // Adding child data
        //listDataChild.put(listDataGroup.get(0), BachelorDegreeList);
        //listDataChild.put(listDataGroup.get(1), MasterDegreeList);


        // notify the adapter
        expandableListViewAdapter.notifyDataSetChanged();
    }


}
