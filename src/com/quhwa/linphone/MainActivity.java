package com.quhwa.linphone;

import org.linphone.core.LinphoneCall;

import com.lichao.lib.QuhwaLinphone;
import com.lichao.lib.callback.PhoneCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = "lichao";
	
	private EditText mDialNum;
	private Button mAudioCall;
	private Button mVideoCall;
	private Button mHangUp;
	private Button mCallIn;
	private Button mToggleSpeaker;
	private Button mToggleMute;
	private Button mOpenLock;
	private Button mVisitePwd;
	private Button mNoCallIn;
	private Button mCanCallIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);           
        
        init();
        
        QuhwaLinphone.addCallback(null, new PhoneCallback() {
            @Override
            public void incomingCall(LinphoneCall linphoneCall) {
                super.incomingCall(linphoneCall);
                // 开启铃声免提
                QuhwaLinphone.toggleSpeaker(true);
                mCallIn.setVisibility(View.VISIBLE);
                mHangUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void outgoingInit() {
                super.outgoingInit();
                mHangUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void callConnected() {
                super.callConnected();
                // 视频通话默认免提，语音通话默认非免提
                QuhwaLinphone.toggleSpeaker(QuhwaLinphone.getVideoEnabled());
                // 所有通话默认非静音
                QuhwaLinphone.toggleMicro(false);
                mCallIn.setVisibility(View.GONE);
                mToggleSpeaker.setVisibility(View.VISIBLE);
                mToggleMute.setVisibility(View.VISIBLE);
            }

            @Override
            public void callEnd() {
                super.callEnd();
                sendBroadcast(new Intent(VideoActivity.RECEIVE_FINISH_VIDEO_ACTIVITY));
                mCallIn.setVisibility(View.GONE);
                mHangUp.setVisibility(View.GONE);
                mToggleMute.setVisibility(View.GONE);
                mToggleSpeaker.setVisibility(View.GONE);
            }
        });
    }

	private void init() {
		mDialNum = (EditText) findViewById(R.id.dial_num);
		mAudioCall = (Button) findViewById(R.id.audio_call);
		mVideoCall = (Button) findViewById(R.id.video_call);
        mHangUp = (Button) findViewById(R.id.hang_up);
        mCallIn = (Button) findViewById(R.id.accept_call);
        mToggleSpeaker = (Button) findViewById(R.id.toggle_speaker);
        mToggleMute = (Button) findViewById(R.id.toggle_mute);
        mOpenLock = (Button) findViewById(R.id.open_lock);
        mVisitePwd = (Button) findViewById(R.id.visite_pwd);
        mNoCallIn = (Button) findViewById(R.id.no_call_in);
        mCanCallIn = (Button) findViewById(R.id.can_call_in);
        
        mAudioCall.setOnClickListener(this);
        mVideoCall.setOnClickListener(this);
        mHangUp.setOnClickListener(this);
        mCallIn.setOnClickListener(this);
        mToggleSpeaker.setOnClickListener(this);
        mToggleMute.setOnClickListener(this);
        mOpenLock.setOnClickListener(this);
        mVisitePwd.setOnClickListener(this);
        mNoCallIn.setOnClickListener(this);
        mCanCallIn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		String dialNum = mDialNum.getText().toString();
		switch (view.getId()) {
		case R.id.audio_call:
	        QuhwaLinphone.callTo(dialNum, false);
			break;
		case R.id.video_call:
	        QuhwaLinphone.callTo(dialNum, true);
	        startActivity(new Intent(MainActivity.this, VideoActivity.class));
	        break;
		case R.id.hang_up:
			QuhwaLinphone.hangUp();
			break;
		case R.id.accept_call:
			QuhwaLinphone.acceptCall();
	        if (QuhwaLinphone.getVideoEnabled()) {
	            startActivity(new Intent(MainActivity.this, VideoActivity.class));
	        }
			break;
		case R.id.toggle_mute:
			QuhwaLinphone.toggleMicro(!QuhwaLinphone.getLC().isMicMuted());
			break;
		case R.id.toggle_speaker:
			QuhwaLinphone.toggleSpeaker(!QuhwaLinphone.getLC().isSpeakerEnabled());
			break;
		case R.id.open_lock:
			String openText = "{\"cmd\":\"openlock\"}";
			QuhwaLinphone.SendMessage(dialNum, openText);
			break;
		case R.id.visite_pwd:
			String vistText = "{\"visitorpw\":\"1234\",\"time\":\"900\",\"roomNo\":\"1010101010101\"}";
			QuhwaLinphone.SendMessage(dialNum, vistText);
			break;
		case R.id.no_call_in:
			String no_call_in_text = "{\"cmd\":\"openbancall\",\"account\":\"1051\"}";
			QuhwaLinphone.SendMessage(dialNum, no_call_in_text);
			break;
		case R.id.can_call_in:
			String can_call_in_text = "{\"cmd\":\"closebancall\",\"account\":\"1051\"}";
			QuhwaLinphone.SendMessage(dialNum, can_call_in_text);
			break;
		default:
			break;
		}
	}
    
}
