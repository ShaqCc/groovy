package com.bunny.groovy.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.bunny.groovy.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/****************************************
 * 功能说明:  音乐播放服务
 *
 * Author: Created by bayin on 2017/12/14.
 ****************************************/

public class MusicService extends Service {

    private MediaPlayer mPlayer;
    private String musicPath;
    private int currentPos;
    public static String MUSIC_EXTRA = "music_path";
    private int mPlayState = STATE_PLAY_PREPARE;
    private final static int STATE_PLAY_NORMAL = 0;
    private final static int STATE_PLAY_PATH_FAILED = 1;
    private final static int STATE_PLAY_PREPARE = 2;

    public interface CallBack {
        boolean isPlayerMusic();

        int callTotalDate();

        int callCurrentTime();

        void iSeekTo(int m_second);

        //        void isPlayPre();
//        void isPlayNext();
        boolean isPlaying();
    }

    public class MyBinder extends Binder implements CallBack {

        @Override
        public boolean isPlayerMusic() {
            return playerMusic();
        }

        @Override
        public int callTotalDate() {
            if (mPlayer != null) {
                return mPlayer.getDuration();
            } else {
                return 0;
            }
        }

        @Override
        public int callCurrentTime() {
            if (mPlayer != null) {
                return mPlayer.getCurrentPosition();
            } else {
                return 0;
            }
        }

        @Override
        public void iSeekTo(int m_second) {
            if (mPlayer != null) {
                mPlayer.seekTo(m_second);
            }
        }

//        @Override
//        public void isPlayPre() {
//            if (--currentPos < 0) {
//                currentPos = 0;
//            }
//            initMusic();
//            playerMusic();
//        }

//        @Override
//        public void isPlayNext() {
//            if (++currentPos > musicPathLists.size() - 1) {
//                currentPos = musicPathLists.size() - 1;
//            }
//            initMusic();
//            playerMusic();
//        }

        @Override
        public boolean isPlaying() {
            return mPlayer.isPlaying();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
    }

    private void initMusic() {
        mPlayState = STATE_PLAY_PREPARE;
        // 根路径
        //      String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmd.mp3";
        mPlayer.reset();
        try {
            mPlayer.setDataSource(musicPath);
            mPlayer.prepareAsync();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    initMusic();
                    EventBus.getDefault().post("end");
                }
            });
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
//                    UIUtils.showToast("Load music success!");
                    mPlayState = STATE_PLAY_NORMAL;
                }
            });
        } catch (Exception e) {
            mPlayState = STATE_PLAY_PATH_FAILED;
            e.printStackTrace();
        }
    }

    public boolean playerMusic() {
        if (mPlayState == STATE_PLAY_NORMAL) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            } else {
                mPlayer.start();
                return true;
            }
        } else if (mPlayState == STATE_PLAY_PREPARE) {
            UIUtils.showToast("Loading music now, please wait a moment.");
        } else if (mPlayState == STATE_PLAY_PATH_FAILED) {
            UIUtils.showToast("Wrong music path.");
        }
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) musicPath = intent.getStringExtra("music_path");
        initMusic();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
        }
    }

    public void setMusicResource(String musicPath) {
        mPlayer.reset();
        try {
            mPlayer.setDataSource(musicPath);
            mPlayer.prepareAsync();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    initMusic();
                    EventBus.getDefault().post("end");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}