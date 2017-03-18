package com.ddobryak;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MailServiceApplication.class, webEnvironment = RANDOM_PORT, properties = { "wait.await=false" })
public class MainTest {

	@Test
	public void contextLoads() {
	}

}
