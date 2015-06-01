package es.ucm.fdi.storage.business.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class StorageObjectId implements Serializable {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	private String bucket;
	
	private String objectId;

	public StorageObjectId() {
		
	}
	
	public StorageObjectId(String bucket, String key) {
		this.bucket = bucket;
		this.objectId = key;
	}
	
	public String getBucket() {
		return bucket;
	}
	
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getKey() {
		return objectId;
	}
	
	public void setKey(String key) {
		this.objectId = key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
		result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
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
		StorageObjectId other = (StorageObjectId) obj;
		if (bucket == null) {
			if (other.bucket != null)
				return false;
		} else if (!bucket.equals(other.bucket))
			return false;
		if (objectId == null) {
			if (other.objectId != null)
				return false;
		} else if (!objectId.equals(other.objectId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "StorageObjectId [bucket=>'"+bucket+"', key=>'"+objectId+"']";
	}
}
