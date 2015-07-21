
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.faces.bean.ManagedBean;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

@ManagedBean
@SessionScoped
public class Profile {

    private boolean phoneEditable;
    private boolean emailEditable;
    private boolean addressEditable;
    private boolean nokPhoneEditable;
    private boolean nokEmailEditable;
    private boolean nokEditable;
    private boolean bankEditable;
    private boolean accountEditable;

    //allow the server to inject DataSource
    @Resource(name = "jdbc/EmployeePool")
    DataSource dataSource;

    public boolean isEmailEditable() {
        return emailEditable;
    }

    public void setEmailEditable(boolean emailEditable) {
        this.emailEditable = emailEditable;
    }

    public boolean isAddressEditable() {
        return addressEditable;
    }

    public void setAddressEditable(boolean addressEditable) {
        this.addressEditable = addressEditable;
    }

    public boolean isNokPhoneEditable() {
        return nokPhoneEditable;
    }

    public void setNokPhoneEditable(boolean nokPhoneEditable) {
        this.nokPhoneEditable = nokPhoneEditable;
    }

    public boolean isNokEmailEditable() {
        return nokEmailEditable;
    }

    public void setNokEmailEditable(boolean nokEmailEditable) {
        this.nokEmailEditable = nokEmailEditable;
    }

    public boolean isNokEditable() {
        return nokEditable;
    }

    public void setNokEditable(boolean nokEditable) {
        this.nokEditable = nokEditable;
    }

    public boolean isBankEditable() {
        return bankEditable;
    }

    public void setBankEditable(boolean bankEditable) {
        this.bankEditable = bankEditable;
    }

    public boolean isAccountEditable() {
        return accountEditable;
    }

    public void setAccountEditable(boolean accountEditable) {
        this.accountEditable = accountEditable;
    }

    public boolean isPhoneEditable() {
        return phoneEditable;
    }

    public void setPhoneEditable(boolean phoneEditable) {
        this.phoneEditable = phoneEditable;
    }

    //create new instance of the profile object
    public void profiled(Home user) throws SQLException {

        //check if the dataSource was injected
        if (dataSource == null) {
            throw new SQLException("Unable to obtain to DataSource");
        }

        //obtain the connection from the connection pool
        Connection connection = dataSource.getConnection();

        //check if the connection was successful 
        if (connection == null) {
            throw new SQLException("Unabale to connect to DataSource");
        }

        ResultSet result;
        try {

            //PreparedStatement to select the user's information from database
            if (user.isUser()) {
                setIdNumber(user.getUserID());

                String stat = "SELECT * FROM employeepersonalinfo WHERE EmployeeID ='" + getIdNumber() + "'";

                PreparedStatement userInfo = connection.prepareStatement(stat);
                result = userInfo.executeQuery();

                while (result.next()) {
                    //store query results data in fields
                    setIdNumber(result.getObject(1).toString());
                    setFirstName(result.getObject(2).toString());
                    setLastName(result.getObject(3).toString());
                    setDob(result.getObject(4).toString());
                    setEmail(result.getObject(5).toString());
                    setPhone(result.getObject(6).toString());
                    setAddress(result.getObject(7).toString());
                    setStateOfOrigin(result.getObject(8).toString());
                    setLga(result.getObject(9).toString());
                    setNok(result.getObject(10).toString());
                    setNokPhone(result.getObject(11).toString());
                    setNokEmail(result.getObject(12).toString());
                    setEmploymentDate(result.getObject(13).toString());
                    setTitle(result.getObject(14).toString());
                    setDepartment(result.getObject(15).toString());
                    setGender(result.getString("EmployeeGender"));
                    setEmployeeBank(result.getString("EmployeeBank"));
                    setEmployeeAccount(result.getString("EmployeeAccount"));

                }

                //redefine sto store the picture
                //profilePic = result.getbyte(16);
            }//end if                                       
        }//end try
        finally {
            connection.close();
        }//end finally
    }

    //save the edited personal info
    public void savePersonal() throws SQLException {

        //check if the dataSource was injected
        if (dataSource == null) {
            throw new SQLException("Unable to obtain to DataSource");
        }

        //obtain the connection from the connection pool
        Connection connection = dataSource.getConnection();

        //check if the connection was successful 
        if (connection == null) {
            throw new SQLException("Unabale to connect to DataSource");
        }

        try {

            //PreparedStatement to store the user's information to database
            PreparedStatement userInfo = connection.prepareStatement("update employeepersonalinfo set EmployeeEMail= ?,"
                    + "EmployeePhone=?,EmployeeAddress=? where EmployeeID =?");

            userInfo.setString(1, getEmail());
            userInfo.setString(2, getPhone());
            userInfo.setString(3, getAddress());
            userInfo.setString(4, getIdNumber());

            userInfo.executeUpdate();
        } finally {
            setPhoneEditable(false);
            setEmailEditable(false);
            setAddressEditable(false);

            connection.close();
        }
    }

