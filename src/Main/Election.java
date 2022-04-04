package Main;

import dataStructures.List.ArrayList;
import java.io.*; //used to access file writer, reader, etc

/**
 * The Election class
 * 
 * Where we decide who wins and is our new president base on the information provided
 * 
 * @author: Ordep E. Agustin (@OrdepA17)
 * @version: 2.1
 * @since 2022-04-3
 */
public class Election {
	
public static void main(String[] args) throws IOException {
	
	//File directories to be read and file directory where results file will be stored
	String candidatesFile = "./inputFiles/candidates.csv";// this would have to be updated to the file that would like to be read/tested
	String ballotFiles = "./inputFiles/ballots2.csv"; // this would have to be updated to the file that would like to be read/tested
	String resultsFile = "./inputFiles/results.txt"; //created when program is ran for the first time but updates it after each run
	BufferedWriter writer = writerCreator(resultsFile);
	
	// Stores all candidates in the ballot in the order of entry
	ArrayList<Candidate> candidates = candidateCreator(candidatesFile);
	
	ArrayList<ArrayList<Ballot>> validBallots = ballotOrganizer(ballotCreator(ballotFiles, candidates), candidates);
	
	int[] ballotsCount = ballotCounter(ballotFiles, candidates); 
	
	writerOfFiles(writer, "Number of ballots: " + ballotsCount[2]);
	writerOfFiles(writer, "Number of blank ballots: " + ballotsCount[0]);
	writerOfFiles(writer, "Number of invalid ballots: " + ballotsCount[1]);
	
	Candidate winner = elections(validBallots, candidates, ballotsCount, writer);
	
	writerOfFiles(writer, "Winner: " + winner + " wins with " + validBallots.get(winner.getCandidateID() - 1).size() +  " #1's");// sets the line to be printed for winner
	
	try {
		writer.close();
	} 
	catch (IOException e) {
		e.printStackTrace();
	}
	

}
	private static Candidate elections(ArrayList<ArrayList<Ballot>> validBallots, ArrayList<Candidate> candidates, int[] ballotsCount, BufferedWriter writer) {
		boolean electionFinished = false; 
		int round = 1;
		Candidate president = null;
		
		while(!electionFinished) {
			
			for (int i = 0; i < validBallots.size(); i++) { // Checks for the majority of the votes
				ArrayList<Ballot> votes = validBallots.get(i);
				if (!(votes == null) && votes.size() >= (ballotsCount[2] - (ballotsCount[1] + ballotsCount[0]))/2) { //subtracts total ballots with invalid and blank ballots, leaving only the valid ballots 
					electionFinished = true; // this is set up so that once the 50% rule is met then the elections end
					president = candidates.get(votes.get(0).getCandidateByRank(1) - 1);
					break;
				}
			}
			if(!electionFinished) {
				
				ArrayList<Integer> position = getPosOfLowerCandidates(validBallots); //Positions of candidates with least amount of 1's
				
				int min = position.remove(position.size() - 1); 
				int posOfCandidateToEliminate = position.get(0); //Gets the first one if there is only on person with least amounts of 1's
				if (position.size() > 1) { //If there is a tie to be eliminated we enter this if and check the number of 2's and go from there 
					posOfCandidateToEliminate = posOfLowerCandidate(position, validBallots, candidates);
				}
	
				//Eliminates the candidate using the method			
				validBallots = eliminatingCandidate(validBallots, candidates, posOfCandidateToEliminate);
				
				//prints the line saying who was eliminated with what amount of 1's
				writerOfFiles(writer, "Round " + round + ": " + candidates.get(position.get(0)) + " was eliminated with " + min + " #1's");
	
				//Advances to next round
				round++;
			}
		}
		return president;
	}
	
	private static ArrayList<ArrayList<Ballot>> eliminatingCandidate(ArrayList<ArrayList<Ballot>> validBallots, ArrayList<Candidate> candidates, int pos) {
		candidates.get(pos).setEliminatedCandidate(true); //Sets eliminated candidate as eliminated so their data doesn't interfere
		
		for (int i = 0; i < validBallots.get(pos).size(); i++) { 
			
			Ballot currentBallot = validBallots.get(pos).get(i); 
			currentBallot.eliminate(pos + 1); //position +1 accesses the rank of the candidate
			Candidate currentCandidate = candidates.get(currentBallot.getCandidateByRank(1) - 1); //#1 rank of the candidates
			
			while(currentCandidate.isEliminatedCandidate()) { //goes through until valid candidate to be eliminated is found
				currentBallot.eliminate(currentCandidate.getCandidateID());
				currentCandidate = candidates.get(currentBallot.getCandidateByRank(1) - 1);
			}
			
			validBallots.get(currentCandidate.getCandidateID() - 1).add(currentBallot);
		}
		
		validBallots.set(pos,null);//set to null to avoid position complications
		return validBallots;
	}
	
