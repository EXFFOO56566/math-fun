package com.arkay.funwithmaths;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.arkay.funwithmaths.beans.Question;
import com.arkay.funwithmaths.dao.QuestionsDAO;

/**
 * Single play game screen display score, level no, levle etc.
 * @author I-BALL
 *
 */
public class SinglePlayerPlayFragment extends android.support.v4.app.Fragment  implements OnClickListener {

	private TextView txtTrue, txt_timer, txtQuestion, txtLevelNo,txtquestionNo,txtReadyUser, txtTotaScore;
	private Button btnFalse, btnTrue;
	
	private MediaPlayer rightAnswareSound, background_music;
	
	
	private  long startTime = 21000;
	private long interval = 1000;
	private CountDownTimer countDownTimer;
	private boolean isMusicSound=false;
	private boolean isSoundEffect=false;
	
	private int trueCount=0;
	private List<Question> playQuizquestions = null;
	private int currentIndexQuestion = 0;
	private int countQuestionCompleted =0;
	
	private static int NO_OF_QUESTION = 9;
	private boolean isAnsTrueOrFalse= false;
	Activity myActivity;
	SharedPreferences prefs;
	int levelNo=0;
	int totalScore = 0;
	int totlaScoreBefor=0;
	int countHowManyTimePlay=0;
	int countHowManyQuestionCompleted=0;
	public static int LEVEL_COMPLETED = 1;
	public static int LEVEL_NOT_COMPLETED = 2;
	public static int TIME_UP =3;
	private boolean isVibration = false;
	private long lastProblemCompletedTime=0;
	private Animation in, aniZoonInOne,aniZoonInTwo,aniZoonInThree,aniZoonInFour;
	private View v;
	private boolean isQuickSolveAchive=false;
	
	  
	 public interface Listener {
	        public void onStartGameRequested(boolean hardMode);
	        public void onShowAchievementsRequested();
	        public void onShowLeaderboardsRequested();
	        public void onSignInButtonClicked();
	        public void onSignOutButtonClicked();
	        public void unlockAchievement(int achievementId, String fallbackString);
	        public void displyHomeScreen();
	        public void  updateLeaderboards(int finalScore);
	        public GameData getGameData();
	        public void saveDataToCloud();
	    }
	  Listener mListener = null;
	  

	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        v = inflater.inflate(R.layout.fragment_single_player, container, false);
	        final int[] CLICKABLES = new int[] {
	                R.id.btnTrue, R.id.btnFalse
	               
	        };
	        
	        for (int i : CLICKABLES) {
	            v.findViewById(i).setOnClickListener(this);
	        }
	        
	       
			prefs = getActivity().getSharedPreferences(HomeMenuActivity.PREFS_NAME, getActivity().MODE_PRIVATE);
			isQuickSolveAchive = prefs.getBoolean("ISQUICKSOLVEACHIVE", false);
			countHowManyTimePlay = mListener.getGameData().getCountHowManyTimePlay();
			countHowManyQuestionCompleted =mListener.getGameData().getCountHowManyQuestionCompleted();
			
