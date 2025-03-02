package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public Job handleCreateJob(Job job) {
        if(job.getSkills() != null) {
            List<Skill> skills = this.skillRepository.findAllById(job.getSkills().stream().map(Skill::getId).toList());
            job.setSkills(skills);
        }
        if(job.getCompany() != null) {
            Optional<Company> cOptional = this.companyRepository.findById(job.getCompany().getId());
            cOptional.ifPresent(job::setCompany);
        }
        return this.jobRepository.save(job);
    }


    public Job handleUpdateJob(Job job) throws IdInvalidException {
        Optional<Job> currentJob = this.jobRepository.findById(job.getId());
        if(currentJob.isEmpty()) {
            throw new IdInvalidException("Job not found");
        }
        Job jobToUpdate = currentJob.get();
        jobToUpdate.setName(job.getName());
        jobToUpdate.setDescription(job.getDescription());
        jobToUpdate.setLocation(job.getLocation());
        jobToUpdate.setActive(job.isActive());
        jobToUpdate.setSalary(job.getSalary());
        jobToUpdate.setQuantity(job.getQuantity());
        jobToUpdate.setLevel(job.getLevel());
        jobToUpdate.setStartDate(job.getStartDate());
        jobToUpdate.setEndDate(job.getEndDate());

        if(job.getSkills() != null) {
            List<Skill> skills = this.skillRepository.findAllById(job.getSkills().stream().map(Skill::getId).toList());
            jobToUpdate.setSkills(skills);
        }
        if(job.getCompany() != null) {
            Optional<Company> cOptional = this.companyRepository.findById(job.getCompany().getId());
            cOptional.ifPresent(jobToUpdate::setCompany);
        }
        return this.jobRepository.save(jobToUpdate);
    }

    public ResCreateJobDTO convertToResCreateJobDTO(Job job) {
        ResCreateJobDTO res = new ResCreateJobDTO();
        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setDescription(job.getDescription());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.isActive());
        res.setCreatedAt(job.getCreatedAt());
        res.setCreatedBy(job.getCreatedBy());
        res.setSkills(job.getSkills().stream().map(Skill::getName).toList());

        return res;
    }

    public ResUpdateJobDTO convertToResUpdateJobDTO(Job job) {
        ResUpdateJobDTO res = new ResUpdateJobDTO();
        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setDescription(job.getDescription());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.isActive());
        res.setUpdatedAt(job.getUpdatedAt());
        res.setUpdatedBy(job.getUpdatedBy());
        res.setSkills(job.getSkills().stream().map(Skill::getName).toList());

        return res;
    }

    public void handleDeleteJob(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllJob(Specification<Job> spec, Pageable pageable) {
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        Page<Job> jobs = this.jobRepository.findAll(spec, pageable);

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(jobs.getTotalElements());
        mt.setPages(jobs.getTotalPages());

        rs.setMeta(mt);
        rs.setResult(jobs.getContent());

        return rs;
    }

    public Job fetchJobById(long id) throws IdInvalidException {
        Optional<Job> jOptional = this.jobRepository.findById(id);
        if(jOptional.isEmpty()) {
            throw new IdInvalidException("Job does not exist in system");
        }
        return jOptional.get();
    }
}
