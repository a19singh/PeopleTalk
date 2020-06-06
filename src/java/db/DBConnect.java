package db;

import java.io.InputStream;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBConnect {

    private Connection c;
    private Statement st;
    private PreparedStatement checkLogin, insertUser, searchPeople, getPhoto, getPeopleByEmail, updateProfile,sendMessage;

    public DBConnect() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        c = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "PEOPLETALK", "singh");
        st = c.createStatement();
        checkLogin = c.prepareStatement(
                "select * from userinfo where email=? and pass = ?");
        insertUser = c.prepareStatement(
                "insert into userinfo values(?,?,?,?,?,?,?,?,?,?)");
        updateProfile = c.prepareStatement(
                "update userinfo set phone=?,name=?,gen=?,dob=?,state=?,city=?,area=? where email=?");
        getPhoto = c.prepareStatement(
                "select photo from userinfo where email=?");
        getPeopleByEmail = c.prepareStatement(
                "select * from userinfo where email=?");
        searchPeople = c.prepareStatement(
                "select name,email,gen,dob,phone from userinfo where state=? and city=? and email!=? and area like ?");
        sendMessage=c.prepareStatement(
                "insert into peoplemsg  (sid,rid,msg,filename,ufile,udate) values (?,?,?,?,?,now())");
    }
    public String sendMessage(String s,String r,String m,String f,java.io.InputStream in) throws SQLException {        
        sendMessage.setString(1, s);
        sendMessage.setString(2, r);
        sendMessage.setString(3, m);
        sendMessage.setString(4, f);
        sendMessage.setBinaryStream(5, in);
        int x=sendMessage.executeUpdate();
        if(x==1)
         return "Done";
        else 
         return "Error";
    }

    public HashMap checkLogin(String e, String p) throws SQLException {
        checkLogin.setString(1, e);
        checkLogin.setString(2, p);
        ResultSet rs = checkLogin.executeQuery();
        if (rs.next()) {
            HashMap userDetails = new HashMap();
            userDetails.put("email", rs.getString("email"));
            userDetails.put("name", rs.getString("name"));
            userDetails.put("phone", rs.getString("phone"));
            userDetails.put("gender", rs.getString("gen"));
            userDetails.put("dob", rs.getDate("dob"));
            userDetails.put("state", rs.getString("state"));
            userDetails.put("city", rs.getString("city"));
            userDetails.put("area", rs.getString("area"));
            return userDetails;
        } else {
            return null;
        }

    }

    public String insertUser(HashMap userDetails) throws SQLException {
        try {
            insertUser.setString(1, (String) userDetails.get("email"));
            insertUser.setString(2, (String) userDetails.get("pass"));
            insertUser.setString(3, (String) userDetails.get("name"));
            insertUser.setString(4, (String) userDetails.get("phone"));
            insertUser.setString(5, (String) userDetails.get("gender"));
            insertUser.setDate(6, (java.sql.Date) userDetails.get("dob"));
            insertUser.setString(7, (String) userDetails.get("state"));
            insertUser.setString(8, (String) userDetails.get("city"));
            insertUser.setString(9, (String) userDetails.get("area"));
            insertUser.setBinaryStream(10, (InputStream) userDetails.get("photo"));

            int x = insertUser.executeUpdate();
            out.print(x);
            if (x != 0) {
                return "Success";

            } else {
                return "Failed";
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
            return "Already";
        }
    }

    public byte[] getPhoto(String e) {
        try {
            getPhoto.setString(1, e);
            ResultSet rs = getPhoto.executeQuery();
            if (rs.next()) {
                byte[] b = rs.getBytes("photo");
                if (b.length != 0) {
                    return b;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public java.util.ArrayList<java.util.HashMap> searchPeople(
            String s, String c, String e, String a) throws SQLException {
        searchPeople.setString(1, s);
        searchPeople.setString(2, c);
        searchPeople.setString(3, e);
        searchPeople.setString(4, "%" + a + "%");
        ResultSet rs = searchPeople.executeQuery();
        java.util.ArrayList<java.util.HashMap> allUserDetails
                = new java.util.ArrayList();
        while (rs.next()) {
            java.util.HashMap userDetails = new java.util.HashMap();
            userDetails.put("email", rs.getString("email"));
            userDetails.put("name", rs.getString("name"));
            userDetails.put("phone", rs.getString("phone"));
            userDetails.put("gender", rs.getString("gen"));
            userDetails.put("dob", rs.getString("dob"));
            allUserDetails.add(userDetails);
        }
        return allUserDetails;
    }

    public HashMap getPeopleByEmail(String e) throws SQLException {
        try {

            getPeopleByEmail.setString(1, e);
            ResultSet rs = getPeopleByEmail.executeQuery();
            if (rs.next()) {
                HashMap userDetails = new HashMap();
                userDetails.put("email", rs.getString("email"));
                userDetails.put("name", rs.getString("name"));
                userDetails.put("phone", rs.getString("phone"));
                userDetails.put("gender", rs.getString("gen"));
                userDetails.put("dob", rs.getDate("dob"));
                userDetails.put("state", rs.getString("state"));
                userDetails.put("city", rs.getString("city"));
                userDetails.put("area", rs.getString("area"));
                return userDetails;
            } else {
                return null;
            }
        } catch (Exception ex) {
            
            return null;
        }
    }

    public String updateProfile(HashMap userDetails) throws SQLException {
        try {
            updateProfile.setString(1, (String) userDetails.get("phone"));
            updateProfile.setString(2, (String) userDetails.get("name"));
            updateProfile.setString(3, (String) userDetails.get("gender"));
            updateProfile.setDate(6, (java.sql.Date) userDetails.get("dob"));
            updateProfile.setString(7, (String) userDetails.get("state"));
            updateProfile.setString(8, (String) userDetails.get("city"));
            updateProfile.setString(9, (String) userDetails.get("area"));
            

            int x = updateProfile.executeUpdate();
            if (x != 0) {
                return "Success";

            } else {
                return "Failed";
            }
        } catch (Exception ex) {
            return "Failed";
        }
    }

}

   