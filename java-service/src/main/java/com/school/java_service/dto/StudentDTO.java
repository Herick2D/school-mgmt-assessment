package com.school.java_service.dto;

public record StudentDTO(Integer id,
                         String name,
                         String email,
                         String phone,
                         String gender,
                         String dob,
                         String className,
                         String section,
                         String roll,
                         String fatherName,
                         String fatherPhone,
                         String motherName,
                         String motherPhone,
                         String currentAdress,
                         String permanetAdress,
                         String admissionDate,
                         String reporterName,
                         Boolean systemAccess) {
}
