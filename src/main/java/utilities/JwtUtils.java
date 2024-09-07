package utilities;

import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {
	
	private static String secret = "This_is_secret";
	
	public String generateJwt(String user,String type,int id) {
		Claims claims = Jwts.claims().setIssuer(id+"").setIssuedAt(new Date());
		
		claims.put("type", type);
		claims.put("name", user);
		
		return Jwts.builder().signWith(SignatureAlgorithm.HS512, secret).setClaims(claims).compact();
	}
	
	public boolean validate(String authorization) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(authorization);
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	public HashMap<String, Object> getHeaderValues(String authorization){
		HashMap<String, Object> headerValue = new HashMap<>();
		if(validate(authorization)) {
			Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authorization).getBody();
			headerValue.put("id", Integer.parseInt((String) claims.getIssuer()));
			headerValue.put("name", claims.get("name"));
			headerValue.put("type", claims.get("type"));
		}
		return headerValue;
	}
}
