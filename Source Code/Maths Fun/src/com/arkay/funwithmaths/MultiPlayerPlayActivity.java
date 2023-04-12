package com.arkay.funwithmaths;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.arkay.funwithmaths.beans.Question;
import com.arkay.funwithmaths.dao.QuestionsDAO;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Multipler game screen that play Mutiplayer 
 * @author I-BALL
 *
 */
public class MultiPlayerPlayActivity extends Activity {

	private UpsideDownText upsideTxtQuestionTop, upSideDowntxtTrue ,UpSideDownTxtTotalQuestion;
	private TextView txtQuestionBottom, txtTrue, txtTotalQuestion, txtReadyUser;
	private Button btnSmailTop,btnSmailBottom, btnTrueTop, btnFalseTop, btnTrueBottom, btnFalseBottom;
	private List<Question> playQuizquestions=null;
	private final static int NO_OF_QUESTION = 9;
	int currentIndexQuestion = 0;
	private Handler delayhandler = new Handler();
	private int topTrueCount, bottomTrueCount;
	Dialog dialog =null;
	Activity myActivity;
	private SharedPreferences settings;
	
	 Animation in,out, aniZoonInOne,aniZoonInTwo,aniZoonInThree,aniZoonInFour;
	 
	 //Intersial Ads
	 private InterstitialAd interstitial;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.activity_multy_player);
		myActivity  = this;
		settings = getSharedPreferences(HomeMenuActivity.PREFS_NAME, 0);
		
		 txtReadyUser = (TextView)findViewById(R.id.txtReadyUser);
		 in =  AnimationUtils.loadAnimation(this, R.anim.in_from_right);
		 in.setDuration(200);
		 out =  AnimationUtils.loadAnimation(this, R.anim.in_from_left);
		 out.setDuration(200);

		QuestionsDAO questions = new QuestionsDAO(getPackageName());
		playQuizquestions = questions.getTrueFalseQuestionRendomForMultiPlayer(NO_OF_QUESTION);
		
		upsideTxtQuestionTop = (UpsideDownText)findViewById(R.id.upsideTxtQuestionTop);
		
		txtQuestionBottom  = (TextView)findViewById(R.id.txtQuestionBottom);
		
		txtTrue  = (TextView)findViewById(R.id.txtTrue);
		upSideDowntxtTrue = (UpsideDownText)findViewById(R.id.upSideDowntxtTrue);
		
		txtTotalQuestion  = (TextView)findViewById(R.id.txtTotalQuestion);
		UpSideDownTxtTotalQuestion = (UpsideDownText)findViewById(R.id.UpSideDownTxtTotalQuestion);
		
		btnSmailTop = (Button)findViewById(R.id.btnSmailTop);
		btnSmailBottom = (Button)findViewById(R.id.btnSmailBottom);
		
		btnTrueTop = (Button)findViewById(R.id.btnTrueTop);
		btnFalseTop = (Button)findViewById(R.id.btnFalseTop);
		btnTrueBottom = (Button)findViewById(R.id.btnTrueBottom);
		btnFalseBottom = (Button)findViewById(R.id.btnFalseBottom);
		btnTrueTop.setEnabled(false);
		btnFalseTop.setEnabled(false);
		btnTrueBottom.setEnabled(false);
		btnFalseBottom.setEnabled(false);
		
		System.out.println("Question Size: "+playQuizquestions.size());
		
		btnSmailBottom.setVisibility(View.GONE);
		btnSmailTop.setVisibility(View.GONE);
		
		startAnimation();
		
        /** Create the interstitial.*/
	    interstitial = new InterstitialAd(this);
	    interstitial.setAdUnitId(getString(R.string.admob_intersititial));

	    /** Create ad request. */
	    Resources ress = getResources();
	    /**set testmost false when publish app*/
	    boolean isTestMode = ress.getBoolean(R.bool.istestmode); 
	    AdRequest adRequest =null;
	    if(isTestMode){
	    	  adRequest = new AdRequest.Builder()
	         .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	         .addTestDevice("B15149A4EC1ED23173A27B04134DD483").build();
	    }else{
	    	 adRequest = new AdRequest.Builder().build();
	    }
	    /** Begin loading your interstitial. */
	    interstitial.loadAd(adRequest);

        
	}

	  public void displayInterstitial() {
	    if (interstitial.isLoaded()) {
	      interstitial.show();
	    }
	  }
 	  
	  //Set and start animation that display on starting
	public void startAnimation(){
		aniZoonInOne=  AnimationUtils.loadAnimation(this, R.anim.zoom_in);
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
		 aniZoonInTwo=  AnimationUtils.loadAnimation(this, R.anim.zoom_in);
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
		 
		 aniZoonInThree=  AnimationUtils.loadAnimation(this, R.anim.zoom_in);
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
		 aniZoonInFour=  AnimationUtils.loadAnimation(this, R.anim.zoom_in);
		 aniZoonInFour.setAnimationListener(new Animation.AnimationListener(){
			    @Override
			    public void onAnimationStart(Animation arg0) {
			    }           
			    @Override
			    public void onAnimationRepeat(Animation arg0) {
			    }           
			    @Override
			    public void onAnimationEnd(Animation arg0) {
					btnTrueTop.setEnabled(true);
					btnFalseTop.setEnabled(true);
					btnTrueBottom.setEnabled(true);
					btnFalseBottom.setEnabled(true);

			    	txtReadyUser.setText("");
			    	upsideTxtQuestionTop.setText(playQuizquestions.get(currentIndexQuestion).getQuestion());
			    	upsideTxtQuestionTop.startAnimation(out);
					txtQuestionBottom.setText(playQuizquestions.get(currentIndexQuestion).getQuestion());
					txtQuestionBottom.startAnimation(in);

			    }
			});
		 	}
	public void btnClick(View v){
		switch(v.getId()){
		case R.id.btnTrueTop:
			if(playQuizquestions.get(currentIndexQuestion).isQuestionTrue()){
				topTrueCount++;
				showTrueFalseDialog(true);
			}else{
				bottomTrueCount++;
				showTrueFalseDialog(false);
			}
			 delayhandler = new Handler();
			 delayhandler.postDelayed(nextQuestionRunnable, 1500);
			break;
		case R.id.btnFalseTop:
			if(!playQuizquestions.get(currentIndexQuestion).isQuestionTrue()){
				topTrueCount++;
				showTrueFalseDialog(true);
			}else{
				System.out.println("You are Answare Wronge...TOP");
				bottomTrueCount++;
				showTrueFalseDialog(false);
			}
			 delayhandler = new Handler();
			 delayhandler.postDelayed(nextQuestionRunnable, 1500);
			break;
		case R.id.btnTrueBottom:
			if(playQuizquestions.get(currentIndexQuestion).isQuestionTrue()){
				bottomTrueCount++;
				showTrueFalseDialog(false);
			}else{
				topTrueCount++;
				showTrueFalseDialog(true);
			}
			 delayhandler = new Handler();
			 delayhandler.postDelayed(nextQuestionRunnable, 1500);
			break;
		case R.id.btnFalseBottom:
			if(!playQuizquestions.get(currentIndexQuestion).isQuestionTrue()){
				bottomTrueCount++;
				showTrueFalseDialog(false);
			}else{
				showTrueFalseDialog(true);
				topTrueCount++;
			}
			 delayhandler = new Handler();
			 delayhandler.postDelayed(nextQuestionRunnable, 1500);
			break;
			
		}
		txtTrue.setText(""+bottomTrueCount);
		txtTotalQuestion.setText(""+topTrueCount);
		
		upSideDowntxtTrue.setText(""+topTrueCount);
		UpSideDownTxtTotalQuestion.setText(""+bottomTrueCount);
		
		btnSmailBottom.setVisibility(View.VISIBLE);
		btnSmailTop.setVisibility(View.VISIBLE);

		btnTrueTop.setVisibility(View.GONE);
		btnFalseTop.setVisibility(View.GONE);
		btnTrueBottom.setVisibility(View.GONE);
		btnFalseBottom.setVisibility(View.GONE);
		
		
	}
	 Runnable nextQuestionRunnable = new Runnable()
		{   public void run()
		    {   
			
			currentIndexQuestion++;
			if(currentIndexQuestion>NO_OF_QUESTION){
				System.out.println("GAME over");
				showAnsSaveQuizCompletedDialog();
				return;
			}
			
			upsideTxtQuestionTop.setText(playQuizquestions.get(currentIndexQuestion).getQuestion());
			upsideTxtQuestionTop.startAnimation(out);
			txtQuestionBottom.setText(playQuizquestions.get(currentIndexQuestion).getQuestion());
			txtQuestionBottom.startAnimation(in);
			
			btnTrueTop.setVisibility(View.VISIBLE);
			btnFalseTop.setVisibility(View.VISIBLE);
			btnTrueBottom.setVisibility(View.VISIBLE);
			btnFalseBottom.setVisibility(View.VISIBLE);
			
			btnSmailBottom.setVisibility(View.GONE);
			btnSmailTop.setVisibility(View.GONE);
		    }
		};
		
		public void showTrueFalseDialog(boolean isTopTrue){
			btnSmailBottom.setBackgroundResource(R.drawable.sad_bottom);
			if(isTopTrue){
				btnSmailTop.setBackgroundResource(R.drawable.smile_top);
				btnSmailBottom.setBackgroundResource(R.drawable.sad_bottom);
			}else{
				btnSmailBottom.setBackgroundResource(R.drawable.smile_bottom);
				btnSmailTop.setBackgroundResource(R.drawable.sad_top);
			}
			
		}
		
		public void showAnsSaveQuizCompletedDialog(){
			SharedPreferences.Editor edit = settings.edit();
			edit.putInt(HomeMenuActivity.MULTI_PLAYER_TOP_TRUE_SCORE, topTrueCount);
			edit.commit();
			QuizCompletedForMultiPlayerDialog cdd=new QuizCompletedForMultiPlayerDialog(myActivity);
			
			cdd.show();
			displayInterstitial();
		}


		
}
