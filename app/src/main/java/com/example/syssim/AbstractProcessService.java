package com.example.syssim;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public abstract class AbstractProcessService extends Service {

    private static final String TAG = "AbsProcess";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service Started");
        int processMemoryMB = intent.getIntExtra("processMemoryMB", 50);
        AllocateMB.getInstance().allocate(processMemoryMB);
        return START_NOT_STICKY;
    }
}
