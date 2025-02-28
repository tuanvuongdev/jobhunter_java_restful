package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private final UserRepository userRepository;

    private final JobRepository jobRepository;


    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public ResCreateResumeDTO handleCreateResume(Resume resume) throws IdInvalidException {
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty()) {
            throw new IdInvalidException("User not exist in system");
        }

        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty()) {
            throw new IdInvalidException("Job not exist in system");
        }

        resume.setUser(userOptional.get());
        resume.setJob(jobOptional.get());
        Resume resumeSaved = this.resumeRepository.save(resume);

        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO();
        resCreateResumeDTO.setId(resumeSaved.getId());
        resCreateResumeDTO.setCreatedAt(resumeSaved.getCreatedAt());
        resCreateResumeDTO.setCreatedBy(resumeSaved.getCreatedBy());

        return resCreateResumeDTO;
    }

    public ResUpdateResumeDTO handleUpdateResume(Resume resume) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(resume.getId());
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume not exist in system");
        }

        Resume currentResume = resumeOptional.get();
        currentResume.setStatus(resume.getStatus());
        Resume resumeSaved = this.resumeRepository.save(currentResume);

        ResUpdateResumeDTO resCreateResumeDTO = new ResUpdateResumeDTO();
        resCreateResumeDTO.setId(resumeSaved.getId());
        resCreateResumeDTO.setUpdatedAt(resumeSaved.getCreatedAt());
        resCreateResumeDTO.setUpdatedBy(resumeSaved.getCreatedBy());

        return resCreateResumeDTO;
    }

    public Void handleDeleteResume(long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume not exist in system");
        }
        this.resumeRepository.deleteById(id);
        return null;
    }
}
