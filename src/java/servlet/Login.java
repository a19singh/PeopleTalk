
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class Login extends HttpServlet {

   
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session=request.getSession();
        try{
            String email=request.getParameter("email");
            String pass=request.getParameter("password");
            db.DBConnect db=new db.DBConnect();
            java.util.HashMap userDetails=db.checkLogin(email, pass);
            if(userDetails!=null)
            {
                session.setAttribute("userDetails",userDetails);
                response.sendRedirect("profile.jsp");
            }
            else{
                session.setAttribute("msg","Invalid Entries!");
                response.sendRedirect("home.jsp");
            }
        }catch(Exception ex){
            session.setAttribute("msg","Login Failed: "+ex);
            response.sendRedirect("home.jsp");
        }
       
    }

    
}
