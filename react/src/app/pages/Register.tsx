import React, { useEffect, useState} from "react";
import { Link, useNavigate } from "react-router-dom";
import RegistrationDto from "../dto/RegistrationDto";
import LoginDto from "../dto/LoginDto";
import userType from "../types/UserType";

type registerProps = {
    setPage: (page: string) => void,
    setUserToken: (token: string) => void,
    setUser: (user: userType | undefined) => void,
    fetchUser: (token: string, setUserFn: (user: userType | undefined) => void) => Promise<void>,
    user: userType | undefined
}

const Register = ({ setPage, setUserToken, setUser, fetchUser, user }: registerProps) => {

    const [errorMessage, setErrorMessage] = useState<string>("");
    const navigate = useNavigate();

    useEffect(()=>{
        if(user) {
            navigate("/play");
        } else {
            setPage("/register");
        }
    });

    function validatePassword(password: string, password2: string){
        if(password !== password2) {
            setErrorMessage("Passwords do not match.");
            return false;
        }
        if(password.length<8) {
            setErrorMessage("Password must be at least 8 characters.");
            return false;
        }
        return true;
    }

    async function handleSubmit(event: React.FormEvent){

        event.preventDefault();

        let formData = new FormData(event.target as HTMLFormElement);
        let email: string = formData.get('inputEmail') as string;
        let first: string = formData.get('inputFirst') as string;
        let last: string = formData.get('inputLast') as string;
        let password: string = formData.get('inputPassword') as string;
        let password2: string = formData.get('inputPassword2') as string;

        if(!validatePassword(password, password2)){
            return;
        }

        fetch('/api/user/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                },
            body: JSON.stringify(new RegistrationDto(email, first, last, password))
        })
        .then((response) => response.json())
        .then((data) => {
            if(data.result==="success") {
                setErrorMessage("");
                loginNewUser(email, password);
            } else if (data.result) {
                setErrorMessage(data.result);
            } else {
                throw new Error("Internal server error.")
            }
        })
        .catch((e) => {
            console.error(e)
            setErrorMessage("Unexpected error. Please contact support.")
        })
    }

    async function loginNewUser(email: string, password: string): Promise<void> {
        
        fetch('/api/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', 
                },
            body: JSON.stringify(new LoginDto(email,password))
        })
        .then((response) => {
            if(response.ok) {
                return response.json();
            } else {
                console.log("Internal error - account was created but login failed.");
                return Promise.reject();
            }
        })
        .then((data) => {
            let newToken: string = "Bearer " + data.sessionToken;
            setUserToken(newToken);
            return fetchUser(newToken, setUser);
        })
        .then(() => navigate('/play')
        )
        .catch((e) => {
            console.log(e);
            navigate('/login');
        })
    }


    return(
        <div className="container col-sm-8 col-md-5 col-lg-4">

            <form className="text-center" onSubmit={handleSubmit}>

                <h1 className="m-3">Sign up</h1>
                
                {errorMessage && 
                <div className="alert alert-danger my-2 w-100 px-4 py-2" role="alert">
                    <strong>Whoops!</strong> {errorMessage}
                </div>}

                <div className="row g-2">
                    <div className="col">
                        <div className="form-floating">
                            <input type="text"
                                name="inputFirst"
                                id="inputFirst"
                                className="form-control" 
                                placeholder="First"
                                required={true}>
                            </input>
                            <label htmlFor="inputFirst" className="form-text">First name</label>
                        </div>
                    </div>
                    
                    <div className="col">
                        <div className="form-floating">
                            <input type="text" 
                                name="inputLast" 
                                id="inputLast"
                                className="form-control"
                                placeholder="Last"
                                required={true}>
                            </input>
                            <label htmlFor="inputLast" className="form-text">Last name</label>
                        </div>
                    </div>
                </div>

                <div className="form-floating my-2">
                    <input type="email"
                        name="inputEmail" 
                        id="inputEmail"
                        className="form-control" 
                        placeholder="Email@example.com"
                        required={true}>
                    </input>
                    <label htmlFor="inputEmail" className="form-text">Email address</label>
                </div>

                <div className="form-floating my-2">
                    <input type="password"
                        name="inputPassword" 
                        id="inputPassword"
                        className="form-control" 
                        placeholder="Password"
                        required={true}>
                    </input>
                    <label htmlFor="inputPassword" className="form-text">Password (must be at least 8 characters)</label>
                </div>

                <div className="form-floating my-2">
                    <input type="password"
                        name="inputPassword2" 
                        id="inputPassword2"
                        className="form-control" 
                        placeholder="Confirm password"
                        required={true}>
                    </input>
                    <label htmlFor="inputPassword2" className="form-text">Confirm password</label>
                </div>

                <button className="btn btn-success w-100 my-2 p-2" type="submit">Submit</button>

                <Link type="button" className="btn btn-outline-success m-2" to="/login">Already have an account?</Link>

            </form>
        </div>
    );
}
export default Register;