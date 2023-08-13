package com.lean.news.entity;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 *
 * @author Lean
 */
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

public class Reader extends CustomUser {

    
}
