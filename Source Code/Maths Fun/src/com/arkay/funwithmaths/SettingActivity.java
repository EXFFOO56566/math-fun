package com.arkay.funwithmaths;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

/**
 * Setting screen with activity
 * @author I-BALL
 *
 */
public class SettingActivity  extends Activity implements OnClickListener {

	private ToggleButton togBackgorundMusic, toggleSoundEffect,
			toggleVibration;
	private Button btnShareMe, btnRateMe;
	
	private boolean isBackgroundMusic, isSoundEffect, isVibration;
	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.activity_setting);
		
		settings = getSharedPreferences(HomeMenuActivity.PREFS_NAME, 0);

		isBackgroundMusic = settings.getBoolean(
				HomeMenuActivity.MUSIC_SOUND, false);
		isSoundEffect = settings.getBoolean(
				HomeMenuActivity.SOUND_EFFECT, true);
		isVibration = settings.getBoolean(HomeMenuActivity.VIBRATION,
				true);
		
		togBackgorundMusic = (ToggleButton) findViewById(R.id.togBackgorundMusic);
		togBackgorundMusic.setOnClickListener(this);
		togBackgorundMusic.setText(null);
		togBackgorundMusic.setChecked(isBackgroundMusic);
		

		toggleSoundEffect = (ToggleButton) findViewById(R.id.toggleSoundEffect);
		toggleSoundEffect.setOnClickListener(this);
		toggleSoundEffect.setText(null);
		toggleSoundEffect.setChecked(isSoundEffect);

		toggleVibration = (ToggleButton) findViewById(R.id.toggleVibration);
		toggleVibration.setOnClickListener(this);
		toggleVibration.setText(null);
		toggleVibration.setChecked(isVibration);

		btnShareMe = (Button) findViewById(R.id.btnShareMe);
		btnShareMe.setOnClickListener(this);
		
		btnRateMe = (Button) findViewById(R.id.btnRateMe);
		btnRateMe.setOnClickListener(this);
	}

	public void onClick(View v) {
		Log.i("info", "Start");

		switch (v.getId()) {
		case R.id.togBackgorundMusic:
			//Log.i("info", "" + togBackgorundMusic.isChecked());
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(HomeMenuActivity.MUSIC_SOUND,
					togBackgorundMusic.isChecked());
			editor.commit();
			break;
		case R.id.toggleSoundEffect:
			//Log.i("info", "" + toggleSoundEffect.isChecked());
			editor = settings.edit();
			editor.putBoolean(HomeMenuActivity.SOUND_EFFECT,
					toggleSoundEffect.isChecked());
			editor.commit();
			break;
		case R.id.toggleVibration:
			//Log.i("info", "" + toggleVibration.isChecked());
			editor = settings.edit();
			editor.putBoolean(HomeMenuActivity.VIBRATION,
					toggleVibration.isChecked());
			editor.commit();
			break;
		case R.id.btnRateMe:
			String appPackageName = getPackageName();
			Intent marketIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=" + appPackageName));
			marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
					| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			startActivity(marketIntent);
			break;
		case R.id.btnShareMe:
			startSendMailActivity();
			break;

		}
	}

	public void startSendMailActivity() {

		try {
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_SUBJECT,
					"" + getResources().getString(R.string.app_name));
			String sAux = "\nLet me recommend you this application\n\n";
			sAux = sAux + "https://play.google.com/store/apps/details?id="
					+ getPackageName() + " \n\n";
			i.putExtra(Intent.EXTRA_TEXT, sAux);
			startActivity(Intent.createChooser(i, "choose one"));
		} catch (Exception e) { // e.toString();
		}
	}

}