	/**
	 * Position Of Lower Candidate Method
	 * 
	 * Only used when there is a tie with the candidates to be eliminated
	 * It searches for the candidates with the least amount of votes of a certain rank starting from 2 (since they are tied at # of 1's)
	 * 
	 * @param pos - Position of candidate to be eliminated
	 * @param validBallots - List with the candidates ordered by entry
	 * @param candidates - List of candidates themselves
	 * @return returns position of candidate to be eliminated
	 */
	private static int posOfLowerCandidate(ArrayList<Integer> pos, ArrayList<ArrayList<Ballot>> validBallots, ArrayList<Candidate> candidates) {
		int rank = 2;
		int[] toEliminate = new int[candidates.size()]; 
		
		while(true) { //Only runs until there is one eligible candidate to be eliminated
			if (rank > candidates.size()) { //if tied in all the ranks then we take the one with largest ID
				while(pos.size() > 1) {
					pos.remove(0);
				}
				break;
			}								
			//this for loop broke a lot of things :( but fixed it
			for (int i = 0; i < pos.size(); i++) { // counts the ballots where the candidate has the searched rank and sums the pos of candidate(id-1)
				for (int j = 0; j < validBallots.size(); j++) {
					if (!(validBallots.get(j) == null)) {
						for (int k = 0; k < validBallots.get(j).size(); k++) {
							Ballot currentBallot = validBallots.get(j).get(k);
							if (currentBallot.getRankByCandidate(pos.get(i) + 1) == rank) {
								toEliminate[pos.get(i)]++;
							}
						}
					}
				}
			}
			
			int minimunRankVotes = toEliminate[pos.get(0)];
			
			ArrayList<Integer> subPosition = new ArrayList<>();
			
			for (int i = 0; i < pos.size(); i++) {// discards the votes with the most of the searched rank
				if (toEliminate[pos.get(i)] < minimunRankVotes) { //if there's a smaller amount of the votes then it clears the subPos and adds it
					minimunRankVotes = toEliminate[pos.get(i)];
					subPosition.clear();
					subPosition.add(pos.get(i));
				}
				else if (toEliminate[pos.get(i)] == minimunRankVotes) { //if there's another candidate with same amount of searched for rank then it is also added
					subPosition.add(pos.get(i));
				}
			}
			if (subPosition.size() == 1) { //Found the candidate to be eliminated
				pos.clear(); //Safety precaution to assure that no other candidate that could be eliminated tags along
				pos.add(subPosition.get(0)); //adds to the list the candidate to be eliminated
				break;
			}
			rank++;
		}
		return pos.get(0); 
	}
	
	
	/**
	 * Getting Position Of Lower Candidates Method
	 * 
	 * Searches for the position with the least amounts of 1's and stores that position
	 * 
	 * @param validBallots - List with the candidates in order of entry
	 * @return List with the position of the candidates to be eliminated and the last value of the list is the least amount of 1's
	 */
	private static ArrayList<Integer> getPosOfLowerCandidates(ArrayList<ArrayList<Ballot>> validBallots) {
		
		ArrayList<Integer> positon = new ArrayList<>();
		int minimum = -1; 
		
		for (int i = 0; i < validBallots.size(); i++) {//Searches for candidate with least amount of 1's 
			if (minimum == -1) {// this only occurs if there are no minimums already stored
				minimum = validBallots.get(i).size();
			}
			if (!(validBallots.get(i) == null)) { 
				if (validBallots.get(i).size() < minimum) { //Checks if there's a smaller amount of 1's than the one already found
					minimum = validBallots.get(i).size(); //Changes to the new minimum
					positon.clear(); //Clears to avoid contamination of old values
					positon.add(i); //Adds position of candidate
				}
				else if (minimum == validBallots.get(i).size()) { //If there's a candidate with same amount of 1's
					positon.add(i);
				}
			}			
		}
		positon.add(minimum);
		return positon;
	}


