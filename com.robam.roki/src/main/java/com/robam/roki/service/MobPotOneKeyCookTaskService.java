package com.robam.roki.service;

import com.legent.plat.Plat;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.pojos.device.Stove.StoveStatus;

/**
 * Created by Dell on 2018/5/22.
 */

public class MobPotOneKeyCookTaskService extends MobPotCooktaskService {
    static private MobPotOneKeyCookTaskService instance = new MobPotOneKeyCookTaskService();

    static public MobPotOneKeyCookTaskService getInstance() {

        if (instance == null) {
            synchronized (MobPotOneKeyCookTaskService.class) {
                if (instance == null) {
                    instance = new MobPotOneKeyCookTaskService();
                }
            }
        }
        return instance;
    }

    @Override
    protected void onShowCookingView() {
    }

    private MobPotOneKeyCookTaskService() {
        SpeechManager.getInstance().init(Plat.app);
    }


    @Override
    public void stop() {
        setFanLevel(0);
        setStoveStatus(StoveStatus.Off, null);
        stopCountdown();
    }

    @Override
    public void stopCountdown() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        SpeechManager.getInstance().dispose();
    }
}
