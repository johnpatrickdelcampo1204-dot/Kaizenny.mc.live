<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Student Records</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Student Record Module</h1>

    <c:if test="${not empty success}"><div class="alert success"><c:out value="${success}"/></div></c:if>
    <c:if test="${not empty error}"><div class="alert error"><c:out value="${error}"/></div></c:if>

    <!-- FORM -> CONTROLLER (POST) -> DATABASE INSERT -->
    <section class="card">
        <h2>Add Student</h2>
        <form action="students" method="post">
            <input type="hidden" name="action" value="create">
            <label for="studentId">Student ID</label>
            <input id="studentId" name="studentId" required maxlength="20" placeholder="e.g. 2026-0003">
            <label for="name">Name</label>
            <input id="name" name="name" required maxlength="100" placeholder="Full name">
            <label for="program">Program</label>
            <input id="program" name="program" required maxlength="100" placeholder="e.g. BSIT">
            <button type="submit" class="btn primary">Save Student</button>
        </form>
    </section>

    <!-- SELECT -> DISPLAY -->
    <section class="card">
        <h2>Active Students</h2>
        <div class="table-wrap">
        <table>
            <thead><tr><th>Student ID</th><th>Name</th><th>Program</th><th>Actions</th></tr></thead>
            <tbody>
            <c:forEach var="s" items="${activeList}">
                <tr>
                    <td><c:out value="${s.studentId}"/></td>
                    <td><c:out value="${s.name}"/></td>
                    <td><c:out value="${s.program}"/></td>
                    <td class="actions">
                        <a class="btn" href="students?action=edit&id=<c:out value='${s.studentId}'/>">Edit</a>
                        <form action="students" method="post" onsubmit="return confirm('Archive this student?');">
                            <input type="hidden" name="action" value="archive">
                            <input type="hidden" name="studentId" value="${s.studentId}">
                            <button type="submit" class="btn danger">Archive</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty activeList}"><tr><td colspan="4" class="empty">No active students yet.</td></tr></c:if>
            </tbody>
        </table>
        </div>
    </section>

    <section class="card">
        <h2>Archived Students</h2>
        <div class="table-wrap">
        <table>
            <thead><tr><th>Student ID</th><th>Name</th><th>Program</th><th>Archived At</th><th>Actions</th></tr></thead>
            <tbody>
            <c:forEach var="s" items="${archivedList}">
                <tr class="archived">
                    <td><c:out value="${s.studentId}"/></td>
                    <td><c:out value="${s.name}"/></td>
                    <td><c:out value="${s.program}"/></td>
                    <td><c:out value="${s.archivedAt}"/></td>
                    <td class="actions">
                        <form action="students" method="post">
                            <input type="hidden" name="action" value="restore">
                            <input type="hidden" name="studentId" value="${s.studentId}">
                            <button type="submit" class="btn">Restore</button>
                        </form>
                        <form action="students" method="post" onsubmit="return confirm('Permanently delete this student? This cannot be undone.');">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="studentId" value="${s.studentId}">
                            <button type="submit" class="btn danger">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty archivedList}"><tr><td colspan="5" class="empty">Nothing archived.</td></tr></c:if>
            </tbody>
        </table>
        </div>
    </section>
</div>
</body>
</html>
