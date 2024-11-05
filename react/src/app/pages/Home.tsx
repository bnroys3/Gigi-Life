import { useEffect } from "react";
import { Link } from "react-router-dom";
import React from "react";


type homeProps = {
  setPage: (page: string) => void
}

const Home = ({setPage}: homeProps) => {

    useEffect(()=>{
        setPage("/")
    }, [setPage]);

    return(
            <div className="container col-sm-10 text-center">
              <h1> Here's the title.</h1>
              <p className="lead">This is the home page of the website. Here is some extra text to make it look more professional. Thanks for checking out the website. Thank you!</p>

              <Link type="button" className="btn btn-success m-2 px-4 py-3 fs-4" to="/register">Get started</Link>
              <Link type="button" className="btn btn-outline-success m-2 px-4 py-3 fs-4 " to="/about">Learn more</Link>

            </div>
  
  
    );
}
export default Home;