/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.repository;

import com.lean.news.entity.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lean
 */
@Repository
public interface WriterRepository extends JpaRepository<Writer, String> {
    
    @Query("SELECT wr FROM Writer wr WHERE wr.email = :email")
    public Writer findWriterByEmail(@Param("email") String email);

}
