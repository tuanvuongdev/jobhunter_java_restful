package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role handleCreateRole(Role role) throws IdInvalidException {
        Optional<Role> rOptional = this.roleRepository.findByName(role.getName());
        if (rOptional.isPresent()) {
            throw new IdInvalidException("Role name already existed!");
        }

        if (role.getPermissions() != null) {
            List<Long> listPermissionIds = role.getPermissions().stream().map(Permission::getId).toList();
            List<Permission> listPermisstion = this.permissionRepository.findAllById(listPermissionIds);
            role.setPermissions(listPermisstion);
        }


        return this.roleRepository.save(role);
    }

    public Role handleUpdateRole(Role role) throws IdInvalidException {
        Optional<Role> rOptionalById = this.roleRepository.findById(role.getId());
        if (rOptionalById.isEmpty()) {
            throw new IdInvalidException("Role with id = " + role.getId() + " does not exist in system");
        }

//        Optional<Role> rOptionalByName = this.roleRepository.findByIdNotAndName(role.getId(), role.getName());
//        if (rOptionalByName.isPresent()) {
//            throw new IdInvalidException("Role name already existed!");
//        }

        Role currentRole = rOptionalById.get();

        if (role.getPermissions() != null) {
            List<Long> listPermissionIds = role.getPermissions().stream().map(Permission::getId).toList();
            List<Permission> listPermisstion = this.permissionRepository.findAllById(listPermissionIds);
            currentRole.setPermissions(listPermisstion);
        }

//        currentRole.setName(role.getName());
        currentRole.setDescription(role.getDescription());
        currentRole.setActive(role.isActive());

        return this.roleRepository.save(currentRole);
    }

    public ResultPaginationDTO fetchRolePagination(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageRole = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageRole.getTotalPages());
        mt.setTotal(pageRole.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageRole.getContent());

        return rs;
    }

    public void handleDeleteRoleById(long id) throws IdInvalidException {
        Optional<Role> rOptionalById = this.roleRepository.findById(id);
        if (rOptionalById.isEmpty()) {
            throw new IdInvalidException("Role with id = " + id + " does not exist in system");
        }

        this.roleRepository.deleteById(id);
    }

    public Role fetchById(long id) {
        Optional<Role> rOptional = this.roleRepository.findById(id);
        return rOptional.orElse(null);
    }
}
