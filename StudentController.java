package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Controller: Form -> Controller -> Database -> Result.
 *
 *  doGet  -> READ   (SELECT, show lists / edit page)
 *  doPost -> CREATE | UPDATE | ARCHIVE | RESTORE | DELETE
 *
 * (In NetBeans: Ctrl+7 opens the Navigator to jump between methods.)
 */
@WebServlet("/students")
public class StudentController extends HttpServlet {

    private final StudentDAO dao = new StudentDAO();

    // <editor-fold defaultstate="expanded" desc="READ  (GET request -> SELECT -> display)">
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if ("edit".equals(req.getParameter("action"))) {
                showEditPage(req, resp);
            } else {
                showStudentLists(req, resp);
            }
        } catch (SQLException e) {
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.getRequestDispatcher("/students.jsp").forward(req, resp);
        }
    }

    /** READ - SELECT active + archived students and show them in students.jsp. */
    private void showStudentLists(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        HttpSession session = req.getSession();
        for (String key : new String[]{"success", "error"}) {      // one-time messages
            Object v = session.getAttribute(key);
            if (v != null) { req.setAttribute(key, v); session.removeAttribute(key); }
        }
        req.setAttribute("activeList", dao.readStudentsByStatus("ACTIVE"));
        req.setAttribute("archivedList", dao.readStudentsByStatus("ARCHIVED"));
        req.getRequestDispatcher("/students.jsp").forward(req, resp);
    }

    /** READ - load one student into edit.jsp. */
    private void showEditPage(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        Student s = dao.readStudentById(req.getParameter("id"));
        if (s == null || !"ACTIVE".equals(s.getStatus())) {
            flash(req, "error", "Record not found or already archived.");
            resp.sendRedirect("students");
            return;
        }
        req.setAttribute("student", s);
        req.getRequestDispatcher("/edit.jsp").forward(req, resp);
    }
    // </editor-fold>

    // <editor-fold defaultstate="expanded" desc="POST dispatcher (decides CREATE / UPDATE / ARCHIVE / RESTORE / DELETE)">
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        try {
            switch (action == null ? "" : action) {
                case "create":  createStudent(req);  break;
                case "update":  updateStudent(req);  break;
                case "archive": archiveStudent(req); break;
                case "restore": restoreStudent(req); break;
                case "delete":  deleteStudent(req);  break;
                default:        flash(req, "error", "Unknown action.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            flash(req, "error", "That Student ID already exists.");
        } catch (SQLException e) {
            flash(req, "error", "Database error: " + e.getMessage());
        }
        // Post/Redirect/Get: avoids duplicate inserts when the browser is refreshed
        resp.sendRedirect("students");
    }
    // </editor-fold>

    // <editor-fold defaultstate="expanded" desc="CREATE  (form POST -> INSERT -> success/error message)">
    private void createStudent(HttpServletRequest req) throws SQLException {
        String id = param(req, "studentId"), name = param(req, "name"), program = param(req, "program");
        if (id.isEmpty() || name.isEmpty() || program.isEmpty()) {
            flash(req, "error", "All fields are required.");
        } else if (dao.createStudent(new Student(id, name, program))) {
            flash(req, "success", "Student " + id + " was saved successfully.");
        } else {
            flash(req, "error", "Student ID " + id + " already exists.");
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="expanded" desc="UPDATE  (edit form POST -> UPDATE)">
    private void updateStudent(HttpServletRequest req) throws SQLException {
        String oldId = param(req, "oldId");
        String id = param(req, "studentId"), name = param(req, "name"), program = param(req, "program");
        if (id.isEmpty() || name.isEmpty() || program.isEmpty()) {
            flash(req, "error", "All fields are required.");
        } else if (dao.updateStudent(oldId, new Student(id, name, program))) {
            flash(req, "success", "Student " + id + " was updated.");
        } else {
            flash(req, "error", "Update failed: record not found or archived.");
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="expanded" desc="ARCHIVE / RESTORE  (soft delete, data stays in the database)">
    private void archiveStudent(HttpServletRequest req) throws SQLException {
        String id = param(req, "studentId");
        if (dao.archiveStudent(id)) flash(req, "success", "Student " + id + " was archived (kept in the database, not deleted).");
        else flash(req, "error", "Archive failed: record not found or already archived.");
    }

    private void restoreStudent(HttpServletRequest req) throws SQLException {
        String id = param(req, "studentId");
        if (dao.restoreStudent(id)) flash(req, "success", "Student " + id + " was restored.");
        else flash(req, "error", "Restore failed: record not found or not archived.");
    }
    // </editor-fold>

    // <editor-fold defaultstate="expanded" desc="DELETE  (permanent, archived records only)">
    private void deleteStudent(HttpServletRequest req) throws SQLException {
        String id = param(req, "studentId");
        if (dao.deleteStudent(id)) flash(req, "success", "Student " + id + " was permanently deleted.");
        else flash(req, "error", "Delete failed: only archived records can be deleted.");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Helpers">
    private void flash(HttpServletRequest req, String type, String msg) {
        req.getSession().setAttribute(type, msg);
    }

    private String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }
    // </editor-fold>
}
