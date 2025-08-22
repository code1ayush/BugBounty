package com.Ayush.BugBounty.Controller;

import com.Ayush.BugBounty.Service.LeaderBoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("leaderBoard")
@CrossOrigin(origins = "https://bug-bounty-ui-rouge.vercel.app")
public class LeaderBoardController {

    private final LeaderBoardService leaderBoardService;

    public LeaderBoardController(LeaderBoardService leaderBoardService){
        this.leaderBoardService = leaderBoardService;
    }

    @GetMapping
    public ResponseEntity<?> getLeaderBoard(){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            return new ResponseEntity<>(leaderBoardService.getLeaderBoard(userName).getBody(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
