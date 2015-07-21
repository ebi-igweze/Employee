/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.sql.DataSource;

/**
 *
 * @author WELCOME
 */
@ManagedBean
@RequestScoped
public class Colleagues implements Serializable {

    //fields to store colleagues info
    private String searchKey;
    private String colleagueID;
    private String colleagueFName;
    private String colleagueLName;
    private String colleagueDept;
    private String colleagueSession;
    private boolean online;
    private final List<Colleagues> colleagues = new ArrayList<>();
    private String currentDisplay;

    //allow the server to inject DataSource
    @Resource(name = "jdbc/EmployeePool")
    DataSource dataSource;

    public String colleaguesDisplay(Profile user) throws SQLException {
        //check if datasource injection was successful
        if (dataSource == null) {
            throw new SQLException("Unabale to inject DataSource");
        }

        Connection connect = dataSource.getConnection();

        //check if the connection was successful 
        if (connect == null) {
            throw new SQLException("Unabale to connect to DataSource");
        }
        try {
            PreparedStatement getColleague = connect.prepareStatement("SELECT EmployeeID, EmployeeFName, EmployeeLName, EmployeeDept FROM employeepersonalinfo WHERE EmployeeDept = ?");

            //set the string for the query
            getColleague.setString(1, user.getDepartment());

            ResultSet result = getColleague.executeQuery();

            while (result.next()) {
                boolean present = false;
                 for (Colleagues st : getColleagues()) {
                    //mark present if the colleague's ID is already in the colleagues list
                    //or if the colleague's ID is the same as the user.
                     String id = result.getString("EmployeeID");
                    if (st.colleagueID.equals(result.getString("EmployeeID"))) {
                       present = true;
                    }
                }
                 //if the colleague is not present in the list and the colleague is not 
                 //the same as the user, then add colleague to the colleagues list.
                if (!present && !user.getIdNumber().equals(result.getString("EmployeeID"))){
                    Colleagues col = new Colleagues();
                    col.setColleagueID(result.getString("EmployeeID"));
                    col.setColleagueFName(result.getString("EmployeeFName"));
                    col.setColleagueLName(result.getString("EmployeeLName"));
                    col.setColleagueDept(result.getString("EmployeeDept"));
                    
                    PreparedStatement getSession = connect.prepareStatement("Select EmployeeSession from userlogin WHERE EmployeeID = ?");
                    getSession.setString(1, col.getColleagueID());
                    
                    ResultSet rs = getSession.executeQuery();
                    while(rs.next()){
                        col.setColleagueSession(rs.getString("EmployeeSession"));
                        if(col.getColleagueSession().equals("online")){
                            col.setOnline(true);
                        }//end inner if
                    }//end inner while
                    colleagues.add(col);
                }//end if
            }//end while

        } finally {
            connect.close();
        }
        return null;
    }

    public String colleaguesSearch() throws SQLException {
        //check if datasource injection was successful
        if (dataSource == null) {
            throw new SQLException("Unabale to inject DataSource");
        }

        Connection connect = dataSource.getConnection();

        //check if the connection was successful 
        if (connect == null) {
            throw new SQLException("Unabale to connect to DataSource");
        }
        try {
            PreparedStatement getColleague = connect.prepareStatement("SELECT EmployeeID, EmployeeFName, EmployeeLName, EmployeeDept FROM employeepersonalinfo WHERE "
                    + "EmployeeFName like ? or EmployeeLName like ? order by EmployeeFName, EmployeeLName");

            //set the string for the query
            getColleague.setString(1, (getSearchKey()+"%"));
            getColleague.setString(2, (getSearchKey()+"%"));
            
            ResultSet result = getColleague.executeQuery();

            while (result.next()) {
                boolean present = false;
                for (Colleagues st : getColleagues()) {
                    if (st.colleagueID.equals(result.getString("EmployeeID"))) {
                        present = true;
                    }
                }
                if (!present) {
                    Colleagues col = new Colleagues();
                    col.setColleagueID(result.getString("EmployeeID"));
                    col.setColleagueFName(result.getString("EmployeeFName"));
                    col.setColleagueLName(result.getString("EmployeeLName"));
                    col.setColleagueDept(result.getString("EmployeeDept"));
                    
                    PreparedStatement getSession = connect.prepareStatement("Select EmployeeSession from userlogin WHERE EmployeeID = ?");
                    getSession.setString(1, col.getColleagueID());
                    
                    ResultSet rs = getSession.executeQuery();
                    while(rs.next()){
                        col.setColleagueSession(rs.getString("EmployeeSession"));
                        if(col.getColleagueSession().equals("online")){
                            col.setOnline(true);
                        }//end inner if
                    }//end inner while
                    getColleagues().add(col);
                }//end if
            }//end while

        } finally {
            connect.close();
        }
        return null;
    }
    
    public String colleaguesDisplayed(Profile user) throws SQLException{
        //check if there is a serchKey
        if(searchKey == null || searchKey.equals(""))
        {
            colleaguesDisplay(user); //diplay colleagues if no searchKey
            setCurrentDisplay(user.getDepartment()+" Department");
        }//end if
        else{
            colleaguesSearch(); //else perform search
            setCurrentDisplay("Search for "+getSearchKey());

        }//end else
        return null;
    }//end method colleaguesDisplayed

    public String getColleagueID() {
        return colleagueID;
    }

    public void setColleagueID(String colleagueID) {
        this.colleagueID = colleagueID;
    }

    public String getColleagueFName() {
        return colleagueFName;
    }

    public void setColleagueFName(String colleagueFName) {
        this.colleagueFName = colleagueFName;
    }

    public String getColleagueLName() {
        return colleagueLName;
    }

    public void setColleagueLName(String colleagueLName) {
        this.colleagueLName = colleagueLName;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<Colleagues> getColleagues() {
        return colleagues;
    }

    public String getCurrentDisplay() {
        return currentDisplay;
    }

    public void setCurrentDisplay(String currentDisplay) {
        this.currentDisplay = currentDisplay;
    }

    public String getColleagueDept() {
        return colleagueDept;
    }

    public void setColleagueDept(String colleagueDept) {
        this.colleagueDept = colleagueDept;
    }

    public String getColleagueSession() {
        return colleagueSession;
    }

    public void setColleagueSession(String colleagueSession) {
        this.colleagueSession = colleagueSession;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
