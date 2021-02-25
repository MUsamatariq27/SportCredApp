package projectWhat;

import static org.neo4j.driver.Values.parameters;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;

public class ProfileDB extends ParentDB {

	public ProfileDB() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean insertUser(User user) {
		try (Session session = driver.session()){
			try (Transaction tx = session.beginTransaction()) {
        		Result node_boolean = tx.run("MATCH(u:user) where u.email=$x\n" + "Return u"
						,parameters("x", user.getEmailAddress()));
        		
        		if(node_boolean.hasNext()) {
        			// user already exists
        			return true;
        		}
			}
			session.writeTransaction(tx -> tx.run("MERGE (u:user {first_name: $a, last_name: $b,"
					+ " email: $d, pwd: $e , age: $f , fav_sport: $h, fav_team: $i, highest_lvl_play: $j,"
					+ " is_admin: $k})", 
					parameters("a", user.getFirstName(), "b", user.getLastName(),
							"d", user.getEmailAddress(), "e", user.getPassword(), "f", user.getAge(),
							"h", user.getFavSport(), "i", user.getFavTeam(), "j", user.getHighestLvlSportsPlay(),
							"k", user.isAdmin())));
			session.close();
			// user does not already exist and has been added to the database
			return false;
		}
	}
	
	public User getUser(String emailAddress) {
		try (Session session = driver.session()){
			try (Transaction tx = session.beginTransaction()) {
        		Result result = tx.run("MATCH(u:user) where u.email=$x\n" + "Return u"
						,parameters("x", emailAddress));
        		
        		User user = null;
        		if(result.hasNext()) {
        			// user exists and has been found in the database
        			Record node = result.single();
        			Value record = node.get("u");
        			
        			user = new User.UserBuilder(emailAddress)
        					.isAdmin(record.get("is_admin").asBoolean())
							.build();
        		}
        		return user;
			}
		}
		
	}
	
	public boolean makeAdmin(String emailAddress) {
		try (Session session = driver.session()){
    		User user = getUser(emailAddress);
    		if(user == null) {
    			return false;
    		}
    		session.writeTransaction(tx -> tx.run("MATCH(u:user) where u.email=$x\n" + "SET u.is_admin = true"
					,parameters("x", emailAddress)));
    		return true;
			
		}
		
	}
	// Given user with email, password and status, updates status
	public boolean updateStatus(User user) {
		try (Session session = driver.session()){
			try (Transaction tx = session.beginTransaction()) {
				Result node_boolean = tx.run("MATCH(u:user) where u.email=$x and u.pwd=$y\n" + "Return u"
						,parameters("x", user.getEmailAddress(), "y", user.getPassword()));
        		
        		// user doesn't exist, don't update
        		if(!node_boolean.hasNext()) {
        			return true;
        		}
			}
			session.writeTransaction(tx -> tx.run("MATCH(u:user {email : $x})\n"
												+ "SET u.status = $z"
					,parameters("x", user.getEmailAddress(), "y", user.getPassword(),
							"z", user.getStatus())));
			session.close();
			// user exists in the database and its status was updated
			return false;
		}
	}
	
	// Given user with email and password, returns user with status
	public User getStatus(String emailAddress, String password) {
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
        						.status(record.get("status").toString())
								.build();
        		}
        		return user;
			}
		}
	}
	
	// Delete user with given email from database, return true if failed
	public boolean deleteUser(String removeEmail) {
		try (Session session = driver.session()){
			try (Transaction tx = session.beginTransaction()) {
        		Result result = tx.run("MATCH(u:user) where u.email=$x\n" + "Return u"
						,parameters("x", removeEmail));
        		
        		if(!result.hasNext()) {
        			// user already exists
        			return true;
        		}
			}
			session.writeTransaction(tx -> tx.run("MATCH(u:user) where u.email=$x\n" + "DETACH DELETE u",
					parameters("x", removeEmail)));
			session.close();
			return false;
		}
	}
}
