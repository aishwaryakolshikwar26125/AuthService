package org.example.userauthservice_may2026.services;

import com.mysql.cj.exceptions.PasswordExpiredException;
import lombok.Setter;
import org.example.userauthservice_may2026.exceptions.PasswordMismatchException;
import org.example.userauthservice_may2026.exceptions.UserAlreadyException;
import org.example.userauthservice_may2026.exceptions.UserNotSignedUpException;
import org.example.userauthservice_may2026.models.Role;
import org.example.userauthservice_may2026.models.State;
import org.example.userauthservice_may2026.models.User;
import org.example.userauthservice_may2026.repos.RoleRepo;
import org.example.userauthservice_may2026.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements IAuthService{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public User signUp(String name, String email, String password) {
         Optional<User> optUser=userRepo.findByEmail(email);
         if(optUser.isPresent()){
            throw new UserAlreadyException("Please use different emailId");
         }
         User user=new User();
         user.setEmail(email);
         user.setPassword(bCryptPasswordEncoder.encode(password));//HASH THIS PASSWORD
         user.setName(name);
        Role role=null;
         Optional<Role> optrole=roleRepo.findByValue("NON_ADMIN");
         if(optrole.isEmpty()){
              role=new Role();
              role.setValue("NON_ADMIN");
              roleRepo.save(role);

         }
         else{
             role=optrole.get();

         }
         List<Role> roles=new ArrayList<>();
         roles.add(role);
         user.setRoles(roles);

        return userRepo.save(user);
    }

    @Override
    public User login(String email, String password) {
        Optional<User> optUser=userRepo.findByEmail(email);
        if(optUser.isEmpty() ||optUser.get().getState().equals(State.DELETED)){
            throw new UserNotSignedUpException("user did not signup please signup first!");
        }
        User user=optUser.get();
        if(! bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new PasswordMismatchException("password mismatch exception");
        }
        return user;
        //TOKEN GENERATION ON WED
    }
}
