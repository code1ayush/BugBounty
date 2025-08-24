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

    private String companyName;

    private String description;

    private String scope;

    private String rewardRange;

    private String maxReward;

    private String status;

    private String programType;

    private List<String> tags;

    private String createdBy;

    private String createdAt;

    private String updatedAt;

    private String expiryDate;

    private String reportCount;

    private String resolvedCount;

    private String contactEmail;

    private String policyUrl;

    private String disclosurePolicy;

    private String image;

    @DBRef
    private List<BugReport>submittedReport = new ArrayList<>();

}
