package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission handleSavePermission(Permission permission) throws IdInvalidException {
        boolean isDuplicated = this.permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(), permission.getModule());
        if (isDuplicated) {
            throw new IdInvalidException("Permission is existed");
        }

        return this.permissionRepository.save(permission);
    }

    public Permission handleUpdatePermission(Permission permission) throws IdInvalidException {
        Optional<Permission> pOptional = this.permissionRepository.findById(permission.getId());
        if (pOptional.isEmpty()) {
            throw new IdInvalidException("Permission is not exists in system");
        }

        boolean isDuplicated = this.permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(), permission.getModule());
        if (isDuplicated) {
            throw new IdInvalidException("Permission đã tồn tại");
        }

        Permission currentPermission = pOptional.get();
        currentPermission.setApiPath(permission.getApiPath());
        currentPermission.setMethod(permission.getMethod());
        currentPermission.setModule(permission.getModule());

        return this.permissionRepository.save(currentPermission);
    }

    public ResultPaginationDTO fetchPermissionPagination(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());

        mt.setTotal(pagePermission.getTotalElements());
        mt.setPages(pagePermission.getTotalPages());

        rs.setMeta(mt);
        rs.setResult(pagePermission.getContent());

        return rs;
    }
}
