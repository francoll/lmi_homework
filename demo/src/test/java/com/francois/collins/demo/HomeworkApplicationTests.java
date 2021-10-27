package com.francois.collins.demo;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@SpringBootTest()
@AutoConfigureMockMvc
class HomeworkApplicationTests {

	@Autowired
	MockMvc mvc;

	@Test
	void basics() throws Exception {

		this.mvc.perform(get("/games")) //
				.andDo(print()) //
				.andExpect(status().isOk()) //
				.andExpect(content().contentType(MediaTypes.HAL_JSON)) //
				.andExpect(jsonPath("$._links.self.href", is("http://localhost/games")));
	}

	@Test
	void newGame() throws Exception {

		mvc.perform(MockMvcRequestBuilders.post("/games")
				.content("{\"name\": \"New Game\"}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.gameId").exists());

		MvcResult result = this.mvc.perform(get("/games/names/New Game")) //
				.andDo(print()) //
				.andExpect(status().isOk()) //
				.andExpect(content().contentType(MediaTypes.HAL_JSON)) //
				.andExpect(jsonPath("$..gameList", hasSize(1))).andReturn();

		JSONArray values = JsonPath.parse(result.getResponse().getContentAsString())
				.read("$..gameList[0].gameId");

		Long gameId = (Long) values.get(0);

		MvcResult result2 = 
				mvc.perform(MockMvcRequestBuilders.post("/games/" + gameId + "/players")
				.content("{\"name\": \"Player 1\"}")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andReturn();

		Long playerId = JsonPath.parse(result2.getResponse().getContentAsString()).read("$.id");

		this.mvc.perform(get("/games/" + gameId + "/players/" + playerId)) //
				.andDo(print()) //
				.andExpect(status().isOk()) //
				.andExpect(content().contentType(MediaTypes.HAL_JSON)) //
				.andExpect(jsonPath("$.id").exists());

	}

}
