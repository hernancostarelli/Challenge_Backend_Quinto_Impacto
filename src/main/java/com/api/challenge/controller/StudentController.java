package com.api.challenge.controller;

import com.api.challenge.exception.CourseException;
import com.api.challenge.exception.StudentException;
import com.api.challenge.model.entity.Course;
import com.api.challenge.model.entity.Student;
import com.api.challenge.service.ICourseService;
import com.api.challenge.service.IStudentService;
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
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private static final String REDIRECT_STUDENT_SEARCH_STUDENT = "redirect:/student/search-student";
    private static final String STUDENT = "student";
    private final IStudentService service;
    private final ICourseService courseService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @GetMapping("/dashboard")
    public String dashboardStudent() {
        return "/student/dashboard_student.html";
    }

    @GetMapping("/search-student")
    public String searchStudent(ModelMap model, @RequestParam(required = false) String value) throws StudentException {
        List<Student> studentList;
        if (value == null) {
            value = "";
        }
        studentList = service.getByValue(value);
        model.put("studentList", studentList);
        return "/student/get_student.html";
    }

    @GetMapping("/save-student")
    public String saveStudent() {
        return "/student/save_student.html";
    }

    @PostMapping("/save-student")
    public String saveTeacher(ModelMap model, @RequestParam String name, @RequestParam String surname, @RequestParam String dateOfBirth,
                              @RequestParam String story, RedirectAttributes redirectAttributes) {
        try {
            service.save(name, surname, dateOfBirth, story);
        } catch (Exception exception) {
            logger.info("OCURRIÓ UN ERROR : {}", exception.getMessage());
            model.put("error", exception.getMessage());
            redirectAttributes.addFlashAttribute("name", name);
            redirectAttributes.addFlashAttribute("surname", surname);
            redirectAttributes.addFlashAttribute("dateOfBirth", dateOfBirth);
            redirectAttributes.addFlashAttribute("story", story);
            return "error.html";
        }
        return "redirect:/student/dashboard";
    }

    @GetMapping("/modify-student/{id}")
    public String modifyStudent(ModelMap model, @PathVariable String id) throws StudentException {
        model.put(STUDENT, service.getById(id));
        return "/student/modify_student.html";
    }

    @PostMapping("/modify-student/{id}")
    public String modifyTeacher(@PathVariable String id, @RequestParam String name, @RequestParam String surname,
                                @RequestParam String dateOfBirth, @RequestParam String story) throws StudentException {
        service.modify(id, name, surname, dateOfBirth, story);
        return REDIRECT_STUDENT_SEARCH_STUDENT;
    }

    @GetMapping("/enable-student/{id}")
    public String enableStudent(@PathVariable String id) throws StudentException {
        service.enable(id);
        return REDIRECT_STUDENT_SEARCH_STUDENT;
    }

    @GetMapping("/disable-student/{id}")
    public String disableStudent(@PathVariable String id) throws CourseException, StudentException {
        service.disable(id);
        return REDIRECT_STUDENT_SEARCH_STUDENT;
    }

    @GetMapping("/detail-student/{id}")
    public String detailStudent(ModelMap model, @PathVariable String id) throws StudentException {
        Student student = service.getById(id);
        model.put(STUDENT, student);
        List<Course> courseList = student.getCourseList();
        model.put("courseList", courseList);
        return "/student/detail_student.html";
    }

    @GetMapping("/course/{id}")
    public String addRemoveCourse(ModelMap model, @PathVariable String id) throws CourseException, StudentException {
        List<Course> courseList = courseService.getForEnable();
        model.put("course", courseList);
        Student student = service.getById(id);
        model.put(STUDENT, student);
        return "/student/add_remove_course.html";
    }

    @PostMapping("/add-course/{id}/course/{id2}")
    public String addCourse(ModelMap model, @PathVariable String id, @PathVariable String id2) throws StudentException, CourseException {
        try {
            System.err.println("ID STUDENT : " + id);
            System.err.println("ID COURSE : " + id2);
            service.addCourse(id, id2);
            return REDIRECT_STUDENT_SEARCH_STUDENT;
        } catch (Exception exception) {
            System.err.println("OCURRIÓ UN ERROR : " + exception.getMessage());
            model.put("error", exception.getMessage());
            return "error.html";
        }
    }

    @PostMapping("/remove-course/{id}/course/{id2}")
    public String removeCourse(@PathVariable String id, @PathVariable String id2) throws StudentException, CourseException {
        System.err.println("ID STUDENT : " + id);
        System.err.println("ID COURSE : " + id2);
        service.removeCourse(id, id2);
        return REDIRECT_STUDENT_SEARCH_STUDENT;
    }
}