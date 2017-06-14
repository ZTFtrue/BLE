package com.ztftrue.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ztftrue on 2017/6/14
 */

public class BlueToothSacn {
    private BluetoothAdapter mBluetoothAdapter;
    private int scanTime;
    private int sleepTime;
    private Context context;
    private boolean exit = true;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            callBack.callBlueTooth(bleClassList);
            return false;
        }
    });
    private List<BleClass> bleClassList = new ArrayList<>();
    private List<String> bleUUID = new ArrayList<String>();//过滤uuid,由于ble信号不稳定 ，TreeSet无法区分rssi

    public void exit() {
        this.exit = false;
    }

    public int getScanTime() {
        return scanTime;
    }

    public void setScanTime(int scanTime) {
        this.scanTime = scanTime * 1000;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime * 1000;
    }

    public BlueToothSacn(int scanTime, int sleepTime, Context context) {
        this.scanTime = scanTime * 1000;
        this.sleepTime = sleepTime * 1000;
        this.context = context;
    }

    public interface CallBack {
        void callBlueTooth(List<BleClass> bleClassesSet);
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void scan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            start();
        } else {
            Log.e("BLE", "Your phone does not support BLE");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void start() {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (exit) {
                    try {
                        bleClassList.clear();
                        bleUUID.clear();
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
//                      mBluetoothAdapter.startDiscovery();
                        Thread.sleep(scanTime);
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        sortBleList();
                        handler.sendEmptyMessage(1);

                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            BleClass bleClass = BleUtil.getBLE(device, rssi, scanRecord);
            if (bleClass != null && !bleUUID.contains(bleClass.getUuid())) {
                bleClassList.add(bleClass);
                Log.e("ztf", bleClassList.toString());
                bleUUID.add(bleClass.getUuid());
                Log.e("ztf", bleClass.getUuid());
                Log.e("ztf", bleUUID.size() + "\t" + bleClassList.size());
            }
        }
    };

    private void sortBleList() {
        Collections.sort(bleClassList);
    }
}
