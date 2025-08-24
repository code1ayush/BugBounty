package com.Ayush.BugBounty.Service;

import com.Ayush.BugBounty.Entity.Program;
import com.Ayush.BugBounty.Entity.User;
import com.Ayush.BugBounty.Repository.ProgramRepository;
import com.Ayush.BugBounty.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ProgramService(ProgramRepository programRepository,
                          UserRepository userRepository,
                          UserService userService){
        this.programRepository = programRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void postPrograms(Program program, String userName){
        try{
            User user = userRepository.findByUserName(userName);
            Program saved = programRepository.save(program);
            if(user.getProgramEntries()!=null){
                user.getProgramEntries().removeIf(t->t.getId().equals(saved.getId()));
            }
            if(user.getProgramEntries()==null){
                user.setProgramEntries(new ArrayList<>());
            }
            user.getProgramEntries().add(saved);
            userService.saveUser(user);
        }catch (Exception e){
            throw new RuntimeException("an error occured while saving the program",e);
        }
    }

    public List<Program>  getAllProgram(){
        return programRepository.findAll();
    }

    public ResponseEntity<?> getProgram(String userName){
        try{
            User user = userRepository.findByUserName(userName);
            List<Program> programList = user.getProgramEntries();
                return new ResponseEntity<>(programList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getProgramById(String id, String userName){
        User user = userRepository.findByUserName(userName);
        List<Program>collect = programRepository.findAll().stream().filter(t->t.getId().equals(id)).toList();
        if(!collect.isEmpty()){
            Optional<Program> programEntry = programRepository.findById(id);
            if(programEntry.isPresent()){
                return new ResponseEntity<>(programEntry,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    public void updateProgramsById(Program updatedProgram, String userName, String id){
        try{
            User user = userRepository.findByUserName(userName);
            List<Program>collect = user.getProgramEntries().stream().filter(t->t.getId().equals(id)).toList();
            if(!collect.isEmpty()){
                Optional<Program> programEntry = programRepository.findById(id);
                if(programEntry.isPresent()){
                    updatedProgram.setId(id);
                    user.getProgramEntries().removeIf(t->t.getId().equals(id));
                    programRepository.save(updatedProgram);
                }
            }
        }catch (Exception e){
            throw new RuntimeException("something went wrong during updaing programs",e);
        }
    }

    public void deleteProgram(String id,String userName){
        User user = userRepository.findByUserName(userName);
        user.getProgramEntries().removeIf(x->x.getId().equals(id));
        userService.saveUser(user);
        programRepository.deleteById(id);
    }
}
