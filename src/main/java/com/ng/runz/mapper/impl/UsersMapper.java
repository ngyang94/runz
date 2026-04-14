package com.ng.runz.mapper.impl;

import com.ng.runz.dto.UserDto;
import com.ng.runz.mapper.Mapper;
import com.ng.runz.model.Users;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper implements Mapper<Users, UserDto> {

    private final ModelMapper modelMapper;
    public UsersMapper(){
        this.modelMapper = new ModelMapper();
    }

    @Override
    public UserDto mapTo(Users users) {
        return modelMapper.map(users,UserDto.class);
    }

    @Override
    public Users mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, Users.class);
    }
}
