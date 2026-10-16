<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Student</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Edit Student</h1>
    <section class="card">
        <form action="students" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="oldId" value="<c:out value='${student.studentId}'/>">
            <label for="studentId">Student ID</label>
            <input id="studentId" name="studentId" required maxlength="20" value="<c:out value='${student.studentId}'/>">
            <label for="name">Name</label>
            <input id="name" name="name" required maxlength="100" value="<c:out value='${student.name}'/>">
            <label for="program">Program</label>
            <input id="program" name="program" required maxlength="100" value="<c:out value='${student.program}'/>">
            <button type="submit" class="btn primary">Update</button>
            <a class="btn" href="students">Cancel</a>
        </form>
    </section>
</div>
</body>
</html>
