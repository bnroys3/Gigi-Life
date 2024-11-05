import React, { useState } from "react"
import userType from "../types/UserType";
import { useNavigate } from "react-router-dom";
import NewProfileDto from "../dto/NewProfileDto";

type profileProps = {
    user: userType | undefined,
    userToken: string,
    refreshUser: () => Promise<void>
}

const Profile = ({user, userToken, refreshUser}: profileProps) => {

    const [errorMessage, setErrorMessage] = useState<string>();
    const navigate = useNavigate();

    async function handleSubmit(event: React.FormEvent): Promise<void> {
	
        event.preventDefault();

        let formData = new FormData(event.target as HTMLFormElement);

        let ageStr: string = formData.get('inputAge') as string;
        ageStr = ageStr.trim();
        let age: number = parseInt(ageStr);
        if(isNaN(age)) {
            setErrorMessage('Please enter a valid age.');
            return;
        }

        let hobbies: string = formData.get('inputHobbies') as string;
		let goals: string = formData.get('inputGoals') as string;
		let prizeIdeas: string = formData.get('inputPrizes') as string;
		let mobility: string = formData.get('inputMobility') as string;
        if(!mobility) {
            mobility = "N/A";
        }
		let disabilities: string = formData.get('inputDisability') as string;
        if(!disabilities) {
            disabilities = "N/A";
        }

		if(user){
			var userId: number = user.id;
		} else {
			console.error("User id could not be found");
			navigate("/login");
			return;
		}

        const requestUrl = '/api/player/put';
		fetch(requestUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
				'Authorization': userToken
                },
            body: JSON.stringify(new NewProfileDto(
                userId,
                age,
                disabilities,
                goals,
                hobbies,
                mobility,
                prizeIdeas))
        })
		.then((response) => {
			if(response.ok) {
                navigate('/play/new-game/join')
				return;
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
            setErrorMessage(e);
            console.error(e)
        });
	}

    return (

        <form onSubmit={handleSubmit}>
            
            <div className="container col-md-7">

                <h1 className="mb-2 text-center">Profile</h1>

                <p className="mb-1 lead text-center">You'll need to set this up before playing a game.</p>


                <div className="col">

                    {errorMessage && 
                    <div className="alert alert-danger my-2 w-100 px-4 py-2 text-center" role="alert">
                        <strong>Whoops!</strong> {errorMessage}
                    </div>}

                    <div>
                        <label htmlFor="inputAge" className="mx-2">
                            How old are you?
                        </label>
                        <textarea
                            id="inputAge" 
                            name="inputAge" 
                            className="form-control mb-3" 
                            placeholder="82"
                            rows={1}
                            maxLength={3}
                            required={true}>
                        </textarea>
                    </div>
                    <div>
                        <label htmlFor="inputHobbies" className="mx-2">
                            What are your hobbies?
                        </label>
                        <textarea
                            id="inputHobbies" 
                            name="inputHobbies" 
                            className="form-control mb-3"
                            placeholder="ex. bridge, poetry, walking..."
                            rows={3}
                            maxLength={255}
                            required={true}>
                        </textarea>
                    </div>
                    <div>
                        <label htmlFor="inputGoals" className="mx-2">
                            How would you like to grow?
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
                            What types of prizes would you like to enjoy with the one who invited you?
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
                    <div>
                        <label htmlFor="inputMobility" className="mx-2">
                            Please specify any pain or mobility issues you experience, or leave blank if N/A.
                        </label>
                        <textarea
                            id="inputMobility" 
                            name="inputMobility" 
                            className="form-control mb-3" 
                            placeholder="ex. mild hip pain when walking over half a mile"
                            rows={3}
                            maxLength={255}
                            required={false}>
                        </textarea>
                    </div>
                    <div>
                        <label htmlFor="inputDisability" className="mx-2">
                            Please specify any other conditions that could impact your ability to complete challenges, or leave blank if N/A.
                        </label>
                        <textarea
                            id="inputDisability" 
                            name="inputDisability" 
                            className="form-control mb-3" 
                            placeholder="ex. can't eat red meat, don't drive myself"
                            rows={3}
                            maxLength={255}
                            required={false}>
                        </textarea>
                    </div>
                    <div className="mt-2">
                        <button type="submit" className="btn btn-success my-2 py-3 px-1 fs-10 w-100">Submit</button>
                    </div>
                </div>
            </div>
        </form>
    );
}
export default Profile;