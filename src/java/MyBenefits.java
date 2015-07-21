
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@ManagedBean
@RequestScoped
public class MyBenefits {

    private final List<String> benefits = new ArrayList<>();                 //display available benefits of company
    private String selecteBenefits;                                           //store the value of the requested benefit
    private Part requestFile;                                                 //store the submitted request file
    private final List<Benefits> requestedBenefits = new ArrayList<>();      //store and return the list of already requested benefits
    private String message;                                                   //display ammount of benefits requested
    private boolean requestEmpty;                                             //flag if the user has not requested for any leave
    private String uploadMessage;

    //method to upload
    public void upload(Home user) throws IOException, SQLException {
        String picDirectory = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/BenefitRequests");
        String usersDirectory = picDirectory + "/" + user.getUserID() + "/"; //the directory that contains the users innformation.
        File newFile = new File(usersDirectory);
        BufferedOutputStream bw;

        //if the file doesnt exist create a new file for the users request 
        if (newFile.mkdir()) {
            bw = new BufferedOutputStream(new FileOutputStream(usersDirectory + getFilename(getRequestFile())));
        } else {//else use the existing filepath.
            bw = new BufferedOutputStream(new FileOutputStream(usersDirectory + getFilename(getRequestFile())));
        }
        try (InputStream is = getRequestFile().getInputStream()) {
            byte[] buffer = new byte[4096]; //4kb buffer
            int readLength;

            //file 1
            while ((readLength = is.read(buffer)) != -1) {
                bw.write(buffer, 0, readLength);
                bw.flush();

                //clearBuffer(buffer);
            }

            setUploadMessage("Your request has been submitted");
            String letterUrl = user.getUserID() + "/" + getFilename(getRequestFile());
            requestBenefits(user, letterUrl);
        } finally {
            bw.close();
            setRequestFile(null);
            setSelecteBenefits("--select--");
        }//end finally
    }//end method upload

    //method to get the file name
    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1);

            }

        }
        return null;
    }

    //method to check the benefits of the user.
    public void checkBenefits(Home user) throws SQLException {
        //check if datasource injection was successful
        if (user.dataSource == null) {
            throw new SQLException("Unabale to inject DataSource");
        }

        Connection connect = user.dataSource.getConnection();

        //check if the connection was successful 
        if (connect == null) {
            throw new SQLException("Unabale to connect to DataSource");
        }
        try {
            PreparedStatement setSession = connect.prepareStatement("SELECT EmployeeBenefitsID, BenefitRequested, DateOfRequest, dateOfApproval FROM employeebenefits WHERE EmployeeID = ?");

            setSession.setString(1, user.getUserID());
            ResultSet rs = setSession.executeQuery();

            while (rs.next()) {
                Benefits benefit = new Benefits();
                benefit.setBenefitID(rs.getString("EmployeeBenefitsID"));
                benefit.setBenefit(rs.getString("BenefitRequested"));
                benefit.setDateApproved(rs.getString("dateOfApproval"));
                benefit.setDateRequested(rs.getString("DateOfRequest"));
                requestedBenefits.add(benefit);
            }//end while loop
        } finally {
            connect.close();
        }
        if (requestedBenefits.isEmpty()) {
            message = "You currently have not requested for any Benefits. REQUEST NOW!!!";
            setRequestEmpty(true);
        } else {
            message = "Here are your requests: REQUEST FOR MORE!!!";
            setRequestEmpty(false);
        }//end else

    }//end method check benefits

    public void requestBenefits(Home user, String letterOfRequestUrl) throws SQLException {
        //check if datasource injection was successful
        if (user.dataSource == null) {
            throw new SQLException("Unabale to inject DataSource");
        }

        Connection connect = user.dataSource.getConnection();

        //check if the connection was successful 
        if (connect == null) {
            throw new SQLException("Unabale to connect to DataSource");
        }
        try {
            String currentDate = DateFormat.getDateInstance().format(new Date());
            PreparedStatement benefit = connect.prepareStatement("INSERT INTO employeebenefits(BenefitRequested, EmployeeID, DateOfRequest, dateOfApproval, requestLetter) VALUES (?,?,?,?,?)");

            benefit.setString(1, getSelecteBenefits());
            benefit.setString(2, user.getUserID());
            benefit.setString(3, currentDate);
            benefit.setString(4, "-");
            benefit.setString(5, letterOfRequestUrl);
            int row = benefit.executeUpdate();

        } finally {
            connect.close();
        }
    }//end method requestbenefits

    public List<String> getBenefits() {
        benefits.add("-----select-----");
        benefits.add("Holiday/Vacation");
        benefits.add("Sick Leave");
        benefits.add("Personal Leave");
        benefits.add("Pregnancy Leave");

        return benefits;
    }

    public String getSelecteBenefits() {
        return selecteBenefits;
    }

    public void setSelecteBenefits(String selecteBenefits) {
        this.selecteBenefits = selecteBenefits;
    }

    public Part getRequestFile() {
        return requestFile;
    }

    public void setRequestFile(Part requestFile) {
        this.requestFile = requestFile;
    }

    public List<Benefits> getRequestedBenefits() {
        return requestedBenefits;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRequestEmpty() {
        return requestEmpty;
    }

    public void setRequestEmpty(boolean requestEmpty) {
        this.requestEmpty = requestEmpty;
    }

    /**
     * @return the uploadMessage
     */
    public String getUploadMessage() {
        return uploadMessage;
    }

    /**
     * @param uploadMessage the uploadMessage to set
     */
    private void setUploadMessage(String uploadMessage) {
        this.uploadMessage = uploadMessage;
    }

    public class Benefits {

        private String benefit;
        private String dateRequested;
        private String dateApproved;
        private String benefitID;

        public String getBenefitID() {
            return benefitID;
        }

        public void setBenefitID(String benefitID) {
            this.benefitID = benefitID;
        }

        public String getBenefit() {
            return benefit;
        }

        public void setBenefit(String benefit) {
            this.benefit = benefit;
        }

        public String getDateApproved() {
            return dateApproved;
        }

        public void setDateApproved(String dateApproved) {
            this.dateApproved = dateApproved;
        }

        /**
         * @return the dateRequested
         */
        public String getDateRequested() {
            return dateRequested;
        }

        /**
         * @param dateRequested the dateRequested to set
         */
        public void setDateRequested(String dateRequested) {
            this.dateRequested = dateRequested;
        }
    }

}