    //save edited emergency info
    public void saveEmergency() throws SQLException {

        //check if the dataSource was injected
        if (dataSource == null) {
            throw new SQLException("Unable to obtain to DataSource");
        }

        //obtain the connection from the connection pool
        Connection connection = dataSource.getConnection();

        //check if the connection was successful 
        if (connection == null) {
            throw new SQLException("Unabale to connect to DataSource");
        }

        try {

            //PreparedStatement to store the user's information to database
            PreparedStatement userInfo = connection.prepareStatement("update employeepersonalinfo set"
                    + " EmployeeNextofKin=?, EmployeeNOKEMail=?, EmployeeNOKPhone=? where EmployeeID =?");

            userInfo.setString(1, getNok());
            userInfo.setString(2, getNokEmail());
            userInfo.setString(3, getNokPhone());
            userInfo.setString(4, getIdNumber());

            userInfo.executeUpdate();

        } finally {
            setNokEditable(false);
            setNokEmailEditable(false);
            setNokPhoneEditable(false);
            connection.close();
        }//end finally
    }

     //save edited bank info
    public void saveBank() throws SQLException {

        //check if the dataSource was injected
        if (dataSource == null) {
            throw new SQLException("Unable to obtain to DataSource");
        }

        //obtain the connection from the connection pool
        Connection connection = dataSource.getConnection();

        //check if the connection was successful 
        if (connection == null) {
            throw new SQLException("Unabale to connect to DataSource");
        }

        try {

            //PreparedStatement to store the user's information to database
            PreparedStatement userInfo = connection.prepareStatement("update employeepersonalinfo set"
                    + " EmployeeBank=?, EmployeeAccount=? where EmployeeID =?");

            userInfo.setString(1, getEmployeeBank());
            userInfo.setString(2, getEmployeeAccount());
            userInfo.setString(3, getIdNumber());

            userInfo.executeUpdate();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Information Saved"));
        } finally {
            setBankEditable(false);
            setAccountEditable(false);
            connection.close();
        }//end finally
        
    }
    
    
    
    private String idNumber;
    private String firstName;
    private String lastName;
    private String dob;
    private String email;
    private String phone;
    private String stateOfOrigin;
    private String lga;
    private String address;
    private String nok;
    private String nokPhone;
    private String nokEmail;
    private String department;
    private String profilePic;
    private String employmentDate;
    private String title;
    private String gender;
    private String employeeBank;
    private String employeeAccount;

    /*setters and getters for all the fields to be stored and retirieved
     * for each employee.
     */
    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNok() {
        return nok;
    }

    public void setNok(String nok) {
        this.nok = nok;
    }

    public String getNokPhone() {
        return nokPhone;
    }

    public void setNokPhone(String nokPhone) {
        this.nokPhone = nokPhone;
    }

    public String getNokEmail() {
        return nokEmail;
    }

    public void setNokEmail(String nokEmail) {
        this.nokEmail = nokEmail;
    }

    public String getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate) {
        this.employmentDate = employmentDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmployeeBank() {
        return employeeBank;
    }

    public void setEmployeeBank(String employeeBank) {
        this.employeeBank = employeeBank;
    }

    public String getEmployeeAccount() {
        return employeeAccount;
    }

    public void setEmployeeAccount(String employeeAccount) {
        this.employeeAccount = employeeAccount;
    }

    public String getProfilePic() throws SQLException {
        setProfilePic();
        return profilePic;
    }

    private void setProfilePic() throws SQLException {

        //check if the dataSource was injected
        if (dataSource == null) {
            throw new SQLException("Unable to obtain to DataSource");
        }

        //obtain the connection from the connection pool
        Connection connection = dataSource.getConnection();

        //check if the connection was successful 
        if (connection == null) {
            throw new SQLException("Unabale to connect to DataSource");
        }

        ResultSet result = null;
        try {

            //PreparedStatement to select the user's information from database
            PreparedStatement userInfo = connection.prepareStatement("SELECT  EmployeePicture FROM profilepicture WHERE EmployeeID =? ");

            userInfo.setString(1, getIdNumber());

            result = userInfo.executeQuery();
            while (result.next()) {
                profilePic = result.getString("EmployeePicture");
            }
        } finally {
            connection.close();
        }//end finally
    }
}
