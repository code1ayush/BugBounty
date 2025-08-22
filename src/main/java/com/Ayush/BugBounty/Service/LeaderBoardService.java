package com.Ayush.BugBounty.Service;

import com.Ayush.BugBounty.Entity.BugReport;
import com.Ayush.BugBounty.Entity.Reward;
import com.Ayush.BugBounty.Entity.User;
import com.Ayush.BugBounty.Repository.UserRepository;
import com.Ayush.BugBounty.dto.LeaderBoardDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class LeaderBoardService {

    private final UserRepository userRepository;


    public LeaderBoardService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public ResponseEntity<?> getLeaderBoard(String userName) {
        try {
            List<User> userList = userRepository.findAll();

            // userName -> score (size * 5)
            Map<String, Integer> leaderBoard = userList.stream()
                    .collect(Collectors.toMap(
                            User::getUserName,
                            user -> Optional.ofNullable(user.getRewardEntries())
                                    .map(List::size)
                                    .orElse(0) * 5
                    ));

            // Sort and assign rank
            List<LeaderBoardDTO> rankedList = new ArrayList<>();
            AtomicInteger rankCounter = new AtomicInteger(1);

            leaderBoard.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEachOrdered(entry -> {
                        rankedList.add(new LeaderBoardDTO(
                                rankCounter.getAndIncrement(),
                                entry.getKey(),
                                entry.getValue()
                        ));
                    });

            // Now bring the requested user to the top
            List<LeaderBoardDTO> finalList = new ArrayList<>();

            // Find the user entry
            rankedList.stream()
                    .filter(dto -> dto.getUserName().equals(userName))
                    .findFirst()
                    .ifPresent(finalList::add);  // add at top

            // Add all other users except the selected one
            rankedList.stream()
                    .filter(dto -> !dto.getUserName().equals(userName))
                    .forEach(finalList::add);

            return ResponseEntity.ok(finalList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }}
