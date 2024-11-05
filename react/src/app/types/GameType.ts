import profileType from "./ProfileType";

type gameType = {
    gameId: number,
    name: string,
    adminId: number,
    playerId: number,
    points: number,
    startDate: string,
    expiryDate: string
};
export default gameType;