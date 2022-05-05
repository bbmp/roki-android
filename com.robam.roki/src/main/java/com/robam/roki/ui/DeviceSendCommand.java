package com.robam.roki.ui;

import com.robam.common.util.IsendCommand;


public class DeviceSendCommand implements IsendCommand {

    private IsendCommand isendCommand;

    public DeviceSendCommand(IsendCommand isendCommand) {
        this.isendCommand = isendCommand;
    }

    @Override
    public void onStart() {
        isendCommand.onStart();
    }

    @Override
    public void onPause() {
        isendCommand.onPause();
    }

    @Override
    public void onPreSend() {
        isendCommand.onPreSend();
    }

    @Override
    public void onFinish() {
        isendCommand.onFinish();
    }

    @Override
    public void onRestart() {
        isendCommand.onRestart();
    }
}
