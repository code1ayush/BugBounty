package com.Ayush.BugBounty.Service;

import com.Ayush.BugBounty.Entity.Reward;
import com.Ayush.BugBounty.Entity.User;
import com.Ayush.BugBounty.Repository.ProgramRepository;
import com.Ayush.BugBounty.Repository.RewardRepository;
import com.Ayush.BugBounty.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RewardService {

    private final RewardRepository rewardRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public RewardService(RewardRepository rewardRepository,
                         UserRepository userRepository,
                         UserService userService){
        this.rewardRepository = rewardRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void postReward(Reward reward,String userName,String currentUserName){
        try{
            User user = userRepository.findByUserName(userName);
            User currentUser = userRepository.findByUserName(currentUserName);
            Reward saved = rewardRepository.save(reward);
            if(user.getRewardEntries()!=null){
                user.getRewardEntries().removeIf(t->t.getId().equals(saved.getId()));
            }
            if(currentUser.getRewardEntries()!=null){
                currentUser.getRewardEntries().removeIf(t->t.getId().equals(saved.getId()));
            }

            if(user.getRewardEntries()==null){
                user.setRewardEntries(new ArrayList<>());
            }
            if(currentUser.getRewardEntries()==null){
                currentUser.setRewardEntries(new ArrayList<>());
            }
            Integer money = saved.getPoints();
            Integer userTotalMoney = user.getTotalPoints()+money;
            Integer currentUserTotalMoney = currentUser.getTotalPoints()-money;
            user.setTotalPoints(userTotalMoney);
            currentUser.setTotalPoints(currentUserTotalMoney);
            user.getRewardEntries().add(saved);
            userService.saveUser(user);
            userService.saveUser(currentUser);
        }catch (Exception e){
            throw new RuntimeException("an error occured while saving the reward",e);
        }
    }
    public ResponseEntity<?> getMyReward(String userName){
        try{
            User user = userRepository.findByUserName(userName);
            List<Reward> rewardList = user.getRewardEntries();
            return new ResponseEntity<>(rewardList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getTotalRewardPoints(String userName){
        try{
            User user = userRepository.findByUserName(userName);
            return new ResponseEntity<>(user.getTotalPoints(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
