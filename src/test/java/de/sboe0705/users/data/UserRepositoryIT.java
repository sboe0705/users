package de.sboe0705.users.data;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import de.sboe0705.users.model.User;

@DataJpaTest
public class UserRepositoryIT {
	
	@Autowired
	private UserRepository underTest;

	
	@Test
	void testSave() throws Exception {
		User user = new User();
		user.setId("jtra001");
		user.setFirstName("John");
		user.setLastName("Travolta");
		
		// when
		underTest.save(user);
		
		// then
		Assertions.assertThat(user.getId()).isNotNull();
	}
}
