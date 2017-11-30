package com.quhwa.linphone;

import com.lichao.lib.QuhwaLinphone;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VideoActivity extends Activity implements OnClickListener{
	private static final String TAG = "lichao";
	
	private FinishVideoActivityReceiver mReceiver;
    public static final String RECEIVE_FINISH_VIDEO_ACTIVITY = "receive_finish_video_activity";

    private Button videoHang;
    private Button videoMute;
    private Button videoSpeaker;
    private SurfaceView mRenderingView;
    private SurfaceView mPreviewView;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        
        init();
        
        IntentFilter intentFilter = new IntentFilter(RECEIVE_FINISH_VIDEO_ACTIVITY);
        mReceiver = new FinishVideoActivityReceiver();
        registerReceiver(mReceiver, intentFilter);
        QuhwaLinphone.setAndroidVideoWindow(new SurfaceView[]{mRenderingView}, new SurfaceView[]{mPreviewView});
    }
	
	private void init() {
		videoHang = (Button) findViewById(R.id.video_hang);
		videoMute = (Button) findViewById(R.id.video_mute);
		videoSpeaker = (Button) findViewById(R.id.video_speaker);
		mRenderingView = (SurfaceView) findViewById(R.id.video_rendering);
		mPreviewView = (SurfaceView) findViewById(R.id.video_preview);
		
		videoHang.setOnClickListener(this);
		videoMute.setOnClickListener(this);
		videoSpeaker.setOnClickListener(this);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        QuhwaLinphone.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        QuhwaLinphone.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        QuhwaLinphone.onDestroy();
    }
	
	public class FinishVideoActivityReceiver extends BroadcastReceiver {
        
		@Override
        public void onReceive(Context context, Intent intent) {
            VideoActivity.this.finish();
        }
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.video_hang:
			QuhwaLinphone.hangUp();
	        finish();
			break;
		case R.id.video_mute:
			QuhwaLinphone.toggleMicro(!QuhwaLinphone.getLC().isMicMuted());
			break;
		case R.id.video_speaker:
			QuhwaLinphone.toggleSpeaker(!QuhwaLinphone.getLC().isSpeakerEnabled());
			break;
		default:
			break;
		}
		
	}
}
