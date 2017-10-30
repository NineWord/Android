package com.fyl.ninewordjun;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fyl.ninewordjun.greendao.db.DBHelper;
import com.fyl.ninewordjun.greendao.entity.User;
import com.fyl.ninewordjun.greendao.gen.UserDao;
import com.fyl.ninewordjun.media.VoicePlayer;
import com.fyl.ninewordjun.media.VoiceView;
import com.fyl.utils.FilePathUtils;
import com.fyl.utils.Log;
import com.fyl.utils.ScreenUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTitle;
    private Button mAdd,mDelete,mUpdate,mFind;
    private VoiceView voiceView;
    private FrameLayout layout1;

    private User mUser;
    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FilePathUtils.init(this);

        initView();
        initEvent();
        mUserDao = DBHelper.getInstance().getDaoSession().getUserDao();
        EventBus.getDefault().register(this);

        layout1 = (FrameLayout) findViewById(R.id.layout_1);
        voiceView = new VoiceView(this);
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, spToPx(this, 50));
        params.gravity = Gravity.BOTTOM;
        layout1.addView(voiceView, params);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int spToPx(Context appContext, float spValue) {
        return (int) (spValue  * appContext.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    private void initEvent() {
        mAdd.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mFind.setOnClickListener(this);
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.textView);
        mAdd = (Button) findViewById(R.id.button);
        mDelete = (Button) findViewById(R.id.button2);
        mUpdate = (Button) findViewById(R.id.button3);
        mFind = (Button) findViewById(R.id.button4);
    }

    private MediaPlayer player;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                VoicePlayer.getInstance().play("http://172.168.1.108/test.mp3");
                break;
            case R.id.button2:
//                deleteDate();
                VoicePlayer.getInstance().replay();
                break;
            case R.id.button3:
//                updateDate();
                break;
            case R.id.button4:
                layout1.removeView(voiceView);
                voiceView =null;
//                findDate();
                break;
        }
    }

    /**
     * 增加数据
     */
    private void addDate() {
        long id = 1;
        List<User> users = mUserDao.loadAll();
        if (users != null && users.size() > 0) {
            id = users.get(users.size() - 1).getId() + 1;
        }
        mUser = new User(id, "anye" + id);

        try {
            mUserDao.insert(mUser);//添加一个
        } catch (RuntimeException e) {
            Log.e("xx", e.toString());
        }

        mTitle.setText(mUser.getName());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String event) {
        Log.d("xx", event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Number event) {
        Log.d("xx", event + "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event) {
        Log.d("xx", event + "");
    }

    /**
     * 删除数据
     */
    private void deleteDate() {
        // deleteUserById(2);
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().postSticky("test1");
                EventBus.getDefault().post(2);
            }
        });
    }

    /**
     * 根据主键删除User
     *
     * @param id User的主键Id
     */
    public void deleteUserById(long id) {
        mUserDao.deleteByKey(id);
    }

    /**
     * 更改数据
     */
    private void updateDate() {
        mUser = new User((long)2, "anye0803");
        mUserDao.update(mUser);
    }

    /**
     * 查找数据
     */
    private void findDate() {
        List<User> users = mUserDao.loadAll();
        String userName = "";
        for (int i = 0; i < users.size(); i++) {
            userName += users.get(i).getName()+",";
        }
        mTitle.setText("查询:"+userName);
    }
}
