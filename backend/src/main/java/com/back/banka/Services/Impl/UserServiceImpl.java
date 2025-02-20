package com.back.banka.Services.Impl;
import com.back.banka.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {


    @Autowired
    private UserRepository userRepository;


}
