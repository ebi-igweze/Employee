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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

/**
 *
 * @author WELCOME
 */
@ManagedBean
@SessionScoped
public class SkillTraining implements Serializable {

    private List<SkillTrainings> trainings = new ArrayList<>();
    private List<Qualification> qualifications = new ArrayList<>();

    @Resource(name = "jdbc/EmployeePool")
    DataSource dataSource;

    //add a new set of qualification boxes
    public String addQualification() {

        Qualification qualification = new Qualification();
        qualification.setQualificationID("");
        qualification.setInstitutionAttended("");
        qualification.setQualificationAttained("");
        qualification.setYearOfAttendance("");
        qualification.setEditable(true);
        qualifications.add(qualification);
        return null;
    }//end method addQualification

    //remove the added qualification
    public void removeQualification(Qualification qualification) {
        qualifications.remove(qualification);
    }//end method removeQualification

    public void editQualification(Qualification qualification) {
        qualification.setEditable(true);
    }//end method editQualification

    //add a new set of training boxes
    public String addTraining() {

        SkillTrainings trainner = new SkillTrainings();
        trainner.setTrainingID("");
        trainner.setTrainingCourse("");
        trainner.setTrainingInstitution("");
        trainner.setStartDate("");
        trainner.setEndDate("");
        trainner.setEditable(true);
        trainings.add(trainner);
        return null;
    }//end method addTraining

    //remove the added training
    public void removeTraining(SkillTrainings training) {
        trainings.remove(training);
    }//end method removeTraining

    //edit training
    public void editAction(SkillTrainings training) {
        training.setEditable(true);
    }//end method editAction

