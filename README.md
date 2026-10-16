# Student Record Web Application (Form → Controller → Database → Result)

## Stack
Java (Servlet + JSP + JSTL) · MySQL · Maven · Tomcat 9 · NetBeans

## Setup
1. Start MySQL (XAMPP → Start MySQL) and open phpMyAdmin → Import `db/database.sql`.
2. NetBeans → File → Open Project → select this folder (`StudentRecordApp`).
3. Edit DB user/password in `src/main/java/com/student/db/DBConnection.java` if needed.
4. Right-click project → Clean and Build, then Run (Tomcat 9). Open http://localhost:8080/StudentRecordApp/
   (Tomcat 10+? replace every `javax.servlet` import with `jakarta.servlet`, use `jakarta.servlet-api:5.0.0` and `jakarta.servlet.jsp.jstl` in pom.xml.)

## Data Flow
User fills the form → browser sends HTTP POST to `/students` → `StudentController` validates input
→ `StudentDAO` runs a prepared `INSERT` on MySQL → controller stores a success/error message and
redirects (Post/Redirect/Get) → `doGet` runs `SELECT` → `students.jsp` displays the table and message to the user.

| Operation | HTTP | SQL |
|-----------|------|-----|
| Create | POST action=create | INSERT |
| Read | GET /students | SELECT ... WHERE status='ACTIVE' |
| Update | POST action=update | UPDATE name, program |
| Delete → Archive | POST action=archive | UPDATE status='ARCHIVED', archived_at=NOW() (row is kept) |
| Restore (bonus) | POST action=restore | UPDATE status='ACTIVE' |
| Delete permanently (from Archived list only) | POST action=delete | DELETE ... WHERE status='ARCHIVED' |

## Evidence checklist
- [ ] Working form · [ ] Controller route `/students` · [ ] Record visible in phpMyAdmin
- [ ] Data displayed from DB · [ ] Success/error messages (try a duplicate ID)
- [ ] Screenshots + screen recording (UI side-by-side with phpMyAdmin) · [ ] ZIP/PDF of code
