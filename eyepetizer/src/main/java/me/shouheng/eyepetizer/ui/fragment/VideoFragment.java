package me.shouheng.eyepetizer.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;

import me.shouheng.commons.config.BaseConstants;
import me.shouheng.commons.tools.LogUtils;
import me.shouheng.commons.view.fragment.CommonFragment;
import me.shouheng.eyepetizer.R;
import me.shouheng.eyepetizer.databinding.FragmentVideoBinding;

/**
 * @author shouh
 * @version $Id: VideoFragment, v 0.1 2018/6/9 14:19 shouh Exp$
 */
@Route(path = BaseConstants.EYEPETIZER_VIDEO)
public class VideoFragment extends CommonFragment<FragmentVideoBinding> {

    private String url;

    private boolean fullscreen;

    private int mRotation;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void doCreateView(Bundle savedInstanceState) {
        handleArgument();

        configVideo();
    }

    private void handleArgument() {
        Bundle args = getArguments();
        assert args != null;
        url = args.getString(BaseConstants.EYEPETIZER_VIDEO_EXTRA_URL);
        fullscreen = args.getBoolean(BaseConstants.EYEPETIZER_VIDEO_EXTRA_FULLSCREEN);
    }

    private void configVideo() {
        getBinding().vtv.setVideoPath(url);
        getBinding().vtv.setDisplayOrientation(fullscreen ? PLVideoView.ASPECT_RATIO_PAVED_PARENT : PLVideoView.ASPECT_RATIO_16_9);
        getBinding().vtv.setOnPreparedListener(plMediaPlayer -> start());
        getBinding().vtv.setOnBufferingUpdateListener((plMediaPlayer, i) -> LogUtils.d(i + ""));
        getBinding().vtv.setOnCompletionListener(plMediaPlayer -> LogUtils.d("completion"));
        getBinding().vtv.setOnInfoListener((plMediaPlayer, i, i1) -> {
            LogUtils.d(i + "--i1:" + i1);
            return false;
        });
        getBinding().vtv.setOnErrorListener((plMediaPlayer, i) -> {
            LogUtils.d(i + "");
            return false;
        });
    }

    public void play(String url){
        this.url = url;
        getBinding().vtv.setVideoPath(url);
        getBinding().vtv.start();
    }

    private void start(){
        getBinding().vtv.start();
    }

    public void pause(){
        getBinding().vtv.pause();
    }

    public void stopPlayback(){
        getBinding().vtv.stopPlayback();
    }

    public void seekTo(long i){
        getBinding().vtv.seekTo(i);
    }

    public boolean isPlaying(){
        return getBinding().vtv.isPlaying();
    }

    public PLVideoTextureView getVideoView(){
        return getBinding().vtv;
    }

    public int getDisplayAspectRatio(){
        return getBinding().vtv.getDisplayAspectRatio();
    }

    public void onClickRotate(View v) {
        mRotation = (getBinding().vtv.getDisplayAspectRatio() + 90) % 360;
        setDisplayAspectRatio(mRotation);
    }

    /**
     * Should be one of below:
     * {@link PLVideoView#ASPECT_RATIO_4_3}
     * {@link PLVideoView#ASPECT_RATIO_16_9}
     * {@link PLVideoView#ASPECT_RATIO_PAVED_PARENT}
     * {@link PLVideoView#ASPECT_RATIO_FIT_PARENT}
     * {@link PLVideoView#ASPECT_RATIO_ORIGIN}
     *
     * @param ratio see above */
    public void setDisplayAspectRatio(int ratio){
        getBinding().vtv.setDisplayAspectRatio(ratio);
    }

    public void setDisplayOrientation(int orientation){
        getBinding().vtv.setDisplayOrientation(orientation);
    }

    @Override
    public void onResume() {
        super.onResume();
        start();
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayback();
    }
}