			 txtReadyUser = (TextView)v.findViewById(R.id.txtReadyUser);
			 in =  AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.in_from_right);

			rightAnswareSound = MediaPlayer.create(getActivity().getBaseContext(), R.raw.right_ans);
			rightAnswareSound.setVolume(1, 1);
			
			AudioManager am = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

			switch (am.getRingerMode()) {
			    case AudioManager.RINGER_MODE_SILENT:
			    	
			    	rightAnswareSound.setVolume(0, 0);
			        break;
			}
			txtTrue = (TextView)v.findViewById(R.id.txtTrue);
			txtquestionNo = (TextView)v.findViewById(R.id.txtquestionNo);
			txtTotaScore = (TextView)v.findViewById(R.id.txtTotaScore);
			txtLevelNo = (TextView)v.findViewById(R.id.txtLevelNo);
			btnFalse = (Button)v.findViewById(R.id.btnFalse);
			btnTrue = (Button)v.findViewById(R.id.btnTrue);
			txt_timer = (TextView)v.findViewById(R.id.txt_timer);
			txtQuestion = (TextView)v.findViewById(R.id.txtQuestion);
			
			resetAllValue();
	        
	        return v;
	  }
	  
	  /**
	   * Set valuer to all dynamic parameter also set time according level completed
	   */
	  public void resetAllValue(){
			 trueCount=0;
			 currentIndexQuestion=0;
			 countQuestionCompleted=0;
			 totalScore=0;
			 totlaScoreBefor=0;
			 calculateGameTime();
			totalScore = mListener.getGameData().getTotalScore();
			totlaScoreBefor = mListener.getGameData().getTotalScore();
			txtTotaScore.setText("Score: "+totalScore);
			
			levelNo = mListener.getGameData().getLevelCompleted();
			unloadLevelAchivement(levelNo);
			levelNo++;
			txtLevelNo.setText("Level : "+levelNo);
			
			
			isMusicSound = prefs.getBoolean(HomeMenuActivity.MUSIC_SOUND, false);
			isSoundEffect= prefs.getBoolean(HomeMenuActivity.SOUND_EFFECT, true);
			
			myActivity  = getActivity();
			isVibration = prefs.getBoolean(HomeMenuActivity.VIBRATION,
					true);
			isSoundEffect = prefs.getBoolean(HomeMenuActivity.SOUND_EFFECT, true);
			playBackgroundMusic();
			QuestionsDAO questions = new QuestionsDAO(getActivity().getPackageName());
			playQuizquestions = questions.getTrueFalseQuestionRendom(NO_OF_QUESTION,levelNo);
			
			btnFalse.setEnabled(false);
			btnTrue.setEnabled(false);
			
			txtTotaScore.setText("Score: "+totalScore);
			txtTrue.setText(""+trueCount);
			int temp  = currentIndexQuestion;
			temp++;
			txtquestionNo.setText("Q. No: "+temp);
			txtQuestion.setText("");
			txt_timer.setText("00");
			
			if(levelNo>=0 && levelNo<=100){
				if(startTime<21000){
					restartCounter();
				}else{
					startAnimation();	
				}
			}else if(levelNo>=101 && levelNo<=200){
				if(startTime<31000){	
					restartCounter();
				}else{
					startAnimation();	
				}
			}else if(levelNo>=201 && levelNo<=520){
				if(startTime<41000){	
					restartCounter();
				}else{
					startAnimation();	
				}
			}else if(levelNo>=521){
				if(startTime<26000){	
					restartCounter();
				}else{
					startAnimation();	
				}
			}else{
				if(startTime<26000){	
					restartCounter();
				}else{
					startAnimation();	
				}
			}
			
	  }
	  
	  /**
	   * Restart timer
	   */
	    @Override
		public void onResume() {
			super.onResume();
			System.out.println("Resume Call");
			if(background_music!=null){
				if(!background_music.isPlaying()){
					playBackgroundMusic();
				}
			}
			if(countDownTimer!=null){
				if(levelNo>=0 && levelNo<=100){
					if(startTime<21000){
						restartCounter();
					}
				}else if(levelNo>=101 && levelNo<=200){
					if(startTime<31000){	
						restartCounter();
					}
				}else if(levelNo>=201 && levelNo<=520){
					if(startTime<41000){	
						restartCounter();
					}
				}else if(levelNo>=521){
					if(startTime<26000){	
						restartCounter();
					}
				}
			}
			resetAllValue();
			
		}
	    
	    public void onBackPressed()
	    {
	    	
	    	if(background_music!=null){
				if(background_music.isPlaying()){
					background_music.stop();
				}
			}
			if(countDownTimer!=null){
				countDownTimer.cancel();
				
			}
			
			Log.i("INOF", "Stop Call");
			if(countDownTimer!=null){
				countDownTimer.cancel();
			}
			
			 mListener.displyHomeScreen();
	    }
		
	    @Override
		public void onPause() {
			super.onPause();
			
				if(background_music!=null){
					if(background_music.isPlaying()){
						background_music.stop();
					}
				}
				if(countDownTimer!=null){
					countDownTimer.cancel();
				}
				
				if(countDownTimer!=null){
					
					countDownTimer.cancel();
					startTime=   startTime + 1000;
					
				}
			
		}
	   
		
	public void setListener(Listener l) {
	        mListener = l;
	}
	
	/**
	 * Start amimation at Game startup it disply 3..2...1...GO
	 */
	public void startAnimation(){
		if(countDownTimer!=null){
			countDownTimer.cancel();
		}
		
		aniZoonInOne=  AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in);
		 aniZoonInOne.setAnimationListener(new Animation.AnimationListener(){
			    @Override
			    public void onAnimationStart(Animation arg0) {
			    }           
			    @Override
			    public void onAnimationRepeat(Animation arg0) {
			    }           
			    @Override
			    public void onAnimationEnd(Animation arg0) {
			    	txtReadyUser.startAnimation(aniZoonInTwo);
			    	txtReadyUser.setText("2");
			    }
			});
		 txtReadyUser.startAnimation(aniZoonInOne);
		 aniZoonInTwo=  AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in);
		 aniZoonInTwo.setAnimationListener(new Animation.AnimationListener(){
			    @Override
			    public void onAnimationStart(Animation arg0) {
			    }           
			    @Override
			    public void onAnimationRepeat(Animation arg0) {
			    }           
			    @Override
			    public void onAnimationEnd(Animation arg0) {
			    	txtReadyUser.startAnimation(aniZoonInThree);
			    	txtReadyUser.setText("1");
			    }
			});
		 
		 aniZoonInThree=  AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in);
		 aniZoonInThree.setAnimationListener(new Animation.AnimationListener(){
			    @Override
			    public void onAnimationStart(Animation arg0) {
			    }           
			    @Override
			    public void onAnimationRepeat(Animation arg0) {
			    }           
			    @Override
			    public void onAnimationEnd(Animation arg0) {
			    	txtReadyUser.startAnimation(aniZoonInFour);
			    	txtReadyUser.setText("GO..");
			    }
			});
		 aniZoonInFour=  AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in);
		 aniZoonInFour.setAnimationListener(new Animation.AnimationListener(){
			    @Override
			    public void onAnimationStart(Animation arg0) {
			    }           
			    @Override
			    public void onAnimationRepeat(Animation arg0) {
			    }           
			    @Override
			    public void onAnimationEnd(Animation arg0) {
			    	txtReadyUser.setText("");
			    	startGame();
			    }
			});
	}
	/**
	 * calculate time according level completed by user
	 */
	public void calculateGameTime(){
		
		if(levelNo>=0 && levelNo<=100){
			startTime=21000;
		}else if(levelNo>=101 && levelNo<=200){
			startTime=31000;
		}else if(levelNo>=201 && levelNo<=520){
			startTime=41000;
		}else if(levelNo>=521){
			startTime=26000;
		}else{
			startTime =26000;
		}
		
	}
	/** 
	 * Start game means everything is ready now.
	 */
	public void startGame(){
		btnTrue.setEnabled(true);
		btnFalse.setEnabled(true);
		txtQuestion.setText(playQuizquestions.get(currentIndexQuestion).getQuestion());
		int temp  = currentIndexQuestion;
		temp++;
		txtquestionNo.setText("Q. No: "+temp);
		txtQuestion.startAnimation(in);
		txtLevelNo.setText("Level : "+levelNo);
		calculateGameTime();
		totalScore = mListener.getGameData().getTotalScore();
		txtTotaScore.setText("Score: "+totalScore);
		if(background_music!=null){
			if(!background_music.isPlaying()){
				playBackgroundMusic();
			}
		}
		if(countDownTimer!=null){
			restartCounter();
		}else{
			restartCounter();
		}
		
	}
	
	
	 Runnable mUpdateTimeTask = new Runnable()
	{   public void run()
	    {   
		currentIndexQuestion++;
		int temp  = countQuestionCompleted;
		temp++;
		
		txtquestionNo.setText("Q. No: "+temp);
		try{
			txtQuestion.startAnimation(in);
			txtQuestion.setText(playQuizquestions.get(currentIndexQuestion).getQuestion());
		}catch(IndexOutOfBoundsException iob){
			
		}
	    }
	};
	
	/**
	 * Display next question is time is up or question is completed then display game completed dialog.
	 */
	public void nextQuestionDisplay(){
		
		if(isAnsTrueOrFalse){
			playRightSound();
		}else{
			smallViberation();
			System.out.println("Game Over");
			countDownTimer.cancel();
			showAnsSaveQuizCompletedDialog(LEVEL_NOT_COMPLETED);
			return;
		}
		if(countQuestionCompleted>NO_OF_QUESTION){
			if(trueCount>=7){
				//levelNo++;
				showAnsSaveQuizCompletedDialog(LEVEL_COMPLETED);
			}else{
				showAnsSaveQuizCompletedDialog(LEVEL_NOT_COMPLETED);
			}
			countDownTimer.cancel();
		}
	}
	
	/**
	 * play true sound
	 */
	private void playRightSound(){
		if(isSoundEffect){
			rightAnswareSound.start();
		}
		
	}
	
	/**
	 * play small vibration
	 */
	public void smallViberation(){
		if(isVibration){
			Vibrator v = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(150);
		}
		
	}
	public void showAnsSaveQuizCompletedDialog(int levelStatus){
		
		mListener.updateLeaderboards(totalScore);
		SharedPreferences.Editor edit = prefs.edit();
		
		countHowManyTimePlay++; 
		unloadPlayTimeAchivement(countHowManyTimePlay);
		mListener.getGameData().setCountHowManyTimePlay(countHowManyTimePlay);
		mListener.getGameData().setCountHowManyQuestionCompleted(countHowManyQuestionCompleted);
		
		edit.putInt(HomeMenuActivity.IS_LAST_LEVEL_COMPLETED, levelStatus);
		if(levelStatus==LEVEL_NOT_COMPLETED || levelStatus==TIME_UP){
			levelNo--;
		}else{
			if(txt_timer.getText().toString().equalsIgnoreCase("1")){
				mListener.unlockAchievement(R.string.achievement_narrow_escape, "Narrow escapse");
			}
			if(levelNo>=0 && levelNo<=50){
				// now is 20000 second
				if(txt_timer.getText().toString().equalsIgnoreCase("10")){
					mListener.unlockAchievement(R.string.achievement_fast_on_maths, "Narrow escapse");
				}
			}else if(levelNo>=50 && levelNo<=100){
				// now is 15000 second
				if(txt_timer.getText().toString().equalsIgnoreCase("5")){
					mListener.unlockAchievement(R.string.achievement_fast_on_maths, "Narrow escapse");
				}
			}else if(levelNo>=100){
				// now is 14000 second
				if(txt_timer.getText().toString().equalsIgnoreCase("3")){
					mListener.unlockAchievement(R.string.achievement_fast_on_maths, "Narrow escapse");
				}
			}
				
		}
		
		mListener.getGameData().setLevelCompleted(levelNo);
		mListener.getGameData().setTotalScore(totalScore);
		edit.putInt(HomeMenuActivity.THIS_lEVEL_TOTAL_SCORE,totalScore-totlaScoreBefor);
		edit.commit();
		mListener.getGameData().saveDataLocal(prefs);
		mListener.saveDataToCloud();
		QuizCompletedDialog cdd=new QuizCompletedDialog(myActivity,this);
		cdd.show();
	}
	
	public void playAgain(){
		System.out.println("test");
		levelNo = mListener.getGameData().getLevelCompleted();
		unloadLevelAchivement(levelNo);
		txtLevelNo.setText("Level : "+levelNo);
		
		calculateGameTime();
		resetAllValue();
	}
	 
	public void playBackgroundMusic(){
		 if(isMusicSound){
			 AudioManager meng = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		     int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);
	
		     if (volume != 0)
		     {
		    	//set backgroung music here... we can't provide it.
		        /* if (background_music == null)
		        	 
		         	 //background_music = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.rain);
		         if (background_music != null)
		        	 background_music.reset();
		         	//set backgroung music here... we can provie it.
		         	 background_music = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.rain);
		        	 if(background_music!=null){
		        		 background_music.start();
		        	 }*/
		     }
		 }
	 }
	
	 public void restartCounter(){
			if(countDownTimer!=null){
				countDownTimer.cancel();
			}
			countDownTimer = null;
			countDownTimer = new CountDownTimer(startTime, interval) {

				public void onTick(long millisUntilFinished) {
					startTime = millisUntilFinished;
					txt_timer.setText("" + millisUntilFinished / 1000);
				}

				public void onFinish() {
					
					txt_timer.setText("00");
					showAnsSaveQuizCompletedDialog(TIME_UP);
					
				}
			}.start();
	
	 }


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnTrue:
			countHowManyQuestionCompleted++;
			mListener.getGameData().setCountHowManyQuestionCompleted(countHowManyQuestionCompleted);
			
				if(playQuizquestions.get(currentIndexQuestion).isQuestionTrue()){
					long timediff = lastProblemCompletedTime-startTime;
					
					if(!isQuickSolveAchive){
						if(timediff<=1000){
							mListener.unlockAchievement(R.string.achievement_quick_solver, "Quick Solver");
							isQuickSolveAchive=true;
							SharedPreferences.Editor edit = prefs.edit();
							edit.putBoolean("ISQUICKSOLVEACHIVE", isQuickSolveAchive);
							edit.commit();
						}
					}
					lastProblemCompletedTime=startTime;
					isAnsTrueOrFalse=true;
					trueCount++;
					totalScore+=25;
					unloadScoreAchivement(totalScore);
					txtTrue.setText(""+trueCount);
					
				}else{
					System.out.println("You are Answare Wronge...");
					isAnsTrueOrFalse=false;
					//totalScore-=7;
				}
				txtTotaScore.setText("Score: "+totalScore);
				countQuestionCompleted++;
				nextQuestionDisplay();
				Handler delayhandler = new Handler();
				delayhandler.postDelayed(mUpdateTimeTask, 2);
				
			
			break;
		case R.id.btnFalse:
			
				if(!playQuizquestions.get(currentIndexQuestion).isQuestionTrue()){
					System.out.println("You are Answare Right...");
					long timediff = lastProblemCompletedTime-startTime;
					System.out.println("Time Diff: "+timediff);
					if(!isQuickSolveAchive){
						if(timediff<=1000){
							mListener.unlockAchievement(R.string.achievement_quick_solver, "Quick Solver");
							isQuickSolveAchive=true;
							SharedPreferences.Editor edit = prefs.edit();
							edit.putBoolean("ISQUICKSOLVEACHIVE", isQuickSolveAchive);
							edit.commit();
						}
					}
					lastProblemCompletedTime=startTime;
					isAnsTrueOrFalse=true;
					trueCount++;
					totalScore+=25;
					txtTrue.setText(""+trueCount);
				}else{
					isAnsTrueOrFalse=false;
				}
				txtTotaScore.setText("Score: "+totalScore);
				countQuestionCompleted++;
				nextQuestionDisplay();
				 delayhandler = new Handler();
				delayhandler.postDelayed(mUpdateTimeTask, 2);
			break;
		
		}
	}
		
	public void unloadLevelAchivement(int levelNo){
		if(levelNo==0){
			mListener.unlockAchievement(R.string.achievement_beginner, "bigninner");
		}
		if(levelNo==1){
			mListener.unlockAchievement(R.string.achievement_started,"started");
		}
		if(levelNo==10){
			mListener.unlockAchievement(R.string.achievement_right_track,"right track");
		}
		if(levelNo==50){
			mListener.unlockAchievement(R.string.achievement_good_in_maths_on_fast_calculation,"Good in Maths on Fast Calculation");
		}
		if(levelNo==100){
			mListener.unlockAchievement(R.string.achievement_master_in_maths_on_fast_calculation,"Master in Maths on Fast Calculation");
		}
		if(levelNo==500){
			mListener.unlockAchievement(R.string.achievement_expert_in_maths_on_fast_calculation,"Expert in Maths on Fast Calculation");
		}
	}
	public void unloadScoreAchivement(int score){
		if(score>=1000 && score<=1200){
			mListener.unlockAchievement(R.string.achievement_good_in_maths, "good in maths");
		}
		if(score>=5000 && score<=5200){
			mListener.unlockAchievement(R.string.achievement_cool_in_maths, "Cool in maths");
		}
		if(score>=50000 && score<=50200){
			mListener.unlockAchievement(R.string.achievement_very_good_in_mahs, "Very good in maths");
		}
		if(score>=100000 && score<=100200){
			mListener.unlockAchievement(R.string.achievement_fentastic_in_mahts, "fentastic in maths");
		}
		if(score>=200000 && score<=200200){
			mListener.unlockAchievement(R.string.achievement_phenomenal_in_maths, "phenomenal in maths");
		}
		if(score>=300000 && score<=300200){
			mListener.unlockAchievement(R.string.achievement_perfect_on_maths, "perfect on maths");
		}
		if(score>=500000 && score<=500200){
			mListener.unlockAchievement(R.string.achievement_arkay_maths_master_award, "arkay maths master award");
		}
	}
	public void unloadPlayTimeAchivement(int howManyTimePlay){
		if(howManyTimePlay == 2){
			mListener.unlockAchievement(R.string.achievement_good_luck, "Good Luck");
		}
		if(howManyTimePlay == 50){
			mListener.unlockAchievement(R.string.achievement_fifty, "Fifty");
		}
		if(howManyTimePlay == 100){
			mListener.unlockAchievement(R.string.achievement_hundred, "Hundred");
		}
		if(howManyTimePlay == 1000){
			mListener.unlockAchievement(R.string.achievement_thousand, "Thousand");
		}
		if(howManyTimePlay == 2500){
			mListener.unlockAchievement(R.string.achievement_no_higher, "No higher");
		}
	}
}