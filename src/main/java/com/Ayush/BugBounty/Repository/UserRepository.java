package com.Ayush.BugBounty.Repository;
import com.Ayush.BugBounty.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
    User findByUserName(String userName);
    void deleteByUserName(String userName);
}
