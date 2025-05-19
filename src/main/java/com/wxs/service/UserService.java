package com.wxs.service;



import com.wxs.pojo.entity.User;

import java.util.List;

public interface UserService {


    int insertUser(User user);


    boolean deleteUserById(Integer id);


    boolean updateUser(User user);


    User getUserById(Integer id);


    List<User> getAllUsers();

    boolean existsByUsername(String username);

    User getUserByUsername(String username);
}
