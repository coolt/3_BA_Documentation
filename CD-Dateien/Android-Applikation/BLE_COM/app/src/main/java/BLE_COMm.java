//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.text.DecimalFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.zip.CRC32;
//
//public class BLE_COMm extends Activity{
//    private BluetoothAdapter mBluetoothAdapter;
//    private final static int REQUEST_ENABLE_BT = 0;
//    private boolean mScanning;
//    private byte preamble, length, header;
//    private byte[] adress, data, crc;
//
//    public void BLE_COMm(BluetoothAdapter bluetoothAdapter){
//        mBluetoothAdapter = bluetoothAdapter;
//    }
//
//    public boolean BLE_init(){
//        //Check if the device supports BluetoothLowEnergy. If not: finish the app with a Errormassege to the User.
//        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this, "BLE wird nicht unterstützt.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (!mScanning){
//
//            // Initializes Bluetooth adapter.
//            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//            mBluetoothAdapter = bluetoothManager.getAdapter();
//
//            // Ensures Bluetooth is available on the device and it is enabled. If not,
//            // displays a dialog requesting user permission to enable Bluetooth.
//            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            }
//
//            scanLeDevice(true);
//        }
//
//        return true;
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent data) {
//
//        //Check if it was the Request for enable Bluetooth.
//        if (requestCode == REQUEST_ENABLE_BT) {
//
//            //Act only if the User clicked no.
//            if (resultCode != RESULT_OK) {
//
//                //BLE was not enabled by the user. Show an Error-Popup and finish the app.
//                Toast.makeText(this, "BLE nicht aktiviert", Toast.LENGTH_LONG).show();
//                finish();
//            }
//        }
//    }
//
//    public void BLE_start(){
//        scanLeDevice(true);
//    }
//
//    public void BLE_stop(){
//        scanLeDevice(false);
//    }
//
//    class BLE_data{
//        public int adress;
//
//        public int length;
//
//        public int[] data;
//
//        public int crc;
//
//        public Calendar timestamp;
//
//        // Initialize the data with the right length
//        public void BLE_data(int data_length, int data_adress, Calendar data_timestamp){
//            data = new int[data_length];
//
//            length = data.length;
//
//            adress = data_adress;
//
//            timestamp = data_timestamp;
//        }
//
//        /*public void BLE_data_crc(){
//            byte[] temp = new byte[];
//
//            CRC32 crc32 = new CRC32();
//
//            crc32.update();
//        }*/
//    }
//
//    // Start/stops the BLE-Scan
//    private void scanLeDevice(final boolean enable){
//        if (enable){
//            mScanning = true;
//            mBluetoothAdapter.startLeScan(mLeScanCallback);
//        }
//        else{
//            mScanning = false;
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//        }
//    }
//
//    // wird aufgerufen, wenn Daten empfangen werden
//    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback(){
//        @Override
//        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    //Copy the received data.
//                    byte[] receiveddata = scanRecord.clone();
//
//                    int i;
//
//                    preamble = receiveddata[0];
//                    adress[0] = receiveddata[1];
//                    adress[1] = receiveddata[2];
//                    adress[2] = receiveddata[3];
//                    adress[3] = receiveddata[4];
//                    header = receiveddata[5];
//                    length = receiveddata[6];
//                    data = new byte[receiveddata.length - 10];
//                    for (i = 0; i < (receiveddata.length-10); i++) {
//                        data[i] = receiveddata[i + 7];
//                    }
//                    i = i + 7;
//                    crc[0] = receiveddata[i];
//                    i++;
//                    crc[1] = receiveddata[i];
//                    i++;
//                    crc[2] = receiveddata[i];
//                }
//            });
//        }
//    };
//}
