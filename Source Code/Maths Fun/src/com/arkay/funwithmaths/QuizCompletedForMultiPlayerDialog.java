package com.arkay.funwithmaths;


import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Display when mutiplayer quiz completed
 * @author I-BALL
 *
 */
		
public class QuizCompletedForMultiPlayerDialog extends Dialog implements
		android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	private Button   btnPlayAgain;
	
	private TextView txtHeaderBottom ,txtBottomScoreBoard;
	private UpsideDownText txtHeaderTop,txtTopScoreBoard;
	int levelStaus = 1;
	private SharedPreferences settings;
	
	int levelNo=1;
	int lastLevelScore = 0;
	public QuizCompletedForMultiPlayerDialog(Activity a) {
		super(a);
		this.c = a;
		settings = a.getSharedPreferences(HomeMenuActivity.PREFS_NAME, a.MODE_PRIVATE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_multiplayer_dialog);
		
		txtHeaderBottom = (TextView)findViewById(R.id.txtHeaderBottom);
		
		txtHeaderTop = (UpsideDownText)findViewById(R.id.txtHeaderTop);
		
		txtBottomScoreBoard = (TextView)findViewById(R.id.txtBottomScoreBoard);
		txtTopScoreBoard = (UpsideDownText)findViewById(R.id.txtTopScoreBoard);
		int topTrueScrore =0;
		
		topTrueScrore = settings.getInt(HomeMenuActivity.MULTI_PLAYER_TOP_TRUE_SCORE, 0);
		if(topTrueScrore>5){
			txtHeaderTop.setText("Congratulation! You Win..");
			txtHeaderBottom.setText("You lose! Please try again");
		}else{
			txtHeaderBottom.setText("Congratulation! You Win..");
			txtHeaderTop.setText("You lose! Please try again");
		}
		if(topTrueScrore==5){
			txtHeaderBottom.setText("You are Lucky");
			txtHeaderTop.setText("You are Lucky");
		}
		txtTopScoreBoard.setText("True Answare "+topTrueScrore+" Questions out of 10");
		txtBottomScoreBoard.setText("True Answare "+(10-topTrueScrore)+" Questions out of 10");
		btnPlayAgain = (Button) findViewById(R.id.btnPlayAgain);
		btnPlayAgain.setOnClickListener(this);
		
		levelStaus = settings.getInt(HomeMenuActivity.IS_LAST_LEVEL_COMPLETED, 1);
		levelNo =settings.getInt(HomeMenuActivity.LEVEL_COMPLETED, 1);
		switch(levelStaus){
		case 1:
			levelNo--;
			break;
		case 2:
			levelNo--;
			break;
		case 3:
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnPlayAgain:
			c.onBackPressed();
			break;
		default:
			break;
		}
		dismiss();
	}

}