package com.example.restservice.Repository;
import org.springframework.data.repository.CrudRepository;
import com.example.restservice.Model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
