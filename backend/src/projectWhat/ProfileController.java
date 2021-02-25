package projectWhat;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ProfileController implements HttpHandler{

	public ProfileDB db;
	
	public ProfileController(ProfileDB db) {
		this.db = db;
	}
	
	public void handle(HttpExchange r) throws IOException {
		try {
            if (r.getRequestMethod().equals("PUT")) {
            	if (r.getHttpContext().getPath().equals("/addUser")) {
            		handleAddUser(r);
            	} else if (r.getHttpContext().getPath().equals("/updateStatus")){
            		handleUpdateStatus(r);
            	} else if (r.getHttpContext().getPath().equals("/makeAdmin")) {
            		handleMakeAdmin(r);
            	} else if (r.getHttpContext().getPath().equals("/makeAdminTest")) {
            		handleMakeAdminTest(r);
            	}
            } else if(r.getRequestMethod().equals("POST")) {
            	if (r.getHttpContext().getPath().equals("/getUser")) {
            		handleGetUser(r);
            	} else if (r.getHttpContext().getPath().equals("/getStatus")) {
            		handleGetStatus(r);
            	}
            } else if(r.getRequestMethod().equals("DELETE")) {
            	if (r.getHttpContext().getPath().equals("/removeUser")) {
            		handleDeleteUser(r);
            	}
            }
        } catch (Exception e) {
        	r.sendResponseHeaders(500, -1);
        }
	}
	
	private void handleMakeAdmin(HttpExchange r) throws IOException {
		try {
			String body = Utils.convert(r.getRequestBody());
	        JSONObject deserialized = new JSONObject(body);
	        
	        String requestorEmail = null, requestorPassword = null, requesteeEmail = null;

	        if(deserialized.has("requestorEmail")) {
	        	requestorEmail = deserialized.getString("requestorEmail");
	        } if(deserialized.has("requestorPassword")) {
	        	requestorPassword = deserialized.getString("requestorPassword");
	        } if(deserialized.has("requesteeEmail")) {
	        	requesteeEmail = deserialized.getString("requesteeEmail");
	        }
	        
	        if(requestorEmail == null || requestorPassword == null || requesteeEmail == null) {
	        	r.sendResponseHeaders(400, -1);
	        }
	        
	        User requestorUser = db.getUser(requestorEmail, requestorPassword); 
	        if(requestorUser != null && requestorUser.isAdmin()) {
	        	boolean isSuccessful = db.makeAdmin(requesteeEmail);
	        	if(isSuccessful) {
	        		r.sendResponseHeaders(200, -1);
	        	} else {
	        		r.sendResponseHeaders(400, -1);
	        	}
	        } else {
	        	r.sendResponseHeaders(400, -1);
	        }
		} catch (IOException e) {
        	r.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
        	r.sendResponseHeaders(400, -1);
        }
	}
	
	// For making an admin without permissions, for testing only
	private void handleMakeAdminTest(HttpExchange r) throws IOException {
		try {
			String body = Utils.convert(r.getRequestBody());
	        JSONObject deserialized = new JSONObject(body);
	        
	        String requestorEmail = null, requestorPassword = null, requesteeEmail = null;

	        if(deserialized.has("requestorEmail")) {
	        	requestorEmail = deserialized.getString("requestorEmail");
	        } if(deserialized.has("requestorPassword")) {
	        	requestorPassword = deserialized.getString("requestorPassword");
	        } if(deserialized.has("requesteeEmail")) {
	        	requesteeEmail = deserialized.getString("requesteeEmail");
	        }
	        
	        if(requestorEmail == null || requestorPassword == null || requesteeEmail == null) {
	        	r.sendResponseHeaders(400, -1);
	        }
	        
	        boolean isSuccessful = db.makeAdmin(requesteeEmail);
        	if(isSuccessful) {
        		r.sendResponseHeaders(200, -1);
        	} else {
        		r.sendResponseHeaders(400, -1);
        	}
		} catch (IOException e) {
        	r.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
        	r.sendResponseHeaders(400, -1);
        }
	}

	public void handleAddUser(HttpExchange r) throws IOException {
		try {
			String body = Utils.convert(r.getRequestBody());
	        JSONObject deserialized = new JSONObject(body);
	        
	        String firstName = null, lastName = null, email = null, password = null;
	        int age = 0;
	        String favSport = null, favTeam = null, highestLvlPlay = null;
	        
	        if(deserialized.has("firstName")) {
	        	firstName = deserialized.getString("firstName");
	        }
	        if(deserialized.has("lastName")) {
	        	lastName = deserialized.getString("lastName");
	        } 
	        if(deserialized.has("email")) {
	        	email = deserialized.getString("email");
	        } 
	        if(deserialized.has("password")) {
	        	password = deserialized.getString("password");
	        } 
	        if(deserialized.has("age")) {
	        	age = deserialized.getInt("age");
	        } 
	        if(deserialized.has("favSport")) {
	        	favSport = deserialized.getString("favSport");
	        }
	        if(deserialized.has("favTeam")) {
	        	favTeam = deserialized.getString("favTeam");
	        } 
	        if(deserialized.has("highestLvlPlay")) {
	        	highestLvlPlay = deserialized.getString("highestLvlPlay");
	        } 
	        
	        if(firstName == null|| lastName == null || email == null || password == null || age == 0
	        		|| favSport == null || favTeam == null || highestLvlPlay == null) {
	        	r.sendResponseHeaders(400, -1);
	        }
	        
        	User user = new User.UserBuilder(email, password)
					.firstName(firstName)
					.lastName(lastName)
					.age(age)
					.favSport(favSport)
					.favTeam(favTeam)
					.highestLvlPlay(highestLvlPlay)
					.build();
        	
        	boolean userExists = db.insertUser(user);
        	
        	if(userExists) {
        		r.sendResponseHeaders(400,-1);
        	} else {
        		r.sendResponseHeaders(200, -1);
        	}
        	
        } catch (IOException e) {
        	r.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
        	r.sendResponseHeaders(400, -1);
        }
	}
	
	public void handleGetUser(HttpExchange r) throws IOException {
		try {
			String body = Utils.convert(r.getRequestBody());
	        JSONObject deserialized = new JSONObject(body);
	        
	        String email = null, password = null;
	        
	        if(deserialized.has("email")) {
	        	email = deserialized.getString("email");
	        } else {
	        	r.sendResponseHeaders(400,-1);
	        }
	        if(deserialized.has("password")) {
	        	password = deserialized.getString("password");
	        } else {
	        	r.sendResponseHeaders(400, -1);
	        }
    
        	User user = db.getUser(email, password); 
        	
        	if(user == null) {
        		r.sendResponseHeaders(400,-1);
        	} 
        	
        	
        	JSONObject jo = new JSONObject();
        	jo.put("firstName", user.getFirstName().replaceAll("\"", ""));
        	jo.put("lastName", user.getLastName().replaceAll("\"", ""));
        	jo.put("email", user.getEmailAddress().replaceAll("\"", ""));
        	jo.put("password", user.getPassword().replaceAll("\"", ""));
        	jo.put("isAdmin", user.isAdmin());
        	jo.put("age", user.getAge());
        	jo.put("favSport", user.getFavSport().replaceAll("\"", ""));
        	jo.put("favTeam", user.getFavTeam().replaceAll("\"", ""));
        	jo.put("highestLvlPlay", user.getHighestLvlSportsPlay().replaceAll("\"", ""));
 
        	String response = jo.toString();
        	
        	r.sendResponseHeaders(200, response.length());
            OutputStream os = r.getResponseBody();
            os.write(response.getBytes());
            os.close();
        	
        } catch (IOException e) {
        	r.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
        	r.sendResponseHeaders(400, -1);
        }
	}
	
	public void handleUpdateStatus(HttpExchange r) throws IOException {
		try {
			String body = Utils.convert(r.getRequestBody());
	        JSONObject deserialized = new JSONObject(body);
	        
	        String email = null, password = null;
	        String status = null;
	        
	        if(deserialized.has("email")) {
	        	email = deserialized.getString("email");
	        } 
	        if(deserialized.has("password")) {
	        	password = deserialized.getString("password");
	        }
	        if(deserialized.has("status")) {
	        	status = deserialized.getString("status");
	        }
	        
	        if(email == null || password == null || status == null) {
	        	r.sendResponseHeaders(400, -1);
	        }
	        
	        User user = new User.UserBuilder(email, password)
					.status(status)
					.build();
        	
        	boolean failed = db.updateStatus(user);
        	
        	if(failed) {
        		r.sendResponseHeaders(400,-1);
        	} else {
        		r.sendResponseHeaders(200, -1);
        	}
        	
        } catch (IOException e) {
        	r.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
        	r.sendResponseHeaders(400, -1);
        }
	}
	
	public void handleGetStatus(HttpExchange r) throws IOException {
		try {
			String body = Utils.convert(r.getRequestBody());
	        JSONObject deserialized = new JSONObject(body);
	        
	        String email = null, password = null;
	        
	        if(deserialized.has("email")) {
	        	email = deserialized.getString("email");
	        } else {
	        	r.sendResponseHeaders(400,-1);
	        }
	        if(deserialized.has("password")) {
	        	password = deserialized.getString("password");
	        } else {
	        	r.sendResponseHeaders(400, -1);
	        }
    
        	User user = db.getStatus(email, password);
        	
        	if(user == null) {
        		r.sendResponseHeaders(400,-1);
        	}
        	
        	
        	JSONObject jo = new JSONObject();
        	jo.put("status", user.getStatus().replaceAll("\"", ""));
 
        	String response = jo.toString();
        	
        	r.sendResponseHeaders(200, response.length());
            OutputStream os = r.getResponseBody();
            os.write(response.getBytes());
            os.close();
        	
        } catch (IOException e) {
        	r.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
        	r.sendResponseHeaders(400, -1);
        }
	}
	
	public void handleDeleteUser(HttpExchange r) throws IOException {
		try {
			String body = Utils.convert(r.getRequestBody());
	        JSONObject deserialized = new JSONObject(body);
	        
	        String email = null, password = null, removeEmail = null;
	        
	        // email and password are from user making delete request
	        // removeEmail is the user to be deleted
	        if(deserialized.has("email")) {
	        	email = deserialized.getString("email");
	        } else {
	        	r.sendResponseHeaders(400,-1);
	        }
	        if(deserialized.has("password")) {
	        	password = deserialized.getString("password");
	        } else {
	        	r.sendResponseHeaders(400, -1);
	        }
	        if(deserialized.has("removeEmail")) {
	        	removeEmail = deserialized.getString("removeEmail");
	        } else {
	        	r.sendResponseHeaders(400, -1);
	        }
    
	        // check user who is requesting exists and is admin
	        User user = db.getUser(email, password);
        	if(user == null) {
        		r.sendResponseHeaders(400,-1);
        	} else if (!user.isAdmin()) {
        		// If user requesting is not an admin, send "Forbidden" response
    			r.sendResponseHeaders(403, -1);
    		} else {
    			boolean failed = db.deleteUser(removeEmail);
            	
            	if(failed) {
            		r.sendResponseHeaders(400,-1);
            	} else {
            		r.sendResponseHeaders(200, -1);
            	}
    		}
        } catch (IOException e) {
        	r.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
        	r.sendResponseHeaders(400, -1);
        }
	}
	
}
