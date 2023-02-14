package com.example.syssim;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ProcessService extends AbstractProcessService{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
