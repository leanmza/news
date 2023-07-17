
package com.lean.news.repository;

import com.lean.news.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




/**
 *
 * @author Lean
 */
@Repository
public interface ReaderRepository extends JpaRepository<Reader, String>{
    
    @Query("SELECT r FROM Reader r WHERE r.email = :email")
   public Reader findReaderByEmail(@Param("email") String email);
   
}
