package com.example.theking.ble_com;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

public class BLE_COM extends AppCompatActivity {

    // Enumerationen
    private enum tempSetting{
        celsius(1, 0, " °C"),
        fahrenheit(1.8, 32, " °F"),
        kelvin(1, -273.15, " K");

        private double factor, summand;
        private String unit;

        private tempSetting(double factor, double summand, String unit){
            this.factor = factor;
            this.summand = summand;
            this.unit = unit;
        }
    }
    private enum velocitySetting{
        kmh(1, 0, " km/h"),
        mph(0.62137, 0, " mph");

        private double factor, summand;
        private String unit;

        private velocitySetting(double factor, double summand, String unit){
            this.factor = factor;
            this.summand = summand;
            this.unit = unit;
        }
    }
    private enum pressureSetting{
        hPa(1, 0, " hPa"),
        bar(0.001, 0, " bar"),
        atm(0.00987, 0, " atm"),
        psi(0.0145, 0, " psi"),
        mmHG(0.75, 0, " mmHG");

        private double factor, summand;
        private String unit;

        private pressureSetting(double factor, double summand, String unit){
            this.factor = factor;
            this.summand = summand;
            this.unit = unit;
        }
    }

    // Konstanten (private)
    private final static int REQUEST_ENABLE_BT = 0, SELECT_SENSOR = 1, UNIT_SETTINGS = 2;
    private final static byte lengthFilter = 23, typeFilter = 3;
    private final static byte[] uuidFilter = {-34 , -70}; // DE , BA
    private final static int noAdressFilter = -1, noAdressesListed = -1;
    private final static float pressureFactor = (float)0.01, temperaturFactor = (float)0.001,
            value1Sec = (float)65536, pressure0m = (float)1013.25, refHight = (float)-7991,
            kmhFactor = (float)3.6, tempNotCalibrated = (float)-300, huminityFactor = (float)0.01;
    private final static char mask = 0x00FF;
    private final static int delay1s = 1000;

