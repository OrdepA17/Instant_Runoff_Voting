package Main;

import dataStructures.List.ArrayList;

/**
 * The Ballot
 * 
 * Stores all the votes casted by one person who votes in the Poor Harbor Election.
 * 
 * @author: Ordep E. Agustin (@OrdepA17)
 * @version: 2.1
 * @since: 2022-04-3
 */
public class Ballot implements BaseBallot {
	private int ballotNum;
	private int [] candidatesSorted; // Used for sorting Candidates by their rank
	private String[] votes; //Used for saving the votes
	private boolean blankBallots = true;
	private boolean valid = true;

	@Override
	public int getBallotNum() {
				return ballotNum;
	}

	@Override
	public int getRankByCandidate(int candidateID) {
		for (int i = 0; i < candidatesSorted.length; i++) {
			if (candidatesSorted[i] == candidateID) {
				return i + 1;
			}
		}
		return -1;
	}

	@Override
	public int getCandidateByRank(int rank) {
		if (rank < 0 || rank > candidatesSorted.length) {
			return -1;
		}
		return candidatesSorted[rank - 1];
	}

	@Override
	public boolean eliminate(int candidateId) {
		if (candidateId < 0 || candidateId > candidatesSorted.length) {
			return false;
		}
		if (getCandidateByRank(2) == -1) {
			return false; //Only hits this case when there is only one candidate in the list
		}
		for (int i = 0; i < candidatesSorted.length - 1; i++) {
			candidatesSorted[i] = candidatesSorted[i + 1]; // This would rotates the values to the previous position
		}
		for (int i = 0; i < candidatesSorted.length - 1; i++) {
			if (candidatesSorted[i] == candidatesSorted[i + 1]) {
				candidatesSorted[i + 1] = 0; //Makes sure that the numbers are not repeated with the exception of 0 which means their is no one with that rank
			}
		}
		return false;
	}
	
	public Ballot(String votes, ArrayList<Candidate> candidates) {// This method is what checks the ballots and sets the votes, validness and the candidates
		this.votes = votes.split(",");
		ballotNum = Integer.parseInt(this.votes[0]); 
		candidatesSorted = new int[candidates.size()];
		blankBallots = checkIfBlank(this.votes);
		if(!blankBallots) {
			valid = checkIfValid(this.votes);
		}
		if (valid && !blankBallots) {
			rankCandidates();
		}
	}
	public void rankCandidates() { 
		for (int i = 1; i < votes.length; i++) {
			int colon = votes[i].indexOf(":");
			candidatesSorted[Integer.parseInt(votes[i].substring(colon + 1)) - 1] = Integer.parseInt(votes[i].substring(0,colon));
			
		}
	}
	private boolean checkIfBlank(String[] votes) {
		return (votes.length == 1);
	}
	
	private boolean checkIfValid(String[] votes) {
		for (int i = 1; i < votes.length - 1; i++) {
			int colonPostion = votes[i].indexOf(":");
			String name = votes[i].substring(0, colonPostion); //Name of the candidate 
			String rank = votes[i].substring(colonPostion + 1); //Rank given to the candidate 
			if (Integer.parseInt(rank) > candidatesSorted.length) {//sets to invalid since the rank is greater than the number of candidates
				return false; 
			}
			if (Integer.parseInt(votes[i + 1].substring(votes[i + 1].indexOf(":") + 1)) - Integer.parseInt(rank) > 1) {
				return false; //Invalid since ranks skip a number and do not follow linear sequence
			}
			for (int j = i + 1; j < votes.length; j++) {
				if (votes[j].startsWith(name)) { //Sets to invalid if the same candidate appears
					return false;
				}
				else if (votes[j].substring(votes[j].indexOf(":") + 1).equals(rank)) { //Since the rank appears on a different candidate again then its set to invalid
					return false;
				}
			}
		}
		return true;
	}
	public String toString() {
		return Integer.toString(getBallotNum());
	}
	
	//Getters
	public boolean isBlank() {
		return blankBallots;
	}
	public boolean isValid() {
		return valid;
	}
	
}