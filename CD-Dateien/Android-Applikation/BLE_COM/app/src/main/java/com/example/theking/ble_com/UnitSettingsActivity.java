package com.example.theking.ble_com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class UnitSettingsActivity extends AppCompatActivity {

    private Spinner temp_spinner,
            velocity_spinner,
            pressure_spinner;
    private EditText wheelsizeEditText, calibTempEditText;

    public final static String ACTUAL_SETTING = "actual_setting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_settings);

        temp_spinner = (Spinner)findViewById(R.id.temp_spinner);
        velocity_spinner = (Spinner)findViewById(R.id.velocity_spinner);
        pressure_spinner = (Spinner)findViewById(R.id.pressure_spinner);
        wheelsizeEditText = (EditText)findViewById(R.id.wheelsizeEditText);
        calibTempEditText = (EditText)findViewById(R.id.calibTempEditTex);

        Intent settings = getIntent();
        String actSettings = settings.getStringExtra(ACTUAL_SETTING);
        String temp[] = actSettings.split(",");

        if(temp[0].compareTo("celsius") == 0){
            temp_spinner.setSelection(0);
        }
        else if(temp[0].compareTo("fahrenheit") == 0){
            temp_spinner.setSelection(1);
        }
        else {
            temp_spinner.setSelection(2);
        }

        if (temp[1].compareTo("kmh") == 0){
            velocity_spinner.setSelection(0);
        }
        else{
            velocity_spinner.setSelection(1);
        }

        if (temp[2].compareTo("hPa") == 0){
            pressure_spinner.setSelection(0);
        }
        else if (temp[2].compareTo("bar") == 0){
            pressure_spinner.setSelection(1);
        }
        else if (temp[2].compareTo("atm") == 0){
            pressure_spinner.setSelection(2);
        }
        else if (temp[2].compareTo("psi") == 0){
            pressure_spinner.setSelection(3);
        }
        else {
            pressure_spinner.setSelection(4);
        }

        wheelsizeEditText.setText(temp[3]);
    }

    public void onClickSave(View view){
        int temp = temp_spinner.getSelectedItemPosition(),
                velocity = velocity_spinner.getSelectedItemPosition(),
                pressure = pressure_spinner.getSelectedItemPosition();
        float wheeelsize = Float.parseFloat(wheelsizeEditText.getText().toString());
        float calibTemperature = 0;
        String unitSettings;

        String calibTemp = calibTempEditText.getText().toString();

        if (calibTemp.compareTo("") != 0){
            calibTemperature = Float.parseFloat(calibTempEditText.getText().toString());
        }

        if (calibTemperature != 0){
            unitSettings = temp + "," + velocity + "," + pressure + "," + wheeelsize + "," + calibTemperature;
        }
        else {
            unitSettings = temp + "," + velocity + "," + pressure + "," + wheeelsize;
        }


        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", unitSettings);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

}
