package com.microservices.second;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.microservices.second.app.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SecondApplicationTests {

	@Test
	public void contextLoads() {
	}

}
