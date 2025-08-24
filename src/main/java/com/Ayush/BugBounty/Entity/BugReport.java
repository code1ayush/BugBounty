package com.Ayush.BugBounty.Entity;

import com.Ayush.BugBounty.emum.Severity;
import com.Ayush.BugBounty.emum.Status;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bug-Reports")
@Data
public class BugReport {

    @Id
    private String id;

    @NonNull
    private String programId;

    private String reportedBy;

    private String title;

    private String description;

    private Severity severity;

    private Status status;

    private String createdAt;

    private String updatedAt;

    private String assignee;

    private String vulnerabilityType;

    private String stepsToReproduce;

    private String impact;

    private String references;

    private String reporterScore;

    private String isDuplicate;

}
