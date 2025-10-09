package com.lucasavs.cryptoexchange;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
//@ActiveProfiles({"jdbc-test", "test"})
@ActiveProfiles({"jpa-test", "test"})
class CryptoexchangeApplicationTests {

	@Test
	void contextLoads() {
	}

}
