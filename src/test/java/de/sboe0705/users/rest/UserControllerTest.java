package de.sboe0705.users.rest;

import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.sboe0705.users.data.UserRepository;
import de.sboe0705.users.model.User;
import de.sboe0705.utils.CustomMockMvcResultMatchers;

@WebMvcTest
class UserControllerTest {

	@MockBean
	private UserRepository userRepositoryMock;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@AfterEach
	public void tearDown() {
		Mockito.verifyNoMoreInteractions(userRepositoryMock);
	}

	@Test
	void testCreateUser() throws Exception {
		User user = new User();
		user.setId("sboe0705");
		user.setFirstName("Sebastian");
		user.setLastName("Böhm");

		// when
		String body = objectMapper.writeValueAsString(user);
		ResultActions result = mockMvc.perform( //
				MockMvcRequestBuilders.post("/users/new") //
						.contentType(MediaType.APPLICATION_JSON) //
						.content(body) //
		);

		// then
		result.andDo(MockMvcResultHandlers.print()) //
				.andExpect(MockMvcResultMatchers.status().isOk());

		Mockito.verify(userRepositoryMock).existsById(user.getId());
		Mockito.verify(userRepositoryMock).save(ArgumentMatchers.refEq(user));
	}

	@Test
	void testCreateUserAlreadyExisting() throws Exception {
		User user = new User();
		user.setId("sboe0705");
		user.setFirstName("Sebastian");
		user.setLastName("Böhm");

		// given
		Mockito.when(userRepositoryMock.existsById(user.getId())).thenReturn(true);

		// when
		String body = objectMapper.writeValueAsString(user);
		ResultActions result = mockMvc.perform( //
				MockMvcRequestBuilders.post("/users/new") //
						.contentType(MediaType.APPLICATION_JSON) //
						.content(body) //
		);

		// then
		result.andDo(MockMvcResultHandlers.print()) //
				.andExpect(MockMvcResultMatchers.status().isConflict()) //
				.andExpect(CustomMockMvcResultMatchers.errorMessage().is(Matchers.equalTo("User ID already exists!")));

		Mockito.verify(userRepositoryMock).existsById(user.getId());
	}

	@Test
	void testCreateUserWithMissingId() throws Exception {
		User user = new User();
		user.setFirstName("Sebastian");
		user.setLastName("Böhm");

		// when
		String body = objectMapper.writeValueAsString(user);
		ResultActions result = mockMvc.perform( //
				MockMvcRequestBuilders.post("/users/new") //
						.contentType(MediaType.APPLICATION_JSON) //
						.content(body) //
		);

		// then
		result.andDo(MockMvcResultHandlers.print()) //
				.andExpect(MockMvcResultMatchers.status().isBadRequest()) //
				.andExpect(CustomMockMvcResultMatchers.errorMessage()
						.is(Matchers.equalTo("Mandatory User ID is not provided!")));
	}

	@Test
	void testGetUsers() throws Exception {
		User user = new User();
		user.setId("sboe0705");
		user.setFirstName("Sebastian");
		user.setLastName("Böhm");

		// given
		Mockito.when(userRepositoryMock.findAll()) //
				.thenReturn(List.of(user));

		// when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/users"));

		// then
		result.andDo(MockMvcResultHandlers.print()) //
				.andExpect(MockMvcResultMatchers.status().isOk()) //
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1))) //
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.equalTo(user.getId()))) //
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Matchers.equalTo(user.getFirstName()))) //
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName", Matchers.equalTo(user.getLastName())));

		Mockito.verify(userRepositoryMock).findAll();
	}

	@Test
	void testGetUser() throws Exception {
		User user = new User();
		user.setId("sboe0705");
		user.setFirstName("Sebastian");
		user.setLastName("Böhm");

		// given
		Mockito.when(userRepositoryMock.findById(user.getId())) //
				.thenReturn(Optional.of(user));

		// when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/user/" + user.getId()));

		// then
		result.andDo(MockMvcResultHandlers.print()) //
				.andExpect(MockMvcResultMatchers.status().isOk()) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(user.getId()))) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.equalTo(user.getFirstName()))) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.equalTo(user.getLastName())));

		Mockito.verify(userRepositoryMock).findById(user.getId());
	}

	@Test
	void testGetUserNotExisting() throws Exception {
		String id = "missing1234";

		// given
		Mockito.when(userRepositoryMock.findById(id)) //
				.thenReturn(Optional.empty());

		// when
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/user/" + id));

		// then
		result.andDo(MockMvcResultHandlers.print()) //
				.andExpect(MockMvcResultMatchers.status().isNotFound());

		Mockito.verify(userRepositoryMock).findById(id);
	}

}