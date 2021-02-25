package projectWhat;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;


public class OpenCourtDB extends ParentDB{

	public OpenCourtDB() {
		// TODO Auto-generated constructor stub
	}
  
	public User getUser(String emailAddress, String password) {
		try (Session session = driver.session()){
			try (Transaction tx = session.beginTransaction()) {
        		Result result = tx.run("MATCH(u:user) where u.email=$x and u.pwd=$y\n" + "Return u"
						,parameters("x", emailAddress, "y", password));
        		
        		User user = null;
        		if(result.hasNext()) {
        			// user exists and has been found in the database
        			Record node = result.single();
        			Value record = node.get("u");
        			
        			user = new User.UserBuilder(emailAddress, password)
    						.firstName(record.get("first_name").toString())
    						.lastName(record.get("last_name").toString())
    						.age(record.get("age").asInt())
    						.favSport(record.get("fav_sport").toString())
    						.favTeam(record.get("fav_team").toString())
    						.highestLvlPlay(record.get("highest_lvl_play").toString())
							.build();
        		}
        		return user;
			}
		}
		
	}
	
	public void insertPost(String title, String description) {
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MERGE (p:post {title: $x, description: $y})", 
					parameters("x", title, "y", description)));
			session.close();
		}
	}
	
	public boolean addPost(String email, String password, String title, String description) {
		// each new post has a post id incremented by 1
		int postId = getHighestPostId() + 1;
		// each new post starts with a default score of 0
		int postScore = 0;
		User user = getUser(email, password);
		if(user == null) {
			return false;
		}
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MERGE (p:post {title: $x, description: $y,"
					+ "post_id : $i, post_score : $s})\n"
					+ "WITH p\n"
					+ "MATCH (u: user {email: $z})\n"  + "MERGE (u)-[r:CREATED]->(p)", 
					parameters("x", title, "y", description, "z", email, "i", postId,
								"s", postScore)));

			session.close();
			
			return true;
		}
	}
	
	public ArrayList<Post> getPosts() {
		ArrayList<Post> posts = new ArrayList<Post>();
		Post post;
		Value postValue;
		Value userValue;
		Record record;
		try (Session session = driver.session()){
			try (Transaction tx = session.beginTransaction()) {
				// get post and user who made it
				Result result = tx.run("MATCH (u:user)-[r:CREATED]->(p:post)\n" 
										+ "RETURN u,p\n"
										+ "ORDER BY p.post_id DESC");
				while (result.hasNext()) {
					record = result.next();
					userValue = record.get("u");
					postValue = record.get("p");
					
					// get email from user and post data from post
					post = new Post.PostBuilder()
	        				.emailAddress(userValue.get("email").toString())
	        				.title(postValue.get("title").toString())
	        				.description(postValue.get("description").toString())
	        				.postId(postValue.get("post_id").asInt())
	        				.postScore(postValue.get("post_score").asInt())
	        				.build();
					
					posts.add(post);
				}
			}

			session.close();
			
			return posts;
		}
	}
	
	public int getHighestPostId() {
		int HighestPostId = 0;
		Value postValue;
		Record record;
		try (Session session = driver.session()){
			try (Transaction tx = session.beginTransaction()) {
				// get post and user who made it
				Result result = tx.run("MATCH (u:user)-[r:CREATED]->(p:post)\n" 
										+ "RETURN p\n"
										+ "ORDER BY p.post_id DESC");
				
				if (result.hasNext()) {
					record = result.next();
					postValue = record.get("p");
					
					// get first post id, since retrieved in descending order
					HighestPostId = postValue.get("post_id").asInt();
				}
			}

			session.close();
			
			return HighestPostId;
		}
	}
	
	public Post getPost(int postId) {
		Post post = null;
		Value postValue;
		Value userValue;
		Record record;
		try (Session session = driver.session()){
			try (Transaction tx = session.beginTransaction()) {
				// get post and user who made it
				Result result = tx.run("MATCH (u:user)-[r:CREATED]->(p:post)\n"
										+ "where p.post_id=$x\n"
										+ "RETURN u,p\n", parameters("x", postId));
				
				while (result.hasNext()) {
					record = result.next();
					userValue = record.get("u");
					postValue = record.get("p");
					
					// get first post id, since retrieved in descending order
					post = new Post.PostBuilder()
	        				.emailAddress(userValue.get("email").toString())
	        				.title(postValue.get("title").toString())
	        				.description(postValue.get("description").toString())
	        				.postId(postValue.get("post_id").asInt())
	        				.postScore(postValue.get("post_score").asInt())
	        				.build();
				}
			}

			session.close();
			
			return post;
		}
	}
	
	// increases/decreases post score with given post id by given value
	// returns false if post does not exist
	public boolean updatePostScore(int postId, int increment) {
		Post post = getPost(postId);
		if (post == null) {
			return false;
		}
		try (Session session = driver.session()){
			// set new post score
			session.writeTransaction(tx -> tx.run("MATCH(p:post {post_id : $x})\n"
					+ "SET p.post_score = $y"
					, parameters("x", postId, "y", post.getPostScore() + increment)));
			session.close();
			
			return true;
		}
	}
	
	// Creates relationship where given user agrees with given post
	// and increments post score accordingly
	// returns false if user already agrees or post does not exist
	public boolean agreeWithPost(String email, int postId) {
		int increment = 1;
		if (userAgreedWithPost(email, postId)) {
			return false;
		}
		boolean disagreed = userDisagreedWithPost(email, postId);
		if (disagreed) {
			increment = 2;
		}
		try (Session session = driver.session()){
			// delete relationship where user disagreed if it exists
			if (disagreed) {
				session.writeTransaction(tx -> tx.run("MATCH (u:user {email : $x})-[r:DISAGREED]->"
						+ "(p:post {post_id : $y})\n" 
						+ "DELETE r", parameters("x", email, "y", postId)));
			}
			
			// create relationship where user agrees
			session.writeTransaction(tx -> tx.run("MATCH (u:user {email : $x}), (p:post {post_id : $y})\n"
					+ "CREATE (u)-[r:AGREED]->(p)", parameters("x", email, "y", postId)));

			session.close();
		}
		// update the post score
		return updatePostScore(postId, increment);
	}
	
	// Creates relationship where given user disagrees with given post
	// and increments post score accordingly
	public boolean disagreeWithPost(String email, int postId) {
		int increment = -1;
		if (userDisagreedWithPost(email, postId)) {
			return false;
		}
		boolean agreed = userAgreedWithPost(email, postId);
		if (agreed) {
			increment = -2;
		}
		try (Session session = driver.session()){
			// delete relationship where user agreed if it exists
			if (agreed) {
				session.writeTransaction(tx -> tx.run("MATCH (u:user {email : $x})-[r:AGREED]->"
						+ "(p:post {post_id : $y})\n" 
						+ "DELETE r", parameters("x", email, "y", postId)));
			}
			
			// create relationship where user disagrees
			session.writeTransaction(tx -> tx.run("MATCH (u:user {email : $x}), (p:post {post_id : $y})\n"
					+ "CREATE (u)-[r:DISAGREED]->(p)", parameters("x", email, "y", postId)));

			session.close();
		}
		// update the post score
		return updatePostScore(postId, increment);
	}
	
	// Checks if the user has already voted "agree" on a post
	public boolean userAgreedWithPost(String email, int postId) {
		try (Session session = driver.session()){
			try (Transaction tx = session.beginTransaction()) {
				Result result = tx.run("MATCH (u:user {email : $x})-[r:AGREED]->"
						+ "(p:post {post_id : $y})\n" 
						+ "RETURN p", parameters("x", email, "y", postId));
				
				if (result.hasNext()) {
					return true;
				}
			}

			session.close();
			
			return false;
		}
	}
	
	// Checks if the user has already voted "disagree" on a post
	public boolean userDisagreedWithPost(String email, int postId) {
		try (Session session = driver.session()){
			try (Transaction tx = session.beginTransaction()) {
				Result result = tx.run("MATCH (u:user {email : $x})-[r:DISAGREED]->"
						+ "(p:post {post_id : $y})\n" 
						+ "RETURN p", parameters("x", email, "y", postId));
				
				if (result.hasNext()) {
					return true;
				}
			}

			session.close();
			
			return false;
		}
	}
}
