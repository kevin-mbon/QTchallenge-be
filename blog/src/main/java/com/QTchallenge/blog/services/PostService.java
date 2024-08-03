package com.QTchallenge.blog.services;

import com.QTchallenge.blog.dto.PostDTO;
import com.QTchallenge.blog.dto.UserDTO;
import com.QTchallenge.blog.model.Post;
import com.QTchallenge.blog.model.Users;
import com.QTchallenge.blog.repositories.PostRepository;
import com.QTchallenge.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<PostDTO> findAll() {
        return postRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PostDTO> findById(Long id) {
        return postRepository.findById(id)
                .map(this::convertToDTO);
    }

    public PostDTO save(Post post, Long userId) throws IOException {
        Optional<Users> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            post.setAuthor(user);
            post.setTitle(post.getTitle());
            post.setContent(post.getContent());
            post.setImageUrl(post.getImageUrl());
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
            Post savedPost = postRepository.save(post);
            return convertToDTO(savedPost);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public PostDTO update(Long postId, Post updatedPost, Long userId) throws IOException {
        Optional<Post> existingPostOpt = postRepository.findById(postId);
        if (existingPostOpt.isPresent()) {
            Post existingPost = existingPostOpt.get();
            Optional<Users> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                existingPost.setTitle(updatedPost.getTitle());
                existingPost.setContent(updatedPost.getContent());
                existingPost.setImageUrl(updatedPost.getImageUrl());
                existingPost.setUpdatedAt(LocalDateTime.now());
                existingPost.setAuthor(user);
                Post savedPost = postRepository.save(existingPost);
                return convertToDTO(savedPost);
            } else {
                throw new RuntimeException("User not found");
            }
        } else {
            throw new RuntimeException("Post not found");
        }
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        postDTO.setImageUrl(post.getImageUrl());

        if (post.getAuthor() != null) {
            UserDTO authorDTO = new UserDTO();
            authorDTO.setId(post.getAuthor().getId());
            authorDTO.setUsername(post.getAuthor().getUsername());
            postDTO.setAuthor(authorDTO);
        }

        return postDTO;
    }
}



//package com.QTchallenge.blog.services;
//
//import com.QTchallenge.blog.dto.PostDTO;
//import com.QTchallenge.blog.dto.UserDTO;
//import com.QTchallenge.blog.model.Post;
//import com.QTchallenge.blog.model.Users;
//import com.QTchallenge.blog.repositories.PostRepository;
//import com.QTchallenge.blog.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class PostService {
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public List<PostDTO> findAll() {
//        return postRepository.findAll().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    public Optional<PostDTO> findById(Long id) {
//        return postRepository.findById(id)
//                .map(this::convertToDTO);
//    }
//
//    public PostDTO save(Post post, Long userId) throws IOException {
//        Optional<Users> userOpt = userRepository.findById(userId);
//        if (userOpt.isPresent()) {
//            Users user = userOpt.get();
//            post.setAuthor(user);
//            post.setTitle(post.getTitle());
//            post.setContent(post.getContent());
//            post.setImageUrl(post.getImageUrl());
//            post.setCreatedAt(LocalDateTime.now());
//            post.setUpdatedAt(LocalDateTime.now());
//            Post savedPost = postRepository.save(post);
//            return convertToDTO(savedPost);
//        } else {
//            throw new RuntimeException("User not found");
//        }
//    }
//
//    public void deleteById(Long id) {
//        postRepository.deleteById(id);
//    }
//
//    private PostDTO convertToDTO(Post post) {
//        PostDTO postDTO = new PostDTO();
//        postDTO.setId(post.getId());
//        postDTO.setTitle(post.getTitle());
//        postDTO.setContent(post.getContent());
//        postDTO.setCreatedAt(post.getCreatedAt());
//        postDTO.setUpdatedAt(post.getUpdatedAt());
//        postDTO.setImageUrl(post.getImageUrl());
//
//        if (post.getAuthor() != null) {
//            UserDTO authorDTO = new UserDTO();
//            authorDTO.setId(post.getAuthor().getId());
//            authorDTO.setUsername(post.getAuthor().getUsername());
//            postDTO.setAuthor(authorDTO);
//        }
//
//        return postDTO;
//    }
//}
//
