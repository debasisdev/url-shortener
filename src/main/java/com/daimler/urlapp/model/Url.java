package com.daimler.urlapp.model;

import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.daimler.urlapp.GLOBALS;
import com.daimler.urlapp.exception.BusinessLogicException;

/**
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@Entity
@Table(name = "urls")
@NamedQuery(name = "Url.fetchByCustomHash", query = "SELECT u FROM Url u WHERE u.customHash = :hash")
public class Url extends AuditEntity implements Comparable<Url> {
	
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
		return this.getClass().getSimpleName() + "[id:" + id + ", path:" + path + "]";
	}
	
	public String computeDomain() {
		try {
			URI uri = new URI(this.getPath());
			String[] domain = uri.getHost().split(GLOBALS.DOT_ESCAPED);
			StringBuilder dnsName = new StringBuilder(domain.length > 2 ? domain[1] : domain[0]);
			if (dnsName.length() < 3) {
				dnsName.append(GLOBALS.GENERIC_ZOP_DOMAIN);
			}
			return uri.getScheme() + GLOBALS.PROTOCOL_TO_DOMAIN_SEPARATOR
			        + new StringBuilder(dnsName).insert(dnsName.length() - 2, GLOBALS.DOMAIN_NAME_SEPARATOR).toString();
		} catch (URISyntaxException uriSyntaxException) {
			throw new BusinessLogicException("Domain from the URL coudln't be computed", uriSyntaxException);
		}
	}
	
	@Override
	public int compareTo(final Url url) {
		if (this.computeDomain().equals(url.computeDomain()))
			return 0;
		else
			return 1;
	}
	
}
