import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, User } from 'src/app/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SettingsComponent } from 'src/app/modals/settings/settings.component';

@Component({
  selector: 'layout-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {

  routes:{path:string, title:string, navigation?:boolean}[] = [
    { path:'/zone', title: 'The Zone', navigation: false  },
    { path:'/opencourt', title: 'Open Court', navigation: true  },
    { path:'/picks', title: 'Picks & Predictions', navigation: true  },
    { path:'/trivia', title: 'Trivia', navigation: true  },
    { path:'/debate', title: 'Tiered Debate', navigation: true  },
    { path:'/profile', title: 'Profile', navigation: false  },
  ]
  currentPath:string;

  sidebarOpen:boolean = false;

  user:User;

  darkmode:boolean = true;

  constructor(private auth:AuthService, private router:Router,  private modalService:NgbModal) { }
  
  getSource() {
    return this.darkmode? '/assets/images/logo alt - transparent small.png' : '/assets/images/logo - transparent small.png';
  }

  ngOnInit(): void {
    this.darkmode = localStorage.getItem("darkmode") !== "false";
    if (this.darkmode && document.body.classList.contains('notDarkmode')) document.body.classList.remove('notDarkmode');
    if (!this.darkmode && !document.body.classList.contains('notDarkmode')) document.body.classList.add('notDarkmode');
    this.user = this.auth.getUser();
    if (!this.user) this.user = { email:"", firstName: "User Not", lastName: "Logged In"}
  }

  isCurrentPath(path:string):boolean {
    return  this.currentPath.includes(path)
  }

  getTitle() {
    this.currentPath = window.location.pathname;
    return this.routes.find(r => this.isCurrentPath(r.path)).title;
  }

  getNavRoutes() {
    return this.routes.filter(r=>r.navigation);
  }

  toggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
    /*
    let e = document.querySelector('.wrapper');
    if (e.classList.contains('.sidebarOpen')) e.classList.remove('.sidebarOpen');
    else e.classList.add('.sidebarOpen');*/
  }

  logout() {
    if (this.auth.logout()) {
      this.router.navigate(['/login']);
    }
  }

  openSettings() {
    this.sidebarOpen = false;
    const ref = this.modalService.open(SettingsComponent, { size: 'sm', centered: true });
  }
}
