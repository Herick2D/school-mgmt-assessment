package com.school.java_service.service;

import com.school.java_service.dto.StudentDTO;
import com.school.java_service.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private StudentService studentService;

    private StudentDTO mockStudent;

    @BeforeEach
    void setUp() {
        mockStudent = new StudentDTO(
                2,
                "Herick da Silva Moreira",
                "herick@email.com",
                "21999999999",
                "Male",
                "2000-01-01",
                "Java",
                "C1",
                "13",
                "Junior",
                "21988888888",
                "Edna",
                "21977777777",
                "Rua Teste, 123",
                "Rua Teste, 123",
                "2026-01-01",
                "John Doe",
                true
        );
    }

    @Test
    void shouldGeneratePdfWithCorrectContent() {
        byte[] pdf = studentService.generateStudentPdf(mockStudent);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void shouldGeneratePdfStartingWithPdfHeader() {
        byte[] pdf = studentService.generateStudentPdf(mockStudent);

        String header = new String(pdf, 0, 4);
        assertEquals("%PDF", header);
    }

    @Test
    void shouldHandleNullFieldsInPdf() {
        StudentDTO studentWithNulls = new StudentDTO(
                3, "Nome Teste", "email@test.com",
                null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, true
        );

        byte[] pdf = studentService.generateStudentPdf(studentWithNulls);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }
}