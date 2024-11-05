import NavBar from './NavBar.tsx';
import Home from './pages/Home.tsx';
import Register from './pages/Register.tsx';
import Login from './pages/Login.tsx';
import Details from './pages/Details.tsx';
import Play from './pages/play/Play.tsx';
import userType from './types/UserType.tsx';
import NewGameMenu from './pages/play/NewGameMenu.tsx';
import JoinGame from './pages/play/JoinGame.tsx';

import { Route, Routes, useNavigate } from "react-router-dom";
import React, { useEffect, useState } from 'react';
import CreateGame from './pages/play/CreateGame.tsx';
import Profile from './pages/Profile.tsx';
import profileType from './types/ProfileType.ts';
import Challenges from './pages/play/Challenges.tsx';
import challengeType from './types/ChallengeType.ts';

function App() {
    

    const [page, setPage] = useState<string>('/');
    const [userToken, setUserToken] = useState<string>('');
    const [user, setUser] = useState<userType>();
    const [profile, setProfile] = useState<profileType>();
    const [challenges, setChallenges] = useState<challengeType[]>();
    const navigate = useNavigate();

    //on app load, attempt to restore user session
    useEffect(()=> {
        refreshUser()
    },[]);

    async function logout(): Promise<void> {

        //use the logout endpoint to clear the refresh token cookie
        fetch('/api/user/logout', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then((response) => response.json())
        .then((data) => {
        })
        .catch((e) => {
            console.error(e);
        });

        //clear the session token
        setUserToken('');
        setUser(undefined);

        //navigate to the login page
        navigate('/login');
    }

    async function refreshUser(): Promise<void> {

        return new Promise((resolve, reject) => {

            fetch('/api/user/refresh', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
            .then((response) => response.json())
            .then((data) => {
                
                if(data.sessionToken){
                    let newToken: string = "Bearer " + data.sessionToken;
                    setUserToken(newToken);
                    fetchUser(newToken, setUser);
                    console.log("Session refreshed.");
                    resolve();
                } else {
                    throw new Error('Session extention unavailable. Please log in.');
                }
            })
            .catch((e) => {
                console.log(e);
                setUserToken('');
                setUser(undefined);
                navigate('/login');
            });
        });
    }

    async function fetchUser(userToken: string, setUser: (user: userType | undefined) => void): Promise<void> {

        var newUser: userType | undefined;

        return new Promise<void>((resolve, reject) => {

            fetch('/api/user/principal', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': userToken
                }
            })
            .then((response) => response.json())
            .then((data) => {
                if(data.first){
                    let first: string = data.first;
                    let last: string = data.last;
                    let id: number = parseInt(data.id);
                    
                    newUser = {first, last, id};

                } else {
                    newUser = undefined;
                }
                setUser(newUser);
                resolve();
            })
            .catch((e) => {
                console.error(e);
                reject(e);
            });
        });
    }

    async function fetchProfile(user: userType) {
        if(!user || !user.id) {
            return null;
        }
        let requestUrl: string = "/api/player/by-id/" + user.id;

        return new Promise<profileType>((resolve, reject) => {

            fetch(requestUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': userToken
                }
            })
            .then((response) => response.json())
            .then((data) => {
                let newProfile: profileType | undefined;
                if(data.player){
                    console.log(data.player);
                    let userId: number = parseInt(data.userId);
                    let age: number = parseInt(data.age);
                    let disabilities: string = data.disabilities;
                    let goals: string = data.goals;
                    let hobbies: string = data.hobbies;
                    let mobility: string = data.mobility;
                    let prizeIdeas: string = data.prizeIdeas;

                    newProfile = {
                        userId,
                        age,
                        disabilities,
                        goals,
                        hobbies,
                        mobility,
                        prizeIdeas
                    };

                } else {
                    newProfile = undefined;
                }
                setProfile(newProfile);
                resolve(newProfile as profileType);
            })
            .catch((e) => {
                console.error(e);
                reject(e);
            });
        });
    }

    return (
        <div>
            <NavBar page={page} isLoggedIn={user!==undefined} logout={logout}/>
            <div>
                <Routes>
                    <Route path="/" element={<Home setPage={setPage} />} />
                    <Route path="/about" element={<Details setPage={setPage} />} />
                    <Route path="/register" element={<Register setPage={setPage} setUserToken={setUserToken} setUser={setUser} fetchUser={fetchUser} user={user}/>} />
                    <Route path="/login" element={<Login setPage={setPage} setUserToken={setUserToken} setUser={setUser} fetchUser={fetchUser}/>} />
                    <Route path="/play" element={<Play setPage={setPage} user={user as userType} userToken={userToken} refreshUser={refreshUser} setChallenges={setChallenges}/>} />
                    <Route path="/play/new-game" element={<NewGameMenu setPage={setPage} user={user} userToken={userToken} fetchProfile={fetchProfile} profile={profile}/>} />
                    <Route path="/play/new-game/create" element={<CreateGame setPage={setPage} user={user} userToken={userToken} refreshUser={refreshUser} />} />
                    <Route path="/play/new-game/join" element={<JoinGame setPage={setPage} user={user} userToken={userToken} refreshUser={refreshUser} />} />
                    <Route path="/profile" element={<Profile user={user} userToken={userToken} refreshUser={refreshUser}/>} />
                    <Route path="/play/challenges" element={<Challenges challenges={challenges}/>} />
                </Routes>
            </div>
        </div>
    );
}

export default App;
