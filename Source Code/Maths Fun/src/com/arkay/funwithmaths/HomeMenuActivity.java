package com.arkay.funwithmaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.appstate.AppStateManager;
import com.google.android.gms.appstate.AppStateStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

/**
 * Home Screen of the app. It will display tow button one for Play and one for play multiplayer
 * @author I-BALL
 *
 */

public class HomeMenuActivity extends BaseGameActivity  implements
	View.OnClickListener,  GooglePlayServicesClient.OnConnectionFailedListener, SinglePlayerPlayFragment.Listener  {

	 public static final String PREFS_NAME = "preferences";
	 private static final String DATABASE_NAME = "database.db";

	 public static final String MUSIC_SOUND = "music_sound";
	 public static final String SOUND_EFFECT = "sound_effect";
	 public static final String VIBRATION = "vibration";
	 
	 public static final String THIS_lEVEL_TOTAL_SCORE = "this_level_total_score";
	 public static final String IS_LAST_LEVEL_COMPLETED = "is_last_level_completed";
	 public static final String LAST_LEVEL_SCORE = "last_level_score";
	
	 public static final String MULTI_PLAYER_TOP_TRUE_SCORE="multi_player_game_score";

	 //Achivement
	 public static final String HOW_MANY_TIMES_PLAY_QUIZ = "how_many_time_play_quiz";
	 public static final String HOW_MANY_QUESTION_COMPLETED = "count_question_completed";
	 public static final String LEVEL_COMPLETED = "level_completed";
	 public static final String TOTAL_SCORE = "total_score";
	 public static final String LEVEL ="level";

	 public static final String VERY_CURIOUS_UNLOCK="is_very_curious_unlocked";
	 
	 private Button btnSinglePlayer, btnMultiplayer, btnSetting,btnAchivement,btnLeaderBoard,btnHelp;
	 final int RC_RESOLVE = 5000, RC_UNUSED = 5001;
	 private static final int OUR_STATE_KEY = 2;
	 
	 private GameData gameData;
	 SharedPreferences settings;
	 SinglePlayerPlayFragment singlePlayerFragment;
	 
	 /** The view to show the ad. */
	  private AdView adView;
	  //Intersial Ads
	 private InterstitialAd interstitial;
	 private final Handler mHandler = new Handler();
	  
	 private ProgressDialog progress;
	  
	 public HomeMenuActivity() {
		 super( BaseGameActivity.CLIENT_GAMES | BaseGameActivity.CLIENT_APPSTATE);
	 }
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.activity_home_menu);
		
		settings = getSharedPreferences(HomeMenuActivity.PREFS_NAME, 0);
		gameData = new GameData(getSharedPreferences(HomeMenuActivity.PREFS_NAME, 0));
		
		btnSinglePlayer = (Button)findViewById(R.id.btnSinglePlayer);
		btnSinglePlayer.setOnClickListener(this);
		btnMultiplayer = (Button)findViewById(R.id.btnMultiplayer);
		btnMultiplayer.setOnClickListener(this);
		btnSetting = (Button)findViewById(R.id.btnSetting);
		btnSetting.setOnClickListener(this);
		
		btnAchivement = (Button)findViewById(R.id.btnAchivement);
		btnAchivement.setOnClickListener(this);
		btnLeaderBoard = (Button)findViewById(R.id.btnLeaderBoard);
		btnLeaderBoard.setOnClickListener(this);
		
		btnHelp= (Button)findViewById(R.id.btnHelp);
		btnHelp.setOnClickListener(this);
		
		checkDB();
		
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);
	    
	    singlePlayerFragment = new SinglePlayerPlayFragment();
	    singlePlayerFragment.setListener(this);

        SignInButton mSignInButton = (SignInButton)findViewById(R.id.sign_in_button);
        
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the asynchronous sign in flow
            	System.out.println("Click on Sign-in");
                beginUserInitiatedSignIn();
            }
        });
         
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
	    
        // Create an ad.
        adView = new AdView(getApplicationContext());
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.admob_banner));

        // Add the AdView to the view hierarchy. The view will have no size until the ad is loaded.
        LinearLayout layout = (LinearLayout) findViewById(R.id.ads_layout);
        layout.addView(adView);

        mHandler.postDelayed(mUpdateUITimerTask, 10 * 1200);
        
	    progress = new ProgressDialog(this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Data Loading..");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        
	}

	/**
	 * Display Interstital ads.
	 */
	public void displayInterstitial() {
	    if (interstitial.isLoaded()) {
	      interstitial.show();
	    }
	  }
	
	public void checkDB() {
		try {
			String packageName = this.getPackageName();
			String destPath = "/data/data/" + packageName + "/databases";
			String fullPath = "/data/data/" + packageName + "/databases/"+ DATABASE_NAME;
			// this database folder location
			File f = new File(destPath);
			// this database file location
			File obj = new File(fullPath);
			// check if databases folder exists or not. if not create it
			if (!f.exists()) {
				f.mkdirs();
				f.createNewFile();
			}else{
				boolean isDelete = f.delete();
				Log.i("Delete", "Delete"+isDelete);
				
			}
			
			// check database file exists or not, if not copy database from assets
			if (!obj.exists()) {
				this.CopyDB(fullPath);
			}else{
				this.CopyDB(fullPath);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void CopyDB(String path) throws IOException {

		InputStream databaseInput = null;
		String outFileName = path;
		OutputStream databaseOutput = new FileOutputStream(outFileName);
		
		byte[] buffer = new byte[1024];
		int length;

		// open database file from asset folder
		databaseInput = this.getAssets().open(DATABASE_NAME);
		while ((length = databaseInput.read(buffer)) > 0) {
			databaseOutput.write(buffer, 0, length);
			databaseOutput.flush();
		}
		databaseInput.close();

		databaseOutput.flush();
		databaseOutput.close();
	}

	@Override
	public void onSignInFailed() {
		System.out.println("Sing In Fail");
		findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
		findViewById(R.id.sign_out_button).setVisibility(View.GONE);
		
		findViewById(R.id.sign_out_bar).setVisibility(View.GONE);
		findViewById(R.id.sign_in_bar).setVisibility(View.VISIBLE);
		progress.cancel();
	}

	@Override
	public void onSignInSucceeded() {
		System.out.println("Sing In Succcess");
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
		
		findViewById(R.id.sign_in_bar).setVisibility(View.GONE);
		findViewById(R.id.sign_out_bar).setVisibility(View.VISIBLE);
		
		loadFromCloud();
		 
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		System.out.println("Result Code: onConnectionFailed: " + arg0);
	}

	public void unlockAchievement(int achievementId, String fallbackString) {
		if (isSignedIn()) {
			Games.Achievements.unlock(getApiClient(), getString(achievementId));
		} 
	}

	@Override
	public void onStartGameRequested(boolean hardMode) {
		 getSupportFragmentManager().popBackStack();
		 this.findViewById(R.id.linearLayout1).setVisibility(View.VISIBLE);
	}

	@Override
	public void onShowAchievementsRequested() {
		// TODO Auto-generated method stub
		if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
        }
	}
	
	@Override
	public void onShowLeaderboardsRequested() {
		
	}

	@Override
	public void onSignInButtonClicked() {
		
	}

	@Override
	public void onSignOutButtonClicked() {
		
	}
	boolean addList = false;

	@Override
	public void onBackPressed() {
		getSupportFragmentManager().popBackStack();
		this.findViewById(R.id.linearLayout1).setVisibility(View.VISIBLE);
		if(getSupportFragmentManager().getBackStackEntryCount()==0){
			super.onBackPressed();
			displayInterstitial(false);
		}
		
	}
	 public void displayInterstitial(boolean isFirstTime) {
		    if (interstitial.isLoaded()) {
		      interstitial.show();
		      if(isFirstTime){
		    	  // Create the interstitial.
				    interstitial = new InterstitialAd(this);
				    interstitial.setAdUnitId(getString(R.string.admob_intersititial));

				    // Begin loading your interstitial.
				    interstitial.loadAd(new AdRequest.Builder().build());
		      }
		    }
		  }
	 

	  
	@Override
	public void displyHomeScreen() {
		getSupportFragmentManager().popBackStack();
	}

	 /**
     * Update leaderboards with the user's score.
     *
     * @param finalScore The score the user got.
     */
	@Override
    public void updateLeaderboards(int finalScore) {
		if (isSignedIn()) {
	    	if (finalScore >= 0) {
	            Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_fun_with_maths),
	                   finalScore);
	            
	            byte[] scoreData = intToByteArray(finalScore); 
	            AppStateManager.update(getApiClient(), 1, scoreData);
	            

	        }
		}
    }
	public static byte[] intToByteArray(int a) {
	    return BigInteger.valueOf(a).toByteArray();
	}

	public static int byteArrayToInt(byte[] b) {
	    return new BigInteger(b).intValue();
	}
    

	 public  void loadFromCloud() {
		  if(isSignedIn()){
	        AppStateManager.load(getGameHelper().getApiClient(), OUR_STATE_KEY).setResultCallback(mResultCallback);
		  }
	    }

	 public   void saveToCloud() {
		 if(isSignedIn()){
	        AppStateManager.update(getGameHelper().getApiClient(), OUR_STATE_KEY, gameData.toBytes());
		 }
	    }


	    ResultCallback<AppStateManager.StateResult> mResultCallback = new
	            ResultCallback<AppStateManager.StateResult>() {
	        @Override
	        public void onResult(AppStateManager.StateResult result) {
	            AppStateManager.StateConflictResult conflictResult = result.getConflictResult();
	            AppStateManager.StateLoadedResult loadedResult = result.getLoadedResult();
	            if (loadedResult != null) {
	            	processStateLoaded(loadedResult);
	            	
	            } else if (conflictResult != null) {
	                processStateConflict(conflictResult);
	            }
	        }
	    };
	    private void processStateConflict(AppStateManager.StateConflictResult result) {
	        // Need to resolve conflict between the two states.
	        // We do that by taking the union of the two sets of cleared levels,
	        // which means preserving the maximum star rating of each cleared
	        // level:
	        byte[] serverData = result.getServerData();
	        byte[] localData = result.getLocalData();

	        GameData localGame = new GameData(localData);
	        GameData serverGame = new GameData(serverData);
	        GameData resolvedGame = localGame.unionWith(serverGame);

	        AppStateManager.resolve(getApiClient(), result.getStateKey(), result.getResolvedVersion(),
	                resolvedGame.toBytes()).setResultCallback(mResultCallback);
	    }
	    
	    private void processStateLoaded(AppStateManager.StateLoadedResult result) {
	        
	    	switch (result.getStatus().getStatusCode()) {
	        case AppStateStatusCodes.STATUS_OK:
	            // Data was successfully loaded from the cloud: merge with local data.
	            gameData = gameData.unionWith(new GameData(result.getLocalData()));
	            saveToCloud();
	            gameData.saveDataLocal(settings);
	            GameData tem = new GameData(result.getLocalData());
	            System.out.println("Game Data: "+tem);
	            System.out.println("Local Data: "+gameData);
	            progress.cancel();
	            break;
	        case AppStateStatusCodes.STATUS_STATE_KEY_NOT_FOUND:
	            // key not found means there is no saved data. To us, this is the same as
	            // having empty data, so we treat this as a success.
	        	progress.cancel();
	            break;
	        case AppStateStatusCodes.STATUS_NETWORK_ERROR_NO_DATA:
	            // can't reach cloud, and we have no local state. Warn user that
	            // they may not see their existing progress, but any new progress won't be lost.
	        	progress.cancel();
	            break;
	        case AppStateStatusCodes.STATUS_NETWORK_ERROR_STALE_DATA:
	            // can't reach cloud, but we have locally cached data.
	        	progress.cancel();
	            break;
	        case AppStateStatusCodes.STATUS_CLIENT_RECONNECT_REQUIRED:
	            // need to reconnect AppStateClient
	            reconnectClient();
	            break;
	        default:
	            break;
	        }
	    }
		@Override
		public GameData getGameData() {
			return this.gameData;
		}

		@Override
		public void saveDataToCloud() {
			saveToCloud();
		}

		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			case R.id.btnSinglePlayer:
				getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, singlePlayerFragment ).addToBackStack( "tag" ).commit();
				
			break;
			case R.id.btnMultiplayer:
				Intent selectLevelActivity = new Intent(this, MultiPlayerPlayActivity.class);
				startActivity(selectLevelActivity);
				break;
			case R.id.btnSetting:
				selectLevelActivity = new Intent(this, SettingActivity.class);
				startActivity(selectLevelActivity);
				break;
			case R.id.btnLeaderBoard:
				if (isSignedIn()) {
					SharedPreferences.Editor edit = settings.edit();
					edit.putInt(VERY_CURIOUS_UNLOCK, 1);
					edit.commit();
					unlockAchievement(R.string.achievement_very_curious,"Very Curious");
					startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),RC_UNUSED);
				} else {
					showAlert(getString(R.string.leaderboards_not_available));
				}
				
				
				break;
			case R.id.btnAchivement:
				if (isSignedIn()) {
					unlockAchievement(R.string.achievement_curious, "Curious");
					startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),RC_UNUSED);
				}else {
					showAlert(getString(R.string.achievements_not_available));
				}
			break;
			case R.id.btnHelp:
				QuizCompletedForMultiPlayerDialog cdd=new QuizCompletedForMultiPlayerDialog(this);
				
				cdd.show();
				break;
		}
			if (v.getId() == R.id.sign_in_button) {
				beginUserInitiatedSignIn();
				findViewById(R.id.sign_in_button).setVisibility(View.GONE);
				findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
				findViewById(R.id.sign_in_bar).setVisibility(View.GONE);
				findViewById(R.id.sign_out_bar).setVisibility(View.VISIBLE);
			} else if (v.getId() == R.id.sign_out_button) {
				signOut();
				findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
				findViewById(R.id.sign_out_button).setVisibility(View.GONE);
				findViewById(R.id.sign_out_bar).setVisibility(View.GONE);
				findViewById(R.id.sign_in_bar).setVisibility(View.VISIBLE);
			}
		}
		
		 @Override
		  public void onResume() {
		    super.onResume();
		    if (adView != null) {
		      adView.resume();
		    }
		  }

		  @Override
		  public void onPause() {
		    if (adView != null) {
		      adView.pause();
		    }
		    super.onPause();
		  }

		  /** Called before the activity is destroyed. */
		  @Override
		  public void onDestroy() {
		    // Destroy the AdView.
		    if (adView != null) {
		      adView.destroy();
		    }
		    super.onDestroy();
		  }

		
		  class QuizCompletedForMultiPlayerDialog extends Dialog implements
			android.view.View.OnClickListener {

		public Activity c;
		public Dialog d;
		int levelStaus = 1;

		int levelNo = 1;
		int lastLevelScore = 0;
		public QuizCompletedForMultiPlayerDialog(Activity a) {
			super(a);
			this.c = a;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.custom_help_dialog);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			
			case R.id.btnPlayAgain:
				
				break;
			case R.id.btnQuit:
				
			default:
				break;
			}
			dismiss();
		}
	}

			
	/**
	 * Display Ads after some interval of game start.
	 */
	private final Runnable mUpdateUITimerTask = new Runnable() {
		public void run() {
			displayInterstitial(true);

		}
	};
		  
}
