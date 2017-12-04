package com.quhwa.linphone;

import com.lichao.lib.QuhwaLinphone;
import com.lichao.lib.callback.RegistrationCallback;
import com.lichao.lib.service.LinphoneService;
import com.quhwa.linphone.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	private static final String TAG = "lichao";
	
	private EditText mAccount;
	private EditText mPassword;
	private EditText mServer;
	private Button mLogin;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        mAccount = (EditText) findViewById(R.id.sip_account);
        mPassword = (EditText) findViewById(R.id.sip_password);
        mServer = (EditText) findViewById(R.id.sip_server);
        mLogin = (Button) findViewById(R.id.press_login);
        
        mLogin.setOnClickListener(this);
        init();
    }
	
	private void init() {
		if (!LinphoneService.isReady()) {
            QuhwaLinphone.startService(this);
            QuhwaLinphone.addCallback(new RegistrationCallback() {
                @Override
                public void registrationOk() {
                    super.registrationOk();
                    Log.e(TAG, "LoginActivity->registrationOk");
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                }

                @Override
                public void registrationFailed() {
                    super.registrationFailed();
                    Log.e(TAG, "LoginActivity->registrationFailed");
                    Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } else {
            goToMainActivity();
        }
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.press_login:
			String account = mAccount.getText().toString();
	        String password = mPassword.getText().toString();
	        String serverIP = mServer.getText().toString();
	        QuhwaLinphone.setAccount(account, password, serverIP);
	        QuhwaLinphone.login();
			break;
		default:
			break;
		}
	}

	private void goToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
	
}
