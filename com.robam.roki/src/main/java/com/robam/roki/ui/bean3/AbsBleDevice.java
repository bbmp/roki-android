package com.robam.roki.ui.bean3;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;

import com.clj.fastble.data.BleDevice;

public class AbsBleDevice extends BleDevice {

    public String iconUrl;

    public AbsBleDevice(BluetoothDevice device) {
        super(device);
    }

}
