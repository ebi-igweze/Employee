/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author WELCOME
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "webTimeBean")
@RequestScoped
public class WebTimeBean {

    public final String DRIVER = "com.mysql.jdbc.Driver";
    public final String DATABASE_URL = "jdbc:mysql://localhost:3306/employee";
    private String fromUserID;
    private String userSession;
    private String ChatMessage;
    private String toUserID;
    private String userName;
    private String timeStamp;
    private final List<Chat> displayMessages = new ArrayList<>();

    //return the time to the server at which the request was recieved
    public String getTime() {
        timeStamp = DateFormat.getTimeInstance(DateFormat.LONG).format(new Date());
        return timeStamp;
    }//end method getTime

    public String chat(Colleagues colleague)throws SQLException, ClassNotFoundException
    {
        setUserName(colleague.getColleagueFName());
        setToUserID(colleague.getColleagueID());
        disPlay();
         //return the chat page for chatting with the clicked colleague
        return "peerChatting";
    }
    
    public void disPlay() throws SQLException, ClassNotFoundException {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, "ebube", "ebube");

            PreparedStatement st = connection.prepareStatement("SELECT chatID,fromUserID,toUserID, chatMessage,timeStamp FROM chat Where (fromUserID =? and  toUserID = ?) or "
                    + "(toUserID = ? and fromUserID=?)");
            st.setString(1,getFromUserID());
            st.setString(2,getToUserID());
            st.setString(3,getFromUserID());
            st.setString(4,getToUserID());
            ResultSet result = st.executeQuery();
            
            while(result.next())
            {
//                setToUserID(result.getString("toUserID"));
                
                Chat ch = new Chat(result.getString("chatMessage"),result.getString("chatID"),result.getString("timeStamp"));
                if(getFromUserID().equals(result.getString("fromUserID"))){  //check if the current message is from the user
                    ch.setFromUser(true);
                }//end if
                displayMessages.add(ch);
            }//end while
//            //get the name of the user to chat with
//            PreparedStatement uName = connection.prepareStatement("select EmployeeFName from employeepersonalinfo where EmployeeID = ?");
//            //set the values of the prepared statement
//            uName.setString(1, getToUserID());
//            
//            result = uName.executeQuery();
//            
//            while(result.next())
//            {
//                setUserName(result.getString("EmployeeFName"));
//            }//end while loop
        }//end try
        finally {
            connection.close();
        }
    }

    public void send() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, "ebube", "ebube");

            //decalre an integer to store the updated rowsResultSet resultSet;
            PreparedStatement st = connection.prepareStatement("INSERT INTO chat(chatMessage, fromUserID, toUserID, timeStamp) VALUES (?,?,?,?)");

            st.setString(1, getChatMessage());
            st.setString(2, getFromUserID());
            st.setString(3, getToUserID());
            st.setString(4, getTime());
            
            int row = st.executeUpdate();

        }//end try
        finally {
            connection.close();
        }
        setChatMessage("");

    }//end method send

    public String getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(Home user) {
        this.fromUserID = user.getUserID();
    }

    public String getUserSession() {
        return userSession;
    }

    public void setUserSession(String userSession) {
        this.userSession = userSession;
    }

    public String getChatMessage() {
        return ChatMessage;
    }

    public void setChatMessage(String ChatMessage) {
        this.ChatMessage = ChatMessage;
    }

    public String getToUserID() {
        return toUserID;
    }

    public void setToUserID(String toUserID) {
        this.toUserID = toUserID;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Chat> getDisplayMessages() {
        return displayMessages;
    }

    public class Chat {

        private String message;
        private String messageID;
        private boolean fromUser; //to check if the current message is from the user
        private String messageTimeStamp;
        
        public Chat() {
        }

        public Chat(String message, String messageID,String messageTimeStamp) {
            this.message = message;
            this.messageID = messageID;
            this.messageTimeStamp = messageTimeStamp;
            setFromUser(false);
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessageID() {
            return messageID;
        }

        public void setMessageID(String messageID) {
            this.messageID = messageID;
        }

        /**
         * @return the fromUser
         */
        public boolean isFromUser() {
            return fromUser;
        }

        /**
         * @param fromUser the fromUser to set
         */
        private void setFromUser(boolean fromUser) {
            this.fromUser = fromUser;
        }

        /**
         * @return the messageTimeStamp
         */
        public String getMessageTimeStamp() {
            return messageTimeStamp;
        }

        /**
         * @param messageTimeStamp the messageTimeStamp to set
         */
        public void setMessageTimeStamp(String messageTimeStamp) {
            this.messageTimeStamp = messageTimeStamp;
        }
        
       
    }
}//end class WebTImeBean
