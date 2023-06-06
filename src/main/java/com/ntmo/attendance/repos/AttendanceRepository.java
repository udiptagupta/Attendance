package com.ntmo.attendance.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ntmo.attendance.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {


	List<Attendance> findByEmployeeIdAndPresenceDateBetween(int employeeId, Date startDate, Date endDate);
	
	long count();
	
	void delete(Attendance entity);
	
	<S extends Attendance> S save(S entity);
	
}
