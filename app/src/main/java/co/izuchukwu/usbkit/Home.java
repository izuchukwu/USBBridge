package co.izuchukwu.usbkit;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import co.izuchukwu.usbbridge.BridgeFileSystemCreator;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // fab
        FloatingActionButton clearFab = findViewById(R.id.clearFab);
        clearFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });

        // bridge button
        Button bridgeButton = findViewById(R.id.bridgeButton);
        bridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bridgeIt();
            }
        });

        // register usb permission broadcast receiver
        final PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbPermissionReceiver, filter);
    }

    //
    // Bridge
    //

    public void bridgeIt(){
        String hello = BridgeFileSystemCreator.hello();
        log("Bridge", hello);
    }

    //
    // Options Menu
    //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    //
    // Log
    //

    public void log(String tag, String message) {
        Log.v(tag, message);
        TextView logTextView = (TextView) findViewById(R.id.logTextView);
        logTextView.append("\n[" + tag + "] " + message);
        scrollLogToBottom();
    }

    public void scrollLogToBottom() {
        final ScrollView scrollView = findViewById(R.id.logScrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void clear() {
        TextView logTextView = (TextView) findViewById(R.id.logTextView);
        logTextView.setText("readyyy");
    }

    //
    // USB Permissions Listener
    //

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver usbPermissionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            //call method to set up device communication
                            log("USBKit", "Permission received");
                        }
                    }
                    else {
                        log("USBKit", "Permission denied");
                    }
                }
            }
        }
    };
}
