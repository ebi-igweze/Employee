

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.sql.DataSource;

@FacesValidator(value = "signIN")
public class LoginValidator implements Validator {

    //allow the server to inject DataSource
    @Resource(name = "jdbc/EmployeePool")
    DataSource dataSource;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        // Obtain the client ID of the first field from f:attribute.
        System.out.println(component.getFamily());
        String userID = (String) component.getAttributes().get("userID");

        // Find the actual JSF component for the client ID.
        UIInput textInput = (UIInput) context.getViewRoot().findComponent(userID);
        if (textInput == null) {
            throw new IllegalArgumentException(String.format("Unable to find component with id %s", userID));
        }
        // Get its value, the entered text of the first field.
        String ID = (String) textInput.getValue();

        // Cast the value of the entered text of the second field back to String.
        String Password = (String) value;

        Connection connect = null;
        //check if the login credentials are correct
        try {
            //check if datasource injection was successful
            if (dataSource == null) {
                throw new SQLException("Unabale to inject DataSource");
            }

            connect = dataSource.getConnection();

            //check if the connection was successful 
            if (connect == null) {
                throw new SQLException("Unabale to connect to DataSource");
            }

            PreparedStatement check = connect.prepareStatement("select * from userlogin where EmployeeID = ? and EmployeePassword = ?");
            check.setString(1, ID);
            check.setString(2, Password);
            
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new ValidatorException(new FacesMessage("invalid ID or password"));
            }//end if statement  
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            try{connect.close();}
            catch (SQLException e) {
            e.getMessage();
        } 
        }
    }
}
