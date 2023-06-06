package com.ntmo.attendance.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import com.ntmo.attendance.entity.Manager;
@Slf4j
@Repository
public class ManagerDAOImpl implements ManagerDAO {
	
	@Autowired
	private EntityManager entityManager;

	@Override
	public List<Manager> get() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Manager> query = currentSession.createQuery("from Manager", Manager.class);
		List<Manager> list = query.getResultList();
		return list;
	}

	@Override
	public Manager get(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Manager mgr = currentSession.get(Manager.class, id);
		return mgr;
	}

	@Override
	public Manager save(Manager mgr) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.persist(mgr);
		return mgr;
	}

	@Override
	public void delete(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Manager mgr = currentSession.get(Manager.class, id);
		if(mgr == null) {
			log.error("ManagerDAOImpl:delete " + id + " Not Found");
			throw new RuntimeException("Manager with id " + id + " not found.");
		}
		currentSession.remove(mgr);
	}

}
