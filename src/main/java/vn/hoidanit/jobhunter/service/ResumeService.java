package vn.hoidanit.jobhunter.service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private final UserRepository userRepository;

    private final JobRepository jobRepository;

    private final FilterParser filterParser;

    private final FilterSpecificationConverter filterSpecificationConverter;


    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, JobRepository jobRepository, FilterParser filterParser, FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.filterParser = filterParser;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    public boolean checkResumeExistByUserAndJob(Resume resume) {
        if(resume.getUser() == null) {
            return false;
        }
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty()) {
            return false;
        }
        if(resume.getJob() == null) {
            return false;
        }
        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        return jobOptional.isPresent();
    }

    public ResCreateResumeDTO handleCreateResume(Resume resume) throws IdInvalidException {
        if (!this.checkResumeExistByUserAndJob(resume)) {
            throw new IdInvalidException("User/Job not exist in system");
        }

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

    public void handleDeleteResume(long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume with id= " + id + " not exist in system");
        }
        this.resumeRepository.deleteById(id);
    }

    public ResResumeDTO handleFetchResumeById(long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume with id= " + id + " not exist in system");
        }
         Resume currentResume = resumeOptional.get();

        return convertToResResumeDTO(currentResume);
    }

    public ResultPaginationDTO handleFetchResumePageable(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setTotal(pageResume.getTotalElements());
        mt.setPages(pageResume.getTotalPages());

        List<ResResumeDTO> resResume = pageResume.getContent()
                .stream()
                .map(ResumeService::convertToResResumeDTO).toList();
        res.setMeta(mt);
        res.setResult(resResume);

        return res;
    }

    private static ResResumeDTO convertToResResumeDTO(Resume currentResume) {
        ResResumeDTO res = new ResResumeDTO();
        ResResumeDTO.UserResume userResume = new ResResumeDTO.UserResume();
        ResResumeDTO.JobResume jobResume = new ResResumeDTO.JobResume();

        userResume.setId(currentResume.getUser().getId());
        userResume.setName(currentResume.getUser().getName());

        jobResume.setId(currentResume.getJob().getId());
        jobResume.setName(currentResume.getJob().getName());

        res.setId(currentResume.getId());
        res.setEmail(currentResume.getEmail());
        res.setUrl(currentResume.getUrl());
        res.setStatus(currentResume.getStatus());
        res.setCreatedAt(currentResume.getCreatedAt());
        res.setCreatedBy(currentResume.getCreatedBy());
        res.setUpdatedAt(currentResume.getUpdatedAt());
        res.setUpdatedBy(currentResume.getUpdatedBy());
        res.setUser(userResume);
        res.setJob(jobResume);

        if(currentResume.getJob() != null) {
            res.setCompanyName(currentResume.getJob().getCompany().getName());
        }

        return res;
    }

    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setTotal(pageResume.getTotalElements());
        mt.setPages(pageResume.getTotalPages());

        List<ResResumeDTO> resResume = pageResume.getContent()
                .stream()
                .map(ResumeService::convertToResResumeDTO).toList();
        res.setMeta(mt);
        res.setResult(resResume);

        return res;
    }
}
