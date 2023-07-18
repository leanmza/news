package com.lean.news.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Lean
 */
@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;
    private String mime;  // IDENTIFICA EL FORMATO DE LA IMAGEN

    @Lob
    @Basic(fetch = FetchType.LAZY) // ALMACENA LOS DATOS BINARIOS DE LA IMAGEN
    private byte[] contenido;

}
