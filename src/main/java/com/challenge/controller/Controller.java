package com.challenge.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.model.UserInput;
import com.challenge.service.ServiceClass;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags = {"GameTakeaway"})
public class Controller {
	
	@Autowired
	ServiceClass service;

	@GetMapping("/game/{auto}")
	@ApiOperation("Begin the Game (Automatic or Manual)	")
	public String initiateGameManuallyOrAuto(@ApiParam("choose true when automatic  choose false when manual") @PathVariable boolean auto,
			 @ApiParam("Manual Option chosen with number") @RequestParam(value = "number") Optional<Integer> manualNumber) {
		return service.configureGame(auto, manualNumber.orElse(null));
	}
	
	@PostMapping("/startGame")
	@ApiOperation("Start the Game Directly")
	public  ResponseEntity<String> beginGame(@RequestBody UserInput inputNum) {
		return service.gameLogic(inputNum);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation("Check Status Of Service")
	@GetMapping("/checkServiceStatus")
	public ResponseEntity<String> check() {
		return new ResponseEntity(HttpStatus.OK);
	}
}
