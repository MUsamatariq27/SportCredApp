package projectWhat;

public class User {
	private String firstName, lastName;
	private String emailAddress, password;
	private int age;
	private String favSport, favTeam, highestLvlPlay;
	private String status;
	private ACSHistory acsHist;
	private boolean isAdmin = false;
	
	private User() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFavSport() {
		return favSport;
	}

	public void setFavSport(String favSport) {
		this.favSport = favSport;
	}

	public String getFavTeam() {
		return favTeam;
	}

	public void setFavTeam(String favTeam) {
		this.favTeam = favTeam;
	}

	public String getHighestLvlSportsPlay() {
		return highestLvlPlay;
	}

	public void setHighestLvlSportsPlay(String highestLvlSportsPlay) {
		this.highestLvlPlay = highestLvlSportsPlay;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ACSHistory getAcsHist() {
		return acsHist;
	}

	public void setAcsHist(ACSHistory acsHist) {
		this.acsHist = acsHist;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String jsonString() {
		String response = "{" + "\"firstName\": " + firstName + ",\"lastName\": " +
	        	lastName + ", \"email\": \"" + emailAddress + "\", \"password\": " + password
	        	+ ", \"age\": " + age + ", \"favSport\": " + favSport + ", \"favTeam\": "
	        	+ favTeam + ", \"highestLvlPlay\": " + highestLvlPlay + "}";
		
		return response;
	}

	// Builder class for User
	public static class UserBuilder {
		private String firstName, lastName;
		private String emailAddress, password;
		private int age;
		private String favSport, favTeam, highestLvlPlay;
		private String status;
		private ACSHistory acsHist;
		private boolean isAdmin = false;
		
		public UserBuilder(String emailAddress, String password){
			this.emailAddress = emailAddress;
			this.password = password;
		}
		
		public UserBuilder(String emailAddress){
			this.emailAddress = emailAddress;
		}
		
		public UserBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public UserBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public UserBuilder age(int age) {
			this.age = age;
			return this;
		}
		
		public UserBuilder favSport(String favSport) {
			this.favSport = favSport;
			return this;
		}
		
		public UserBuilder favTeam(String favTeam) {
			this.favTeam = favTeam;
			return this;
		}
		
		public UserBuilder highestLvlPlay(String highestLvlPlay) {
			this.highestLvlPlay = highestLvlPlay;
			return this;
		}
		
		public UserBuilder status(String status) {
			this.status = status;
			return this;
		}
		
		public UserBuilder acsHist(ACSHistory acsHist) {
			this.acsHist = acsHist;
			return this;
		}
		
		public UserBuilder isAdmin(boolean isAdmin) {
			this.isAdmin = isAdmin;
			return this;
		}
		
		public User build() {
			User user = new User();
			user.firstName = this.firstName;
			user.lastName = this.lastName;
			user.emailAddress = this.emailAddress;
			user.password = this.password;
			user.age = this.age;
			user.favSport = this.favSport;
			user.favTeam = this.favTeam;
			user.highestLvlPlay = this.highestLvlPlay;
			user.acsHist = this.acsHist;
			user.status = this.status;
			user.isAdmin = this.isAdmin;
			
			return user;
		}
	}
	
}
