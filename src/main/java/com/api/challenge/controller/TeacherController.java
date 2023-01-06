package com.api.challenge.controller;

import com.api.challenge.exception.CourseException;
import com.api.challenge.exception.TeacherException;
import com.api.challenge.model.entity.Course;
import com.api.challenge.model.entity.Teacher;
import com.api.challenge.service.ICourseService;
import com.api.challenge.service.ITeacherService;
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
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private static final String REDIRECT_TEACHER_SEARCH_TEACHER = "redirect:/teacher/search-teacher";
    private static final String TEACHER_ADD_REMOVE_COURSE_HTML = "/teacher/add_remove_course.html";
    private static final String TEACHER = "teacher";
    private final ITeacherService service;
    private final ICourseService courseService;
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @GetMapping("/dashboard")
    public String dashboardTeacher() {
        return "/teacher/dashboard_teacher.html";
    }

    @GetMapping("/search-teacher")
    public String searchTeacher(ModelMap model, @RequestParam(required = false) String value) throws TeacherException {
        List<Teacher> teacherList;
        if (value == null) {
            value = "";
        }
        teacherList = service.getByValue(value);
        model.put("teacherList", teacherList);
        return "/teacher/get_teacher.html";
    }

    @GetMapping("/save-teacher")
    public String saveTeacher() {
        return "/teacher/save_teacher.html";
    }

    @PostMapping("/save-teacher")
    public String saveTeacher(ModelMap model, @RequestParam String name, @RequestParam String surname,
                              RedirectAttributes redirectAttributes) {
        try {
            service.save(name, surname);
        } catch (Exception exception) {
            logger.info("OCURRIÓ UN ERROR : {}", exception.getMessage());
            model.put("error", exception.getMessage());
            redirectAttributes.addFlashAttribute("name", name);
            redirectAttributes.addFlashAttribute("surname", surname);
            return "error.html";
        }
        return "redirect:/teacher/dashboard";
    }

    @GetMapping("/modify-teacher/{id}")
    public String modifyTeacher(ModelMap model, @PathVariable String id) throws TeacherException {
        model.put(TEACHER, service.getById(id));
        return "/teacher/modify_teacher.html";
    }

    @PostMapping("/modify-teacher/{id}")
    public String modifyTeacher(@PathVariable String id, @RequestParam String name,
                                @RequestParam String surname) throws TeacherException {
        service.modify(id, name, surname);
        return REDIRECT_TEACHER_SEARCH_TEACHER;
    }

    @GetMapping("/enable-teacher/{id}")
    public String enableTeacher(@PathVariable String id) throws TeacherException {
        service.enable(id);
        return REDIRECT_TEACHER_SEARCH_TEACHER;
    }

    @GetMapping("/disable-teacher/{id}")
    public String disableTeacher(@PathVariable String id) throws TeacherException, CourseException {
        service.disable(id);
        return REDIRECT_TEACHER_SEARCH_TEACHER;
    }

    @GetMapping("/detail-teacher/{id}")
    public String detailTeacher(ModelMap model, @PathVariable String id) throws TeacherException {
        Teacher teacher = service.getById(id);
        model.put(TEACHER, teacher);
        List<Course> courseList = teacher.getCourseList();
        model.put("courseList", courseList);
        return "/teacher/detail_teacher.html";
    }

    @GetMapping("/course/{id}")
    public String addRemoveCourse(ModelMap model, @PathVariable String id) throws CourseException, TeacherException {
        List<Course> courseList = courseService.getForEnable();
        model.put("course", courseList);
        Teacher teacher = service.getById(id);
        model.put(TEACHER, teacher);
        return TEACHER_ADD_REMOVE_COURSE_HTML;
    }

    @PostMapping("/add-course/{id}/course/{id2}")
    public String addCourse(ModelMap model, @PathVariable String id, @PathVariable String id2) throws TeacherException, CourseException {
        try {
            System.err.println("ID TEACHER : " + id);
            System.err.println("ID COURSE : " + id2);
            service.addCourse(id, id2);
            return REDIRECT_TEACHER_SEARCH_TEACHER;
        } catch (Exception exception){
            System.err.println("OCURRIÓ UN ERROR : "+ exception.getMessage());
            model.put("error", exception.getMessage());
            return "error.html";
        }
    }

    @PostMapping("/remove-course/{id}/course/{id2}")
    public String removeCourse(@PathVariable String id, @PathVariable String id2) throws TeacherException, CourseException {
        System.err.println("ID TEACHER : " + id);
        System.err.println("ID COURSE : " + id2);
        service.removeCourse(id, id2);
        return REDIRECT_TEACHER_SEARCH_TEACHER;
    }
}