export default class NewGameDto {

    private adminId: number;
    private name: string;
    private goals: string;
    private prizes: string;
    
    constructor(adminId: number, name: string, goals: string, prizes: string){
        this.adminId = adminId;
        this.name = name;
        this.goals = goals;
        this.prizes = prizes;
    }

    toJSON() {
        return {
            "adminId": this.adminId,
            "name": this.name,
            "goals": this.goals,
            "prizes": this.prizes
        }
    }

}