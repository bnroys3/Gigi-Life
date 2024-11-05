import {Link} from 'react-router-dom';
import React from 'react';

type navBarProps = {
    page: string
    isLoggedIn: boolean
    logout: () => void
}

const NavBar = ({page, isLoggedIn, logout}: navBarProps) => {


    return(

        <nav className="navbar border bg-white py-0 mb-4">
            <div className="container-fluid">

                <div className="navbar navbar-expand navbar-left">
                    <ul className="nav navbar-nav">
                        <Link className="navbar-brand" to="/"><span className='h4 fw-bold'>Gigi</span><span className='h4 fw-bold text-success'>Life</span></Link>
                        <Link className={page==="/about"?"nav-link active":"nav-link"} to="/about">About</Link>
                        <Link className={page.includes("/play")?"nav-link active":"nav-link"} to={isLoggedIn?"/play":"/login"}>Play</Link>
                    </ul>
                </div>

                <div className="navbar navbar-expand navbar-right">
                    <ul className="nav navbar-nav">
                        {!isLoggedIn && <Link type="button" className="btn btn-outline-success me-2" to="/login">Log in</Link>}
                        {!isLoggedIn && <Link type="button" className="btn btn-success me-2 " to="/register">Create account</Link>}
                        {isLoggedIn && <button type="button" className="btn btn-outline-danger me-2" onClick={logout}>Sign out</button>}
                    </ul>
                </div>
            </div>
        </nav>

    );

}
export default NavBar;