package com.arkay.funwithmaths.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arkay.funwithmaths.beans.Question;

public class QuestionsDAO {
	
	private String packageName;
	
	private static final String DATABASE_NAME = "database.db";
	
	public QuestionsDAO(String packageName){
		this.packageName = packageName;
		
	}
	
	/** 
	 * Get Maths problem from Database according to what's level user have completed. more level completed means
	 * it fetch difficult Maths problem.
	 * @param noOfQuestion
	 * @param levelNo
	 * @return
	 */
	public List<Question> getTrueFalseQuestionRendom(int noOfQuestion,int levelNo){
		List<Question> playQuizquestions = new ArrayList<Question>();
		int total = noOfQuestion +5;
		int questionLevel = 0;
		if(levelNo>=1 && levelNo<=10){
			questionLevel =1;
		}else if(levelNo>=11 && levelNo<=30){
			questionLevel =2;
		}else if(levelNo>=21 && levelNo<=50){
			questionLevel =3;
		}else if(levelNo>=51 && levelNo<=70){
			questionLevel =4;
		}else if(levelNo>=71 && levelNo<=90){
			questionLevel =5;
		}else if(levelNo>=91 && levelNo<=100){
			questionLevel =6;
		}else if(levelNo>=101 && levelNo<=120){
			questionLevel =7;
		}else if(levelNo>=121 && levelNo<=140){
			questionLevel =8;
		}else if(levelNo>=141 && levelNo<=160){
			questionLevel =9;
		}else if(levelNo>=161 && levelNo<=180){
			questionLevel =10;
		}else if(levelNo>=181 && levelNo<=200){
			questionLevel =11;
		}else if(levelNo>=201 && levelNo<=220){
			questionLevel =12;
		}else if(levelNo>=221 && levelNo<=240){
			questionLevel =11;
		}else if(levelNo>=241 && levelNo<=260){
			questionLevel =12;
		}else if(levelNo>=261 && levelNo<=280){
			questionLevel =13;
		}else if(levelNo>=281 && levelNo<=280){
			questionLevel =14;
		}else if(levelNo>=281 && levelNo<=300){
			questionLevel =15;
		}else if(levelNo>=301 && levelNo<=320){
			questionLevel =16;
		}else if(levelNo>=321 && levelNo<=340){
			questionLevel =17;
		}else if(levelNo>=341 && levelNo<=360){
			questionLevel =18;
		}else if(levelNo>=361 && levelNo<=380){
			questionLevel =19;
		}else if(levelNo>=381 && levelNo<=400){
			questionLevel =20;
		}else if(levelNo>=401 && levelNo<=420){
			questionLevel =21;
		}else if(levelNo>=421 && levelNo<=440){
			questionLevel =22;
		}else if(levelNo>=441 && levelNo<=460){
			questionLevel =23;
		}else if(levelNo>=461 && levelNo<=480){
			questionLevel =24;
		}else if(levelNo>=481 && levelNo<=500){
			questionLevel =25;
		}else if(levelNo>=501 && levelNo<=520){
			questionLevel =26;
		}else{
			questionLevel=0;
		}
		String sql = "select *  FROM questions ORDER BY RANDOM() LIMIT "+total;
		if(questionLevel==0){
			sql = "select *  FROM questions ORDER BY RANDOM() LIMIT "+total;
		}else{
			sql = "select *  FROM questions where question_level = "+ questionLevel+ " ORDER BY RANDOM() LIMIT "+total;
		}
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/" + packageName + "/databases/"	+ DATABASE_NAME, null, 0);
        Cursor cursor = db.rawQuery(sql, null);
        int i=1;
        if (cursor.moveToFirst()){ // data?
         	do{
         		Question question = new Question();
         		question.setQuestionID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("question_id"))));
         		question.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
         		question.setAnsware(cursor.getString(cursor.getColumnIndex("right_answare")));
         		question.setQuestionTrue(cursor.getInt(cursor.getColumnIndex("is_question_true"))>0);
         		playQuizquestions.add(question);
         		i++;
        	   }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Collections.shuffle(playQuizquestions);
        return playQuizquestions;
	}
	public List<Question> getTrueFalseQuestionRendomForMultiPlayer(int noOfQuestion ){
		List<Question> playQuizquestions = new ArrayList<Question>();
		int total = noOfQuestion +5;
		String sql = "select *  FROM questions ORDER BY RANDOM() LIMIT "+total;
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/" + packageName + "/databases/"	+ DATABASE_NAME, null, 0);
        Cursor cursor = db.rawQuery(sql, null);
        int i=1;
        if (cursor.moveToFirst()){ // data?
         	do{
         		Question question = new Question();
         		question.setQuestionID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("question_id"))));
         		question.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
         		question.setAnsware(cursor.getString(cursor.getColumnIndex("right_answare")));
         		question.setQuestionTrue(cursor.getInt(cursor.getColumnIndex("is_question_true"))>0);
         		playQuizquestions.add(question);
         		i++;
        	   }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return playQuizquestions;
	}

}
