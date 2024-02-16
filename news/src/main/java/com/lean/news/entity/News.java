/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lean.news.entity;

import com.lean.news.enums.Category;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;


/**
 *
 * @author Lean
 */
@Entity
@Data
public class News {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String title;
    
    @Column(columnDefinition = "LONGTEXT")
    private String body;
  
    private LocalDateTime dateLog;
    
    @OneToOne
    private Image image;
    
    @OneToOne
    private Writer writer;

    
    @Enumerated(EnumType.STRING)
    private Category category;
    
    private boolean subscriberContent = false;

}
