package com.Ayush.BugBounty.Controller;

import com.Ayush.BugBounty.Entity.Reward;
import com.Ayush.BugBounty.Service.RewardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rewards")
@CrossOrigin(origins = "https://bug-bounty-ui-rouge.vercel.app")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService){
        this.rewardService = rewardService;
    }

    @PostMapping("{userName}")
    public ResponseEntity<?> postReward(@RequestBody Reward reward, @PathVariable String userName){
        try{
            String currentUseName = SecurityContextHolder.getContext().getAuthentication().getName();
            rewardService.postReward(reward,userName,currentUseName);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Could not save the reward",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getReward(){
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            return rewardService.getMyReward(userName);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("totalPoints")
    public ResponseEntity<?> getTotalPoints(){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            return rewardService.getTotalRewardPoints(userName);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
