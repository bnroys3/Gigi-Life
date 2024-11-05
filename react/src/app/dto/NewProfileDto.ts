export default class NewProfileDto {

    private userId: number;
    private age: number;
    private disabilities: string;
    private goals: string;
    private hobbies: string;
    private mobility: string;
    private prizeIdeas: string;
    
    constructor(userId: number, age: number, disabilities: string, goals: string, hobbies: string, mobilitiy: string, prizeIdeas: string){
        this.userId = userId;
        this.age = age;
        this.disabilities = disabilities;
        this.goals = goals;
        this.hobbies = hobbies;
        this.mobility = mobilitiy;
        this.prizeIdeas = prizeIdeas;
    }

    toJSON() {
        return {
            "userId": this.userId,
            "age": this.age,
            "disabilities": this.disabilities,
            "goals": this.goals,
            "hobbies": this.hobbies,
            "mobility": this.mobility,
            "prizeIdeas": this.prizeIdeas,
        }
    }

}