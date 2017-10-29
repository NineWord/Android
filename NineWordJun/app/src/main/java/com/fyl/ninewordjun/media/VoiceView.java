package com.fyl.ninewordjun.media;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fyl.ninewordjun.R;
import com.fyl.utils.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by laofuzi on 2017/10/29.
 */

public class VoiceView extends LinearLayout implements View.OnClickListener{
    private Button btnClose;
    private Button btnPlay;
    private ImageView ivIcon, ivOpen;
    private TextView tvTitle, tvTime, tvAuthor;


    public VoiceView(@NonNull Context context) {
        this(context, null);
    }

    public VoiceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_voice, this);

        ivIcon = view.findViewById(R.id.ivIcon);
        ivOpen = view.findViewById(R.id.ivOpen);

        tvTitle = view.findViewById(R.id.tvTitle);
        tvTime = view.findViewById(R.id.tvTime);
        tvAuthor = view.findViewById(R.id.tvAuthor);

        btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);

        this.setOnClickListener(this);
        this.updatePlayButton(VoicePlayer.getInstance().status);
    }

    @Override
    public void setVisibility(int visibility) {
        if (VoicePlayer.getInstance().getStatus() == VoicePlayer.Status.Stop) {
            super.setVisibility(GONE);
        } else {
            super.setVisibility(visibility);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                VoicePlayer.getInstance().setStatus(VoicePlayer.Status.Stop);
                break;
            case R.id.btnPlay:
                if (VoicePlayer.getInstance().status == VoicePlayer.Status.Play) {
                    VoicePlayer.getInstance().setStatus(VoicePlayer.Status.Pause);
                } else if (VoicePlayer.getInstance().status == VoicePlayer.Status.Pause) {
                    VoicePlayer.getInstance().setStatus(VoicePlayer.Status.Play);
                }
                break;
            default:
                doBtnOpen();
                break;
        }
    }

    private void doBtnOpen() {

    }

    private void updatePlayButton(VoicePlayer.Status status) {
        if (status == VoicePlayer.Status.Play) {
            btnPlay.setText("播放");
            btnClose.setVisibility(GONE);
            this.setVisibility(VISIBLE);

        } else if (status == VoicePlayer.Status.Pause){
            btnPlay.setText("暂停");
            btnClose.setVisibility(VISIBLE);
            this.setVisibility(VISIBLE);
        } else { // stop
            this.setVisibility(GONE);
        }
    }

    /***** eventbus *****/
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VoicePlayer.Status status) {
        this.updatePlayButton(status);
    }

}
