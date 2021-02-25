package projectWhat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class OpenCourtController implements HttpHandler{

	public OpenCourtDB db;
	
	public OpenCourtController(OpenCourtDB db) {
		this.db = db;
	}

	public void handle(HttpExchange r) throws IOException {
		try {
            if (r.getRequestMethod().equals("PUT")) {
            	if (r.getHttpContext().getPath().equals("/addPost")) {
            		handleAddPost(r);
            	} else if (r.getHttpContext().getPath().equals("/agreePost")){
            		handleAgreePost(r);
            	} else if (r.getHttpContext().getPath().equals("/disagreePost")){
            		handleDisagreePost(r);
            	}
            } else if(r.getRequestMethod().equals("GET")) {
            	if(r.getHttpContext().getPath().equals("/getPosts")) {
            		handleGetPosts(r);
            	}
            }
        } catch (Exception e) {
        	r.sendResponseHeaders(500, -1);
        }
		
	}

	private void handleGetPosts(HttpExchange r) throws IOException {
		// TODO Auto-generated method stub
		try {
        	ArrayList<Post> posts = db.getPosts();
        	
        	if(posts == null || posts.isEmpty()) {
        		r.sendResponseHeaders(400, -1);
        	}
        	
        	JSONObject mainJo = new JSONObject();
        	JSONObject jo = new JSONObject();
        	JSONArray ja = new JSONArray();
        	for (Post post : posts) {
        		jo.put("email", post.getEmailAddress().replaceAll("\"", ""));
        		jo.put("title", post.getTitle().replaceAll("\"", ""));
        		jo.put("description", post.getDescription().replaceAll("\"", ""));
        		jo.put("postId", post.getPostId());
        		jo.put("postScore", post.getPostScore());
        		// put post into array
        		ja.put(jo);
        		// clear jo for next post
        		jo = new JSONObject();
        	}
 
        	// return array of all posts
        	mainJo.put("posts", ja);
        	String response = mainJo.toString();
        	
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

	private void handleAddPost(HttpExchange r) throws IOException {
		try {
			String body = Utils.convert(r.getRequestBody());
	        JSONObject deserialized = new JSONObject(body);
	        boolean isSuccessful = false;
	        
	        String email = null, password = null, title = null, description = null;


	        if(deserialized.has("email")) {
	        	email = deserialized.getString("email");
	        } 
	        if(deserialized.has("password")) {
	        	password = deserialized.getString("password");
	        } 
	        if(deserialized.has("title")) {
	        	title = deserialized.getString("title");
	        } 
	        if(deserialized.has("description")) {
	        	description = deserialized.getString("description");
	        }
	        
	        
	        if(email == null|| password == null || title == null || description == null) {
	        	r.sendResponseHeaders(400, -1);
	        }
	        
	        // check user is valid
	        User user = db.getUser(email, password);
        	if(user != null) {
        		isSuccessful = db.addPost(email, password, title, description); 
        	}
        	
        	if(isSuccessful) {
        		r.sendResponseHeaders(200, -1);
        	} else {
        		r.sendResponseHeaders(400,-1);
        	} 
        	
        } catch (IOException e) {
        	r.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
        	r.sendResponseHeaders(400, -1);
        }
	}
	
	private void handleAgreePost(HttpExchange r) throws IOException {
		try {
			String body = Utils.convert(r.getRequestBody());
	        JSONObject deserialized = new JSONObject(body);
	        
	        String email = null, password = null;
	        int postId = -1;
	        boolean isSuccessful = false;


	        if(deserialized.has("email")) {
	        	email = deserialized.getString("email");
	        } 
	        if(deserialized.has("password")) {
	        	password = deserialized.getString("password");
	        } 
	        if(deserialized.has("postId")) {
	        	postId = deserialized.getInt("postId");
	        }
	        
	        
	        if(email == null|| password == null || postId == -1) {
	        	r.sendResponseHeaders(400, -1);
	        }
	        
	        // check user is valid
	        User user = db.getUser(email, password);
	        if(user != null) {
        		isSuccessful = db.agreeWithPost(email, postId); 
        	}
        	
        	if(isSuccessful) {
        		r.sendResponseHeaders(200, -1);
        	} else {
        		r.sendResponseHeaders(400,-1);
        	} 
        	
        } catch (IOException e) {
        	r.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
        	r.sendResponseHeaders(400, -1);
        }
	}
	
	private void handleDisagreePost(HttpExchange r) throws IOException {
		try {
			String body = Utils.convert(r.getRequestBody());
	        JSONObject deserialized = new JSONObject(body);
	        
	        String email = null, password = null;
	        int postId = -1;
	        boolean isSuccessful = false;

	        if(deserialized.has("email")) {
	        	email = deserialized.getString("email");
	        } 
	        if(deserialized.has("password")) {
	        	password = deserialized.getString("password");
	        } 
	        if(deserialized.has("postId")) {
	        	postId = deserialized.getInt("postId");
	        }
	        
	        
	        if(email == null|| password == null || postId == -1) {
	        	r.sendResponseHeaders(400, -1);
	        }
	        
	        // check user is valid
	        User user = db.getUser(email, password);
        	if(user != null) {
        		isSuccessful = db.disagreeWithPost(email, postId); 
        	}
        	
        	if(isSuccessful) {
        		r.sendResponseHeaders(200, -1);
        	} else {
        		r.sendResponseHeaders(400,-1);
        	} 
        	
        } catch (IOException e) {
        	r.sendResponseHeaders(500, -1);
        } catch (JSONException e) {
        	r.sendResponseHeaders(400, -1);
        }
	}
}
