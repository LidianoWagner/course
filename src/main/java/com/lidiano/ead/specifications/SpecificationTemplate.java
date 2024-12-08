package com.lidiano.ead.specifications;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.lidiano.ead.models.CourseModel;
import com.lidiano.ead.models.CourseUserModel;
import com.lidiano.ead.models.LessonModel;
import com.lidiano.ead.models.ModuleModel;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public class SpecificationTemplate {

	@And({
		@Spec(path = "courseLevel", spec = Equal.class),
		@Spec(path = "courseStatus", spec = Equal.class),
		@Spec(path = "name", spec = Like.class)
	})
	public interface CourseSpec extends Specification<CourseModel> {
		
	}
	
	@Spec(path = "title", spec = Like.class)
	public interface ModuleSpec extends Specification<ModuleModel> {
		
	}
	
	@Spec(path = "title", spec = Like.class)
	public interface LessonSpec extends Specification<LessonModel> {
		
	}
	
	public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
		return (root, query, cb) -> {
			query.distinct(true);
			Root<ModuleModel> module = root;
			Root<CourseModel> course = query.from(CourseModel.class);
			Expression<Collection<ModuleModel>> coursesModules = course.get("modules");
			return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(module, coursesModules));
		};
	}
	
	public static Specification<LessonModel> LessonModuleId(final UUID moduleId) {
		return (root, query, cb) -> {
			query.distinct(true);
			Root<LessonModel> lesson = root;
			Root<ModuleModel> module = query.from(ModuleModel.class);
			Expression<Collection<LessonModel>> moduleLessons = module.get("lessons");
			return cb.and(cb.equal(module.get("moduleId"), moduleId), cb.isMember(lesson, moduleLessons));
		};
	}
	
	public static Specification<CourseModel> courseUserId(final UUID userId) {
		return (root, query, cb) -> {
			query.distinct(true);
			Join<CourseModel, CourseUserModel> courseProd = root.join("coursesUsers");
			return cb.equal(courseProd.get("userId"), userId);
		};
	}
}
