package com.school.java_service.controller;

import com.school.java_service.dto.StudentDTO;
import com.school.java_service.service.StudentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<byte[]> getStudentReport(@PathVariable Integer id) {
        StudentDTO student = studentService.fetchStudentFromBackend(id);
        byte[] pdf = studentService.generateStudentPdf(student);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=student-report-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
