package Main;

import dataStructures.List.ArrayList;

/**
 * The Candidate Running for President
 * Class contains the candidate name, ID, who is first in the ballot and their status in the election
 * 
 * @author Ordep E. Agustin (@OrdepA17)
 * @version 2.0
 * @since 2022-03-20
 */

public class Candidate {
	
	private String candidateName;
	private int candidateID;
	private ArrayList<Ballot> firstInBallot;
	private boolean activeCandidate = true;
	
	//Constructor
	public Candidate(String name, int ID) {
		this.candidateName = name;
		this.candidateID = ID;
	}
	
	//Getters
	public String getCandidateName() {
		return candidateName;
	}
	
	public int getCandidateID() {
		return candidateID;
	}	
	
	public boolean isActiveCandidate() {
		return activeCandidate;
	}
	
	public ArrayList<Ballot> getFirstInBallot() {
		return firstInBallot;
	}
	
	//Setters
	public void setCandidateName(String name) {
		this.candidateName = name;
	}
	
	public void setCandidateID(int ID) {
		this.candidateID = ID;
	}
	
	public void setActiveCandidate(boolean activeCandidate) {
		this.activeCandidate = activeCandidate;
	}
	
	public void setFirstInBallot(ArrayList<Ballot> firstInBallot) {
		this.firstInBallot = firstInBallot;
	}

	public void eliminatedCandidate() {
		this.activeCandidate = false;
	}
	
	public void printCandidateInformation() {
		System.out.println("Candidate: " + getCandidateName() + ", ID Number: " + getCandidateID());
	
	}


	

	
	






	
		
}
