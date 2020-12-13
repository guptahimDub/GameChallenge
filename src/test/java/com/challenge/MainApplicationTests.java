package com.challenge;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.challenge.controller.Controller;
import com.challenge.service.ServiceClass;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainApplicationTests {

	private MockMvc mock;

	@Autowired
	private Controller controller;

	@Autowired
	private ServiceClass service;

	@Before
	public void setUp() throws Exception {
		mock = MockMvcBuilders.standaloneSetup(controller)
				.build();    
	}

	@Test
	public void checkStatus() {
		try {
			mock.perform(MockMvcRequestBuilders.get("/checkServiceStatus"))
			.andExpect(MockMvcResultMatchers.status().isOk()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWithInvalidInput() {

		try {
			String json = "{\n" +"  \"value\": \"In Valid Number\"\n" +"}";
			mock.perform(MockMvcRequestBuilders.post("/startGame").contentType(MediaType.APPLICATION_JSON).content(json))
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGameWithNegativeInput() {

		try {
			String json = "{\n" +"  \"number\": -19\n" + "}";
			mock.perform(MockMvcRequestBuilders.post("/startGame").contentType(MediaType.APPLICATION_JSON).content(json))
			.andExpect(MockMvcResultMatchers.status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGameWithRandomNumber() {
		if (service.findNextPlayer(false)) {
			try {
				mock.perform(MockMvcRequestBuilders.get("/game/true"))
				.andExpect(MockMvcResultMatchers.content().string("Game Started!"))
				.andExpect(MockMvcResultMatchers.status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testStartGameDown() {
		if (!service.findNextPlayer(false)) {
			try {
				mock.perform(MockMvcRequestBuilders.get("/game/true"))
				.andExpect(MockMvcResultMatchers.content().string("No second Player Found!")) 
				.andExpect(MockMvcResultMatchers.status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testWhenSecondPlayerNotPresent() {

		try {
			if (!service.findNextPlayer(false)) {

				String json = "{\n" +"  \"number\": 27\n" +"}";
				mock.perform(MockMvcRequestBuilders.post("/startGame").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGameWithManualInput() {

		try {
			String json = "{\n" +"  \"number\": 3\n" + "}";
			mock.perform(MockMvcRequestBuilders.post("/startGame").contentType(MediaType.APPLICATION_JSON).content(json))
			.andExpect(MockMvcResultMatchers.status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

