package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a new permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.handleSavePermission(permission));
    }


    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        return ResponseEntity.ok(this.permissionService.handleUpdatePermission(permission));
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch permission by pagination")
    public ResponseEntity<ResultPaginationDTO> fetchPermissionPagination(
            @Filter Specification<Permission> spec,
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.permissionService.fetchPermissionPagination(spec, pageable));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission by id")
    public ResponseEntity<Void> deletePermissionById(@PathVariable long id) throws IdInvalidException {
        this.permissionService.handleDeleteById(id);
        return ResponseEntity.ok(null);
    }
}
