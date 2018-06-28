package com.example.bimalbhattarai.wifi_scan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.net.wifi.*;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    public WifiManager wifiManager;
    public ScanResult  wifiScanResult;
    public boolean isStart=false;
    public DBHelper dbHelper;
    public WifiScanReciever wifiScanReciever;
    public Context context;
    public FileWriter file_writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper= new DBHelper(this);

        WifiScanReciever wifiScanReciever= new WifiScanReciever();
        registerReceiver(wifiScanReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


            final Handler handler = new Handler();
            final Runnable run = new Runnable() {
                @Override
                public void run() {
                    getRSSI();
                    handler.postDelayed(this, 1000);
                }
            };
            handler.post(run);
            Log.d("scan", "reach 1");



        Button button= (Button) findViewById(R.id.btnScan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Scan Started", Toast.LENGTH_SHORT).show();
                isStart=true;
            }
        });

        Button save= (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    BackupDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void getRSSI(){
            if(isStart){
                WifiManager wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE) ;
                Context context = getApplicationContext();
                wifiManager.startScan();
                Log.d("scan","reach 3");
            }
    }
    public void BackupDatabase() throws IOException {
        boolean success = true;
        File file = null;
        file = new File(Environment.getExternalStorageDirectory() + "/bimal");
        Toast.makeText(getApplicationContext(), "saving", Toast.LENGTH_SHORT).show();

        if (file.exists()) {
            success = true;
        } else {
            success = file.mkdir();
        }

        if (success) {
            String inFileName = "/data/data/com.example.bimalbhattarai.wifi_scan/databases/wifi.db";
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = Environment.getExternalStorageDirectory() + "/bimal/wifi.db";

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            fis.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        wifiScanReciever.abortBroadcast();

    }

    class WifiScanReciever extends BroadcastReceiver{
        @Override
        public void onReceive(Context c, Intent intent) {

                HashMap<String, ArrayList>rssiList= new HashMap<String, ArrayList>();
                ArrayList rssi= new ArrayList();

                WifiManager wifiManager= (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
                TextView text= (TextView) findViewById(R.id.textDisplay);
                List<ScanResult>wifiscan= wifiManager.getScanResults();
                for(ScanResult scanning: wifiscan){
                    Log.d("scan", "reach 4");
                    String Name= scanning.SSID;
                    Float value= Float.valueOf(scanning.level);
                    rssi.add(value);
                   rssiList.put(Name,rssi);
                    text.setText("SSID:" +"\n"+ Name + "\n" + "RSSI:"+ value+"\n");


                    dbHelper.insert(Name, value);
                }
               // unregisterReceiver(this);
         //   Toast.makeText(getApplicationContext(),"scan: " + rssiList, Toast.LENGTH_SHORT).show();
            }

    }

}
