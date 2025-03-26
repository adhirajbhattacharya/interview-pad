package com.adhiraj.dsa.lld.second;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.*;
import java.util.stream.Collectors;

import static com.adhiraj.dsa.lld.second.Rank.*;

/**
 * There are multiple candidates participating in a competition. Winners will be
 * judged by a group of judges. Who will provide rank 1,2,3 to their top 3 candidates.
 * Based on the votes a candidate receives his score will be calculated.
 * Given a list of ballot of votes by each judge, return the candidates in order of their score.
 *
 * Model the problem to easily extensible.
 *
 * Q1. How to handle a tie? - Provide in any order as of now
 *
 * Follow up:
 * 1. Now consider following tie-breaker and provide answer.
 *      Tie-Breaker:
 *          if POINTS same
 *              consider RANK_1 count
 *          if RANK_1 count same
 *              consider RANK_2 count
 *          .... till RANK_n
 *          return name lexicologically
 * 2. What if we decide, judges can give from RANK_1 to RANK_5
 *      Had to implement RANK enum and List<Vote> in the Ballot class. Previously they were missing
 */

public class ElectionResult {

    public static void main(String[] args) {
        List<Ballot> ballots = new ArrayList<>();
        ballots.add(new Ballot(new BallotVote("A", RANK_1), new BallotVote("E", RANK_2), new BallotVote("F", RANK_3)));
        ballots.add(new Ballot(new BallotVote("B", RANK_1), new BallotVote("C", RANK_2), new BallotVote("A", RANK_3)));
        ballots.add(new Ballot(new BallotVote("C", RANK_1), new BallotVote("B", RANK_2), new BallotVote("C", RANK_3)));
        ballots.add(new Ballot(new BallotVote("D", RANK_1), new BallotVote("Z", RANK_2), new BallotVote("F", RANK_3)));

        List<String> candidates = new ElectionResult().getResults(ballots);
        System.out.println(candidates);
    }

    public List<String> getResults(List<Ballot> ballots) {
        Map<String, CandidateScore> map = new HashMap<>();

        ballots.stream().flatMap(ballot -> ballot.getVotes().stream())
                .forEach(ballotVote -> updateCandidateScore(ballotVote, map));

        return map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .sorted((can1, can2) -> new CandidateScoreComparator().compare(can1.getValue(), can2.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void updateCandidateScore(BallotVote vote, Map<String, CandidateScore> map) {
        String candidateName = vote.getName();
        CandidateScore currentScore = map.getOrDefault(candidateName, new CandidateScore(candidateName));
        currentScore.setScore(currentScore.getScore() + vote.getRank().getPoint());
        map.put(candidateName, currentScore);
    }
}

@Value
class Ballot {
    List<BallotVote> votes;

    Ballot(BallotVote... votes) {
        if (votes == null || votes.length != Rank.values().length) {
            throw new IllegalArgumentException(
                    String.format("Incorrect Number of Votes -> Expected %d, Actual %d",
                            Rank.values().length, votes == null ? 0 : votes.length));
        }
        this.votes = Arrays.stream(votes).collect(Collectors.toList());
    }
}

@Value
class BallotVote {
    String name;
    Rank rank;

    BallotVote(String name, Rank rank) {
        this.name = name;
        this.rank = rank;
    }
}

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
enum Rank {
    RANK_1(3), RANK_2(2), RANK_3(1);

    static List<Rank> ranks = null;
    int point;
    private Rank(int point) {
        this.point = point;
    }

    static List<Rank> getAllSortedRanks() {
        if (ranks == null) {
            ranks = Arrays.stream(Rank.values()).sorted((r1, r2) -> Integer.compare(r1.getPoint(), r2.getPoint()) * (-1)).collect(Collectors.toList());
        }
        return ranks;
    }
}

@Value
class CandidateScore {
    String name;
    Map<Rank, Integer> rankCount;
    @NonFinal @Setter
    int score;

    CandidateScore(String name) {
        this.name = name;
        this.rankCount = new HashMap<>();
        this.score = 0;
    }
}

class CandidateScoreComparator implements Comparator<CandidateScore> {
    public int compare(CandidateScore cs1, CandidateScore cs2) {
        int cmp = Integer.compare(cs1.getScore(), cs2.getScore()) * (-1);
        if (cmp != 0) return cmp;

        for(Rank rank : Rank.getAllSortedRanks()) {
            cmp = getInteger(cs1, rank);
            if (cmp != 0) return cmp;
        }

        return String.CASE_INSENSITIVE_ORDER.compare(cs1.getName(), cs2.getName());
    }

    private int getInteger(CandidateScore cs1, Rank rank) {
        int count1 = cs1.getRankCount().getOrDefault(rank, 0);
        int count2 = cs1.getRankCount().getOrDefault(rank, 0);
        return Integer.compare(count1, count2) * (-1);
    }
}
