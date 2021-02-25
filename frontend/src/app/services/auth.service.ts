import { Injectable } from '@angular/core';
import { ApiService } from './api.service';

export interface User {
  email:string;
  firstName: string;
  lastName: string;
  age?: number;
  favSport?: string;
  favTeam?: string;
  highestLvlPlay?: string;
  isAdmin?: boolean;
  password?: string;
}


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private user:User;

  constructor(private api: ApiService) { }

  login(email:string, password:string):Promise<boolean> {
    return new Promise((resolve, _) => {
      this.api.post('/getUser', {email: email, password: password}).subscribe(res=> {
        this.user = res;
        localStorage.setItem('fakeSession', JSON.stringify(res));
        resolve(true);
      }, _=> {
        resolve(false);
      });
    });
  }

  logout():boolean {
    this.user = null;
    localStorage.setItem('fakeSession', null);
    return true;
  }

  signup(payload:User):Promise<boolean> {
    console.log("asd", payload)
    return new Promise((resolve, _) => {
      this.api.put('/addUser', payload).subscribe(_=> {
        resolve(true);
      }, _=> {
        resolve(false);
      });
    });
  }

  getUser():User | null {
    if (this.user) return this.user;
    else {
      let user = JSON.parse(localStorage.getItem('fakeSession'));
      if (user) {
        this.user = user;
        return JSON.parse(JSON.stringify(user));
      } else return null;

    }
  }
}
