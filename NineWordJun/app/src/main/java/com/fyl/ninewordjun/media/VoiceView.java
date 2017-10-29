package com.fyl.ninewordjun.media;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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

import com.fyl.ninewordjun.AppContext;
import com.fyl.ninewordjun.R;
import com.fyl.ninewordjun.VoiceActivity;
import com.fyl.utils.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by laofuzi on 2017/10/29.
 */

public class VoiceView extends LinearLayout implements View.OnClickListener{
    private Button btnClose;
    private Button btnPause;
    private ImageView ivIcon, ivOpen;
    private TextView tvTitle, tvTime, tvAuthor;

    //TODO 设置文本图片。。。

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
        btnPause = view.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);

        this.setOnClickListener(this);
    }



    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            EventBus.getDefault().register(this);
            this.updatePlayButton(VoicePlayer.getInstance().getStatus());
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                VoicePlayer.getInstance().stop();
                this.setVisibility(GONE);
                break;
            case R.id.btnPause:
                VoicePlayer.getInstance().pause();
                break;
            default:
                Context context = getContext().getApplicationContext();
                Intent intent = new Intent(context, VoiceActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    private void updatePlayButton(VoicePlayer.Status status) {
        if (status == VoicePlayer.Status.Play) {
            btnPause.setText("播放");
            btnClose.setVisibility(GONE);
            this.setVisibility(VISIBLE);
        } else {
            btnPause.setText("暂停");
            btnClose.setVisibility(VISIBLE);
        }
    }

    /***** eventbus *****/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VoicePlayer.Status status) {
        this.updatePlayButton(status);
    }

}
