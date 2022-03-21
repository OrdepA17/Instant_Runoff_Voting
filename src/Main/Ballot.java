package Main;

import dataStructures.List.ArrayList;

/**
 * The Ballot
 * 
 * Stores all the votes casted by one person who votes in the Poor Harbor Election.
 * 
 * @author: Ordep E. Agustin (@OrdepA17)
 * @version: 2.0
 * @since: 2022-03-20
 */
public class Ballot implements BaseBallot {

	private int ballotNum;
	private ArrayList<Integer> castedVotes = new ArrayList<>(); // This way we can store the votes of each candidate
	private boolean validBallot = true;
	private boolean blankBallot = true;
	
	@Override
	public int getBallotNum() {
		return ballotNum;
	}
	@Override
	/**
	 * Returns -1 if candidateID is not found
	 * Also we add 1 because firstIndexOf returns the position i and since 
	 * we want to save the ranking we add 1 to the position
	 * 
	 */
	
	public int getRankByCandidate(int candidateID) {
		if(candidateID > castedVotes.size()) {
			
			throw new IndexOutOfBoundsException();
		}
		return castedVotes.firstIndexOf(candidateID) + 1;
	}

	@Override
	
	/**
	 * Returns -1 if the rank is greater than the size of the array, since there cannot be higher ranks than the amount of candidates
	 * Also we subtract 1 because get returns the element in the position and before the rank is the candidateID
	 * 
	 */
	
	public int getCandidateByRank(int rank) {
		if(rank > castedVotes.size()) {
			return -1;
		}
		return castedVotes.get(rank-1);
	
	}

	@Override
	public void eliminate(int candidateID) {
		if(this.getVotes().firstIndexOf(candidateID) < 0) return;
		
		this.getVotes().remove(this.getVotes().firstIndexOf(candidateID));
	}
	
	//Constructor
	Ballot(int ballotNum, ArrayList<Integer> votes){
		this.ballotNum = ballotNum;
		
		/**
		 * checks if the votes arrayList has any "votes" (preference given to candidate)
		 * if there are none we set it as blank
		 */
	
		if(votes.size() == 0) {
			blankBallot=true;
		}
		
		//checks if the votes arrayList properly contains the preference for the candidate
		for(int i: votes) {
			if(castedVotes.contains(i)) validBallot = true;
			this.castedVotes.add(i);
		}
	}
	
	//Getters
	public boolean isValid() {
		return validBallot;
	}
	
	public boolean getValidBallot() {
		return validBallot;
	}
	
	public ArrayList<Integer> getVotes() {
		return castedVotes;
	}
	
	public boolean isBlankBallot() {
		return blankBallot;
	}
	
	//Setters
	
	public void setBallotValidity(boolean validBallot) {
		this.validBallot = validBallot;
	}
	
	public void setVotes(ArrayList<Integer> votes) {
		this.castedVotes = votes;
	}
	
	public void setBlankBallot(boolean blankBallot) {
		this.blankBallot = blankBallot;
	}
	
	//useful for printing the value of the Ballot number
	public String toString() {
		return Integer.toString(this.getBallotNum());
	}




	
	

}