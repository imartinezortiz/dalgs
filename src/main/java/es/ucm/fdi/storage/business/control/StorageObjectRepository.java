package es.ucm.fdi.storage.business.control;

import org.springframework.data.repository.CrudRepository;

import es.ucm.fdi.storage.business.entity.StorageObject;
import es.ucm.fdi.storage.business.entity.StorageObjectId;

public interface StorageObjectRepository extends CrudRepository<StorageObject, StorageObjectId> {

}
