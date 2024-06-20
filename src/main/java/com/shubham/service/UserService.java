package com.shubham.service;

import com.shubham.exception.ResourceNotFoundException;
import com.shubham.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    // CRUD

    public User createUser(User user);

    public List<User> findAllUser();

    public User updateUserByID(String id, User user);

    public Map<String, String> deleteUserByID(String id) throws ResourceNotFoundException;


    public User findByUserID(String userID) throws ResourceNotFoundException;



}
