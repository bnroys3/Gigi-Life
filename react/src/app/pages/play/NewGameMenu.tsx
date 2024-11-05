import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import React from "react";
import userType from "../../types/UserType";
import profileType from "../../types/ProfileType";


type newGameProps = {
	setPage: (page: string) => void,
	user: userType | undefined,
	userToken: string,
	fetchProfile: (user: userType) => Promise<profileType | null>,
	profile: profileType | undefined
}

const NewGameMenu = ({setPage, user, userToken, fetchProfile, profile}: newGameProps) => {

    useEffect(() => {
      	setPage("/play/new-game");

		fetchProfile(user as userType)

  	}, []);



    return(

      <div className="container col-sm-10 text-center">

        <h2>Starting a game is free and easy.</h2>
        <p className="lead">Play for however long you would like with no cost.</p>

        <div className="container col-sm-9 col-md-7 col-lg-5 text-center">

			<p className="form-text m-1">Select the option that best describes you.</p>
			<Link type="button" className="btn btn-success mb-2 w-100 py-3 fs-5 text-white" to={profile ? "join" : "/profile"}>I was invited to play</Link>
			<Link type="button" className="btn btn-outline-success w-100 py-3 fs-5" to="create">I'm challenging a loved one to play</Link>

        </div>

    </div>
  
    );
}
export default NewGameMenu;