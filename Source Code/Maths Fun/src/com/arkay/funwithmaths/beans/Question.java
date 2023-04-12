package com.arkay.funwithmaths.beans;

/**
 * Question beans, When question fetch from database then create list of this object and provide to
 * single or multiplay.
 * @author I-BALL
 *
 */
public class Question {
	private int questionID;
	private String question;
	private boolean isQuestionTrue;
	private String answare;
	
		
	public Question() {
		super();
	}

	public int getQuestionID() {
		return questionID;
	}

	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public boolean isQuestionTrue() {
		return isQuestionTrue;
	}

	public void setQuestionTrue(boolean isQuestionTrue) {
		this.isQuestionTrue = isQuestionTrue;
	}

	public String getAnsware() {
		return answare;
	}


	public void setAnsware(String answare) {
		this.answare = answare;
	}

	

}
