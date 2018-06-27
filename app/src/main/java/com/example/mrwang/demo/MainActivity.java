package com.example.mrwang.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private MyDialogFragment mMMyDialogFragment;
    private FragmentManager mMFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button_network);
        initListener();
    }

    private void initListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });
    }

    private void initDialog() {

        if (mMMyDialogFragment == null && mMFragmentManager == null) {
            mMMyDialogFragment = new MyDialogFragment();
            mMFragmentManager = getSupportFragmentManager();
        }
        //mMyDialogFragment.setCancelable(false);
        if (!mMMyDialogFragment.isAdded()) {
            mMMyDialogFragment.show(mMFragmentManager, "DialogFragments");
        }
        // }

        mMMyDialogFragment.setDialogCallback(new MyDialogFragment.DialogCallback() {
            @Override
            public void openPhoto() {
                Toast.makeText(MainActivity.this, "打开相册", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void openCamera() {
                Toast.makeText(MainActivity.this, "打开相机", Toast.LENGTH_SHORT).show();
            }

        });
    }

    /**
     * 在Activity销毁之前，确保对话框已关闭
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMMyDialogFragment != null && mMMyDialogFragment.getDialog().isShowing())
            mMMyDialogFragment.dismiss();
    }

}


