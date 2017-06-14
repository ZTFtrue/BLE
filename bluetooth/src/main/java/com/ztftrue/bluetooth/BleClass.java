package com.ztftrue.bluetooth;

import android.support.annotation.NonNull;

/**
 * Created by ztftrue on 2017/6/13
 */

public class BleClass implements Comparable<BleClass> {
    private String uuid = "";
    private int major = 0;
    private int minor = 0;
    private String ibeaconName = "";
    private String mac = "";
    private int rssi = 0;
    private double distance = 0;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String getIbeaconName() {
        return ibeaconName;
    }

    public void setIbeaconName(String ibeaconName) {
        this.ibeaconName = ibeaconName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public int compareTo(@NonNull BleClass other) {

        int value = this.getRssi() - other.getRssi();
        if (value == 0) {
            value = this.getMajor() - other.getMajor();
        }
        return value;
    }

    @Override
    public String toString() {
        return "rssi:" + rssi + ",uuid:" + uuid;
    }
}
