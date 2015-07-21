/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.javafx.tk.Toolkit;
import java.io.BufferedOutputStream;
import javax.faces.bean.RequestScoped;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.model.CroppedImage;

@ManagedBean
@RequestScoped
public class CropperView {

    private CroppedImage croppedImage;

    private String newImageName;

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }

    public void crop() throws FileNotFoundException, IOException {
        if (croppedImage == null) {
            return;
        }//end if

        setNewImageName("new1");
        String picDirectory = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/userProfilePictures");
        BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(picDirectory + "/new1.jpg"));
        try {
            byte[] buffer = croppedImage.getBytes();//create a new byte array to take the cropped pictures

            //write the cropped image to the specified directory
            bw.write(buffer);
            bw.flush();//clear buffer
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cropping failed."));
        } finally {
            bw.close(); //close the buffer writer
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Cropping finished."));
    }

    public String getNewImageName() {
        return newImageName;
    }

    public void setNewImageName(String newImageName) {
        this.newImageName = newImageName;
    }
    
    
    public void downloadClicked()throws IOException{
        download();
    }

    private void download() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String picDirectory = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/profilePictures");
        File downloadFile = new File(picDirectory + "/profile1.jpg");
        String fileName = "profile.jpg";
        int fileLength = (int) downloadFile.length();
        InputStream is = new FileInputStream(picDirectory + "/profile.jpg");//get the download file inputStream

        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        ec.setResponseContentType(ec.getMimeType(fileName)); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
        ec.setResponseContentLength(fileLength); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

        OutputStream output = ec.getResponseOutputStream();
        // Now you can write the InputStream of the file to the above OutputStream the usual way.
        // ...
        try {
            byte[] buffer = new byte[4096]; //4kb buffer
            int readLength;

            //file 1
            while ((readLength = is.read(buffer)) != -1) {
                output.write(buffer, 0, readLength);
                output.flush();  //clearBuffer(buffer);
            }
        } finally {//close streams
            is.close();
            output.close();
        }//end finally
        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }
}
