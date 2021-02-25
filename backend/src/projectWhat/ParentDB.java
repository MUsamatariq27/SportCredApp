package projectWhat;

import org.neo4j.driver.Driver;

import static org.neo4j.driver.Values.parameters;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;


public class ParentDB {
	
	protected Driver driver;
	protected String uriDb;
	
	public ParentDB() {
		uriDb = "bolt://localhost:7687";
		driver = GraphDatabase.driver(uriDb, AuthTokens.basic("neo4j","1234"));
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
}
