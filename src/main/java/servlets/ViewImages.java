package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Servlet implementation class ViewImages
 */
public class ViewImages extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewImages() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream outputStream = null;
		InputStream is = null;
		try {
			String file_name = request.getParameter("file_name");
			String file_path = System.getProperty("user.home")+"\\Documents\\zkart\\images\\"+file_name;
			String file_type = "image/"+file_name.substring(file_name.lastIndexOf(".")+1);
			response.setContentType(file_type);
			response.setHeader("Content-Disposition", "attachemnt; filename=\"file\"");
			outputStream = response.getOutputStream();
			byte[] array = new byte[1024*4];
			is = getImageInputStream(file_path);
			while(true) {
				int bytesRead = is.read(array);
				if(bytesRead == -1) {
					break;
				}
				outputStream.write(array, 0, bytesRead);
			}
		}catch(Exception e) {
			
		}
		finally {
			if(is != null) {
				is.close();
			}
			if(outputStream != null) {
				outputStream.close();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	protected InputStream getImageInputStream(String file_path) {
		InputStream is = null;
		try {
			File imageFile = new File(file_path);
			is = new FileInputStream(imageFile);
		}catch(Exception e) {
			
		}
		return is;
	}

}
