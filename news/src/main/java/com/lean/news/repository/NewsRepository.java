/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.repository;

import com.lean.news.entity.News;
import com.lean.news.enums.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lean
 */
@Repository
public interface NewsRepository extends JpaRepository<News, String> {

    @Query("SELECT ne FROM News ne WHERE ne.title LIKE %:palabra%")
    public News findTitleByWord(@Param("palabra") String palabra);

    @Query("SELECT ne FROM News ne ORDER BY ne.dateLog DESC")
    public List<News> listOrderedNews();
    
    @Query("SELECT  ne FROM News ne WHERE ne.category = :category ORDER BY ne.dateLog DESC")
    public List<News>listNewsByCategory(@Param("category") Category categoryEnum);
  }
