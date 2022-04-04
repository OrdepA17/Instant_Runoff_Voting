package Main;

/**
 * The Candidate Running for President
 * Class contains the candidate name, ID, who is first in the ballot and their status in the election
 * 
 * @author Ordep E. Agustin (@OrdepA17)
 * @version 2.1
 * @since 2022-04-3
 */

public class Candidate {
	
	private String candidateName;
	private int ID;
	private boolean eliminatedCandidate;
	
	//Constructor
	public Candidate(String name, int ID) {
		this.candidateName = name;
		this.ID = ID;
		eliminatedCandidate = false;
	}
	
	//Getters
	public String getCandidateName() {
		return candidateName;
	}
	
	public int getCandidateID() {
		return ID;
	}
	
	public boolean isEliminatedCandidate() {
		return eliminatedCandidate;
	}	
	
	public String toString() {
		return getCandidateName();
	}

	//Setters
	public void setCandidateName(String name) {
		this.candidateName = name;
	}
	
	public void setCandidateID(int ID) {
		this.ID = ID;
	}
	
	public void setEliminatedCandidate(boolean eliminatedCandidate) {
		this.eliminatedCandidate = eliminatedCandidate;
	}		
}
