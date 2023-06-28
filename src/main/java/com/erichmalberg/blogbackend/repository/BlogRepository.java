package com.erichmalberg.blogbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erichmalberg.blogbackend.models.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long>{
    List<Blog> findByPublished(boolean published);

    List<Blog> findByTitleContaining(String title);

    List<Blog> findByAuthorContaining(String author);
}
