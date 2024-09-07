package components;

public class IdGenerator {
	   private static final long INITIAL_TIME = System.currentTimeMillis();

	    public static long generateUserPrimaryId() {
	        long currentTime = System.currentTimeMillis()-1000;
//	        int random = (int) (Math.random() * 1000); // Add randomness to avoid collisions
	        return (currentTime);
	    }
}
