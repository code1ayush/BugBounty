package com.Ayush.BugBounty.Repository;

import com.Ayush.BugBounty.Entity.BugReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BugReportRepository extends MongoRepository<BugReport,String> {
}
