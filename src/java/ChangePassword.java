/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

/**
 *
 * @author WELCOME
 */
@ManagedBean
@RequestScoped
public class ChangePassword {

    private String userID;
    private String password;
    private String passwordTwo;

    //allow the server to inject DataSource
    @Resource(name="jdbc/EmployeePool")
    DataSource dataSource;

    public String getUserID() {
        return userID;
    }
    
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public void setUserId(Home user) {
        String userId = user.getUserID();
        this.userID = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordTwo() {
        return passwordTwo;
    }

    public void setPasswordTwo(String passwordTwo) {
        this.passwordTwo = passwordTwo;
    }
    
    
    //update the users password in the database
    public String changePassword()throws SQLException{
        //check if the dataSource was injected
        if(dataSource == null)
            throw new SQLException("Unable to obtain to DataSource");
        
        //obtain the connection from the connection pool
        Connection connection = dataSource.getConnection();
        
        //check if the connection was successful 
        if(connection == null)
            throw new SQLException("Unabale to connect to DataSource");
        
        try
        {
            
            //PreparedStatement to update the user's information in the database
            PreparedStatement changeUserPass;
            changeUserPass = connection.prepareStatement("UPDATE userlogin set EmployeePassword = ? where EmployeeID =?");           
            
            
            //specify the prepared starement's argument
            changeUserPass.setString(1, getPassword());
            changeUserPass.setString(2, getUserID());
            
            int update = changeUserPass.executeUpdate();
            
            if(update > 0)
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(" password change successful"));
            return "Home";            
        }
        finally{
            connection.close();   //return the connection to the pool
        }//end finally
            
    }

    
}
