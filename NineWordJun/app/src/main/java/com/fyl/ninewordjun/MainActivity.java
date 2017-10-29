package com.fyl.ninewordjun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyl.ninewordjun.greendao.db.DBHelper;
import com.fyl.ninewordjun.greendao.entity.User;
import com.fyl.ninewordjun.greendao.gen.UserDao;
import com.fyl.ninewordjun.media.VoicePlayer;
import com.fyl.utils.FilePathUtils;
import com.fyl.utils.Log;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTitle;
    private Button mAdd,mDelete,mUpdate,mFind;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
//                addDate();
                VoicePlayer.getInstance().setStatus(VoicePlayer.Status.Play);
                break;
            case R.id.button2:
                VoicePlayer.getInstance().setStatus(VoicePlayer.Status.Pause);
//                deleteDate();
                break;
            case R.id.button3:
                VoicePlayer.getInstance().setStatus(VoicePlayer.Status.Stop);
//                updateDate();
                break;
            case R.id.button4:
                findDate();
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
