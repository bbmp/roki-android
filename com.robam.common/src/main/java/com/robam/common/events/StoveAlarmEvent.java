package com.robam.common.events;

import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.dictionary.StoveAlarm;
import com.robam.common.services.StoveAlarmManager;

/**
 * 电磁灶报警事件
 *
 * @author sylar
 */
public class StoveAlarmEvent {

    public StoveAlarm alarm;
    public Stove stove;
    public Stove.StoveHead head;

    public StoveAlarmEvent(Stove stove, Stove.StoveHead head, short alarmId) {
        this.stove = stove;
        this.head = head;
        this.alarm = StoveAlarmManager.getInstance().queryById(alarmId);
        if (alarm != null){
            alarm.src = head;
        }

    }
}