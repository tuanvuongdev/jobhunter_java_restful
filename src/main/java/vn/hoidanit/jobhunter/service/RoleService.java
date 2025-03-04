package vn.hoidanit.jobhunter.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public Role handleCreateRole(Role role) {

    }
}
