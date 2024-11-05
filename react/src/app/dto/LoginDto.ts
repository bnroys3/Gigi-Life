export default class RegistrationDto {

    private email: string;
    private password: string;
    
    constructor(email: string, password: string){
        this.email = email;
        this.password = password;
    }

    toJSON() {
        return {
            "email": this.email,
            "password": this.password
        }
    }

}