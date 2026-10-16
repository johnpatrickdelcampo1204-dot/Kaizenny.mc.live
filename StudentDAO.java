package com.student.dao;

import com.student.db.DBConnection;
import com.student.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object: all SQL lives here.
 * Sections: CREATE | READ | UPDATE | ARCHIVE / RESTORE | DELETE
 * (In NetBeans: Ctrl+7 opens the Navigator to jump between methods.)
 */
public class StudentDAO {

    // <editor-fold defaultstate="expanded" desc="CREATE  (SQL INSERT)">

    /** CREATE - INSERT a new student. Returns false if the Student ID already exists. */
    public boolean createStudent(Student s) throws SQLException {
        String sql = "INSERT INTO students (student_id, name, program) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getProgram());
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException dup) {
            return false;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="expanded" desc="READ  (SQL SELECT)">

    /** READ - SELECT all students with a given status (ACTIVE or ARCHIVED). */
    public List<Student> readStudentsByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM students WHERE status = ? ORDER BY created_at DESC";
        List<Student> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    /** READ - SELECT one student by Student ID (used by the Edit page). */
    public Student readStudentById(String id) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="expanded" desc="UPDATE  (SQL UPDATE)">

    /** UPDATE - change Student ID, name and program (oldId identifies the record). */
    public boolean updateStudent(String oldId, Student s) throws SQLException {
        String sql = "UPDATE students SET student_id = ?, name = ?, program = ? WHERE student_id = ? AND status = 'ACTIVE'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getProgram());
            ps.setString(4, oldId);
            return ps.executeUpdate() > 0;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="expanded" desc="ARCHIVE / RESTORE  (soft delete)">

    /** ARCHIVE - the row is kept in the table, only flagged as ARCHIVED. */
    public boolean archiveStudent(String id) throws SQLException {
        String sql = "UPDATE students SET status = 'ARCHIVED', archived_at = NOW() WHERE student_id = ? AND status = 'ACTIVE'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /** RESTORE - bring an archived record back to ACTIVE. */
    public boolean restoreStudent(String id) throws SQLException {
        String sql = "UPDATE students SET status = 'ACTIVE', archived_at = NULL WHERE student_id = ? AND status = 'ARCHIVED'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="expanded" desc="DELETE  (SQL DELETE, archived records only)">

    /** DELETE - permanently remove a record. Only allowed if it is already ARCHIVED. */
    public boolean deleteStudent(String id) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ? AND status = 'ARCHIVED'";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Helper: map a database row to a Student object">
    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student(rs.getString("student_id"), rs.getString("name"), rs.getString("program"));
        s.setStatus(rs.getString("status"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        s.setArchivedAt(rs.getTimestamp("archived_at"));
        return s;
    }
    // </editor-fold>
}
