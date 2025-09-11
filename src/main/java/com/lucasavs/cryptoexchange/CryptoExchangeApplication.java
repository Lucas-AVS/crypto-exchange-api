package com.lucasavs.cryptoexchange;

import com.lucasavs.cryptoexchange.model.User;
import com.lucasavs.cryptoexchange.repo.UserRepoI;
import com.lucasavs.cryptoexchange.repo.UsersRepo;
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
    CommandLineRunner demo(UsersRepo repo) {
        return args -> {

            User user = new User();
            user.setEmail("springdatatest2@example.com");
            user.setPasswordHash("testtesttest1234");

            User saved = repo.save(user);

            System.out.println("Inserted: " + saved);

            repo.findById(saved.getId())
                    .ifPresent(u -> System.out.println("Found: " + u));

            repo.findAll().forEach(System.out::println);
        };
    }

}
