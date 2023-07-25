package com.lean.news.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Lean
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public class Image {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;
    private String mime;  // IDENTIFICA EL FORMATO DE LA IMAGEN

    @Lob
    @Basic(fetch = FetchType.LAZY) // ALMACENA LOS DATOS BINARIOS DE LA IMAGEN
    private byte[] content;

}
