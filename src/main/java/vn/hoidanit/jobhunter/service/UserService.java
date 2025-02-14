package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.*;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResultPaginationDTO<List<ResUserDTO>> fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO<List<ResUserDTO>> rs = new ResultPaginationDTO<>();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPageSize(pageUser.getSize());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(user -> new ResUserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getAge(),
                        user.getGender(),
                        user.getAddress(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                )).toList();
        rs.setResult(listUser);
        return rs;
    }

    public User fetchUserById(long userId) throws Exception {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("User with id = " + userId + " not found");
        }
        return userOptional.get();
    }

    public ResCreateUserDTO handleCreateUser(User user) throws Exception {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        boolean isEmailExisted = this.userRepository.existsByEmail(user.getEmail());

        if (isEmailExisted) {
            throw new Exception("Email " + user.getEmail() + " đã tồn tại, vui lòng sử dụng email khác.");
        }

        User savedUser = this.userRepository.save(user);

        return this.convertToResCreateUserDTO(savedUser);
    }

    public User handleUpdateUser(User user) throws Exception {
        User currentUser = this.fetchUserById(user.getId());

        currentUser.setName(user.getName());
        currentUser.setGender(user.getGender());
        currentUser.setAge(user.getAge());
        currentUser.setAddress(user.getAddress());

        return this.userRepository.save(currentUser);
    }

    public void handleDeleteUser(long userId) {
        this.userRepository.deleteById(userId);
    }

    public User handleGetUserByUsername(String username) {
        Optional<User> userOptional = this.userRepository.findByEmail(username);
        return userOptional.orElse(null);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setCreatedAt(user.getCreatedAt());
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if(currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
}
