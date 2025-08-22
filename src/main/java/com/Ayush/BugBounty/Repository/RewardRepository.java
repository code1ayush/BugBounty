package com.Ayush.BugBounty.Repository;

import com.Ayush.BugBounty.Entity.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RewardRepository extends MongoRepository<Reward,String> {
}
