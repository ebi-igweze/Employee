
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@ManagedBean
@SessionScoped
public class Home implements Serializable{

    //fields for the login details
    private String userID;
    private String password; //for the user's password
    private boolean loggedIn; //to check if the user is logged in
    private boolean user; //check if the person is a user

    //allow the server to inject DataSource
    @Resource(name = "jdbc/EmployeePool")
    DataSource dataSource;
    
    //set the user ID
    public void setUserID(String userID) {
        this.userID = userID;
    }//end method setUserID

    //get user ID
    public String getUserID() {
        return userID;
    }

    //get user password
    public String getPassword() {
        return password;
    }

    //set the users password
    public void setPassword(String password) {
        this.password = password;
    }//end method set

    //check if the user has logged in
    public boolean isLoggedIn() {
        return loggedIn;
    }//end method isLoggedIn

    //set the value of LoggedIn
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }//end method setLoggedIn

    public void setUser(boolean user) {
        this.user = user;
    }//end method setUser

    public boolean isUser() {
        return user;
    }//end method isUser

    public String login() throws SQLException {
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
            PreparedStatement setSession = connect.prepareStatement("UPDATE userlogin SET EmployeeSession = ? WHERE EmployeeID = ?");
            
            setSession.setString(1, "online");
            setSession.setString(2, getUserID());
            int row = setSession.executeUpdate();
        }
        finally{
            connect.close();
        }
        setLoggedIn(true);
        setUser(true);

        if (userID.equals(password)) {
            return "ChangePassword";
        } else {
            return getPage();//
        }//end else
    }//end method getLogin   

    
    //navigation for the master page
    private String page;

    //return the value of the page
    public String getPage() {
        return page;
    }//end method getPage

    //set the value of the page
    private void setPage(String page) {
        this.page = page;

    }

    //log the user out
    private void reset() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
    }
    
    public String logout()throws SQLException {
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
            PreparedStatement setSession = connect.prepareStatement("UPDATE userlogin SET EmployeeSession = ? WHERE EmployeeID = ?");
            
            setSession.setString(1, "offline");
            setSession.setString(2, getUserID());
            
            int row = setSession.executeUpdate();
        }
        finally{
            connect.close();
        }
        this.reset();
        return "Home";
    }

    //check if the user has logged in before navigation
    public String navigate(int page) {
        /* Navigation Pages
     1 ContactHR
     2 profile
     3 EditProfile
     4 programApply
     5 benefits
     6 myBenefits
     7 skillTraining
     8 technicalSupport
     9 newsEvents
     10 colleagues
     */

        switch (page) {

            case 1:
                setPage("ContactHR");
                break;
            case 2:
                setPage("profile");
                break;
            case 3:
                setPage("EditProfile");
                break;
            case 4:
                setPage("skillTraining");
                break;
            case 5:
                setPage("myBenefits");
                break;
            case 6:
                setPage("benefits");
                break;
            case 7:
                setPage("applyForProgram");
                break;
            case 8:
                setPage("technicalSupport");
                break;
            case 9:
                setPage("peerChatting");
                break;
            case 10:
                setPage("colleagues");
                break;
        }

        if (isLoggedIn()) {
            return getPage();
        } else {
            return "Login";
        }
    }//end method navigate

}
