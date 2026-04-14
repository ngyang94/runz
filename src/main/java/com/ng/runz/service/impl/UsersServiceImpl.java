package com.ng.runz.service.impl;

import com.ng.runz.dto.UserDto;
import com.ng.runz.exception.ResourceNotFoundException;
import com.ng.runz.exception.ResourceAlreadyExistException;
import com.ng.runz.mapper.Mapper;
import com.ng.runz.mapper.impl.UsersMapper;
import com.ng.runz.model.Role;
import com.ng.runz.model.Users;
import com.ng.runz.repository.UserRepository;
import com.ng.runz.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Mapper<Users,UserDto> userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersServiceImpl(UserRepository userRepository, UsersMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Users getUserByUsername(String username){
        return Optional.of(userRepository.findByUsername(username)).orElseThrow(()-> new ResourceNotFoundException("User not found."));
    }

    @Override
    public Users createUser(UserDto userDto){
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new ResourceAlreadyExistException("Email already exist.");
        }
        Users user = userMapper.mapFrom(userDto);
        user.setRoles(userDto.getRoles().stream().map(roleItem->new Role(roleItem.getRole())).collect(Collectors.toSet()));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
