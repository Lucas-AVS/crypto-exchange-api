package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository UserRepository;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository) {
        UserRepository = theUserRepository;
    }

    @Override
    public List<User> findAll() {
        return UserRepository.findAll();
    }

    @Override
    public User findById(UUID theId) {
        Optional<User> result = UserRepository.findById(theId);

        User theUser;

        if (result.isPresent()) {
            theUser = result.get();
        }
        else {
            // we didn't find the User
            throw new RuntimeException("Did not find User id - " + theId);
        }

        return theUser;
    }

    @Override
    public User save(User theUser) {
        return UserRepository.save(theUser);
    }

    @Override
    public void deleteById(UUID theId) {
        UserRepository.deleteById(theId);
    }
}