import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import LoginDto from "../dto/LoginDto";
import userType from "../types/UserType";

type loginProps = {
    setPage: (page: string) => void,
    setUserToken: (userToken: string) => void,
    setUser: (user: userType | undefined) => void,
    fetchUser: (userToken: string, setUserFn: (user: userType | undefined) => void) => Promise<void>
}

const Login = ({ setPage, setUserToken, setUser, fetchUser}: loginProps) => {

    const [errorMessage, setErrorMessage] = useState<string>("");
    const navigate = useNavigate();

    useEffect(() => {
        setPage("/login")
    });

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>): Promise<void> {

        event.preventDefault();

        let formData: FormData = new FormData(event.target as HTMLFormElement);
        let email: string = formData.get('inputEmail') as string;
        let password: string = formData.get('inputPassword') as string;


        fetch('/api/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', 
                },
            body: JSON.stringify(new LoginDto(email,password))
        })
        .then((response) => {
            if(response.ok) {
                setErrorMessage("");
                console.log(response);
                return response.json();
            } else {
                setErrorMessage("The email or password you entered didn't match our records.");
                console.log("Login failed.");
                return Promise.resolve();
            }
        })
        .then((data) => {
            let newToken: string = "Bearer " + data.sessionToken;
            setUserToken(newToken);
            return fetchUser(newToken, setUser);
        })
        .then(() => navigate("/play")
        )
        .catch((e) => {
            console.log(e);
        })
    }

    return(
        <div className="container col-sm-7 col-md-5 col-lg-3">
            <form className="text-center" onSubmit = {handleSubmit}>

                <h1 className="m-3">Login</h1>

                {errorMessage && 
                <div className="alert alert-danger my-2 w-100 px-4 py-2" role="alert">
                    <strong>Whoops!</strong> {errorMessage}
                </div>}

                <div className="form-floating my-2">
                    <input type="text"
                        id="inputEmail" 
                        name="inputEmail" 
                        className="form-control" 
                        placeholder="email@example.com"
                        required={true}>
                    </input>
                    <label htmlFor="inputEmail" className="form-text">Email</label>
                </div>

                <div className="form-floating my-2">
                    <input type="password"
                        id="inputPassword"
                        name="inputPassword"
                        className="form-control"
                        placeholder="Password"
                        required={true}>
                    </input>
                    <label htmlFor="inputPassword" className="form-text">Password</label>
                </div>
                
                <button className="btn btn-success my-2 p-2 w-100" type="submit">Submit</button>

                <Link type="button" className="btn btn-outline-success m-2" to="/register">Need to create an account?</Link>

            </form>
        </div>
    );
}   
export default Login;