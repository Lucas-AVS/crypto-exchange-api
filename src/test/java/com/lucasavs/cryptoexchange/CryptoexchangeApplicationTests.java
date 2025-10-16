package com.lucasavs.cryptoexchange;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
//@ActiveProfiles({"jdbc", "test"})
@ActiveProfiles({"jpa", "test"})
class CryptoexchangeApplicationTests {

	@Test
	void contextLoads() {
	}

}
