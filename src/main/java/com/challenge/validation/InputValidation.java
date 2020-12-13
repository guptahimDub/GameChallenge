package com.challenge.validation;

import com.challenge.model.UserInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InputValidation {
	
	Logger logger = LoggerFactory.getLogger(InputValidation.class);
	
	public boolean validateUserInput(UserInput inputNum) {
		if (inputNum == null || inputNum.getNumber() == null) {
			logger.error("Please Provide a Valid Number to Start a Game");
			logger.error("Game Over!");
			return false;
		} 
		return true;
	}

}
