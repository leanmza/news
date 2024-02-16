/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.repository;

import com.lean.news.entity.News;

import com.lean.news.entity.Writer;
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

    @Query("SELECT ne FROM News ne WHERE ne.title LIKE %:word%")
    public List<News> findTitleByWord(@Param("word") String word);

    @Query("SELECT ne FROM News ne ORDER BY ne.dateLog DESC")
    public List<News> listOrderedNews();

    @Query("SELECT  ne FROM News ne WHERE ne.category = :category ORDER BY ne.dateLog DESC")
    public List<News> listNewsByCategory(@Param("category") Category categoryEnum);

    @Query("SELECT ne FROM News ne WHERE ne.writer = :writer")
    public List<News> listNewsByWriter(@Param("writer") Writer writer);

//      QUERYS PARA ORDENAR TABLA DE ADMINISTRAR NEWS
    @Query("SELECT ne FROM News ne ORDER BY ne.title DESC")
    public List<News> orderByTitleDesc(); //ordena por título Z-A

    @Query("SELECT ne FROM News ne ORDER BY ne.title ASC")
    public List<News> orderByTitleAsc(); //ordena por título A-Z

    @Query("SELECT ne FROM News ne ORDER BY ne.writer DESC")
    public List<News> orderByWriterDesc(); //ordena por writer Z-A

    @Query("SELECT ne FROM News ne ORDER BY ne.writer ASC")
    public List<News> orderByWriterAsc(); //ordena por writer A-Z

    @Query("SELECT ne FROM News ne ORDER BY ne.dateLog ASC")
    public List<News> listOrderedNewsAsc(); 

}
