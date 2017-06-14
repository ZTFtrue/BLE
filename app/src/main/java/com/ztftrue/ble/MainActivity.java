package com.ztftrue.ble;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ztftrue.bluetooth.BleClass;
import com.ztftrue.bluetooth.BleUtil;
import com.ztftrue.bluetooth.BlueToothSacn;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BlueToothSacn blueToothSacn = new BlueToothSacn(10, 5, this);
        blueToothSacn.setCallBack(new BlueToothSacn.CallBack() {
            @Override
            public void callBlueTooth(final List<BleClass> bleClassesSet) {
                Toast.makeText(MainActivity.this, String.valueOf(bleClassesSet.size()), Toast.LENGTH_SHORT).show();
            }
        });
        BleUtil.setA_Value(80.0F);//计算距离设置
        BleUtil.setN_Value(2.0F);//计算距离设置
        blueToothSacn.scan();
    }
}
