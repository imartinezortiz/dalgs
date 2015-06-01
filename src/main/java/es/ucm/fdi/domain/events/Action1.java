package es.ucm.fdi.domain.events;

public interface Action1<E> {
	void execute(E obj);
}
