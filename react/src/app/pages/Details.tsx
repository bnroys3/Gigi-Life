import { useEffect } from "react";
import React from "react";
import { Link } from "react-router-dom";

type detailsProps = {
    setPage: (page: string) => void
}

const Details = ( {setPage}: detailsProps) => {

    useEffect(() => {
        setPage("/about")
    }, [setPage]);

    
    return(
        <div className="container col-sm-10 text-center">
            <h2> Here's are the details.</h2>
            <p className="lead">These are the details of this website. Here is some extra text to make it look more professional. Thanks for checking out the website. Thank you!</p>

            <Link type="button" className="btn btn-success m-2 px-4 py-3 fs-4 text-white" to="/register">Get started</Link>

      </div>
    );
}

export default Details;