package com.arkay.funwithmaths;


import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Display when quiz will completed and TimeUp in single player also display score, islevelcompleted or not etc.
 * @author I-BALL
 *
 */
public class QuizCompletedDialog extends Dialog implements
		android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	private Button   btnPlayAgain,btnHome;
	private TextView txtLevelHeading, txtLevelScore,txtLevelTotalScore; 
	int levelStaus = 1;
	private SharedPreferences settings;
	
	int levelNo=1;
	int lastLevelScore = 0;
	SinglePlayerPlayFragment single;
	public QuizCompletedDialog(Activity a, SinglePlayerPlayFragment single) {
		super(a);
		this.c = a;
		this.single = single;
		settings = a.getSharedPreferences(HomeMenuActivity.PREFS_NAME, a.MODE_PRIVATE);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.custom_dialog);
		
		txtLevelHeading = (TextView)findViewById(R.id.txtLevelHeading);
		txtLevelScore = (TextView)findViewById(R.id.txtLevelScore);
		int totalScore =0;
		totalScore = settings.getInt(HomeMenuActivity.TOTAL_SCORE, 0);
		
		txtLevelScore.setText("Total Score: "+totalScore);
		int lastLevelScore = settings.getInt(HomeMenuActivity.THIS_lEVEL_TOTAL_SCORE,0);
		txtLevelTotalScore = (TextView)findViewById(R.id.txtLevelTotalScore);
		txtLevelTotalScore.setText("Total Score of this Level : "+lastLevelScore);
		
		btnPlayAgain = (Button) findViewById(R.id.btnPlayAgain);
		btnPlayAgain.setOnClickListener(this);
		btnHome = (Button)findViewById(R.id.btnHome);
		btnHome.setOnClickListener(this);
		
		levelStaus = settings.getInt(HomeMenuActivity.IS_LAST_LEVEL_COMPLETED, 1);
		levelNo =settings.getInt(HomeMenuActivity.LEVEL_COMPLETED, 1);
		levelNo++;
		switch(levelStaus){
		case 1:
			txtLevelHeading.setText("Level  "+ levelNo +" Completed.." );
			break;
		case 2:
			txtLevelHeading.setText("Game Over..");
			break;
		case 3:
			txtLevelHeading.setText("Time Up!");
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnHome:
			c.onBackPressed();
			dismiss();
			break;
		case R.id.btnPlayAgain:
			single.playAgain();
			dismiss();
			break;
		default:
			break;
		}
		
	}
	
	
}