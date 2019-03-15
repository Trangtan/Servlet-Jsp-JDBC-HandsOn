import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.security.Permission;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class StudentDBUtil {
    private DataSource dataSource = null;
    StudentDBUtil(DataSource theDataSource){
        dataSource = theDataSource;
    }


    public List<Student> getStudents() throws Exception {
        List<Student> students = new ArrayList<Student>();
        Connection conn = null;
        Statement statement = null;
        ResultSet res = null;
        conn = dataSource.getConnection();
        String sql = "select * from student order by last_name";
        statement = conn.createStatement();
        res = statement.executeQuery(sql);
        while (res.next()) {
            String firstName = res.getString("first_name");
            String lastName = res.getString("last_name");
            String email = res.getString("email");
            Student student = new Student(firstName, lastName, email);

            students.add(student);
        }
        return students;

    }



    public void addStudent(Student student) throws Exception {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        conn = dataSource.getConnection();
        String sql = "insert into student (first_name, last_name, email) " + " values (?, ?, ?) ";
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, student.getFirstName());
        preparedStatement.setString(2, student.getLastName());
        preparedStatement.setString(3, student.getEmail());
        preparedStatement.execute();
    }


    public void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        conn = dataSource.getConnection();
        String sql = "update student "
                + "set first_name=?, last_name=?, email=? "
                + "where id=?";
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, request.getParameter("fistName"));
        preparedStatement.setString(2, request.getParameter("lastName"));
        preparedStatement.setString(3, request.getParameter("email"));
        preparedStatement.setInt(4, Integer.parseInt(request.getParameter("studentId")));
        preparedStatement.execute();


    }

    public Student findStudent(int id) throws Exception {
        Connection conn;
        Student student = null;
        PreparedStatement preparedStatement = null;
        ResultSet res = null;
        conn = dataSource.getConnection();
        String sql = "select * from student where id = ?";

        preparedStatement = conn.prepareStatement(sql);

        preparedStatement.setInt(1, id);
        res = preparedStatement.executeQuery();
        if (res.next()) {
        String firstName = res.getString("first_name");
        String lastName = res.getString("last_name");
        String email = res.getString("email");
        student = new Student(firstName, lastName, email);
        }
        return student;
    }
}
