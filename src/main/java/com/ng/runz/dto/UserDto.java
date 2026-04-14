package com.ng.runz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    Long id;
    String username;
    String email;
    String password;
    Collection<RoleDto> roles = new HashSet<RoleDto>();


    public UserDto(String username, String email, String password, List<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles.stream().map(RoleDto::new).collect(Collectors.toSet());
    }
}
