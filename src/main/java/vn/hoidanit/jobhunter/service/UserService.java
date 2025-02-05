package vn.hoidanit.jobhunter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> fetchAllUser() {
        return this.userRepository.findAll();
    }

    public User fetchUserById(long userId) {
        Optional<User> userFetched = this.userRepository.findById(userId);

        return userFetched.orElse(null);
    }

    public User handleSaveUser(User user) {
        return this.userRepository.save(user);
    }
    public User handleUpdateUser(User user) {
        User currentUser = this.fetchUserById(user.getId());
        if(currentUser != null) {
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public void handleDeleteUser(long userId) {
        this.userRepository.deleteById(userId);
    }
}
