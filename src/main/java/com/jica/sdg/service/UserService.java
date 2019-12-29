package com.jica.sdg.service;

import com.jica.sdg.model.User;
import com.jica.sdg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> findOne(String userName) {
        return (List<User>) userRepository.findByUserName(userName);
    }
}
