package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a new role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.handleCreateRole(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
        return ResponseEntity.ok(this.roleService.handleUpdateRole(role));
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch role by pagination")
    public ResponseEntity<ResultPaginationDTO> fetchRolePagination(
            @Filter Specification<Role> spec,
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.roleService.fetchRolePagination(spec, pageable));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role by id")
    public ResponseEntity<Void> deleteRoleById(@PathVariable long id) throws IdInvalidException {
        this.roleService.handleDeleteRoleById(id);
        return ResponseEntity.ok(null);
    }
}
