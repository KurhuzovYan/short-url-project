package goit.com.shorturlproject.v1.user.service.impl;

import goit.com.shorturlproject.v1.registration.exception.UserAlreadyExistException;
import goit.com.shorturlproject.v1.user.dto.Role;
import goit.com.shorturlproject.v1.user.dto.User;
import goit.com.shorturlproject.v1.user.repository.UserRepository;
import goit.com.shorturlproject.v1.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User registerNewUserAccount(User user) {
        if (emailExists(user.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }
    @Override
    public Optional<User> findByUserName(String userName){return userRepository.findByUserName(userName);}
    @Override
    public void saveRegisteredUser( User user) {
        userRepository.save(user);
    }
    private boolean emailExists( String email) {
        return userRepository.findByEmail(email) != null;
    }
    @Override
    public Optional<User> getUserByID( Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findUserByEmail( String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteUser( User user) {
        userRepository.delete(user);
    }
}
