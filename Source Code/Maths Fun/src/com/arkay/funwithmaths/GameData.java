package com.arkay.funwithmaths;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;

/**
 * Game Data that will store on Google cloud.
 * You can add more data if we want that need to store on cloud. 
 * @author I-BALL
 *
 */

public class GameData {
	
    private static final String SERIAL_VERSION = "1.1";
    
	private int totalScore;
	private int levelCompleted;
	private int countHowManyTimePlay;
	private int countHowManyQuestionCompleted;
	
	/**
	 * Load data on Local SharedPreferences if internet not connection
	 * @param settings
	 */
	public GameData(SharedPreferences settings){
		levelCompleted = settings.getInt(HomeMenuActivity.LEVEL_COMPLETED, 0);
		totalScore =  settings.getInt(HomeMenuActivity.TOTAL_SCORE,0);
		countHowManyTimePlay = settings.getInt(HomeMenuActivity.HOW_MANY_TIMES_PLAY_QUIZ,0);
		countHowManyQuestionCompleted = settings.getInt(HomeMenuActivity.HOW_MANY_QUESTION_COMPLETED,0);
	}
	
	/**
	 * Load data on Cloud that time provide game Data object.
	 * @param settings
	 */
	public GameData(GameData gameData) {
		levelCompleted = gameData.getLevelCompleted();
		totalScore =  gameData.getTotalScore();
		countHowManyTimePlay = gameData.getCountHowManyTimePlay();
		countHowManyQuestionCompleted = gameData.getCountHowManyQuestionCompleted();
	}
	
	 /** Constructs a SaveGame object from serialized data. */
    public GameData(byte[] data) {
        if (data == null) return; // default progress
        loadFromString(new String(data));
    }
    
    
	public GameData() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Convert String to Object. Extract all data form JSON object. JOSON string return from google cloud.
	 * @param json
	 */
	private void loadFromString(String json) {
		 if (json == null || json.trim().equals("")) return;
		 
		 try {
	            JSONObject obj = new JSONObject(json);
	            String format = obj.getString("version");
	            if (!format.equals(SERIAL_VERSION)) {
	                throw new RuntimeException("Unexpected loot format " + format);
	            }
	            JSONObject gameData = obj.getJSONObject("score");
	            this.setTotalScore(gameData.getInt(HomeMenuActivity.TOTAL_SCORE));
	            this.setLevelCompleted(gameData.getInt(HomeMenuActivity.LEVEL_COMPLETED));
	            this.setCountHowManyTimePlay(gameData.getInt(HomeMenuActivity.HOW_MANY_TIMES_PLAY_QUIZ));
	            this.setCountHowManyQuestionCompleted(gameData.getInt(HomeMenuActivity.HOW_MANY_QUESTION_COMPLETED));
	        }
	        catch (JSONException ex) {
	            ex.printStackTrace();
	        }
	        catch (NumberFormatException ex) {
	            ex.printStackTrace();
	        }
		
	}

	 /** Serializes this SaveGame to an array of bytes. */
    public byte[] toBytes() {
        return toString().getBytes();
    }
    
    /**
     * Covert JSON string from GameData object for seting data to cloud for saving purpose.
     */
	@Override
	public String toString() {
		  try {
	            JSONObject gameData = new JSONObject();
	            gameData.put(HomeMenuActivity.TOTAL_SCORE, getTotalScore());
	            gameData.put(HomeMenuActivity.LEVEL_COMPLETED, getLevelCompleted());
	            gameData.put(HomeMenuActivity.HOW_MANY_TIMES_PLAY_QUIZ, getCountHowManyTimePlay());
	            gameData.put(HomeMenuActivity.HOW_MANY_QUESTION_COMPLETED, getCountHowManyQuestionCompleted());
	            //gameData.put(MainActivity.COUNT_RIGHT_ANSWARE_QUESTIONS, getCountHowManyRightAnswareQuestion());
	            
	            JSONObject obj = new JSONObject();
	            obj.put("version", SERIAL_VERSION);
	            obj.put("score", gameData);
	            return obj.toString();
	        }
	        catch (JSONException ex) {
	            ex.printStackTrace();
	            throw new RuntimeException("Error converting save data to JSON.", ex);
	        }
	}

	public void saveDataLocal(SharedPreferences settings){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(HomeMenuActivity.LEVEL_COMPLETED,getLevelCompleted());
		editor.putInt(HomeMenuActivity.TOTAL_SCORE, getTotalScore());
		editor.putInt(HomeMenuActivity.HOW_MANY_TIMES_PLAY_QUIZ,getCountHowManyTimePlay());
		editor.putInt(HomeMenuActivity.HOW_MANY_QUESTION_COMPLETED,getCountHowManyQuestionCompleted());
		//editor.putInt(MainActivity.COUNT_RIGHT_ANSWARE_QUESTIONS,getCountHowManyRightAnswareQuestion());
		editor.commit();
	}
	
	/**
     * Computes the union of this SaveGame with the given SaveGame. The union will have any
     * levels present in either operand. If the same level is present in both operands,
     * then the number of stars will be the greatest of the two.
     *
     * @param other The other operand with which to compute the union.
     * @return The result of the union.
     */
    public GameData unionWith(GameData other) {
    	GameData result = clone();
    	int existingPoint = result.getCountHowManyQuestionCompleted();
        int newPoint = other.getCountHowManyQuestionCompleted();
        if (newPoint > existingPoint) {
        	result.setCountHowManyQuestionCompleted(newPoint);
        }
        
        existingPoint = result.getCountHowManyTimePlay();
        newPoint = other.getCountHowManyTimePlay();
        if (newPoint > existingPoint) {
        	result.setCountHowManyTimePlay(newPoint);
        }

        
        existingPoint = result.getLevelCompleted();
        newPoint = other.getLevelCompleted();
        if (newPoint > existingPoint) {
        	result.setLevelCompleted(newPoint);
        }

        existingPoint = result.getTotalScore();
        newPoint = other.getTotalScore();
        if (newPoint > existingPoint) {
        	result.setTotalScore(newPoint);
        }
       return result;
    }
    
    /** Returns a clone of this SaveGame object. */
    public GameData clone() {
    	GameData result = new GameData();
    	result.setCountHowManyQuestionCompleted(countHowManyQuestionCompleted);
    	result.setCountHowManyTimePlay(countHowManyTimePlay);
    	result.setLevelCompleted(levelCompleted);
    	result.setTotalScore(totalScore);
    	return result;
    }
    
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public int getLevelCompleted() {
		return levelCompleted;
	}
	public void setLevelCompleted(int levelCompleted) {
		this.levelCompleted = levelCompleted;
	}
	public int getCountHowManyTimePlay() {
		return countHowManyTimePlay;
	}
	public void setCountHowManyTimePlay(int countHowManyTimePlay) {
		this.countHowManyTimePlay = countHowManyTimePlay;
	}
	public int getCountHowManyQuestionCompleted() {
		return countHowManyQuestionCompleted;
	}
	public void setCountHowManyQuestionCompleted(int countHowManyQuestionCompleted) {
		this.countHowManyQuestionCompleted = countHowManyQuestionCompleted;
	}

	
}
