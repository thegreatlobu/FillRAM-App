package com.example.syssim;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView totalMemory, availableMemory;
    private EditText edtTxtRAMMemory, edtTxtProcessMemory;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    //memory obj container, holds native code obj
    long objContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        totalMemory.setText("Total Memory: " + Long.toString(getAvailableMemoryInfo().totalMem / 0x100000L) + " MB");
        availableMemory.setText("Available Memory: " + Long.toString(getAvailableMemoryInfo().availMem / 0x100000L) + " MB");

        objContainer = initMemoryContainer();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initViews(){
        totalMemory = findViewById(R.id.txtTotalMemory);
        availableMemory = findViewById(R.id.txtAvailableMemory);
        edtTxtRAMMemory = findViewById(R.id.edtTxtRAMMemory);
        edtTxtProcessMemory = findViewById(R.id.edtTxtProcessMemory);
    }

    private ActivityManager.MemoryInfo getAvailableMemoryInfo() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    public void launchProcess(View view) {
        ProcessService processService = new ProcessService();
        Intent serviceIntent = new Intent(MainActivity.this, processService.getClass());
        String input = edtTxtProcessMemory.getText().toString();
        int inputInt = Integer.parseInt(input);
        serviceIntent.putExtra("processMemoryMB", inputInt);
        startService(serviceIntent);
    }

    public void fillRAM(View view){

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                String input = edtTxtRAMMemory.getText().toString();
                if (input.isEmpty()) {
                    return;
                }
                int memToAllocateMB = Integer.parseInt(input);
                allocateMemory(objContainer, memToAllocateMB);
                totalMemory.setText("Total Memory: " + Long.toString(getAvailableMemoryInfo().totalMem / 0x100000L) + " MB");
                availableMemory.setText("Available Memory: " + Long.toString(getAvailableMemoryInfo().availMem / 0x100000L) + " MB");
            }
        });
    }

    public void freeRAM(View view) {
        deallocateMemory(objContainer);
    }

    public native void allocateMemory(long container, int mb);
    public native void deallocateMemory(long container);
    public native long initMemoryContainer();
    public native void Fork();

    static {
        System.loadLibrary("syssim");
    }

}

