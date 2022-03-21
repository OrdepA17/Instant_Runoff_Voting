package Main;

import dataStructures.List.ArrayList;
import java.io.*; //used to access file writer, reader, ect

/**
 * The Election class
 * 
 * Where we decide who wins and is our new president base on the information provided
 * 
 * @author: Ordep E. Agustin (@OrdepA17)
 * @version: 2.0
 * @since 2022-03-20
 */
public class Election {
	
	public static File results = new File("./inputFiles/results.txt");
	public static ArrayList<String> toWrite = new ArrayList<String>();
	public static int totalCandidates;
	
public static void main(String[] args) throws IOException {
		
		ArrayList<Candidate> candidates = readCandidates("./inputFiles/candidates.csv");
		totalCandidates = candidates.size();
		ArrayList<Ballot> ballots = readBallots("./inputFiles/ballots2.csv");
		int totalBallots = ballots.size();
		ArrayList<Ballot> validBallots = getReadableBallots(ballots);
		
		createTops(candidates, validBallots);
		
		int totalValidBallots = validBallots.size();
		int invalidBallots = countInvalids(ballots);
		int blankBallots = countBlanks(ballots);
		
		toWrite.add("Number of ballots: " + totalBallots);
		toWrite.add("Number of blank ballots: " + blankBallots);
		toWrite.add("Number of invalid ballots: " + invalidBallots);

		
		
		int round = 1;
		Candidate winner = null;
		
		while(round <= candidates.size()-1 && winner == null) {
			
			ArrayList<Integer> sizesForRankOnes = new ArrayList<>();
			for(Candidate c: candidates) {
				
				if(c.getFirstInBallot().size() > totalValidBallots/2) {
					winner = c;
					
				if(!c.isActiveCandidate()) { sizesForRankOnes.add(-1); continue; }
				
				sizesForRankOnes.add(c.getFirstInBallot().size());
				break;
			}
			
			ArrayList<Candidate> tied = new ArrayList<>();
			
			int min = getMin(sizesForRankOnes);
		
			if(sizesForRankOnes.firstIndexOf(min) == sizesForRankOnes.lastIndexOf(min) && sizesForRankOnes.contains(min)) {
				Candidate candidateToEliminate = candidates.get(sizesForRankOnes.firstIndexOf(min));
				candidateToEliminate.eliminatedCandidate();
				ArrayList<Ballot> ballotsToEdit = candidateToEliminate.getFirstInBallot();
				
				for(Ballot b: validBallots) {
					b.eliminate(candidateToEliminate.getCandidateID());
				}
				
				String s = 
						"Round " + round + ": " + candidateToEliminate.getCandidateName() + " was eliminated with "
						+ candidateToEliminate.getFirstInBallot().size() + " #1's";
				
				toWrite.add(s);
				
				
				
				createTops(candidates, validBallots);
				round++;
				continue;
			}
			
			//Will only run if there is a tie with the votes
			Candidate c1 = candidates.get(sizesForRankOnes.firstIndexOf(min));
			Candidate c2 = candidates.get(sizesForRankOnes.lastIndexOf(min));
			
			tied.add(c1);
			tied.add(c2);
			
			checkRanksForTiedCandidates(tied, validBallots, candidates, round);
			round++;
		}
			
		toWrite.add("Winner: " + winner.getCandidateName() + " wins with " + winner.getFirstInBallot().size() + " #1's");
		writeToFile();

	}
}
	
	
	
	private static int countBlanks(ArrayList<Ballot> allBallots) {
		int total = 0;
		for(Ballot b: allBallots) {
			if(b.isBlankBallot()) total++;
		}
		return total;
	}



	private static int countInvalids(ArrayList<Ballot> allBallots) {
		int total = 0;
		for(Ballot b: allBallots) {
			if(!b.isValid()) total++;
		}
		return total;
	}



	private static void checkRanksForTiedCandidates(ArrayList<Candidate> tied, ArrayList<Ballot> allBallots, ArrayList<Candidate> candidates, int round) {
		
		
		int currentRank = 2;
		while(currentRank <= candidates.size()) {
			
			ArrayList<Integer> sizes = new ArrayList<>();
			
			for(Candidate c: tied) {
				int ballotsThatMatch = 0;
				for(Ballot b: allBallots) {
					if(b.getCandidateByRank(currentRank) == c.getCandidateID()) ballotsThatMatch++;
				}
				sizes.add(ballotsThatMatch);
			}
			
			
			int min = getMin(sizes);
			
			if(sizes.firstIndexOf(min) == sizes.lastIndexOf(min) && sizes.contains(min)) {
				Candidate candidateToEliminate = tied.get(sizes.firstIndexOf(min));
				
				candidateToEliminate.eliminatedCandidate();
				for(Ballot b: allBallots) {
					b.eliminate(candidateToEliminate.getCandidateID());
				}
				
				String s =
						"Round " + round + ": " + candidateToEliminate.getCandidateName() + " was eliminated with "
						+ candidateToEliminate.getFirstInBallot().size() + " #1's";
				toWrite.add(s);
				
				createTops(candidates, allBallots);
				break;
			}
			currentRank++;
		}
		
		if(currentRank > 5) {
			int ID1 = tied.get(0).getCandidateID();
			int ID2 = tied.get(1).getCandidateID();
			Candidate c;
			
			if(ID1 > ID2) {
				c = tied.get(0);
				c.eliminatedCandidate();
				for(Ballot b: allBallots) {
					b.eliminate(ID1);
				}
				
				String s =
						"Round: " + round + ": " + c.getCandidateName() + " was eliminated with "
						+ c.getFirstInBallot().size() + " #1's";
				toWrite.add(s);
				
				createTops(candidates, allBallots);
			} else {
				c = tied.get(1);
				c.eliminatedCandidate();
				for(Ballot b: allBallots) {
					b.eliminate(ID2);
				}
				
				String s = 
						"Round: " + round + ": " + c.getCandidateName() + " was eliminated with "
						+ c.getFirstInBallot().size() + " #1's";
				toWrite.add(s);
				
				createTops(candidates, allBallots);
			}	
		}
	}



