package com.student.model;

import java.sql.Timestamp;

public class Student {
    private String studentId;
    private String name;
    private String program;
    private String status;
    private Timestamp createdAt;
    private Timestamp archivedAt;

    public Student() {}
    public Student(String studentId, String name, String program) {
        this.studentId = studentId; this.name = name; this.program = program;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String v) { this.studentId = v; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getProgram() { return program; }
    public void setProgram(String v) { this.program = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp v) { this.createdAt = v; }
    public Timestamp getArchivedAt() { return archivedAt; }
    public void setArchivedAt(Timestamp v) { this.archivedAt = v; }
}
