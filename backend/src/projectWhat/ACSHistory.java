package projectWhat;

import java.util.ArrayList;

public class ACSHistory {
	private String histId;
	private ArrayList<Integer> acsScores;
	
	public ACSHistory(String histId, ArrayList<Integer> acsScores) {
		super();
		this.histId = histId;
		this.acsScores = acsScores;
	}
	
	public String getHistId() {
		return histId;
	}
	public void setHistId(String histId) {
		this.histId = histId;
	}
	public ArrayList<Integer> getAcsScores() {
		return acsScores;
	}
	public void setAcsScores(ArrayList<Integer> acsScores) {
		this.acsScores = acsScores;
	}
	public int getCurrACS() {
		return acsScores.get(acsScores.size()-1);
	}
	public void updateCurrACS(int newScore) {
		acsScores.add(newScore);
	}
}
