package com.Ayush.BugBounty.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDTO {

    private String id;
    private String title;
    private String image;
    private String description;
    private String scope;
    private String rewardRange;
    private String createdBy;
    private String createdAt;


}

