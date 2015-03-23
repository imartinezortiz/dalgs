package es.ucm.fdi.dalgs.domain;

public interface Copyable<E> {
	public E depth_copy();
	public E shallow_copy();
}