	public static ArrayList<Ballot> readBallots(String pathToBallots) throws IOException {
		
		ArrayList<Ballot> newBallots = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(pathToBallots))) {
			String line;
			while((line = br.readLine()) != null) {
				String[] ballot = line.split(","); //reads until , and then splits
				ArrayList<Integer> ids = new ArrayList<Integer>(); //store ballot preferences
				int ballotId = Integer.parseInt(ballot[0]); //first element will always be ballot id according to csv format
				ArrayList<Integer> preferences = new ArrayList<>(totalCandidates);
				
				boolean initiallyValid = true;
				for(int i = 1; i < ballot.length; i++) {
					int id = Integer.parseInt(ballot[i].split(":")[0]);
					int pref = Integer.parseInt(ballot[i].split(":")[1]);
					
					if(pref > totalCandidates || preferences.contains(pref) || ids.contains(id)) {
						initiallyValid = false;
					}
					
					
					
					//split at semicolon since format is candidate:preference, takes only the candidate id ranking
					ids.add(id);
					preferences.add(pref);
				}
				Ballot b = new Ballot(ballotId, ids);
				
				if(!initiallyValid) b.setBallotValidity(false);
				
				for(int i = 0; i < ballot.length-1; i++) {
					if(i+1 != preferences.get(i)) b.setBallotValidity(false);
				}
				
				newBallots.add(b);
			}
			
			return newBallots;
			
		} catch(FileNotFoundException e) { //in case file path is not valid or file does not exist
			System.out.println(e + ": Cannot find such file. Returning null");
			return null;
			
		}
	}
	
	public static ArrayList<Candidate> readCandidates(String pathToCandidates) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(pathToCandidates))) {
			
			String line;
			ArrayList<Candidate> candidates = new ArrayList<>();
			while((line = br.readLine()) != null) {
				String[] candidateString = line.split(","); //split at cvs delimiter
				Candidate newCandidate = new Candidate(candidateString[0], Integer.parseInt(candidateString[1])); //[0] is name, [1] is id
				candidates.add(newCandidate);
			}
			
			return candidates;
			
			
			
		} catch(FileNotFoundException e) { //in case file path is not valid or file does not exist
			System.out.println(e + ": Cannot find such file. Returning null");
			return null;
		}
	}
	
	
	public static int getMax(ArrayList<Integer> nums) {
		int max = nums.get(0);
		for (int i = 0; i < nums.size(); i++) {
			if(nums.get(i) > max) max = nums.get(i);
		}
		return max;
	}
	
	public static int getMin(ArrayList<Integer> nums) {
		int min = getMax(nums);
		for (int i = 0; i < nums.size(); i++) {
			if(nums.get(i) < min && nums.get(i) != -1) min = nums.get(i);
		}
		return min;
	}
	
	public static void createTops(ArrayList<Candidate> candidates, ArrayList<Ballot> allBallots) {
		
		for(Candidate c: candidates) {
			ArrayList<Ballot> tops = new ArrayList<>();
			for(Ballot b: allBallots) {
				
				if(!b.isValid()) continue;
				if(b.isBlankBallot()) continue;
				
				if(!c.isActiveCandidate()) break;
				if(b.getCandidateByRank(1) == c.getCandidateID()) tops.add(b);
			}
			c.setFirstInBallot(tops);
		}
		
	}
	
	public static ArrayList<Ballot> getReadableBallots(ArrayList<Ballot> allBallots) {
		ArrayList<Ballot> countable = new ArrayList<>();
		
		for(Ballot b: allBallots) {
			if(	b.isValid() && !b.isBlankBallot()) countable.add(b);
			break;
		}
		return countable;
		
	}
	
	public static void writeToFile() {
	    try {
	    	FileWriter writer = new FileWriter(results);
	    	for(String s: toWrite) {
		        writer.write(s + System.lineSeparator());
		        System.out.println("Successfully wrote to the file.");
	    	}
	    	writer.close();
		       
	    } catch (IOException e) {
	        System.out.println("An unexpected error has occurred.");
	        e.printStackTrace();
	    }
	}
}