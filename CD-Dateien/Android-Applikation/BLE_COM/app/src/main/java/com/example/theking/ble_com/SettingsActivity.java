package com.example.theking.ble_com;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SettingsActivity extends ListActivity {

    private Intent intent;

    public static final String EXTRA_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        String messageText = intent.getStringExtra(EXTRA_MESSAGE);
        String[] temp = messageText.split(",");

        ListView listAdresses = getListView();
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                temp);
        listAdresses.setAdapter(listAdapter);
    }

    public void onListItemClick(ListView listView,
                                View itemView,
                                int position,
                                long id) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", position);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
