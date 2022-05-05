package com.legent.io.channels;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.legent.LogTags;
import com.legent.VoidCallback;
import com.legent.io.AbsIONode;
import com.legent.io.IOWatcher;
import com.legent.io.buses.IBus;
import com.legent.io.msgs.IMsg;
import com.legent.io.msgs.collections.BytesMsg;
import com.legent.io.protocols.IProtocol;
import com.legent.utils.ByteUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

abstract public class AbsChannel extends AbsIONode implements IChannel {

    protected final static String TAG = LogTags.TAG_IO;
    protected final static int Buffer_Size = 1024;
    protected IBus bus;
    protected IProtocol protocol;

    protected byte[] buffer;

    abstract protected IBus createBus();

    abstract protected IProtocol createProtocol();

    public boolean serial;

    public AbsChannel() {
        bus = createBus();
        protocol = createProtocol();

        Preconditions.checkNotNull(bus, "invalid bus");
        Preconditions.checkNotNull(protocol, "invalid protocol");

        bus.setWatcher(busWatcher);
    }

    @Override
    public void dispose() {
        super.dispose();
        bus.dispose();
        protocol.dispose();
    }

    @Override
    protected void onOpen(final VoidCallback callback) {
        if (serial) {
            closeSerial(callback);
        } else{
            bus.open(callback);
        }
    }

    private void closeSerial(final VoidCallback callback) {
        bus.close(new VoidCallback() {
            @Override
            public void onSuccess() {
                bus.open(callback);
            }

            @Override
            public void onFailure(Throwable t) {
                closeSerial(callback);
            }
        });
    }

    @Override
    protected void onClose(VoidCallback callback) {
        bus.close(callback);
    }

    @Override
    public void send(IMsg msg, VoidCallback callback) {

        byte[] data = null;
        try {
            Preconditions.checkNotNull(msg, "msg is null");

            data = protocol.encode(msg);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                stringBuilder.append(ByteUtils.toHex(data[i]) + ",");
            }
            Log.i("00055874", stringBuilder.toString());
        } catch (Exception e) {

            String log = String.format(
                    "protocol encode error. protocol:%s\nmsg:%s", protocol
                            .getClass().getSimpleName(), msg.toString());
            Log.e(TAG, log);
            e.printStackTrace();
        }
        if (data != null) {
            BytesMsg bm = new BytesMsg(data);
            LogUtils.i("1212121212","bm::"+bm.toString());

            bm.setTag(msg.getTag());
            bus.send(bm, callback);
        }
    }

    private IOWatcher busWatcher = new IOWatcher() {

        @Override
        public void onConnectionChanged(boolean isConnected) {

            AbsChannel.this.onConnectionChanged(isConnected);
        }

        @Override
        public void onMsgReceived(IMsg msg) {
            byte[] busData = msg.getBytes();

            List<Byte> list = new ArrayList<Byte>();
            for (int i = 0; i <busData.length ; i++) {
                list.add(busData[i]);
            }
            LogUtils.i("AbsChannel","list:::"+list.toString());

            List<IMsg> msgs = null;

            try {
                msgs = protocol.decode(busData, msg.getTag());
                LogUtils.i("AbsChannel","msgs:::"+msgs.toString());
            } catch (Exception e) {
                String log = String.format(
                        "protocol decode error. protocol:%s\nbyte[]:%s",
                        protocol.getClass().getSimpleName(),
                        StringUtils.bytes2Hex(busData));
                Log.e(TAG, log);
                e.printStackTrace();
            }

            if (msgs != null && msgs.size() > 0) {
                for (IMsg m : msgs) {
                    AbsChannel.this.onMsgReceived(m);
                }
            }

        }
    };

}
