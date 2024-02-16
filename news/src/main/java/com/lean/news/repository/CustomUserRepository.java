/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lean.news.repository;

import com.lean.news.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Lean
 */
public interface CustomUserRepository extends JpaRepository<User, String> {

    @Query("SELECT cu FROM CustomUser cu WHERE cu.email = :email")
    public User findUserByEmail(@Param("email") String email);

}
