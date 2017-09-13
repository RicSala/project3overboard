package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.*;

public class UserTest {

	private Board board;
	private User questioner;
	private User answerer;
	private User anonymous;     //Just to improve readability (thanks that nor the questioner neither the answerer are required)
	private Question question;
	private Answer answer;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {

		//Arrange --> I consider all  of this to be part of the "Arrange" part! :)

		this.board = new Board("Understanding the mocking artifacts");
		this.questioner = new User(board, "Ric");
		this.question = new Question(questioner, "Where can I learn more about mocking frameworks?");
		this.answerer = new User(board, "Craig");
		this.answer = new Answer(question, answerer, "Reviewing our teacher notes!");
		this.anonymous = new User(board, "Mr. Unknown");

	}


	@After
	public void tearDown() throws Exception {
		//Nothing to be done after each tests! :)
	}


	@Test
	public void questionersReputationGoesUpBy5WhenUpVoted() throws Exception {
		//Arrange
		board.addQuestion(question);    //We are not testing this, so it is part of the arrange even if we "act"

		//Act
		answerer.upVote(question);      //This is really the act that we want to assert!

		//Assert
		assertThat(questioner.getReputation(), is(5));

	}

	@Test
	public void answererReputationGoesUpBy10WhenUpVoted() throws Exception {
		//Arrange
		board.addQuestion(question);
		board.addAnswer(answer);

		//Act
		questioner.upVote(answer);

		//Assert
		assertThat(answerer.getReputation(), is(10));


	}

	@Test
	public void havingAnAnswerAcceptedIncreaseReputationBy15() throws Exception {
		//Arrange
		board.addQuestion(question);
		board.addAnswer(answer);

		//Act
		questioner.acceptAnswer(answer);

		//Assert
		assertThat(answerer.getReputation(), is(15));


	}

	@Test(expected = VotingException.class)
	public void selfVotingIsNotAllowedinQuestions() throws Exception { //Even if both are posts, I feel safer testing both...
		//Arrange
		board.addQuestion(question);

		//Act
		questioner.upVote(question);

		//Assert --> This is assert "automatically" thanks to the annotation argument "expected = VotingException").

	}

	@Test(expected = VotingException.class)
	public void selfVotingIsNotAllowedinAnswers() throws Exception { //Even if both are posts, I feel safer testing both...
		//Arrange
		board.addQuestion(question);
		board.addAnswer(answer);

		//Act
		answerer.upVote(answer);

		//Assert --> This is assert "automatically" thanks to the annotation argument "expected = VotingException").
	}

	@Test
	public void onlyOriginalQuestionerCanAcceptAnswer() throws Exception {
		thrown.expect(AnswerAcceptanceException.class);     //Expected exception
		String excMessage = "Only Ric can accept this answer as it is their question";
		thrown.expectMessage(excMessage);                   //Expected exc message

		//Arrange
		board.addQuestion(question);
		board.addAnswer(answer);

		//Act
		anonymous.acceptAnswer(answer);

		//Assert --> This is assert "automatically" thanks to the annotation argument "expected = VotingException").
	}

	@Test
	public void questionerReputationRemainsTheSameWhenDownVoted() throws Exception {
		//Arrange
		board.addQuestion(question);

		//Act
		anonymous.downVote(question);

		//Assert
		assertThat(questioner.getReputation(), is(0));
	}

	@Test
	public void answererReputationDecreaseBy1WhenDownVoting() throws Exception {
		//Arrange
		board.addQuestion(question);
		board.addAnswer(answer);

		//Act
		anonymous.downVote(answer);

		//Assert
		assertThat(answerer.getReputation(), is(-1));
	}
}