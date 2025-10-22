package com.lucasavs.wallet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
//@ActiveProfiles({"jdbc", "test"})
@ActiveProfiles({"jpa", "test"})
class WalletTests {

	@Test
	void contextLoads() {
	}

}
