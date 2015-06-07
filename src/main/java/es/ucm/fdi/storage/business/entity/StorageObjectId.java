/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
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
