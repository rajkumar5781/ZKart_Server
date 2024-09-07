package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fetcher.CSVFetcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilities.JwtUtils;

/**
 * Servlet implementation class ClearSession
 */
public class ClearSession extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClearSession() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HashMap<String, Object> headerValue = new HashMap<>();
		String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
            	JwtUtils jwtUtils = new JwtUtils();
            	headerValue = jwtUtils.getHeaderValues(token);
            }
            catch (Exception e) {
				// TODO: handle exception
            	System.out.println(e.getMessage());
			}
        }
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
		
        //Log signOut process
		CSVFetcher csvFetcher = new CSVFetcher();
		ArrayList<String> csvData = new ArrayList<>();
		csvData.add(""+ headerValue.get("id"));
		csvData.add(""+java.time.LocalDate.now().toString());
		csvData.add(""+System.currentTimeMillis());
		csvData.add("Signed out.");
		csvData.add("");
	
		csvFetcher.activityCSVWriter(csvData.toArray(new String[0]));
	}

}
