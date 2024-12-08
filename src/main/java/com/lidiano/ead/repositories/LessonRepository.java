package com.lidiano.ead.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lidiano.ead.models.LessonModel;

public interface LessonRepository extends JpaRepository<LessonModel, UUID>, JpaSpecificationExecutor<LessonModel> {

	@Query(value = "SELECT * FROM tb_lessons WHERE module_module_id = :moduleId", nativeQuery = true)
	List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleID);

	@Query(value = "SELECT * FROM tb_lessons WHERE module_module_id = :moduleId AND lesson_id = :lessonId", nativeQuery = true)
	Optional<LessonModel> findLessonIntoModule(@Param("moduleId") UUID moduleId, @Param("lessonId") UUID lessonId);

}
