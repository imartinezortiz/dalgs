package es.ucm.fdi.domain.events;

public interface Adaptable {
  <T> T getAdapter(Class<T> clazz);
}
