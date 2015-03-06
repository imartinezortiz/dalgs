package es.ucm.fdi.dalgs.classes;

import java.util.Collection;

public class ResultClass<E> {

	private E e;
	
	private boolean hasErrors;
	
	private Collection <String> errorsList;
	
	private boolean elementDeleted;
	
	public ResultClass() {
		
		
	}
	public E getE() {
		return e;
	}
	public void setE(E e) {
		this.e = e;
	}
	public boolean hasErrors() {
		return hasErrors;
	}
	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}
	public Collection<String> getErrorsList() {
		return errorsList;
	}
	public void setErrorsList(Collection<String> errorsList) {
		this.errorsList = errorsList;
	}
	public boolean isElementDeleted() {
		return elementDeleted;
	}
	public void setElementDeleted(boolean elementDeleted) {
		this.elementDeleted = elementDeleted;
	}
	
	
	
}
