package com.Ayush.BugBounty.Entity;

import com.mongodb.lang.NonNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "users")
@Data
@Builder
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull
    private String userName;

    @NonNull
    private String password;

    private String email;

    @DBRef
    private List<Program> programEntries = new ArrayList<>();

    private List<String> roles;

    private Integer totalPoints=100;
    @DBRef
    private List<BugReport> reportEntries = new ArrayList<>();

    @DBRef
    private List<Reward> rewardEntries =  new ArrayList<>();


}
