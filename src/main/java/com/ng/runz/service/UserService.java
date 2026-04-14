package com.ng.runz.service;

import com.ng.runz.dto.UserDto;
import com.ng.runz.model.Users;

public interface UserService {

    Users getUserByUsername(String username);

    Users createUser(UserDto userDto);
}
