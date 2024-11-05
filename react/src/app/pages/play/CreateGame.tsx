import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import React from "react";
import userType from "../../types/UserType";
import NewGameDto from "../../dto/NewGameDto";


type createGameProps = {
	setPage: (page: string) => void,
	user: userType | undefined,
	userToken: string,
	refreshUser: () => Promise<void>
}

const CreateGame = ({setPage, user, userToken, refreshUser}: createGameProps) => {

	const navigate = useNavigate();
	const [showTextBox, setShowTextBox] = useState<boolean>(false);
	const [gameId, setGameId] = useState<number>();
	const [gameName, setGameName] = useState<string>();

    useEffect(() => {

		setPage("/play/new-game/create");
		setGameId(undefined);
		setGameName(undefined);
		
  	}, [setPage, userToken, navigate,]);

	function handleClickCreate() {
		setShowTextBox(true);
	}

	async function handleSubmit(event: React.FormEvent): Promise<void> {
	
        event.preventDefault();

        let formData = new FormData(event.target as HTMLFormElement);

        let name: string = formData.get('inputName') as string;
		let goals: string = formData.get('inputGoals') as string;
		let prizes: string = formData.get('inputPrizes') as string;

		if(user){
			var adminId: number = user.id;
		} else {
			console.error("User id could not be found");
			navigate("/login");
			return;
		}

		fetch('/api/game/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
				'Authorization': userToken
                },
            body: JSON.stringify(new NewGameDto(adminId, name, goals, prizes))
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
			setGameId(parseInt(data.gameId));
			setGameName(data.name);
		})
		.catch((e) => console.error(e));
	}

    return(

		<div>
			{!gameId ?
			
			<div className="container col-md-7">

				<div className="text-center">

					<h2>Starting a game is free and easy.</h2>

					<p className="lead">Click below to create a new game.
					This option is for <strong>game administrators</strong>,
					those challenging their loved ones to complete weekly tasks.</p>

				</div>

				
				<div className="container col">

					{!showTextBox ?
					<button type="button" className="btn btn-success mb-2 w-100 py-3 fs-5 text-white" onClick={handleClickCreate}>Create new game</button>
					:
					<form onSubmit={handleSubmit}>
						<div className="col">
							<div>
								<label htmlFor="inputName" className="mx-2">
									Name your game
								</label>
								<textarea
									id="inputName" 
									name="inputName" 
									className="form-control mb-3" 
									placeholder="ex. Gigi's Grand Challenges!"
									rows={1}
									maxLength={255}
									required={true}>
								</textarea>
								</div>
							<div>
								<label htmlFor="inputGoals" className="mx-2">
									How do you hope the person you're challenging will grow?
								</label>
								<textarea
									id="inputGoals" 
									name="inputGoals" 
									className="form-control mb-3"
									placeholder="ex. get stronger, eat healthier..."
									rows={3}
									maxLength={255}
									required={true}>
								</textarea>
							</div>
							<div>
								<label htmlFor="inputPrizes" className="mx-2">
									What prize ideas do you have?
								</label>
								<textarea
									id="inputPrizes" 
									name="inputPrizes" 
									className="form-control mb-3" 
									placeholder="ex. dinner together, movie night, visit uncle Joe..."
									rows={3}
									maxLength={255}
									required={true}>
								</textarea>
							</div>
							<div className="mt-2">
								<button type="submit" className="btn btn-success my-2 py-3 px-1 fs-10 w-100">Submit</button>
							</div>
						</div>
					</form>
					}
					<Link type="button" className="btn btn-outline-success my-1 py-2 px-4 fs-6 w-100" to="/play/new-game">Go back</Link>
				</div>
			</div>
			:
			<div className="container col-md-5 text-center">
				<div className="alert alert-success" role="alert">
					<h4 className="fs-2"><strong>All right!</strong></h4>
					<p><strong>{gameName}</strong> has been created!
					<br></br>
					Send the code below to your player for them to join.</p>
					<hr></hr>
					<p className="fs-2">{gameId}</p>
				</div>
			</div>
			}
		</div>
  
    );
}
export default CreateGame;