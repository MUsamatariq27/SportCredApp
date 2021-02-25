import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginFail:boolean = false;

  username:string;
  password:string;

  getSource() {
    return this.darkmode? '../../../assets//images/logo alt - transparent zoomed.png' : '../../../assets//images/logo - transparent zoomed.png';
  }

  darkmode:boolean = true;

  constructor(private auth:AuthService, private router: Router) { }

  ngOnInit(): void {
    this.darkmode = localStorage.getItem("darkmode") !== "false";
    if (this.darkmode && document.body.classList.contains('notDarkmode')) document.body.classList.remove('notDarkmode');
    if (!this.darkmode && !document.body.classList.contains('notDarkmode')) document.body.classList.add('notDarkmode');

  }

  login() {
    console.log(this.username, this.password);
    this.auth.login(this.username, this.password).then(res=>{
      if (res) this.router.navigate(['/']);
      else {
        console.log('returns false')
        this.loginFail = true;
      }
    });
  }
}
