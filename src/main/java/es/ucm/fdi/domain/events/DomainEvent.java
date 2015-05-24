package es.ucm.fdi.domain.events;

import java.util.ArrayList;
import java.util.List;

public class DomainEvent<E> {

	private ThreadLocal<List<Action1<E>>> actions = new ThreadLocal<List<Action1<E>>>(){
        @Override
        protected List<Action1<E>> initialValue() {
            return new ArrayList<>();
        }
	}; 
	
	private List<Action1<E>> getActions() {
		return actions.get();
	}

	public DomainEventRegistrationRemover register(Action1<E> callback) {
		getActions().add(callback);
		return new DomainEventRegistrationRemover(new DomainEventRegistrationRemoverAction(callback)); 
	}

	public void notify(E args) {
		List<Action1<E>> actions = getActions();
		for (Action1<E> action : actions) { 
			action.execute(args);
		}
	}
	
	private class DomainEventRegistrationRemoverAction implements Action0 {
		
		private final Action1<E> listener;

		public DomainEventRegistrationRemoverAction(Action1<E> listener) {
			this.listener = listener; 
		}
		@Override
		public void execute() {
			getActions().remove(listener);
		}

	}
}