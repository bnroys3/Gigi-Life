import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import React from "react";
import userType from "../../types/UserType";
import gameType from "../../types/GameType";
import JoinGameDto from "../../dto/JoinGameDto";


type joinGameProps = {
	setPage: (page: string) => void,
	user: userType | undefined,
	userToken: string,
  refreshUser: () => Promise<void>
}

const JoinGame = ({setPage, user, userToken, refreshUser}: joinGameProps) => {

	const navigate = useNavigate();
  const [game, setGame] = useState<gameType>();
  const [errorMessage, setErrorMessage] = useState<string>();

  useEffect(() => {
    setPage("/play/new-game/join");

  }, []);

  async function handleConfirm(event: React.FormEvent): Promise<void> {
    
    event.preventDefault();

    fetch('/api/game/join', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': userToken
      },
      body: JSON.stringify(new JoinGameDto(game?.gameId as number, user?.id as number))
    })
    .then((response) => {
			if(response.ok) {
				navigate('/play');
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
    .catch((e) => {
      console.error(e);
      setErrorMessage("Error joining the game. Please try again.");
    });
  }

  async function handleSubmit(event: React.FormEvent): Promise<void> {

    event.preventDefault();

    let formData: FormData = new FormData(event.target as HTMLFormElement);

    let gameId: string = formData.get('inputGameId') as string;

    const requestUrl = '/api/game/by-game/' + gameId.toString();

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
      if(data && data.name && !data.playerId && data.adminId !== user?.id){
        setGame(data);
        setErrorMessage("");
      } else if(!data.name){
        setErrorMessage("That game was not found. Confirm your game code with your administrator.");
      } else if(data.adminId === user?.id) {
        setErrorMessage("You can't join your own game!");
      } else {
        setErrorMessage("Someone's already joined that game! Confirm your game code with your administrator.");
      }
    })
    .catch((e) => {
      console.error(e);
      setErrorMessage("The game code you entered is invalid. Confirm your game code with your administrator.");
    });
  }

  return(

    <div className="container col-md-7 text-center">

      <h2>Join a game.</h2>
      <p className="lead">Enter the code that was shared with you.
      This option is for <strong>players</strong>, those who will be receiving weekly challenges.</p>

      {errorMessage &&
      <div className="alert alert-danger my-2 w-100 px-4 py-2" role="alert">
        <strong>Yikes!</strong> {errorMessage}
      </div>}


      <div className="container col-sm-9 col-lg-7 col-xl-6 text-center">

        {!game &&
        <form className="text-center" onSubmit={handleSubmit}>
          <div className="row g-2">
            <div className="col-8">
              <div className="form-floating my-2 ">
                  <input type="text"
                      id="inputGameId" 
                      name="inputGameId" 
                      className="form-control" 
                      placeholder="0000"
                      required={true}>
                  </input>
                  <label htmlFor="inputGameId" className="form-text">Game code</label>
              </div>
            </div>
            <div className="col-4">
              <button type="submit" className="btn btn-success my-2 py-3 px-1 fs-10 w-100">Submit</button>
            </div>
          </div>
        </form>}

        {game &&
        <form className="text-center" onSubmit={handleConfirm}>
        <div className="alert alert-success my-2 w-100 px-4 py-2" role="menuitem">
          <div className="row">
            <div className="col">
              <strong>Awesome!</strong> We found a game called <strong>{game.name}</strong>.
            </div>
            <div className="col-5">
              <button type="submit" className="btn btn-success my-2 py-3 px-2 fs-10 w-100">Join!</button>
            </div>
          </div>

        </div>
      </form>}
      

        <Link type="button" className="btn btn-outline-success my-1 py-2 px-4 fs-6" to="/play/new-game">Go back</Link>

      </div>

    </div>

  );
}
export default JoinGame;