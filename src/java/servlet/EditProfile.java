package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class EditProfile extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        HashMap userDetails = (HashMap) session.getAttribute("userDetails");
        try {
            String pass = (String) userDetails.get("email");
            String email = request.getParameter("email");
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            String area = request.getParameter("area");
            //For date
            String d = request.getParameter("dob");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dt = sdf.parse(d);
            java.sql.Date dob = new java.sql.Date(dt.getTime());
            //for photo
            

            HashMap newuserDetails = new HashMap();
            newuserDetails.put("email", email);
            newuserDetails.put("name", name);
            newuserDetails.put("phone", phone);
            newuserDetails.put("gender", gender);
            newuserDetails.put("pass", pass);
            newuserDetails.put("state", state);
            newuserDetails.put("city", city);
            newuserDetails.put("area", area);
            newuserDetails.put("dob", dob);
            
            db.DBConnect db = new db.DBConnect();
            String m = db.updateProfile(newuserDetails);
            if (m.equalsIgnoreCase("Success")) {
                
                session.setAttribute("userDetails", newuserDetails);
                response.sendRedirect("profile.jsp");
            } else {
                session.setAttribute("msg", "Update Failed ");
                response.sendRedirect("editprofile.jsp");
            }
        } catch (Exception ex) {
            session.setAttribute("msg", "Update Failed" + ex);
            response.sendRedirect("editprofile.jsp");
        }
    }
}
