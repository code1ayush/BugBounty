package com.Ayush.BugBounty.Service;

import com.Ayush.BugBounty.Entity.BugReport;
import com.Ayush.BugBounty.Entity.Program;
import com.Ayush.BugBounty.Entity.User;
import com.Ayush.BugBounty.Repository.BugReportRepository;
import com.Ayush.BugBounty.Repository.ProgramRepository;
import com.Ayush.BugBounty.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BugReportService {

    private final ProgramRepository programRepository;
    private final UserRepository userRepository;
    private final BugReportRepository bugReportRepository;

    public BugReportService(ProgramRepository programRepository,
                          UserRepository userRepository,
                            BugReportRepository bugReportRepository){
        this.programRepository = programRepository;
        this.userRepository = userRepository;
        this.bugReportRepository = bugReportRepository;
    }

    public void postReport(BugReport bugReport,String userName){
        try{
            User user = userRepository.findByUserName(userName);
            BugReport saved = bugReportRepository.save(bugReport);
            if(user.getReportEntries()!=null){
                user.getReportEntries().removeIf(t->t.getId().equals(saved.getId()));
            }

            if(user.getReportEntries()==null){
                user.setReportEntries(new ArrayList<>());
            }
            user.getReportEntries().add(saved);
            userRepository.save(user);
            Program currentProgram = programRepository.findById(saved.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Program not found"));

            if (currentProgram.getSubmittedReport() == null) {
                currentProgram.setSubmittedReport(new ArrayList<>());
            }
            currentProgram.getSubmittedReport().add(saved);
            programRepository.save(currentProgram);
        }catch(Exception e){
            throw new RuntimeException("an error occured",e);
        }
    }

    public ResponseEntity<?> getReports(String programId,String userName){
        try{
            // to check if the given program id really belongs to the this user
            User user = userRepository.findByUserName(userName);
            List<Program> usersPrograms = user.getProgramEntries();
            boolean exists = usersPrograms.stream()
                    .anyMatch(p -> p.getId().equals(programId));
            if(exists){
            Program myProgram = programRepository.findById(programId)
                    .orElseThrow(() -> new RuntimeException("Program not found"));
            List<BugReport> submittedReport = myProgram.getSubmittedReport();
            return new ResponseEntity<>(submittedReport, HttpStatus.OK);}
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(Exception e){
            log.error("Reports not found for the program id {}",programId,e);
            return new ResponseEntity<>("Reports not found",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getMyReport(String userName){
        try{
            User user = userRepository.findByUserName(userName);
            List<BugReport>myReports = user.getReportEntries();
                return new ResponseEntity<>(myReports,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    public void updateReportById(BugReport updatedReport,String id ){
        try{
            BugReport reportInDb = bugReportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Report not found"));
            reportInDb.setTitle(updatedReport.getTitle());
            reportInDb.setDescription(updatedReport.getDescription());
            reportInDb.setSeverity(updatedReport.getSeverity());
            reportInDb.setStatus(updatedReport.getStatus());
            reportInDb.setCreatedAt(updatedReport.getCreatedAt());
            bugReportRepository.save(reportInDb);
        }catch (Exception e){
            throw new RuntimeException("could not update report",e);
        }
    }


}
