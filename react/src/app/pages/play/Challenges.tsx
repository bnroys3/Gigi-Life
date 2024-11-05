import React from "react";
import ChallengeCard from "../../ChallengeCard";
import challengeType from "../../types/ChallengeType";

type challengesProps = {
    challenges: challengeType[] | undefined;
};

const Challenges = ({challenges }: challengesProps) => {

    if(!challenges){
        return;
    }

    var challengeCard1 = <ChallengeCard challenge={challenges[0]}/>
    var challengeCard2 = <ChallengeCard challenge={challenges[1]}/>
    var challengeCard3 = <ChallengeCard challenge={challenges[2]}/>


    return(
        <div className = "vh-100">
            <div className="h-50 row justify-content-center m-2">
                {challengeCard1}
                {challengeCard2}
                {challengeCard3}
            </div>
        </div>
    );
}
export default Challenges;