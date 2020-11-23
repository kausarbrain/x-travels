package com.xtravels.service;
import com.xtravels.auth.LoginUserDetails;
import com.xtravels.models.Role;
import com.xtravels.models.User;
import com.xtravels.models.dto.UserRegistrationDto;
import com.xtravels.repository.UserRepository;
import com.xtravels.repository.UserRoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;


    public User save(UserRegistrationDto userRegistrationDto) {
        User user= new User();
        BeanUtils.copyProperties(userRegistrationDto,user);
        Optional<Role> role= userRoleRepository.getByName("USER");
        if(role.isPresent()){
            user.setRoles(List.of(role.get()));
        }else{
            user.setRoles(Arrays.asList(new Role("USER")));
        }
        user.setActive(true);
        return userRepository.save(user);
    }

    public boolean registerUser(UserRegistrationDto userData, BindingResult bindingResult, PasswordEncoder encoder){
        if(!userData.getPassword().equals(userData.getConfirmPassword()))
            bindingResult.rejectValue("confirmPassword","error.user","Confirm Password is not Matching");
        if(!bindingResult.hasErrors()){
            Optional<User> existUser=getUserByEmail(userData.getEmail());
            if(existUser.isPresent())
                bindingResult.rejectValue("email","error.user","Email is already registerd");
        }
        if(bindingResult.hasErrors()){
            return  false;
        }
        userData.setPassword(encoder.encode(userData.getPassword()));
        return Optional.of(save(userData)).isPresent();
    }


    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user= userRepository.findByEmail(s);
        user.orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return user.map(LoginUserDetails::new).get();
    }

    public Optional<User> getUserById(Long id){
        return  userRepository.getUserById(id);
    }

    public User getLoginUser(Authentication authentication){
        var userDetails=(LoginUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    public User saveAdminUser(User user) {
        Optional<Role> role= userRoleRepository.getByName("ADMIN");
        if(role.isPresent()){
            user.setRoles(List.of(role.get()));
        }else{
            user.setRoles(Arrays.asList(new Role("ADMIN")));
        }
        user.setActive(true);
        return userRepository.save(user);
    }

    


}
