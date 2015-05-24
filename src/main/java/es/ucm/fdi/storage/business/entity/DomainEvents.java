package es.ucm.fdi.storage.business.entity;

import es.ucm.fdi.domain.events.DomainEvent;

public final class DomainEvents {

	public static final DomainEvent<StorageObject> OBJECT_DELETED = new DomainEvent<StorageObject>();
	
	private DomainEvents() {
		// Avoid instantiation
	}
}
