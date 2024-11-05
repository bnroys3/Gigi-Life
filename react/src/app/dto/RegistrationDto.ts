export default class RegistrationDto {

    private email: string;
    private first: string;
    private last: string;
    private password: string;
    
    constructor(email: string, first: string, last: string, password: string){
        this.email = email;
        this.first = first;
        this.last = last;
        this.password = password;
    }

    toJSON() {
        return {
            "email": this.email,
            "first": this.first,
            "last": this.last,
            "password": this.password,
        }
    }

}