package com.xtravels;

import com.xtravels.models.Location;
import com.xtravels.models.Role;
import com.xtravels.models.User;
import com.xtravels.service.LocationService;
import com.xtravels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class XTravelsApplication {

	@Autowired
	UserService userService;
    @Autowired
	LocationService locationService;


    public static void main(String[] args) {
        SpringApplication.run(XTravelsApplication.class, args);
    }



    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            try {
                this.locationService.save(new Location("Dhaka"));
                this.locationService.save(new Location("Rangpur"));
                Collection<Role> roles= List.of(new Role("ADMIN"));
                this.userService.saveAdminUser(new User("Admin","admin@yopmail.com",new BCryptPasswordEncoder().encode("123456"),true,roles ));

            }catch (Exception e){
                
            }
        };
    }

}