    // Variabeln (private)
    private BluetoothAdapter mBluetoothAdapter;
    private ImageView tacho;
    private boolean mScanning;
    private TextView velocityText, tempText, altitudeText, pressureText, huminityText;
    private int seconds, minutes, hours;
    private byte length, type;
    private byte[] uuid, data, id;
    private String adress;
    private List<String> adressList;
    private int adressCounter = noAdressesListed;
    private int selectedAdress = noAdressFilter;
    private Calendar calendar;
    private tempSetting actualTempSetting = tempSetting.celsius;
    private velocitySetting actualVelocitySetting = velocitySetting.kmh;
    private pressureSetting actualPressureSetting = pressureSetting.hPa;
    private float velocity = 0, pressure = 0, altitude = 0, temperature = 0, huminity = 0,
            actVelocity = (float)15.27, actPressure = (float)966.23, actAltitude = (float)826.88, actTemperatur = (float)19.39,
            actHuminity = (float)23.61,wheelsize = (float)2.04, tempOffset = 0, calibTemp = tempNotCalibrated;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble__com);

        //statusText = (TextView) findViewById(R.id.statusText);
        velocityText = (TextView)findViewById(R.id.velocityText);
        tempText = (TextView)findViewById(R.id.temperatureText);
        altitudeText = (TextView)findViewById(R.id.altitudeText);
        pressureText = (TextView)findViewById(R.id.pressureText);
        huminityText = (TextView)findViewById(R.id.huminityText);
        tacho = (ImageView)findViewById(R.id.tacho);

        uuid = new byte[2];
        id = new byte[2];
        adressList = new ArrayList<String>();
        data = new byte[lengthFilter - 7];


        //Check if the device supports BluetoothLowEnergy. If not: finish the app with a Errormassege to the User.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE wird nicht unterstützt.", Toast.LENGTH_SHORT).show();
            finish();
        }

        BLE_init();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                calculate();
                update_screen();
                handler.postDelayed(this, delay1s);
            }
        }, delay1s);
    }

    private void BLE_init(){
        // Check if BLE-Scan isn't running already.
        if (!mScanning){

            // Initializes Bluetooth adapter.
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();

            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            scanLeDevice(true);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        //Check if it was the Request for enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT) {

            //Act only if the User clicked no.
            if (resultCode != RESULT_OK) {

                //BLE was not enabled by the user. Show an Error-Popup and finish the app.
                Toast.makeText(this, "BLE nicht aktiviert", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else if (requestCode == SELECT_SENSOR){
            if (resultCode == RESULT_OK){
                int index = data.getIntExtra("result", -1);
                selectedAdress = index;
            }
        }
        else if (requestCode == UNIT_SETTINGS){
            if (resultCode == RESULT_OK){
                String savedSettings = data.getStringExtra("result");
                String[] settings = savedSettings.split(",");

                switch (Integer.parseInt(settings[0])){
                    case 0:{
                        actualTempSetting = tempSetting.celsius;
                        break;
                    }
                    case 1:{
                        actualTempSetting = tempSetting.fahrenheit;
                        break;
                    }
                    case 2:{
                        actualTempSetting = tempSetting.kelvin;
                        break;
                    }
                    default:{
                        break;
                    }
                }

                switch (Integer.parseInt(settings[1])){
                    case 0:{
                        actualVelocitySetting = velocitySetting.kmh;
                        break;
                    }
                    case 1:{
                        actualVelocitySetting = velocitySetting.mph;
                        break;
                    }
                    default:{
                        break;
                    }
                }

                switch (Integer.parseInt(settings[2])){
                    case 0:{
                        actualPressureSetting = pressureSetting.hPa;
                        break;
                    }
                    case 1:{
                        actualPressureSetting = pressureSetting.bar;
                        break;
                    }
                    case 2:{
                        actualPressureSetting = pressureSetting.atm;
                        break;
                    }
                    case 3:{
                        actualPressureSetting = pressureSetting.psi;
                        break;
                    }
                    case 4:{
                        actualPressureSetting = pressureSetting.mmHG;
                        break;
                    }
                    default:{
                        break;
                    }
                }


                wheelsize = Float.parseFloat(settings[3]);

                if (settings.length > 4){
                    calibTemp = Float.parseFloat(settings[4]);
                }


                calculate();
                update_screen();
            }
        }
    }

    // Start/stops the BLE-Scan
    private void scanLeDevice(final boolean enable){
        if (enable){
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
        else{
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // wird aufgerufen, wenn Daten empfangen werden
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback(){
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    calendar = Calendar.getInstance();
                    seconds = calendar.get(Calendar.SECOND);
                    minutes = calendar.get(Calendar.MINUTE);
                    hours = calendar.get(Calendar.HOUR);
                    //Copy the received data.
                    byte[] receiveddata = scanRecord.clone();
                    if (checkReceivedData(receiveddata)){
                        adress = device.getAddress();
                        saveAdress();

                        if (checkAdress()) {
                            length = receiveddata[0];
                            //data = new byte[length - 7];
                            type = receiveddata[1];
                            uuid[0] = receiveddata[2];
                            uuid[1] = receiveddata[3];
                            id[0] = receiveddata[4];
                            id[1] = receiveddata[5];
                            for (int i = 0; i < (length - 7); i++) {
                                data[i] = receiveddata[i + 6];
                            }
                            calculate();
                            update_screen();
                        }
                    }
                }
            });
        }
    };

    // Berechnet die Werte aus den empfangenen Daten
    private void calculate(){
        if ((data[2] != 0) || (data[3] != 0)){
            velocity = (float)(((data[2]&mask)<<8)+(data[3]&mask))/(float)value1Sec; // Zeit in ms
        }
        else {
            velocity = 0;
        }

        velocity += (float)(((data[0] & mask) << 8) + (data[1] & mask)); // Zeit in s,ms

        if (velocity != 0){
            velocity = (float)(wheelsize / 2) / velocity * kmhFactor; // Geschwindigkeit
        }
        else{
            velocity = 0;
        }

        pressure = (float)(((data[5]&mask)<<16)
                +((data[6]&mask)<<8)+(data[7]&mask))
                * pressureFactor;

        if (pressure > 0){
            altitude = (float)(Math.log(pressure/pressure0m)*(refHight));
        }
        else{
            altitude = -1000;
        }

        temperature = (float)(((data[10]&mask)<<8)+(data[11]&mask)) * temperaturFactor;
        
        //temperature = temperature + tempOffset;

        huminity = (float)(((data[12]&mask)<<24)+((data[13]&mask)<<16)
                +((data[14]&mask)<<8)+(data[15]&mask)) * huminityFactor;

        if (velocity > 0){
            if (velocity > 90){
                actVelocity = 90;
            }
            else{
                actVelocity = velocity;
            }
        }
        if (pressure > 0){
            actPressure = pressure;
        }
        if (altitude > -1000){
            actAltitude = altitude;
        }
        if (temperature != 0){
			if (calibTemp != tempNotCalibrated){
				tempOffset = calibTemp - temperature;
				calibTemp = tempNotCalibrated;
			}
            actTemperatur = temperature + tempOffset;
        }
        if (huminity > 0){
            actHuminity = huminity;
        }
    }

    // Überprüft, ob die empfangenen Daten von unserem Sender kommen
    public boolean checkReceivedData(byte[] receivedData){
        boolean retVal = false;
        if (receivedData[0] == lengthFilter){
            if (receivedData[1] == typeFilter){
                if (receivedData[2] == uuidFilter[0]){
                    if (receivedData[3] == uuidFilter[1]){
                        // Überprüfe, ob die Checksumme stimmt (Sekunden + Checksumme muss 0x010000 ergeben)
                        /*int sec, check;
                        sec = (int)(((receivedData[6]&0x00FF)<<8)+(receivedData[7]&0x00FF));
                        check = (int)(((receivedData[22]&0x00FF)<<8)+(receivedData[23]&0x00FF));
                        if((sec + check) == 0x10000){
                            return true;
                        }*/

                        retVal = true;
                    }
                }
            }
        }
        return retVal;
    }

    // Prüft, ob die Adresse des Senders dem Filder entspricht bzw. kein Filter gesetzt ist.
    private boolean checkAdress(){
        if (selectedAdress == noAdressFilter){
            return true;
        }
        else if (adressList.get(selectedAdress).compareTo(adress) == 0){
            return true;
        }

        return false;
    }

    // Speichert die Adresse falls sie noch nicht gespeichert
    private void saveAdress(){
        if (adressCounter >= 0) {
            boolean isListed = false;

            for (int i = 0; i <= adressCounter; i++) {
                if (adressList.get(i).compareTo(adress) == 0) {
                    isListed = true;
                }
            }
            if (!isListed) {
                adressCounter++;
                adressList.add(adressCounter, adress);
            }
        } else {
            adressCounter = 0;
            adressList.add(adressCounter, adress);
        }
    }

    // aktualisiert die Anzeige
    private void update_screen(){
        tacho.setImageBitmap(drawTacho(actVelocity * 3));
        velocityText.setText(String.format("%.2f", (float) (actVelocity * actualVelocitySetting.factor + actualVelocitySetting.summand))
                + actualVelocitySetting.unit);

        tempText.setText(String.format("%.2f",
                (float)(actTemperatur*actualTempSetting.factor + actualTempSetting.summand))
                        + actualTempSetting.unit);

        altitudeText.setText(String.format("%.2f", (float)(actAltitude)) + " m.ü.M");

        pressureText.setText(String.format("%.2f",
                (float)(actPressure * actualPressureSetting.factor + actualPressureSetting.summand))
                        + actualPressureSetting.unit);

        huminityText.setText(String.format("%.2f",
                (float)(actHuminity))
                + " %");
    }

    public void onClickSettings(View view) {
        if (adressCounter >= 0){
            Intent intent = new Intent(this, SettingsActivity.class);
            String messageText = adressList.get(0);

            for (int i = 1; i <= adressCounter; i++){
                messageText = messageText + "," + adressList.get(i);
            }
            intent.putExtra(SettingsActivity.EXTRA_MESSAGE, messageText);
            startActivityForResult(intent, SELECT_SENSOR);
        }
    }

    public void onClickUnitSettings(View view){
        calibTemp = tempNotCalibrated;
        Intent intent = new Intent(this, UnitSettingsActivity.class);
        String actualSettings = actualTempSetting + "," + actualVelocitySetting + "," +
                actualPressureSetting + "," + wheelsize;
        intent.putExtra(UnitSettingsActivity.ACTUAL_SETTING, actualSettings);
        startActivityForResult(intent, UNIT_SETTINGS);
    }

    protected void onStop(){
        super.onStop();
        scanLeDevice(false);
    }

    protected void onResume(){
        super.onResume();
        scanLeDevice(true);
    }

    private Bitmap drawTacho(double angle){
        int width, height;
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tachometer);
        int pixels[];
        double angleShown; // Winkel der Tachonadel
        if (angle <= (360-136)){
            angleShown = angle + 136;
        }
        else{
            angleShown = angle - 224;
        }
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        pixels = new int[height*width];

        // Pixel in Array einlesen
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        // Transparenz deaktivieren
        for (int i = 0; i < pixels.length; i++){
            pixels[i] = (pixels[i] & 0x00FFFFFF) | 0xFF000000;
        }

        // Bild spiegeln
        int copy[] = pixels.clone();
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                pixels[(i*width) + j] = copy[(i*width) + (width - j - 1)];
            }
        }

        // Zeiger einfügen
        int zeigerlength = width/3, zeigerthickness = height/50, middle = (height/2)*width + (width/2);
        for (int j = -(zeigerthickness / 2); j < (zeigerthickness / 2); j++){
            for (int i = 0; i < zeigerlength; i++){
                int x, y;
                x = width / 2 + (int)((i * Math.cos(angleShown / 180 * Math.PI)) - (j * Math.sin(angleShown / 180 * Math.PI)));
                y = height / 2 + (int)((i * Math.sin(angleShown / 180 * Math.PI)) + (j * Math.cos(angleShown / 180 * Math.PI)));
                pixels[(y * width) + x] = Color.RED;
            }
        }


        // Bitmap aus dem Array erstellen
        bitmap = bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        return bitmap;
    }
}
