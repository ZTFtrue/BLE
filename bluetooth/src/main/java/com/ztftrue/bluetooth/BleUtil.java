package com.ztftrue.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

/**
 * Created by ztftrue on 2017/6/13
 */

public class BleUtil {
    //A和n的值，需要根据实际环境进行检测得出
    private static double A_Value = 50;
    /**
     * A - 发射端和接收端相隔1米时的信号强度
     */
    private static double n_Value = 2.5;
    /**
     * n - 环境衰减因子
     */
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static double getA_Value() {
        return A_Value;
    }

    public static double getN_Value() {
        return n_Value;
    }

    public static void setA_Value(double a_Value) {
        A_Value = a_Value;
    }

    public static void setN_Value(double n_Value) {
        BleUtil.n_Value = n_Value;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0;
        }
        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        }
    }

    static BleClass getBLE(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        BleClass bleClass = new BleClass();
        int startByte = 2;
        boolean patternFound = false;
        while (startByte <= 5) {
            if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && ((int) scanRecord[startByte + 3] & 0xff) == 0x15) {
                patternFound = true;
                break;
            }
            startByte++;
        }
        if (patternFound) {
            // 转换为16进制
            byte[] uuidBytes = new byte[16];
            System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
            String hexString = bytesToHex(uuidBytes);
            // ibeacon的UUID值
            String uuid = hexString.substring(0, 8) + "-" + hexString.substring(8, 12) + "-" + hexString.substring(12, 16) + "-" + hexString.substring(16, 20) + "-" + hexString.substring(20, 32);
            //ibeacon的Major值
            int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);
            //ibeacon的Minor值
            int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);
            String ibeaconName = device.getName();
            String mac = device.getAddress();
            int txPower = (scanRecord[startByte + 24]);
            Log.d("BLE", bytesToHex(scanRecord));
            Log.d("BLE", "Name：" + ibeaconName + "\nMac：" + mac + " \nUUID：" + uuid + "\nMajor：" + major + "\nMinor：" + minor + "\nTxPower：" + txPower + "\nrssi：" + rssi);
            Log.d("BLE", "distance：" + calculateAccuracy(txPower, rssi));
            bleClass.setUuid(uuid);
            bleClass.setIbeaconName(ibeaconName);
            bleClass.setMac(mac);
            bleClass.setMajor(major);
            bleClass.setMinor(minor);
            bleClass.setRssi(rssi);
            bleClass.setDistance(getDistance(rssi));
            return bleClass;
        }
        return null;
    }

    private static double getDistance(int rssi) {
        double power = (Math.abs(rssi) - A_Value) / (10 * n_Value);
        return Math.pow(10, power);
    }
}
