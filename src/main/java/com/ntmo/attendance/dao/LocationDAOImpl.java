package com.ntmo.attendance.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ntmo.attendance.entity.Location;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class LocationDAOImpl implements LocationDAO {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<Location> get() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Location> query = currentSession.createQuery("from Location", Location.class);
		List<Location> list = query.getResultList();
		return list;
	}

	@Override
	public Location get(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Location loc = currentSession.get(Location.class, id);
		return loc;
	}

	@Override
	public Location save(Location loc) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.persist(loc);
		return loc;
	}

	@Override
	public void delete(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Location loc = currentSession.get(Location.class, id);
		if(loc == null) {
			log.error("LocationDAOImpl:delete " + id + " Not Found");
			throw new RuntimeException("Location with id " + id + " not found.");
		}
		currentSession.remove(loc);
	}

}
