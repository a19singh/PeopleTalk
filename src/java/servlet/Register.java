package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@MultipartConfig
public class Register extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {

            String email = request.getParameter("email");
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            String area = request.getParameter("area");
            String pass = request.getParameter("password");
            //For date
            String d = request.getParameter("dob");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dt = sdf.parse(d);
            java.sql.Date dob = new java.sql.Date(dt.getTime());
            //for photo
            Part p = request.getPart("photo");
            InputStream in = null;
            if (p != null) {
                in = p.getInputStream();
            }

            HashMap userDetails = new HashMap();
            userDetails.put("email", email);
            userDetails.put("name", name);
            userDetails.put("phone", phone);
            userDetails.put("gender", gender);
            userDetails.put("pass", pass);
            userDetails.put("state", state);
            userDetails.put("city", city);
            userDetails.put("area", area);
            userDetails.put("dob", dob);
            userDetails.put("photo", in);
            db.DBConnect db = new db.DBConnect();
            String m = db.insertUser(userDetails);
            if (m.equalsIgnoreCase("Success")) {
                userDetails.remove(dob);
                userDetails.remove(p);
                session.setAttribute("userDetails", userDetails);
                response.sendRedirect("home.jsp");
            } else if (m.equalsIgnoreCase("Already")) {
                session.setAttribute("msg", "Email already exists");
                response.sendRedirect("home.jsp");
            } else {
                session.setAttribute("msg", "Registration Failed ");
                response.sendRedirect("home.jsp");
            }
        } catch (Exception ex) {
            session.setAttribute("msg", "Registration Failed"+ex);
            response.sendRedirect("home.jsp");
        }

    }

}
