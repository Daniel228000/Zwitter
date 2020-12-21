package com.example.ZwitterSpring.service;

import com.example.ZwitterSpring.domain.Role;
import com.example.ZwitterSpring.domain.User;
import com.example.ZwitterSpring.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder encoder;

public boolean save(User user) {
    User userFormDB = userRepo.findByUsername(user.getUsername());
    if (userFormDB != null)  return false;
    user.setRoles(Collections.singleton(Role.USER));
    user.setPassword(encoder.encode(user.getPassword()));
    userRepo.save(user);
    return true;
}

public boolean delete(Long id) {
    if (userRepo.findById(id).isPresent()){
        userRepo.deleteById(id);
        return true;
    }
    return false;



}
public List<User> userList() {
    return userRepo.findAll();
}


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepo.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }
}
