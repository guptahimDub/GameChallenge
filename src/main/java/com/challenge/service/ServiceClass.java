package com.challenge.service;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


import com.challenge.model.UserInput;
import com.challenge.validation.InputValidation;

@Service
public class ServiceClass {

	private final RestTemplate restTemplate;
	private String queue = "game_queue";

	@Value("${uri.player}")
	private String nextPlayer;

	@Value("${uri.startGame}")
	private String startGameUrl;

	Logger logger = LoggerFactory.getLogger(ServiceClass.class);

	@Autowired
	ConfigurableApplicationContext context;

	public ServiceClass(RestTemplateBuilder restTemplateBuilder) {
		restTemplate = restTemplateBuilder.build();
	}

	public String configureGame(boolean auto, Integer numberStart) {
		if (findNextPlayer(true)) {
			UserInput inputNum = new UserInput();
			Random random = new Random();
			if (auto) {
				inputNum.setNumber(random.nextInt(100000));
			} else {
				inputNum.setNumber(numberStart);
			}

			JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
			jmsTemplate.convertAndSend(queue, inputNum);

			logger.info("Game Started!");
			return "Game Started!";
		} else {

			return "No second Player Found!";
		}

	}

	@JmsListener(destination = "game_queue")
	public void beginGame(UserInput inputNum) {
		logger.info("Number present in In-memory Queue " + inputNum.getNumber());
		restTemplate.postForObject(startGameUrl, inputNum, UserInput.class);
	}

	public boolean findNextPlayer(boolean logInfo)  {
		if (logInfo) {
			logger.info("Searhing for second player...");
		}
		try {
			restTemplate.getForObject(nextPlayer, String.class);
		} catch (Exception ex) {
			logger.error("Game Over: No Second Player found");
			return false;
		}
		if (logInfo) {
			logger.info("Second player Found!");
		}

		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity<String> gameLogic(UserInput inputNum) {
		InputValidation validation = new InputValidation();
		if (validation.validateUserInput(inputNum)) {
			int inputNumber = inputNum.getNumber();

			logger.info("Number Accepted<- " + inputNumber);

			if (inputNumber <= 0) {
				logger.error("Error : Number cannot be Zero or Negative.");
			} else {

				if (inputNumber % 3 == 0) {
					logger.info("[Added 0]");
					inputNum.setNumber(inputNumber / 3);
				} else if ( (inputNumber + 1) % 3 == 0) {
					logger.info("[Added 1]");
					inputNum.setNumber((inputNumber + 1) / 3);
				} else {
					logger.info("[Subtracted 1]");
					inputNum.setNumber((inputNumber - 1) / 3);
				}

				if (inputNum.getNumber() == 1) {
					logger.info("You win!");
				} else {

					logger.info("Number Sent-> " + inputNum.getNumber());

					if (findNextPlayer(false)) {
						restTemplate.postForObject(startGameUrl, inputNum, UserInput.class);
					} else {
						logger.error("Second Player is not Active");
						logger.error("Game Over!");
						return new ResponseEntity(HttpStatus.NOT_FOUND);
					}

				}

			}
		} else {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity(HttpStatus.OK);

	}
}
