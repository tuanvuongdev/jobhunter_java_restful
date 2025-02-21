package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Update a new job")
    public ResponseEntity<ResCreateJobDTO> createNewJob(@Valid @RequestBody Job job) {
        Job createdJob = this.jobService.handleCreateJob(job);
        return ResponseEntity.ok(
                this.jobService.convertToResCreateJobDTO(createdJob)
        );
    }

    @PutMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateNewJob(@Valid @RequestBody Job job) throws IdInvalidException {
        Job createdJob = this.jobService.handleUpdateJob(job);
        return ResponseEntity.ok(
                this.jobService.convertToResUpdateJobDTO(createdJob)
        );
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) {
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/jobs")
    @ApiMessage("Get all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(
            @Filter Specification<Job> spec,
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.jobService.fetchAllJob(spec, pageable));
    }
}
