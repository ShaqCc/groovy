package com.bunny.groovy.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.bunny.groovy.api.ApiRetrofit;
import com.bunny.groovy.weidget.ProgressHUD;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/****************************************
 * 功能说明:  音乐播放工具，缓存工具
 *
 * Author: Created by bayin on 2018/1/8.
 ****************************************/

public class MusicBox {
    private static final String TAG = MusicBox.class.getSimpleName();
    private static String music_path = Environment.getExternalStorageDirectory() + "/groovy";
    private static MediaPlayer sPlayer;
    private static MusicPlayCallback callback;

    private MusicBox() {
    }

    private static MusicBox mInstance = new MusicBox();

    public static MusicBox getInstance() {
        return mInstance;
    }

    public interface MusicPlayCallback {
        void playMusic();

        void stopPlay();

        void musicEnd();
    }

    /**
     * 释放player
     */
    public void relasePlayer() {
        if (sPlayer != null) {
            sPlayer.stop();
            sPlayer.release();
            sPlayer = null;
        }
        if (callback!=null) callback.stopPlay();
    }

    public void setMusicPlayCallback(MusicPlayCallback l) {
        callback = l;
    }

    /**
     * 播放音乐
     *
     * @param path 音乐文件路径
     */
    public void playMusic(String path) {
        if (sPlayer == null)
            prepareMusicPlayer(path);
        else {
            if (sPlayer.isPlaying()) {//暂停
                sPlayer.pause();
                if (callback != null) callback.stopPlay();
            } else {//播放
                sPlayer.start();
                if (callback != null) callback.playMusic();
            }
        }
    }

    /**
     * 初始化播放器
     *
     * @param path
     */
    private void prepareMusicPlayer(String path) {
        sPlayer = new MediaPlayer();
        sPlayer.reset();
        try {
            sPlayer.setDataSource(path);
            sPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    sPlayer.start();
                    if (callback != null) callback.playMusic();
                }
            });
            sPlayer.prepareAsync();
            sPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //音乐播放结束，执行回调，音乐播放器置0
                    if (callback != null) callback.musicEnd();
                    sPlayer.seekTo(0);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放在线音乐
     *
     * @param url 音乐url
     */
    public void playOnLineMusic(String url, Context context) {
        if (TextUtils.isEmpty(url)) {
            UIUtils.showBaseToast("Music URL is empty.");
        } else {
            //播放音乐
            executeMusicUrl(url, context);
        }
    }

    /**
     * @param url
     * @param context
     */
    private void executeMusicUrl(final String url, final Context context) {
        KLog.d("音乐路径:" + music_path);

        final File file = new File(music_path + File.separator + getMusicFileName(url));

        if (file.exists() && file.length() > 200) {

            //播放本地音乐
            playMusic(file.getPath());

        } else {

            final ProgressHUD progressHUD = ProgressHUD.show(context, "缓冲音乐...", true, false, null);

            ApiRetrofit.getInstance().getApiService().downloadMusicAsync(url)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "server contacted and has file");

                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        boolean writtenToDisk = writeResponseBodyToDisk2(response.body(), getMusicFileName(url));
                                        if (writtenToDisk) {
                                            File file1 = new File(music_path + File.separator + getMusicFileName(url));
                                            KLog.d("写入成功, 文件长度", file1.length());
                                        } else {
                                            UIUtils.showBaseToast("Download failed.");
                                        }
                                        Log.d(TAG, "file download was a success? " + writtenToDisk);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        if (progressHUD != null && progressHUD.isShowing())
                                            progressHUD.dismiss();
                                        playMusic(music_path + File.separator + getMusicFileName(url));
                                    }

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        if (progressHUD != null && !progressHUD.isShowing())
                                            progressHUD.show();
                                    }
                                }.execute();
                            } else {
                                Log.d(TAG, "server contact failed");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
        }
    }

    /**
     * 缓存到本地
     *
     * @param body
     * @param fileName
     * @return
     */

    private File writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            File filePath = new File(music_path);
            if (!filePath.exists())
                filePath.mkdir();
            File futureStudioIconFile = new File(music_path, fileName);
            if (!futureStudioIconFile.exists()) futureStudioIconFile.createNewFile();

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                byte[] bytes = body.bytes();

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return futureStudioIconFile;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param url 音乐地址
     * @return 缓存的音乐名称
     */
    private String getMusicFileName(String url) {
        String[] split = url.split("/");
        if (split.length > 0) {
            return split[split.length - 1];
        }
        return "";
    }

    private boolean writeResponseBodyToDisk2(ResponseBody body, String fileName) {
        try {
            File filePath = new File(music_path);
            if (!filePath.exists())
                filePath.mkdir();
            File futureStudioIconFile = new File(music_path, fileName);
            if (!futureStudioIconFile.exists()) futureStudioIconFile.createNewFile();

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
