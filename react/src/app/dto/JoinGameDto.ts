export default class JoinGameDto {

    private gameId: number;
    private playerId: number;
    
    constructor(gameId: number, playerId: number){
        this.gameId = gameId;
        this.playerId = playerId;
    }

    toJSON() {
        return {
            "gameId": this.gameId,
            "playerId": this.playerId
        }
    }

}