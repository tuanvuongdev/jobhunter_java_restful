package vn.hoidanit.jobhunter.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResCreateJobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public Job handleCreateJob(Job job) {
        if(job.getSkills() != null) {
            List<Skill> skills = this.skillRepository.findAllById(job.getSkills().stream().map(Skill::getId).toList());
            job.setSkills(skills);
        }
        return this.jobRepository.save(job);
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
}
