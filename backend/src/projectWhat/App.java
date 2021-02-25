package projectWhat;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class App {

	static int PORT=8080;
	public static void main(String[] args) throws IOException {
		 HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
	     
		 ProfileDB profileDb = new ProfileDB();
		 OpenCourtDB openCourtDb = new OpenCourtDB();
		 ProfileController profileController = new ProfileController(profileDb);
		 OpenCourtController openCourtController = new OpenCourtController(openCourtDb);
		 
        server.createContext("/addUser", profileController);
        server.createContext("/getUser", profileController);
        server.createContext("/removeUser", profileController);
        
        server.createContext("/updateStatus", profileController);
        server.createContext("/getStatus", profileController);
        server.createContext("/makeAdmin", profileController);
        server.createContext("/addPost", openCourtController);
        server.createContext("/getPosts", openCourtController);
        server.createContext("/agreePost", openCourtController);
        server.createContext("/disagreePost", openCourtController); 	

        // Dev tools:
        // For making an admin without permissions, for testing only
        server.createContext("/makeAdminTest", profileController);
        
        server.start();
        System.out.printf("Server started on port %d...\n", PORT);
	}
}
