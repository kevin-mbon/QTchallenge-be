package com.QTchallenge.blog.services;

import com.QTchallenge.blog.dto.UserDTO;
import com.QTchallenge.blog.model.Users;
import com.QTchallenge.blog.repositories.UserRepository;
import com.QTchallenge.blog.utily.HashingUtil;
import com.QTchallenge.blog.utily.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public UserDTO save(UserDTO userDTO) {
        Users user = convertToEntity(userDTO);
        user.setPassword(HashingUtil.hashPassword(user.getPassword()));
        Users savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public String login(String username, String password) {
        Optional<Users> optionalUser = findByUsername(username);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            if (HashingUtil.verifyPassword(password, user.getPassword())) {
                return jwtTokenUtil.generateToken(username, user.getId());
            }
        }
        return null;
    }

    public boolean logout(String token) {
        return true; // Simple logout implementation
    }

    private UserDTO convertToDTO(Users user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

    private Users convertToEntity(UserDTO userDTO) {
        Users user = new Users();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}
