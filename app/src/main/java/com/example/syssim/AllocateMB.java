package com.example.syssim;


import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.HashSet;

public class AllocateMB {
    public static final String TAG = "ALLOCATEMB";
    int[] chunks;
    static int allocatedMemoryMB = 0;
    private static AllocateMB instance;
    private static HashSet<int[]> allocatedChunks = new HashSet<>();
    private AllocateMB(){
    }

    public static AllocateMB getInstance() {
        if (instance == null) {
            instance = new AllocateMB();
        }
        return instance;
    }

    public void allocate(int memoryToAllocateMB) {
        int toAllocateMB = memoryToAllocateMB * 1024 * 1024;
        int blocks = toAllocateMB / 4;
        Log.d(TAG, "allocating " + memoryToAllocateMB + " MB");
        chunks = new int[blocks];
        Arrays.fill(chunks, Integer.MAX_VALUE);
        allocatedChunks.add(chunks);
        allocatedMemoryMB += memoryToAllocateMB;

    }

    public void deAllocate() {
        allocatedChunks.clear();
        chunks = null;
        allocatedMemoryMB = 0;
    }




}
