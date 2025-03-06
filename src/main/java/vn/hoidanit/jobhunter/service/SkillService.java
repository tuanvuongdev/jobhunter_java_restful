package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    private final SkillRepository skillRepository;


    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(Skill skill) throws IdInvalidException {
        Optional<Skill> skillOptional = this.skillRepository.findByName(skill.getName());
        if (skillOptional.isPresent()) {
            throw new IdInvalidException("Skill with name = " + skillOptional.get().getName() + " is existed");
        }

        return this.skillRepository.save(skill);
    }

    public Skill fetchSkillById(long id) throws IdInvalidException {
        Optional<Skill> currentSkill = this.skillRepository.findById(id);
        if (currentSkill.isEmpty()) {
            throw new IdInvalidException("Does not exist skill with id = " + id);
        }
        return currentSkill.get();
    }

    public Skill handleUpdateSkill(Skill skill) throws IdInvalidException {
        Skill currentSkill = this.fetchSkillById(skill.getId());

        Optional<Skill> skillOptional = this.skillRepository.findByName(skill.getName());
        if (skillOptional.isPresent()) {
            throw new IdInvalidException("Skill with name = " + skillOptional.get().getName() + " is existed");
        }

        currentSkill.setName(skill.getName());

        return this.skillRepository.save(currentSkill);
    }

    public ResultPaginationDTO fetchAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(pageSkill.getTotalElements());
        mt.setPages(pageSkill.getTotalPages());

        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());

        return rs;
    }

    public void handleDeleteById(Long id) throws IdInvalidException {
        Skill currentSkill = this.fetchSkillById(id);

        //delete job (inside job_skill table)
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        //delete subscriber (inside subscriber_skill table)
        currentSkill.getSubscribers().forEach(subscriber -> subscriber.getSkills().remove(currentSkill));

        //delete skill
        this.skillRepository.delete(currentSkill);
    }
}
