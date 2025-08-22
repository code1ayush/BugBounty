package com.Ayush.BugBounty.dto;

import lombok.Data;

@Data
public class LeaderBoardDTO {
    private int rank;
    private String userName;
    private int rewardCount;

    public LeaderBoardDTO(int rank, String userName, int rewardCount) {
        this.rank = rank;
        this.userName = userName;
        this.rewardCount = rewardCount;
    }
}
