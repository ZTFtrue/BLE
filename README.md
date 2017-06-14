# BLE
BLE get uuid 


使安卓自带工具以获取BLE的UUID 、RRsi等信息；

```
       BlueToothSacn blueToothSacn = new BlueToothSacn(10, 5, this);//扫描时长、暂停时长 、context   
          blueToothSacn.setCallBack(new BlueToothSacn.CallBack() {
            @Override
            public void callBlueTooth(final List<BleClass> bleClassesSet) {
                Toast.makeText(MainActivity.this, String.valueOf(bleClassesSet.size()), Toast.LENGTH_SHORT).show();//获取到的数据
                }
        });
        BleUtil.setA_Value(80.0F);//计算距离设置
        BleUtil.setN_Value(2.0F);//计算距离设置
        blueToothSacn.scan();//开始扫描
        
```
