package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a new skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        return ResponseEntity.ok(this.skillService.handleCreateSkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update a new skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        return ResponseEntity.ok(this.skillService.handleUpdateSkill(skill));
    }

    @GetMapping("/skills")
    @ApiMessage("Get all skills")
    public ResponseEntity<ResultPaginationDTO> updateSkill(
            @Filter Specification<Skill> spec,
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.skillService.fetchAllSkill(spec, pageable));
    }
}
