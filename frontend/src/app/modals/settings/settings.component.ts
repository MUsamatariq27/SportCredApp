import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  darkmode:boolean = true;
  
  constructor(public modal:NgbActiveModal) { }

  ngOnInit(): void {
    this.darkmode = localStorage.getItem("darkmode") === 'true';
  }
  save() {
    console.log(this.darkmode)
    if (this.darkmode) localStorage.setItem("darkmode", "true");
    else localStorage.setItem("darkmode", "false");
    this.modal.dismiss();
    window.location.reload();
  }
}
