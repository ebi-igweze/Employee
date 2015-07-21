/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.sun.net.httpserver.HttpContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author WELCOME
 */
@ManagedBean(name="fileUpload")
@SessionScoped
public class FileUpload {

    protected Part file1;
    protected Part file2;
    
    public String upload()throws IOException{
//        String pixLocation = System.getProperty("user.dir") + System.getProperty("file.separator");
//        String fileLocation1 = pixLocation +"profile_picture" + System.getProperty("file.separator") + "file1.jpg";
//        String fileLocation2 = pixLocation +"profile_picture" + System.getProperty("file.separator") + "file2.jpg";
//        System.out.println("fileLocation1>>>>>>>>>>"+fileLocation1);
//        System.out.println("fileLocation2>>>"+fileLocation2);
        FacesContext  fc = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) fc.getExternalContext();
        System.out.println(sc.getRealPath("."));
//        file1.write("profile_picture/file1.jpg");
//        file2.write("profile_picture/file1.jpg");
        return "success";
    }

    public Part getFile1() {
        return file1;
    }

    public void setFile1(Part file1) {
        this.file1 = file1;
    }

    public Part getFile2() {
        return file2;
    }

    public void setFile2(Part file2) {
        this.file2 = file2;
    }
    private static String getFilename(Part part){
        for(String cd : part.getHeader("content-disposition").split(";")){
            if(cd.trim().startsWith("filename")){
                String filename = cd.substring(cd.indexOf('=')+1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/')+1).substring(filename.lastIndexOf('\\')+1);
                
            }
            
        }
        return null;
    }
    
//    public String handleImageFileUpload(FileUploadEvent event){
//
//    File file = new File("PATH_TO_UPLOAD_DIR");
//    file.mkdirs();
//    file = new File("PATH_TO_UPLOAD_FILE");
//
//    try( InputStream is = event.getFile().getInputstream();
//             OutputStream out = new FileOutputStream(file)  ) {
//
//            BufferedImage img = ImageIO.read(is);
//            BufferedImage scaledImg;
//            if(img.getWidth() >= img.getHeight())
//                scaledImg = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 300, 400);
//            else
//                scaledImg = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, 400, 300);
//            ImageIO.write(scaledImg, "jpg", out);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//}
//    
}
