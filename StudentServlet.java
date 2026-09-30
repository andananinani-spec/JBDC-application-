package com.studentapp.controller;

import com.studentapp.dao.StudentDAO;
import com.studentapp.model.Student;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/students")
public class StudentServlet extends HttpServlet {

    private final StudentDAO dao = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            List<Student> students = dao.findAll();
            sendJson(resp, students);
        } catch (SQLException e) {
            sendError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            String name = req.getParameter("name");
            int age = Integer.parseInt(req.getParameter("age"));
            String course = req.getParameter("course");

            validate(name, age, course);
            dao.insert(new Student(name.trim(), age, course.trim()));

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"message\":\"Student added successfully\"}");
        } catch (Exception e) {
            sendError(resp, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            int age = Integer.parseInt(req.getParameter("age"));
            String course = req.getParameter("course");

            validate(name, age, course);
            boolean updated = dao.update(new Student(id, name.trim(), age, course.trim()));

            if (!updated) {
                resp.sendError(404, "Student not found");
                return;
            }

            resp.setContentType("application/json");
            resp.getWriter().print("{\"message\":\"Student updated successfully\"}");
        } catch (Exception e) {
            sendError(resp, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            boolean deleted = dao.delete(id);

            if (!deleted) {
                resp.sendError(404, "Student not found");
                return;
            }

            resp.setContentType("application/json");
            resp.getWriter().print("{\"message\":\"Student deleted successfully\"}");
        } catch (Exception e) {
            sendError(resp, e);
        }
    }

    private void validate(String name, int age, String course) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (age < 1 || age > 120) throw new IllegalArgumentException("Age must be 1-120");
        if (course == null || course.isBlank()) throw new IllegalArgumentException("Course is required");
    }

    private void sendJson(HttpServletResponse resp, List<Student> students)
            throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            if (i > 0) json.append(",");
            json.append("{")
                .append("\"id\":").append(s.getId()).append(",")
                .append("\"name\":\"").append(escape(s.getName())).append("\",")
                .append("\"age\":").append(s.getAge()).append(",")
                .append("\"course\":\"").append(escape(s.getCourse())).append("\"")
                .append("}");
        }
        json.append("]");
        resp.getWriter().print(json);
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void sendError(HttpServletResponse resp, Exception e)
            throws IOException {
        resp.setStatus(500);
        resp.setContentType("application/json");
        resp.getWriter().print("{\"error\":\"" + escape(e.getMessage() == null ? "Server error" : e.getMessage()) + "\"}");
    }
}
