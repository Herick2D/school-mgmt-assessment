package com.school.java_service.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.school.java_service.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final WebClient webClient;

    @Value("${node.backend.username}")
    private String backendUsername;

    @Value("${node.backend.password}")
    private String backendPassword;

    public StudentService(WebClient webClient) {
        this.webClient = webClient;
    }

    private record TokenPair(String accessToken, String refreshToken, String csrfToken) {}

    private TokenPair getTokens() {
        var response = webClient.post()
                .uri("/api/v1/auth/login")
                .bodyValue(Map.of(
                        "username", backendUsername,
                        "password", backendPassword
                ))
                .retrieve()
                .toBodilessEntity()
                .block();

        List<String> cookies = response.getHeaders().get("Set-Cookie");

        String accessToken = cookies.stream()
                .filter(c -> c.startsWith("accessToken=") && c.contains("Max-Age"))
                .map(c -> c.split(";")[0].replace("accessToken=", ""))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not get access token"));

        String refreshToken = cookies.stream()
                .filter(c -> c.startsWith("refreshToken=") && c.contains("Max-Age"))
                .map(c -> c.split(";")[0].replace("refreshToken=", ""))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not get refresh token"));

        String csrfToken = cookies.stream()
                .filter(c -> c.startsWith("csrfToken=") && c.contains("Max-Age"))
                .map(c -> c.split(";")[0].replace("csrfToken=", ""))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not get csrf token"));

        return new TokenPair(accessToken, refreshToken, csrfToken);
    }

    public StudentDTO fetchStudentFromBackend(Integer id) {
        TokenPair tokens = getTokens();
        return webClient.get()
                .uri("/api/v1/students/" + id)
                .cookie("accessToken", tokens.accessToken())
                .cookie("refreshToken", tokens.refreshToken())
                .header("x-csrf-token", tokens.csrfToken())
                .retrieve()
                .bodyToMono(StudentDTO.class)
                .block();
    }

    public byte[] generateStudentPdf(StudentDTO student) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Student Report")
                .setFontSize(22)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));

        Table table = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                .setWidth(UnitValue.createPercentValue(100));

        addRow(table, "Name", student.name());
        addRow(table, "Email", student.email());
        addRow(table, "Phone", student.phone());
        addRow(table, "Gender", student.gender());
        addRow(table, "Date of Birth", student.dob());
        addRow(table, "Class", student.className());
        addRow(table, "Section", student.section());
        addRow(table, "Roll", student.roll());
        addRow(table, "Admission Date", student.admissionDate());
        addRow(table, "Father Name", student.fatherName());
        addRow(table, "Father Phone", student.fatherPhone());
        addRow(table, "Mother Name", student.motherName());
        addRow(table, "Mother Phone", student.motherPhone());
        addRow(table, "Current Address", student.currentAddress());
        addRow(table, "Permanent Address", student.permanentAddress());
        addRow(table, "Reporter", student.reporterName());

        document.add(table);
        document.close();

        return baos.toByteArray();
    }

    private void addRow(Table table, String label, String value) {
        table.addCell(new Cell()
                .add(new Paragraph(label).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell()
                .add(new Paragraph(value != null ? value : "-")));
    }
}