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
        teacherList = service.getByValueEnable(value);
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
        model.put("teacher", service.getById(id));
        return "/teacher/modify_teacher.html";
    }

    @PostMapping("/modify-teacher/{id}")
    public String modificar(@PathVariable String id, @RequestParam String name,
                            @RequestParam String surname, RedirectAttributes redirectAttributes) throws TeacherException {
        service.modify(id, name, surname);
        return REDIRECT_TEACHER_SEARCH_TEACHER;
    }

    @GetMapping("/delete-teacher/{id}")
    public String deleteTeacher(@PathVariable String id) throws TeacherException, CourseException {
        service.delete(id);
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
        model.put("teacher", service.getById(id));
        return "/teacher/detail_teacher.html";
    }

    @GetMapping("/add-remove-course/{id}")
    public String addRemoveCourse(ModelMap model, @PathVariable String id) throws CourseException {
        List<Course> courseList = courseService.getForEnable();
        model.put("course", courseList);
        return "/teacher/add_remove_course.html";
    }

    @PostMapping("/add-course/{id-teacher}/course/{id-course}")
    public String addCourse (ModelMap model, @PathVariable String idTeacher, @PathVariable String idCourse) throws TeacherException, CourseException {
        service.addCourse(idTeacher, idCourse);
        return "/teacher/add_remove_course.html";
    }

    @PostMapping("/remove-course/{id-teacher}/course/{id-course}")
    public String removeCourse (ModelMap model, @PathVariable String idTeacher, @PathVariable String idCourse) throws TeacherException, CourseException {
        service.removeCourse(idTeacher, idCourse);
        return "/teacher/add_remove_course.html";
    }
}