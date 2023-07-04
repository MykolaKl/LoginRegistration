package com.testtask.demo.Service;


import com.testtask.demo.dto.UserDto;
import com.testtask.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserDto userDto);

    void updateUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
