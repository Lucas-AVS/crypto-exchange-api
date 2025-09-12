package com.lucasavs.cryptoexchange;

import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CryptoExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoExchangeApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(UserRepository repo) {
        return args -> {

            User user = new User();
            user.setEmail("sf4@example.com");
            user.setPasswordHash("testtesttest1234");

            User saved = repo.save(user);

            System.out.println("Inserted: " + saved);

            repo.findById(saved.getId())
                    .ifPresent(u -> System.out.println("Found: " + u));

            repo.findAll().forEach(System.out::println);
        };
    }

}
