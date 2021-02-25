import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-opencourt-create',
  templateUrl: './opencourt-create.component.html',
  styleUrls: ['./opencourt-create.component.scss']
})
export class OpencourtCreateComponent implements OnInit {

  title:string;
  content:string;

  constructor(public modal:NgbActiveModal) { }

  ngOnInit(): void {
  }
  post() {
    this.modal.close({ title: this.title, content: this.content });
  }
}
