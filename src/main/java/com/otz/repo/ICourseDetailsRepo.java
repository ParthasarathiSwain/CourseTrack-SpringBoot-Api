package com.otz.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.otz.entity.CourseDetails;

public interface ICourseDetailsRepo extends JpaRepository<CourseDetails, Integer>{
	@Query("select distinct(courseCategory)from CourseDetails" )
	public Set<String> getUniqueCourseCategory();
	
	@Query("select distinct(facultyName)from CourseDetails" )
	public Set<String> getUniqueFacultyName();
	
	@Query("select distinct(trainingMode)from CourseDetails" )
	public Set<String> getUniqueTrainingMode();
}
