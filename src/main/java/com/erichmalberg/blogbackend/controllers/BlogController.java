package com.erichmalberg.blogbackend.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erichmalberg.blogbackend.models.Blog;
import com.erichmalberg.blogbackend.repository.BlogRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BlogController {

    @Autowired
    BlogRepository blogRepository;

    @GetMapping("/blogs")
    public ResponseEntity<List<Blog>> getAllBlogs(@RequestParam(required = false) String title, @RequestParam(required = false) String author) {
        try {
            List<Blog> blogs = new ArrayList<>();

            if (title == null && author == null) {
                blogRepository.findAll().forEach(blogs::add);
            } else if (title != null) {
                blogRepository.findByTitleContaining(title).forEach(blogs::add);
            } else if (author != null) {
                blogRepository.findByAuthorContaining(author).forEach(blogs::add);
            }

            if (blogs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(blogs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/blogs/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable("id") long id) {
        Optional<Blog> blogData = blogRepository.findById(id);

        if (blogData.isPresent()) {
            return new ResponseEntity<>(blogData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/blogs")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
        try {
            Blog _blog = blogRepository.save(new Blog(blog.getTitle(), blog.getBody(), blog.getPublishedDate(), blog.getAuthor(), false));
            return new ResponseEntity<>(_blog, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/blogs/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Blog> updateBlog(@PathVariable("id") long id, @RequestBody Blog blog) {
        Optional<Blog> blogData = blogRepository.findById(id);

        if (blogData.isPresent()) {
            Blog _blog = blogData.get();
            _blog.setTitle(blog.getTitle());
            _blog.setBody(blog.getBody());
            _blog.setPublished(blog.isPublished());
            _blog.setPublishedDate(blog.getPublishedDate());
            _blog.setAuthor(blog.getAuthor());
            return new ResponseEntity<>(blogRepository.save(_blog), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/blogs/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteBlog(@PathVariable("id") long id) {
        try {
            blogRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/blogs/published")
    public ResponseEntity<List<Blog>> findPublishedBlogs() {
        try {
            List<Blog> blogs = blogRepository.findByPublished(true);

            if (blogs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(blogs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
