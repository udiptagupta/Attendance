package com.ntmo.attendance.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import com.ntmo.attendance.entity.Holidays;

@Slf4j
@Repository
public class HolidaysDAOImpl implements HolidaysDAO {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public List<Holidays> get() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Holidays> query = currentSession.createQuery("from Holidays", Holidays.class);
		List<Holidays> list = query.getResultList();
		
		return list;
	}

	@Override
	public Holidays save(Holidays day) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.persist(day);
		return day;
	}

	@Override
	public void delete(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Holidays day = currentSession.get(Holidays.class, id);
		if(day == null) {
			log.error("HolidaysDAOImpl:delete " + id + " Not Found");
			throw new RuntimeException("Holiday with id " + id + " not found.");
		}
	}

}
