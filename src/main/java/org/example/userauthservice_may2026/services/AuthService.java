package org.example.userauthservice_may2026.services;

import com.mysql.cj.exceptions.PasswordExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_may2026.exceptions.PasswordMismatchException;
import org.example.userauthservice_may2026.exceptions.UserAlreadyException;
import org.example.userauthservice_may2026.exceptions.UserNotSignedUpException;
import org.example.userauthservice_may2026.models.Role;
import org.example.userauthservice_may2026.models.State;
import org.example.userauthservice_may2026.models.User;
import org.example.userauthservice_may2026.models.UserSession;
import org.example.userauthservice_may2026.repos.RoleRepo;
import org.example.userauthservice_may2026.repos.UserRepo;
import org.example.userauthservice_may2026.repos.UserSessionrepol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.net.UnknownServiceException;
import java.util.*;

@Service
public class AuthService implements IAuthService{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private UserSessionrepol userSessionrepol;


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
    public Pair<User,String> login(String email, String password) {

        Optional<User> optUser=userRepo.findByEmail(email);
        if(optUser.isEmpty() ||optUser.get().getState().equals(State.DELETED)){
            throw new UserNotSignedUpException("user did not signup please signup first!");
        }
        User user=optUser.get();
        if(! bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new PasswordMismatchException("password mismatch exception");
        }

        //TOKEN GENERATION ON WED

        HashMap<String, Object> claims=new HashMap<>();
        claims.put("user_id",user.getId());
        List<String> permissions = new ArrayList<>();
        for(Role role: user.getRoles()){
            permissions.add(role.getValue());

        }
        claims.put("access",permissions);
        Long currentTime=System.currentTimeMillis();
        claims.put("iat",currentTime);
        claims.put("exp",currentTime+100000);

        MacAlgorithm alogrithm= Jwts.SIG.HS256;

        SecretKey secretKey=alogrithm.key().build();

        String Token=Jwts.builder().claims(claims).signWith(secretKey).compact();

        UserSession userSession= new UserSession();
        userSession.setToken(Token);
        userSession.setUser(user);
        userSession.setState(State.ACTIVE);
        userSessionrepol.save(userSession);
        return new Pair<>(user,Token);

    }
    public Boolean validateToken(String token){
        //generated Token by me
        Optional<UserSession> oUserSession=userSessionrepol.findByToken(token);
        if(oUserSession.isEmpty()){
            return false;
        }
        UserSession userSession=oUserSession.get();
        User user=userSession.getUser();
        JwtParser jwtParser= Jwts.parser().verifyWith(secretKey).build();
        Claims claims=jwtParser.parseSignedClaims(token).getPayload();
        Long expTime=(Long)claims.get("exp");
        Long curtime=System.currentTimeMillis();
        if(curtime>expTime){
            System.out.println("Token is expired");
//            userSession.setState(State.DELETED);
//            userSessionrepol.save(userSession);
            userSessionrepol.deleteById(userSession.getId());
        return false;
        }
        //expired Token or not
        return true;
    }
}
