import javax.annotation.Resource;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {

    StudentDBUtil studentDBUtil;
    @Resource(name = "web_student_tracker")
    private DataSource dataSource = null;

    @Override
    public void init() throws ServletException {
        super.init();
        studentDBUtil = new StudentDBUtil(dataSource);

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String command = request.getParameter("command");
            if(command == null){
                command = "LIST";
            }
            switch (command){
                case "UPDATE":
                    updateStudent(request, response);
                    break;
                case "LOAD":
                    findStudent(request, response);
                    break;
                case "LIST":
                    listStudents(request, response);
                    break;
                case "ADD":
                    addStudent(request, response);
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        studentDBUtil.updateStudent(request, response);
    }

    private void findStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("studentId"));
        Student student;
        student = studentDBUtil.findStudent(id);
        request.setAttribute("THE_STUDENT", student);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/updateTable.jsp");
        dispatcher.forward(request, response);
    }

    private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        Student student = new Student(firstName, lastName, email);
        studentDBUtil.addStudent(student);

        listStudents(request, response);
    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Student> students = new ArrayList<Student>();
        students = studentDBUtil.getStudents();
        request.setAttribute("LIST", students);
        RequestDispatcher dispacher = request.getRequestDispatcher("./index.jsp");
        dispacher.forward(request, response);
    }
}
