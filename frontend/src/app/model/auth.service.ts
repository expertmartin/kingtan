import { Injectable } from "@angular/core";
import { of, Observable } from 'rxjs';
import { RestDataSource } from "./rest.datasource";

@Injectable()
export class AuthService {
    constructor(private datasource: RestDataSource) { }
    authenticate(username: string, password: string): Observable<boolean> {
        return of(true);    // create a boolean observable because authemtication web service not implemented yet
        // return this.datasource.authenticate(username, password);
    }
    get authenticated(): boolean {
        return true;
        // return this.datasource.auth_token != null;
    }
    clear() {
        this.datasource.auth_token = undefined;
    }
}