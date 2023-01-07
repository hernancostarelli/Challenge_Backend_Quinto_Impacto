package com.api.challenge.controller;

import com.api.challenge.exception.CourseException;
import com.api.challenge.exception.StudentException;
import com.api.challenge.model.entity.Course;
import com.api.challenge.model.entity.Student;
import com.api.challenge.model.entity.Teacher;
import com.api.challenge.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private static final String REDIRECT_TEACHER_SEARCH_TEACHER = "redirect:/course/search-course";
    private final ICourseService service;
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @GetMapping("/dashboard")
    public String dashboardCourse() {
        return "/course/dashboard_course.html";
    }

    @GetMapping("/search-course")
    public String searchCourse(ModelMap model, @RequestParam(required = false) String value) throws CourseException {
        List<Course> courseList;
        if (value == null) {
            value = "";
        }
        courseList = service.getByValue(value);
        model.put("courseList", courseList);
        return "/course/get_course.html";
    }

    @GetMapping("/save-course")
    public String saveCourse() {
        return "/course/save_course.html";
    }

    @PostMapping("/save-course")
    public String saveCourse(ModelMap model, @RequestParam String name, @RequestParam String turn,
                             @RequestParam String schedule, RedirectAttributes redirectAttributes) {
        try {
            service.save(name, turn, schedule);
        } catch (Exception exception) {
            logger.info("OCURRIÓ UN ERROR : {}", exception.getMessage());
            model.put("error", exception.getMessage());
            redirectAttributes.addFlashAttribute("name", name);
            redirectAttributes.addFlashAttribute("turn", turn);
            redirectAttributes.addFlashAttribute("schedule", schedule);
            return "error.html";
        }
        return "redirect:/course/dashboard";
    }

    @GetMapping("/modify-course/{id}")
    public String modifyCourse(ModelMap model, @PathVariable String id) throws CourseException {
        model.put("course", service.getById(id));
        return "/course/modify_course.html";
    }

    @PostMapping("/modify-course/{id}")
    public String modifyCourse(@PathVariable String id, @RequestParam String name, @RequestParam String turn,
                               @RequestParam String schedule, RedirectAttributes redirectAttributes) throws CourseException {
        service.modify(id, name, turn, schedule);
        return REDIRECT_TEACHER_SEARCH_TEACHER;
    }

    @GetMapping("/enable-course/{id}")
    public String enableCourse(@PathVariable String id) throws CourseException {
        service.enable(id);
        return REDIRECT_TEACHER_SEARCH_TEACHER;
    }

    @GetMapping("/disable-course/{id}")
    public String disableCourse(@PathVariable String id) throws StudentException, CourseException {
        service.disable(id);
        return REDIRECT_TEACHER_SEARCH_TEACHER;
    }

    @GetMapping("/detail-course/{id}")
    public String detailCourse(ModelMap model, @PathVariable String id) throws CourseException {
        Course course = service.getById(id);
        model.put("course", course);
        List<Student> studentList = course.getStudentList();
        model.put("studentList", studentList);
        Teacher teacher = course.getTeacher();
        model.put("teacher", teacher);
        return "/course/detail_course.html";
    }
}