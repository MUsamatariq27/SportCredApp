package projectWhat;

public class Post {
	private String emailAddress, title, description;
	private int postId, postScore;
	
	private Post() {
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public int getPostScore() {
		return postScore;
	}

	public void setPostScore(int postScore) {
		this.postScore = postScore;
	}

	public String jsonString() {
		String response = "{" + "\"email\": " + getEmailAddress() + ",\"title\": " +
				getTitle() + ", \"description\": \"" + getDescription() + "\", \"postId\": " + getPostId()
	        	+ "}";
		
		return response;
	}

	// Builder class for User
	public static class PostBuilder {
		private String emailAddress, title, description;
		private int postId, postScore;
		
		public PostBuilder(){
		}
		
		public PostBuilder emailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
			return this;
		}
		
		public PostBuilder title(String title) {
			this.title = title;
			return this;
		}
		
		public PostBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		public PostBuilder postId(int postId) {
			this.postId = postId;
			return this;
		}
		
		public PostBuilder postScore(int postScore) {
			this.postScore = postScore;
			return this;
		}
		
		public Post build() {
			Post post = new Post();
			post.setEmailAddress(this.emailAddress);
			post.setTitle(this.title);
			post.setDescription(this.description);
			post.setPostId(this.postId);
			post.setPostScore(this.postScore);

			return post;
		}
	}
	
}
