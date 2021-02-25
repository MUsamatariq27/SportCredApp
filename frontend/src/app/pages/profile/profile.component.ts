import { Component, OnInit } from '@angular/core';
import { AuthService, User } from 'src/app/services/auth.service';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  
  editable: boolean = false;

  user:User;

  status: string;

  friends:User[] = [
    { firstName: "Fake", lastName: "Friend1", email: "fake@fake.ca" },
    { firstName: "Fake", lastName: "Friend2", email: "fake@fake.ca" },
    { firstName: "Fake", lastName: "Friend3", email: "fake@fake.ca" },
    { firstName: "Fake", lastName: "Friend4", email: "fake@fake.ca" },
    { firstName: "Fake", lastName: "Friend5", email: "fake@fake.ca" },
    { firstName: "Fake", lastName: "Friend6", email: "fake@fake.ca" },
  ]
  
  constructor(private auth:AuthService, private api:ApiService) { }

  ngOnInit(): void {
    this.user = this.auth.getUser();
    this.api.post('/getStatus', { email: this.user.email, password: this.user.password })
      .subscribe(res=> {
        console.log(res);
        this.status = (res && res.status === 'NULL')? '' : res.status;
      }, err=> console.error(err));
  }

  toggleEdit() {
    this.editable = !this.editable;
  }

  save() {
    this.toggleEdit();
    
    this.api.put('/updateStatus', { email: this.user.email, password: this.user.password, status: this.status })
    .subscribe(res=> {
      this.toggleEdit();
    }, err=> console.error(err));
  }
}
