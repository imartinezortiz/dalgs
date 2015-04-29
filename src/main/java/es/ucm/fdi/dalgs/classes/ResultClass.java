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
package es.ucm.fdi.dalgs.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ResultClass<E> implements Collection<E> {

	private List<E> e;

	private boolean hasErrors;

	private Collection<String> errorsList;

	private boolean elementDeleted;

	public ResultClass() {
		this.e = new LinkedList<E>();
		this.errorsList = new ArrayList<String>();
	}

	public E getSingleElement() {
		if (this.e.size() == 0) {
			return null;
		}
		return this.e.get(0);
	}

	public void setSingleElement(E e) {
		this.e.clear();
		this.e.add(e);
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

	@Override
	public int size() {
		return this.e.size();
	}

	@Override
	public boolean isEmpty() {
		return this.e.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.e.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.e.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.e.toArray();

	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.e.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return this.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.e.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.e.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.e.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.retainAll(c);
	}

	@Override
	public void clear() {
		this.e.clear();
	}

}
