package com.Ayush.BugBounty.Controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.Ayush.BugBounty.Config.SpringSecurity;
import com.Ayush.BugBounty.Entity.Program;
import com.Ayush.BugBounty.Entity.User;
import com.Ayush.BugBounty.Repository.ProgramRepository;
import com.Ayush.BugBounty.Service.ProgramService;
import com.Ayush.BugBounty.Service.UserService;
import com.Ayush.BugBounty.dto.ProgramDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/programs")
@CrossOrigin(origins = "https://bug-bounty-ui-rouge.vercel.app")
public class ProgramController {

    private final ProgramService programService;
    private final UserService userService;
    private final ProgramRepository programRepository;

    public ProgramController (ProgramService programService,
                              UserService userService,
                              ProgramRepository programRepository){
        this.programService = programService;
        this.userService = userService;
        this.programRepository = programRepository;
    }

    @PostMapping
    public ResponseEntity<?> postProgram(@RequestBody Program program){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            program.setCreatedAt(String.valueOf(LocalDateTime.now()));
            program.setCreatedBy(userName);
            programService.postPrograms(program,userName);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Could not save the program",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/allPrograms")
    public ResponseEntity<?> getAllPrograms(){
        try{
            List<Program> programList = programService.getAllProgram();
            List<ProgramDTO>programDTOlist = programList.stream()
                    .map(p -> new ProgramDTO(
                            p.getId(),
                            p.getTitle(),
                            p.getImage(),
                            p.getDescription(),
                            p.getScope(),
                            p.getRewardRange(),
                            p.getCreatedBy(),
                            p.getCreatedAt()
                    ))
                    .toList();

                return new ResponseEntity<>(programDTOlist,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getPrograms(){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
              return programService.getProgram(userName);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?>getProgramsById(@PathVariable String id){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            ResponseEntity<?> program = programService.getProgramById(id,userName);
            return new ResponseEntity<>(program.getBody(),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?>updateProgramsById(@PathVariable String id, @RequestBody Program updatedPrgoram){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            programService.updateProgramsById(updatedPrgoram,userName,id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserName(userName);
        List<Program> collect = user.getProgramEntries().stream().filter(x -> x.getId().equals(id)).toList();
        if (!collect.isEmpty()) {
            Optional<Program>taskEntry = programRepository.findById(id);
            if(taskEntry.isPresent()){
                programService.deleteProgram(id,userName);
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);

    }

}
