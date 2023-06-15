package de.sboe0705.users.data;

import org.springframework.data.repository.CrudRepository;

import de.sboe0705.users.model.User;

public interface UserRepository extends CrudRepository<User, String> {

}
