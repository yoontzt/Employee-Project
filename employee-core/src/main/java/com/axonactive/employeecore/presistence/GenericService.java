package com.axonactive.employeecore.presistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class GenericService<E> {

	@PersistenceContext(name = "EmployeeCoreDS")
	protected EntityManager em;

	public void save(E entity) {
		em.persist(entity);
	}

	public void update(E entity) {
		em.merge(em.contains(entity) ? entity : em.merge(entity));
	}

	public void remove(E entity) {
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}
}
