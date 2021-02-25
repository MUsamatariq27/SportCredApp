import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { AuthService } from "src/app/services/auth.service";
import { ComparePassword, agerangeValidate } from "./customvalidator.validator";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit{
  signup_form: FormGroup
  submitted=false;
  form_valid =false ;

  constructor(private fb: FormBuilder, private auth:AuthService, private router:Router) {}

  darkmode:boolean = true;

  getSource() {
    return this.darkmode? '../../../assets//images/logo alt - transparent zoomed.png' : '../../../assets//images/logo - transparent zoomed.png';
  }

  ngOnInit(){
    this.darkmode = localStorage.getItem("darkmode") !== "false";
    if (this.darkmode && document.body.classList.contains('notDarkmode')) document.body.classList.remove('notDarkmode');
    if (!this.darkmode && !document.body.classList.contains('notDarkmode')) document.body.classList.add('notDarkmode');

    this.signup_form = this.fb.group({
      firstName: new FormControl('', [Validators.required, Validators.minLength(3),
        Validators.pattern('^[a-zA-Z]+$')]),
      lastName: new FormControl('', [Validators.required, Validators.minLength(3),
        Validators.pattern('^[a-zA-Z]+$')]),
      username: new FormControl('', [Validators.required, Validators.minLength(6),
        Validators.pattern('^(?=.*[a-z])(?=.*[0-9])[a-z0-9]+$')]),
      email: new FormControl('', [Validators.required, 
        Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$')]),
      age: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required, Validators.minLength(8),
        Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!/%*?&])[A-Za-z\d$@$!%*?&].{8,}$')]),
      confirm_password: new FormControl('', [Validators.required]),
      accepterms:  new FormControl( false, Validators.requiredTrue),
      favSport: new FormControl(''),
      favTeam: new FormControl(''),
      highestLvlPlay: new FormControl(''),
    },
    { validators: [ComparePassword("password", "confirm_password"), agerangeValidate("age") ] });
  }

  hide = true;
  get f(){return this.signup_form.controls;}
  
  myFunc()
  {
    var x = document.getElementById("ch_input");
    if(x.getAttribute("type") == "password")
    {
      x.setAttribute("type", "text");
    }
    else
    {
      x.setAttribute("type", "password");
    }
  }

  myFunc_2()
  {
    var x = document.getElementById("ch_input_2");
    if(x.getAttribute("type") == "password")
    {
      x.setAttribute("type", "text");
    }
    else
    {
      x.setAttribute("type", "password");
    }
  }
  clear() {
    this.form_valid=true;
    this.signup_form.reset();
    this.submitted=false; 
  }
  onSubmit() 
  {
    console.log('workls')
    /*if (this.signup_form.invalid)
    {
      return;
    }*/
    this.auth.signup(this.signup_form.value).then(res=>{
      console.log(res);
      if (res) this.submitted=true;
      else this.clear();
      setTimeout(() => { this.router.navigate(['login']); }, 2000)
    });
  }
  rm_Massage()
  {
    this.form_valid=false; 
  }
}
