package com.Ayush.BugBounty.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "programs")
public class Program {

    @Id
    private String id;

    private String title;

    private String description;

    private String scope;

    private String rewardRange;

    private String image;

    private String createdBy;

    private String createdAt;

    @DBRef
    private List<BugReport>submittedReport = new ArrayList<>();

}
