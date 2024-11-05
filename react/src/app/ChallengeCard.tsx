import React from "react";
import challengeType from "./types/ChallengeType";

type challengeCardProps = {
    challenge: challengeType;
};

const ChallengeCard = ({challenge}: challengeCardProps) => {

    let difficulty = challenge.difficulty;
    let challengeText = challenge.challengeText;

    switch(difficulty){
        case "easy":
            return(
            <div className="col-sm-3 bg-primary fs-4 p-3 m-2 rounded">
                {challengeText}
                <br></br> +100 points
            </div>
            );
        case "med":
            return(
            <div className="col-sm-3 bg-warning fs-4 p-3 m-2 rounded">
                {challengeText}
                <br></br> +200 points
            </div>
            );
        case "hard":
            return(
            <div className="col-sm-3 bg-danger fs-4 p-3 m-2 rounded">
                {challengeText}
                <br></br> +300 points
            </div>
            );
    }
}
export default ChallengeCard;