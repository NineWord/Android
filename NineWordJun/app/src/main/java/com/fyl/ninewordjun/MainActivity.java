package com.fyl.ninewordjun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.fyl.ninewordjun.global.FilePathUtils;
import com.fyl.ninewordjun.log.Log;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FilePathUtils.init(this);

        String path = FilePathUtils.SDCARD_PATH;

        Log.d("ttt", "xxx");
    }
}
