package com.lucasavs.cryptoexchange;

import com.lucasavs.cryptoexchange.model.User;
import com.lucasavs.cryptoexchange.repo.UserRepoI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@SpringBootApplication
public class CryptoExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoExchangeApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(UserRepoI repo) {
        return args -> {

            User user = new User(
                    UUID.randomUUID(),
                    "kiryl@example.com",
                    "adsfafsgsgadg1",
                    Timestamp.from(Instant.now())
            );
            int rows = repo.save(user);
            System.out.println("Inserted: " + rows);


            repo.findById(user.getId())
                    .ifPresent(u -> System.out.println("Found: " + u));


            repo.findAll().forEach(System.out::println);
        };
    }
}
