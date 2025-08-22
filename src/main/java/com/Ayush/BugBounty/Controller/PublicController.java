package com.Ayush.BugBounty.Controller;

import com.Ayush.BugBounty.Entity.User;
import com.Ayush.BugBounty.Service.UserDetailServiceImp;
import com.Ayush.BugBounty.Service.UserService;
import com.Ayush.BugBounty.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "https://bug-bounty-ui-rouge.vercel.app")
public class PublicController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImp userDetailServiceImp;
    private final JwtUtils jwtUtils;

    public PublicController(UserService userService,
                            AuthenticationManager authenticationManager,
                            UserDetailServiceImp userDetailServiceImp,
                            JwtUtils jwtUtils){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailServiceImp = userDetailServiceImp;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user){
         userService.saveNewUser(user);
        String jwt= jwtUtils.generateJwtToken(user.getUserName());
        return new ResponseEntity<>(jwt,HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
            UserDetails userDetails = userDetailServiceImp.loadUserByUsername(user.getUserName());
            String jwt= jwtUtils.generateJwtToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Incorrect userName/password", HttpStatus.UNAUTHORIZED);
        }
    }
}