    //save the newly added qualifaction boxes
    public void saveQualification(Home user, Qualification qualification) throws SQLException {

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

            //first check if the user is just editing or is inserting a new qualification.
            if (qualification.qualificationID.equals("")) {//if its a new qualification, then perform insertion

                String insert = "INSERT INTO educationalqualification(InstitutionAttended, "
                        + "QualificationAttained, YearOfAttendance, EmployeeID) VALUES ('" + qualification.getInstitutionAttended() + "','"
                        + qualification.getQualificationAttained() + "','" + qualification.getYearOfAttendance() + "','" + user.getUserID() + "')";
                //PreparedStatement to insert the user's information to database
                PreparedStatement userInfo = connection.prepareStatement(insert);

//                userInfo.setString(1, qualification.getInstitutionAttended());
//                userInfo.setString(2,qualification.getQualificationAttained());
//                userInfo.setString(3,qualification.getYearOfAttendance());
//                userInfo.setString(4,user.getUserID());
                int row = userInfo.executeUpdate();
                if (row > 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Taining saved successfully"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Training save failed"));
                }
            } else {//else if it already exists, perform an update

                //PreparedStatement to update the user's information in database
                PreparedStatement userInfo = connection.prepareStatement("UPDATE educationalqualification SET "
                        + "InstitutionAttended = ?,QualificationAttained = ?,"
                        + "YearOfAttendance = ?,EmployeeID=? WHERE QualificationID = ?");

                //specify the prepared starement's argument
                userInfo.setString(1, qualification.getInstitutionAttended());
                userInfo.setString(2, qualification.getQualificationAttained());
                userInfo.setString(3, qualification.getYearOfAttendance());
                userInfo.setString(4, user.getUserID());
                userInfo.setString(5, qualification.getQualificationID());

                int row = userInfo.executeUpdate();
                if (row > 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Taining saved successfully"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Training save failed"));
                }

            }

        } finally {
            qualification.setEditable(false);
            connection.close();
        FacesContext.getCurrentInstance().addMessage("form1:saveQualification", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Training save failed"));

        }

    }

    //save the newly added training boxes
    public void saveActionTraining(Home user, SkillTrainings training) throws SQLException {

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

            //first check if the user is just editing or is inserting a new training.
            if (training.trainingID.equals("")) {//if its a new training, then perform insertion

                //PreparedStatement to insert the user's information to database
                PreparedStatement userInfo = connection.prepareStatement("INSERT INTO employeeskilltraining(TrainingCourse, TrainingInstitution,"
                        + " StartDate, EndDate, TrainingCertificate, EmployeeID) VALUES (?,?,?,?,?,?)");

                //specify the prepared starement's argument
                userInfo.setString(1, training.getTrainingCourse());
                userInfo.setString(2, training.getTrainingInstitution());
                userInfo.setString(3, training.getStartDate());
                userInfo.setString(4, training.getEndDate());
                userInfo.setString(5, training.getTrainingCertificate());
                userInfo.setString(6, user.getUserID());

                int row = userInfo.executeUpdate();
                if (row > 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Taining saved successfully"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Training save failed"));
                }

            } else {//if it already exists, perform an update

                //PreparedStatement to update the user's information in database
                PreparedStatement userInfo = connection.prepareStatement("Update employeeskilltraining set TrainingCourse = ?, TrainingInstitution = ?,"
                        + " StartDate = ?, EndDate = ?, TrainingCertificate = ?, EmployeeID = ? where TrainingID = ?");

                //specify the prepared starement's argument
                userInfo.setString(1, training.getTrainingCourse());
                userInfo.setString(2, training.getTrainingInstitution());
                userInfo.setString(3, training.getStartDate());
                userInfo.setString(4, training.getEndDate());
                userInfo.setString(5, training.getTrainingCertificate());
                userInfo.setString(6, user.getUserID());
                userInfo.setString(7, training.getTrainingID());

                int row = userInfo.executeUpdate();
                if (row > 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Taining saved successfully"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Training save failed"));
                }
            }

        } finally {
            training.setEditable(false);
            connection.close();
        }
        FacesContext.getCurrentInstance().addMessage("form2:saveTraining", new FacesMessage("Error: Your password is NOT strong enough."));

    }

    public List<SkillTrainings> getTrainings() {
        return trainings;
    }

    public List<Qualification> getQualifications() {
        return qualifications;
    }

    public void getUserTrainingEducation(Home user) throws SQLException {

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

            //PreparedStatement to select the user's training from database
            PreparedStatement userTraining = connection.prepareStatement("SELECT TrainingID, TrainingCourse, TrainingInstitution, StartDate, EndDate, TrainingCertificate FROM employeeskilltraining WHERE EmployeeID =?");

            //PreparedStatement to select user's Qualification from databse
            PreparedStatement userQualification = connection.prepareStatement("SELECT QualificationID, InstitutionAttended, QualificationAttained, YearOfAttendance FROM educationalqualification WHERE EmployeeID = ? ");

            //specify the prepared starement's argument
            userTraining.setString(1, user.getUserID());
            userQualification.setString(1, user.getUserID());

            //decalre resultset to store the users information
            ResultSet resultSetTraining = userTraining.executeQuery();
            ResultSet resultSetQualification = userQualification.executeQuery();

            while (resultSetQualification.next()) {
                boolean present = false;

                //qualifications is empty, then add the new one from the result set
                if (qualifications.isEmpty()) {
                    qualifications.add(new Qualification(resultSetQualification.getString("QualificationID"), resultSetQualification.getString("InstitutionAttended"),
                            resultSetQualification.getString("QualificationAttained"), resultSetQualification.getString("YearOfAttendance")));
                } else {
                    //else loop and check if the qualification already exists in the list of qualifications
                    for (Qualification st : qualifications) {
                        //remove the qulification if it has no qulificationID
                        if (st.getQualificationID().equals("")) {
                            qualifications.remove(st); //if the qualification has no ID, then remove it
                            break;
                        }
                        //mark present if the qualificationID is already in qualifications list
                        if (st.getQualificationID().equals(resultSetQualification.getString("QualificationID"))) {
                            present = true;
                            break;
                        }
                    }//end for
                    if (!present) {//add qualification if the qualification is not in the qualification list.
                        qualifications.add(new Qualification(resultSetQualification.getString("QualificationID"), resultSetQualification.getString("InstitutionAttended"),
                                resultSetQualification.getString("QualificationAttained"), resultSetQualification.getString("YearOfAttendance")));
                    }//end if
                }//end else
            }//end while

            while (resultSetTraining.next()) {
                boolean present = false;

                if (trainings.isEmpty()) {
                    trainings.add(new SkillTrainings(resultSetTraining.getString("TrainingID"), resultSetTraining.getString("TrainingCourse"),
                            resultSetTraining.getString("TrainingInstitution"), resultSetTraining.getString("StartDate"), resultSetTraining.getString("EndDate"),
                            resultSetTraining.getString("TrainingCertificate")));
                } else {

                    //loop and check if the training already exists in the list of trainings
                    for (SkillTrainings st : trainings) {
                        //remove the training if it has no trainingID
                        if (st.getTrainingID().equals("")) {
                            trainings.remove(st);
                            break;
                        }
                        //mark present if the trainingID is already in trainings list
                        if (st.getTrainingID().equals(resultSetTraining.getString("TrainingID"))) {
                            present = true;
                            break;
                        }
                    }
                    if (!present) {
                        trainings.add(new SkillTrainings(resultSetTraining.getString("TrainingID"), resultSetTraining.getString("TrainingCourse"),
                                resultSetTraining.getString("TrainingInstitution"), resultSetTraining.getString("StartDate"), resultSetTraining.getString("EndDate"),
                                resultSetTraining.getString("TrainingCertificate")));
                    }
                }
            }
        } finally {
            connection.close();
        }//end finally
    }

    public class SkillTrainings {

        //fields to store employee trainng program
        private String trainingID;
        private String trainingCourse;
        private String trainingInstitution;
        private String startDate;
        private String endDate;
        private String trainingCertificate;
        private boolean editable;

        public SkillTrainings(String trainingID, String trainingCourse, String trainingInstitution,
                String startDate, String endDate, String trainingCertificate) {
            this.trainingID = trainingID;
            this.trainingCourse = trainingCourse;
            this.trainingInstitution = trainingInstitution;
            this.startDate = startDate;
            this.endDate = endDate;
            this.trainingCertificate = trainingCertificate;
            this.editable = false;

        }

        public SkillTrainings() {
        }

        public String getTrainingID() {
            return trainingID;
        }

        public void setTrainingID(String trainingID) {
            this.trainingID = trainingID;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        public String getTrainingCourse() {
            return trainingCourse;
        }

        public void setTrainingCourse(String trainingCourse) {
            this.trainingCourse = trainingCourse;
        }

        public String getTrainingInstitution() {
            return trainingInstitution;
        }

        public void setTrainingInstitution(String trainingInstitution) {
            this.trainingInstitution = trainingInstitution;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getTrainingCertificate() {
            return trainingCertificate;
        }

        public void setTrainingCertificate(String trainingCertificate) {
            this.trainingCertificate = trainingCertificate;
        }

    }

    public class Qualification {

        private String qualificationID;
        private String institutionAttended;
        private String qualificationAttained;
        private String yearOfAttendance;
        private boolean editable;

        public Qualification() {
        }

        public Qualification(String qualificationID, String institutionAttended, String qualificationAttained, String yearOfAttendance) {

            this.qualificationID = qualificationID;
            this.institutionAttended = institutionAttended;
            this.qualificationAttained = qualificationAttained;
            this.yearOfAttendance = yearOfAttendance;
            this.editable = false;
        }

        public String getQualificationID() {
            return qualificationID;
        }

        public void setQualificationID(String qualificationID) {
            this.qualificationID = qualificationID;
        }

        public String getQualificationAttained() {
            return qualificationAttained;
        }

        public void setQualificationAttained(String qualificationAttained) {
            this.qualificationAttained = qualificationAttained;
        }

        public String getInstitutionAttended() {
            return institutionAttended;
        }

        public void setInstitutionAttended(String institutionAttended) {
            this.institutionAttended = institutionAttended;
        }

        public String getYearOfAttendance() {
            return yearOfAttendance;
        }

        public void setYearOfAttendance(String yearOfAttendance) {
            this.yearOfAttendance = yearOfAttendance;
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

    }
}