	/*
	 * Method used for writing text into files
	 */
	private static void writerOfFiles(BufferedWriter writer, String text) {
		try {
			writer.write(text);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private static BufferedWriter writerCreator(String filePath) {
		BufferedWriter output = null;
		try {
			FileWriter file = new FileWriter(filePath);
			output = new BufferedWriter(file);
		} catch (Exception e) {
			e.getStackTrace();
		}
		return output;
	}
	
	
	/**
	 * Counting Ballots Method
	 * 
	 * Counts the ballots and classifies them as blank or invalid. Then the remaining files are added to the total ballots
	 * 
	 * @param filePath - File to be read (*MAKE SURE FILE PATH IS UPDATED TO FILE THAT WANTS TO BE VERIFIED*)
	 * @param candidates - List of Candidates
	 * @return List of ballots organized, [0] are blank, [1] invalid, [2] total
	 */
	private static int[] ballotCounter(String filePath, ArrayList<Candidate> candidates) {
		int[] result = {0,0,0}; //[0] = blank Ballots, [1] = invalid Ballots, [2] = total Ballots
		BufferedReader file = fileReader(filePath);
		try {
			String line;
			while((line = file.readLine()) != null) {
				Ballot currentBallot = new Ballot(line, candidates);
				if (currentBallot.isBlank()) {
					result[0]++;
				}
				else if (!currentBallot.isValid()) {
					result[1]++;
				}
				result[2]++;
			}
			file.close();
		} catch (IOException e) {
				e.printStackTrace();
			}
		return result;
	}
	
	
	/**
	 * Organizing Ballots Method
	 * Saves in a list another n lists with the ballots in which the candidates has a #1 rank and organizes them by candidate ID
	 * 
	 * @param ballots
	 * @param candidates
	 * @return a list containing n, being the # of candidates, lists of ballots organized by candidate ID
	 */
	private static ArrayList<ArrayList<Ballot>> ballotOrganizer(ArrayList<Ballot> ballots, ArrayList<Candidate> candidates) {
		
		ArrayList<ArrayList<Ballot>> result = new ArrayList<ArrayList<Ballot>>();
		
		for (int i = 0; i < candidates.size(); i++) { //goes through the candidates and if there's a rank #1 candidate then it saves it in position (ID -1)
			int candidateId = candidates.get(i).getCandidateID();
			
			ArrayList<Ballot> storingBallots = new ArrayList<Ballot>();
			
			for (int j = 0; j < ballots.size(); j++) {
				Ballot currentBallot = ballots.get(j);
				if (currentBallot.isValid() && !currentBallot.isBlank()) { 
					if (currentBallot.getRankByCandidate(candidateId) == 1) { //Saves ballot if the rank is #1
						storingBallots.add(currentBallot);
					}
				}
			}
			result.add(i, new ArrayList<Ballot>(storingBallots)); 
		}
		return result;
	}
	
	
	/**
	 * File reader method
	 * 
	 * this method... reads files shocker 
	 * 
	 */
	private static BufferedReader fileReader(String filePath) {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return file;
	}


	/**
	 * Candidate creator
	 * 
	 * This method takes the filePath and reads through it to identify the Candidates
	 * Once found they are stored and a list and returns said list
	 * 
	 * @param filePath - path of file to be read (*MUST BE UPDATED IF NEW FILE IS CREATED*)
	 * @return List of candidates in the document
	 */
	private static ArrayList<Candidate> candidateCreator(String filePath) {
		
		ArrayList<Candidate> list = new ArrayList<Candidate>();
		
		BufferedReader file = fileReader(filePath);
		try {
			String line;
			while((line = file.readLine()) != null) { 
				int coma = line.indexOf(","); //With this we assure that the candidateName is separated from their ID and stored properly
				String name = line.substring(0, coma);
				int id = Integer.parseInt(line.substring(coma + 1));
				list.add(new Candidate(name, id));
			}
			file.close();
		} catch (IOException e) {
				e.printStackTrace();
			}
		return list;
	}


	/**
	 * Ballot Creator Method
	 * 
	 * This method reads a file, creates the ballots and adds them to a list.
	 * 
	 * @param filePath - path of file to be read (*MUST BE UPDATED IF NEW FILE IS CREATED*)
	 * @param candidates - List with candidates
	 * @return List with all the ballots found in the document
	 */
	public static ArrayList<Ballot> ballotCreator(String filePath, ArrayList<Candidate> candidates) {
		
		BufferedReader file = fileReader(filePath);
		ArrayList<Ballot> list = new ArrayList<Ballot>();
		
		try {
			String line;
			while((line = file.readLine()) != null) {
				list.add(new Ballot(line, candidates));
			}
			file.close();
		} catch (IOException e) {
				e.printStackTrace();
			}
		return list;
	}

}
