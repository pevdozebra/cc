package co.sptnk.service;

import co.sptnk.service.model.Interest;
import co.sptnk.service.model.User;
import co.sptnk.service.repositories.InterestRepo;
import co.sptnk.service.repositories.UsersRepo;
import liquibase.pro.packaged.L;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UserServiceApplicationTests {

	@Autowired
	UsersRepo usersRepo;

	@Autowired
	InterestRepo interestRepo;


	@Test
	void contextLoads() {
	}

}
