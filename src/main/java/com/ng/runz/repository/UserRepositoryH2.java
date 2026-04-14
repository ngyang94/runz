package com.ng.runz.repository;

import com.ng.runz.model.Role;
import com.ng.runz.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryH2 {

    private final JdbcClient jdbcClient;
    public UserRepositoryH2(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    @Transactional
    public boolean createUser(Users user){
        int effectedRow = jdbcClient.sql("INSERT INTO users (username,email,password) VALUES(?,?,?);")
                .params(
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword()
                ).update();
        Collection<Role> rolesNeedToBeCreateList = new ArrayList<Role>();
        Collection<Role> rolesExistedList = new ArrayList<Role>();

        user.getRoles().stream().forEach(role->{

            jdbcClient.sql("SELECT * FROM role WHERE role=?").params(role.getRole()).query(Role.class).optional().ifPresentOrElse((role1)->{
                rolesExistedList.add(role);
            },()->{
                rolesNeedToBeCreateList.add(role);
            });
        });

        jdbcClient.sql("SELECT * FROM users WHERE username=?").params(user.getUsername()).query(Users.class).optional().ifPresentOrElse((usesFound)->{

            rolesExistedList.forEach((role)->{
                jdbcClient.sql("INSERT INTO user_role(user_id,role_id) VALUES(?,?)").params(usesFound.getId(),role.getId());
            });
            rolesNeedToBeCreateList.forEach(role->{
                int roleRowEffected = jdbcClient.sql("INSERT INTO role(role) VALUES(?)").params(role.getRole()).update();

                if(roleRowEffected>0){
                    jdbcClient.sql("SELECT * FROM role WHERE role=?").params(role.getRole()).query(Role.class).optional().ifPresentOrElse((role1)->{

                        jdbcClient.sql("INSERT INTO user_role(user_id,role_id) VALUES(?,?)").params(usesFound.getId(),role1.getId()).update();
                    },()->{

                    });
                }
            });
        },()->{

        });

        return effectedRow>0;
    }

    public List<Users> findAll(){
        return jdbcClient.sql("SELECT * FROM users").query(Users.class).list();
    }

    public Optional<Users> findUserById(Users user){
        return jdbcClient.sql("SELECT * FROM users WHERE id=?").params(user.getId()).query(Users.class).stream().findFirst();
    }

    public void updateUser(Users user, Long userId){
        jdbcClient.sql("UPDATE TABLE users SET username=?,email=?,password=? WHERE id=?").params(user.getUsername(),user.getEmail(),user.getPassword(),userId).update();
    }

    public Optional<Users> findUserByUsername(String username) {
//        System.out.println("findUserByUsername");
        return jdbcClient.sql("SELECT * FROM users WHERE username=?").params(username).query(Users.class).optional().map((user)->{
            Collection<Role> roles = jdbcClient.sql("SELECT role.role as role, role.id as id FROM users INNER JOIN user_role ON users.id=user_role.user_id INNER JOIN role ON user_role.role_id=role.id WHERE users.id=?").params(user.getId()).query(Role.class).stream().collect(Collectors.toSet());

            user.setRoles(roles);
//            System.out.println(user);
            return user;
        });
    }
}
