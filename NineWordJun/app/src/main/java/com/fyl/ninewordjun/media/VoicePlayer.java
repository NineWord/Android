package com.fyl.ninewordjun.media;

import android.media.AudioManager;
import android.media.MediaPlayer;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by laofuzi on 2017/10/29.
 *
 */
public class VoicePlayer implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private static final VoicePlayer mInstance = new VoicePlayer();
    private MediaPlayer mPlayer;
    private String voiceUrl;
    private Status status;

    public enum Status {
        Stop,
        Play,
        Pause,
    }

    public static VoicePlayer getInstance() {
        return mInstance;
    }

    private VoicePlayer() {
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setOnBufferingUpdateListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//        skbProgress.setSecondaryProgress(bufferingProgress);
//        int currentProgress=skbProgress.getMax()*mPlayer.getCurrentPosition()/mPlayer.getDuration();
//        Log.e(currentProgress+"% play", bufferingProgress + "% buffer");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        this.status = Status.Stop;
        EventBus.getDefault().post(this.status);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //this.replay();
        return false;
    }

    public Status getStatus () {
        return status;
    }

    /**
     * 播放音乐
     *
     * @param playPosition 播放位置
     */
    private void playByPosition(int playPosition) {
        try {
            if (mPlayer != null) {
                mPlayer.reset();
                mPlayer.setDataSource(voiceUrl);
                // mPlayer.prepare();// 进行缓冲
                mPlayer.prepareAsync();
                mPlayer.setOnPreparedListener(new MyPreparedListener(playPosition));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放
     */
    public void play(String voiceUrl) {
        this.voiceUrl = voiceUrl;
        playByPosition(0);
    }

    /**
     * 重播
     */
    public void replay() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.seekTo(0);// 从开始位置开始播放音乐
        } else {
            playByPosition(0);
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            status = Status.Stop;
            EventBus.getDefault().post(this.status);
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            status = Status.Pause;
            EventBus.getDefault().post(this.status);
        } else {
            if (this.status == Status.Pause && mPlayer != null) {
                mPlayer.start();
                status = Status.Play;
                EventBus.getDefault().post(this.status);
            }
        }
    }

    private final class MyPreparedListener implements android.media.MediaPlayer.OnPreparedListener {
        private int playPosition;

        public MyPreparedListener(int playPosition) {
            this.playPosition = playPosition;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mPlayer.start();// 开始播放
            status = Status.Play;
            EventBus.getDefault().post(status);

            if (playPosition > 0) {
                mPlayer.seekTo(playPosition);
            }
        }
    }
}
