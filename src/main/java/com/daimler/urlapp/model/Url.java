package com.daimler.urlapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@Entity
@Table(name = "urls")
@NamedQuery(name = "Url.fetchByCustomHash", query = "SELECT u FROM Url u WHERE u.customHash = :hash")
public class Url extends AuditEntity {

    private static final long serialVersionUID = -5502043778089640767L;

    @Id
    @GeneratedValue(generator = "url_id_generator")
    @SequenceGenerator(name = "url_id_generator", sequenceName = "url_seq", initialValue = 1000000000)
    private Long id;

    @Column(columnDefinition = "text", nullable = false)
    private String path;

    @Column(columnDefinition = "text", nullable = true)
    private String customHash;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCustomHash() {
        return customHash;
    }

    public void setCustomHash(String customHash) {
        this.customHash = customHash;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Url other = (Url) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "this.getClass().getSimpleName() +  \"[id=" + id + ", path=" + path + "]";
    }

}
