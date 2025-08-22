package com.Ayush.BugBounty.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Rewards")
@Data
public class Reward {
    @Id
    private String id;

    private String programId;

    private String title;

    private Integer points;
}
