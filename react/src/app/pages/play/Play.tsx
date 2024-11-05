import { useEffect, useState } from "react";
import { Link, NavigateFunction, useNavigate } from "react-router-dom";
import React from "react";
import userType from "../../types/UserType";
import gameType from "../../types/GameType";
import challengeType from "../../types/ChallengeType";

type playProps = {
  setPage: (page: string) => void,
  user: userType,
  userToken: string,
  refreshUser: () => Promise<void>,
  setChallenges: (challenges: challengeType[] | undefined) => void
}


const Play = ({ setPage, user, userToken, refreshUser, setChallenges}: playProps) => {

    const navigate = useNavigate();
	const [games, setGames] = useState<gameType[]>([]);

    useEffect(() => {
       
		setPage("/play");
       
    }, []);

	useEffect(() => {

		if(user) {
			getGames(user.id, userToken, setGames, navigate);
		}
	}, [user])

	async function handleClick(game: gameType) {

		let requestUrl: string = '/api/challenge/generate-weekly/' + game.gameId;

		fetch(requestUrl, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				'Authorization': userToken
			}
		})
		.then((response) => response.json())
		.then((data) => {
			let easyChallenge: challengeType = {
				challengeText: data.easy,
				difficulty: "easy"};
			let medChallenge: challengeType = {
				challengeText: data.med,
				difficulty: "med"};
			let hardChallenge: challengeType = {
				challengeText: data.hard,
				difficulty: "hard"};

			let challengeArr = [easyChallenge, medChallenge, hardChallenge];
			
			setChallenges(challengeArr);
			navigate('challenges');

		});


		//

	}



	async function getGames(
		id: number, 
		userToken: string, 
		setGames: (games: gameType[]) => void, 
		navigate: NavigateFunction
		): Promise<void>

		{

		const requestUrl = '/api/game/by-user/' + id.toString();
	
		fetch(requestUrl, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				'Authorization': userToken
			}
		})
		.then((response) => {
			if(response.ok) {
				return response.json();
			}
			if(response.status === 403) {
				try {
					refreshUser();
				} catch {
					throw new Error('Error while refreshing session.');
				}
				throw new Error('Session expired, attempting refresh...');
			}
		})
		.then((data) => {
			setGames(data);
			if(data.length<1){
				navigate('new-game');
			}
		})
		.catch((e) => {
			console.log(e);
		});
	}

	const gameList = (games: gameType[]) => {
		let gameListElement: React.JSX.Element[] = []
		games.forEach(game => {
			if(game.playerId){
				gameListElement.push(<button className="alert alert-success w-100" key={game.gameId} onClick={() => handleClick(game)}> 
					<strong>{game.name} </strong>
					<br></br>
					Your role: <strong>{game.adminId===user.id ? "admin" : "player"}</strong>
					<br></br>
					Let's get started!
					game Id: {game.gameId}. playerId: {game.playerId}. admin Id: {game.adminId}.
					</button>);
			} else {
				gameListElement.push(<div className="alert alert-warning w-100" key={game.gameId}> 
					<strong>{game.name}</strong>
					<br></br>
					Your role: <strong>{game.adminId===user.id ? "admin" : "player"}</strong>
					<br></br>
					No one has joined this game. Share the code
					<strong> {game.gameId}</strong> with the player to get started.</div>);
			}
		});
		
		return gameListElement;
	}


    return(
            <div className="container col-12 col-sm-10 col-md-9 col-lg-7 text-center">
				{user && games.length > 0 &&
				<div>
					<h1>Hi, {user.first}.</h1>
					<p className="lead">Choose a game below to play!</p>
					{gameList(games)}
					<Link type="button" className="btn btn-outline-success mt-2 px-3 py-2" to="new-game">Add another game</Link>
				</div>
				}
            </div>
    );
}
export default Play;