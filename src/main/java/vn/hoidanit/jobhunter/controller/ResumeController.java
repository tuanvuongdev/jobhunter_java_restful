package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a new resume")
    public ResponseEntity<ResCreateResumeDTO> createNewResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.handleCreateResume(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateNewResume(@RequestBody Resume resume) throws IdInvalidException {
        return ResponseEntity.ok(this.resumeService.handleUpdateResume(resume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Deleted resume")
    public ResponseEntity<Void> delete(@PathVariable long id) throws IdInvalidException {
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch a resume by id")
    public ResponseEntity<ResResumeDTO> fetchResumeById(@PathVariable long id) throws IdInvalidException {
        return ResponseEntity.ok(this.resumeService.handleFetchResumeById(id));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch a resume pageable")
    public ResponseEntity<ResultPaginationDTO> fetchResumePageable(
            @Filter Specification<Resume> spec,
            Pageable pageable
            ) throws IdInvalidException {
        return ResponseEntity.ok(this.resumeService.handleFetchResumePageable(spec, pageable));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {
        return ResponseEntity.ok(this.resumeService.fetchResumeByUser(pageable));
    }
}
