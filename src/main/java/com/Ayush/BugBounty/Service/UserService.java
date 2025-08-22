package com.Ayush.BugBounty.Service;

import com.Ayush.BugBounty.Entity.Program;
import com.Ayush.BugBounty.Entity.User;
import com.Ayush.BugBounty.Repository.ProgramRepository;
import com.Ayush.BugBounty.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ProgramRepository programRepository;


    public UserService(UserRepository userRepository,
                       ProgramRepository programRepository){
        this.userRepository = userRepository;
        this.programRepository = programRepository;
    }


    public void saveUser(User user){
        userRepository.save(user);
    }

    public User saveNewUser(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("Publisher"));
            user.setTotalPoints(100);
            userRepository.save(user);
            return user;
        }catch(Exception e){
            log.error("Error creating new user {}",user.getUserName(),e);
            return null;
        }
    }

    public ResponseEntity<?> updateUser(String userName,User updatedUser){
        try{
            User userinDb = userRepository.findByUserName(userName);
            userinDb.setUserName(updatedUser.getUserName());
            userinDb.setPassword(updatedUser.getPassword());
            this.saveNewUser(userinDb);
            List<Program> programList = userinDb.getProgramEntries();
            for(Program program:programList){
                program.setCreatedBy(userinDb.getUserName());
                programRepository.save(program);
            }
            return new ResponseEntity<>(userinDb,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    public void deleteUser(String userName){
        User user = this.findByUserName(userName);
        List<Program> programEntries = user.getProgramEntries();
        for(Program programEntry :  programEntries){
            programRepository.deleteById(programEntry.getId());
        }
        userRepository.deleteByUserName(userName);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }



}
