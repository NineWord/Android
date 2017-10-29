package com.fyl.ninewordjun.media;

import com.fyl.utils.PermissionsUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by laofuzi on 2017/10/29.
 */

public class VoicePlayer {
    private static final VoicePlayer ourInstance = new VoicePlayer();

    public Status status;



    public static VoicePlayer getInstance() {
        return ourInstance;
    }

    private VoicePlayer() {

    }

    public void play() {

    }



    public Status getStatus () {
        return status;
    }

    public void setStatus (Status status) {
        if (this.status == status){
            return;
        }

        if (status == Status.Play) {

        } else if (status == Status.Pause) {

        } else { //Stop

        }

        this.status = status;
        EventBus.getDefault().post(this.status);
    }

    public enum Status {
        Stop,
        Play,
        Pause,
    }
}
