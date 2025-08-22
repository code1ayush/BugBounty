package com.Ayush.BugBounty.Repository;

import com.Ayush.BugBounty.Entity.Program;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProgramRepository extends MongoRepository<Program,String> {
}
