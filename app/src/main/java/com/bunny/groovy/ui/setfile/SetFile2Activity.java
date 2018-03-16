package com.bunny.groovy.ui.setfile;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bunny.groovy.R;
import com.bunny.groovy.adapter.StyleAdapter;
import com.bunny.groovy.base.BaseActivity;
import com.bunny.groovy.model.MusicBean;
import com.bunny.groovy.model.StyleModel;
import com.bunny.groovy.presenter.SetFilePresenter;
import com.bunny.groovy.service.MusicService;
import com.bunny.groovy.ui.login.LoginActivity;
import com.bunny.groovy.utils.AppCacheData;
import com.bunny.groovy.utils.AppConstants;
import com.bunny.groovy.utils.UIUtils;
import com.bunny.groovy.view.ISetFileView;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/****************************************
 * 功能说明:  完善资料第二步
 *
 performTypeName
 signature
 music
 *
 * Author: Created by bayin on 2017/12/12.
 ****************************************/

public class SetFile2Activity extends BaseActivity<SetFilePresenter> implements ISetFileView {

    private MusicBean mMusic_file;
    private MusicService.CallBack callBack;
    private String strFormat = "(%d/400)";
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            callBack = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            callBack = null;
        }
    };
    private PopupWindow popupWindow;
    private StyleAdapter adapter;

    @OnClick(R.id.iv_select_music)
    void selectMusic() {
        startActivityForResult(new Intent(this, MusicListActivity.class), 1);
    }

    @Bind(R.id.bt_play_music)
    Button btPlay;

    @OnClick(R.id.bt_play_music)
    void playMusic() {
        if (mMusic_file == null || TextUtils.isEmpty(mMusic_file.getMusicPath())) {
            UIUtils.showBaseToast("Choose music.");
            return;
        }
        handleMusic();
    }

    private void initMusicService() {
        /** 构造启动音乐播放服务的Intent，设置音乐资源 */
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("music_path", mMusic_file.getMusicPath());
        startService(intent);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    /**
     * 控制音乐播放
     */
    private void handleMusic() {
        if (callBack != null) {
            boolean isPlay = callBack.isPlayerMusic();
            if (isPlay) {
                btPlay.setBackgroundResource(R.drawable.login_stop);
            } else {
                btPlay.setBackgroundResource(R.drawable.login_play);
            }
        }
    }

    @OnClick(R.id.et_select_style)
    void selectStyle() {
        mPresenter.requestStyle();
    }

    @OnClick(R.id.tv_next)
    void next() {
        if (TextUtils.isEmpty(etSelectStyle.getTrimmedString())) {
            UIUtils.showBaseToast("请选择类型");
            return;
        }
        if (TextUtils.isEmpty(etBio.getText().toString())) {
            UIUtils.showBaseToast("请输入签名");
            return;
        }
        AppCacheData.getFileMap().put("performTypeName", etSelectStyle.getTrimmedString());
        AppCacheData.getFileMap().put("signature", etBio.getText().toString().trim());
        if (mMusic_file != null)
            AppCacheData.getFileMap().put("music", mMusic_file.getMusicPath());
        startActivityForResult(new Intent(this, SetFile3Activity.class), 2);
    }

    @OnClick(R.id.tv_login)
    void login() {
        LoginActivity.launch(this, AppConstants.USER_TYPE_MUSICIAN);
    }

    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.et_bio)
    EditText etBio;
    @Bind(R.id.et_select_style)
    XEditText etSelectStyle;

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initListener() {
        super.initListener();
        registerEventBus(this);
        etBio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCount.setText(String.format(strFormat, s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Subscribe
    public void onMusicEnd(String event) {
        btPlay.setBackgroundResource(R.drawable.login_play);
    }


    @Override
    public Activity get() {
        return getCurrentActivity();
    }

    @Override
    public void showStylePop(List<StyleModel> modelList) {
        showPopWindow(this, etSelectStyle, modelList);
    }

    private void initPopWindow(Context context, List<StyleModel> dataList, String selectStyle) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(context);
            View inflate = LayoutInflater.from(context).inflate(R.layout.pop_performer_style_layout, null, false);
            ListView listView = inflate.findViewById(R.id.style_listview);
            adapter = new StyleAdapter(dataList);
            listView.setAdapter(adapter);
            popupWindow.setContentView(inflate);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setWidth(UIUtils.getScreenWidth() - UIUtils.dip2Px(130));
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    etSelectStyle.setText(adapter.getSelectStyle());
                }
            });
        } else {
            adapter.refresh(dataList, selectStyle);
        }
    }

    public void showPopWindow(Context context, View view, List<StyleModel> dataList) {
        UIUtils.hideSoftInput(etSelectStyle);
        initPopWindow(context, dataList, etSelectStyle.getTrimmedString());
        if (popupWindow.isShowing()) popupWindow.dismiss();
        else popupWindow.showAsDropDown(etSelectStyle, 0, 0, Gravity.CENTER);
    }

    @Override
    protected SetFilePresenter createPresenter() {
        return new SetFilePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.second_perfect_file_layout;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (callBack != null && callBack.isPlaying()) {
            callBack.isPlayerMusic();
            btPlay.setBackgroundResource(R.drawable.login_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (callBack != null && callBack.isPlaying()) {
            callBack.isPlayerMusic();
            btPlay.setBackgroundResource(R.drawable.login_play);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                mMusic_file = data.getParcelableExtra("music_file");
                initMusicService();
            }
        } else if (requestCode == 2 && resultCode == AppConstants.ACTIVITY_FINISH){
            setResult(AppConstants.ACTIVITY_FINISH);
            finish();
        }
    }
}
