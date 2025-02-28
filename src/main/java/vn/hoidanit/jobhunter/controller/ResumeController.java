package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
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
        return ResponseEntity.ok(this.resumeService.handleCreateResume(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateNewResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        return ResponseEntity.ok(this.resumeService.handleUpdateResume(resume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Deleted resume")
    public ResponseEntity<Void> delete(@PathVariable long id) throws IdInvalidException {
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.ok(null);
    }
}
