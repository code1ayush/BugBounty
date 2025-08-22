package com.Ayush.BugBounty.Controller;

import com.Ayush.BugBounty.Entity.BugReport;
import com.Ayush.BugBounty.Entity.User;
import com.Ayush.BugBounty.Service.BugReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "https://bug-bounty-ui-rouge.vercel.app")
public class BugReportController {

    private final BugReportService bugReportService;

    public BugReportController(BugReportService bugReportService){
        this.bugReportService = bugReportService;
    }

    @PostMapping
    public ResponseEntity<?> postReport(@RequestBody BugReport bugReport){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            bugReportService.postReport(bugReport,userName);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("programId/{programId}")
    public ResponseEntity<?> getReport(@PathVariable String programId){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            ResponseEntity<?> bugreports = bugReportService.getReports(programId,userName);
            return new ResponseEntity<>(bugreports.getBody(),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/myreports")
    public ResponseEntity<?> getMyreports(){
        try{
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            return bugReportService.getMyReport(userName);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReportById(@RequestBody BugReport updatedReport, @PathVariable String id){
        try{
            bugReportService.updateReportById(updatedReport,id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
