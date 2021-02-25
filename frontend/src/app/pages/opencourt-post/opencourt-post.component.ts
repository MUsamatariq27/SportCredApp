import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OpenCourtPost } from '../opencourt/opencourt.component';
import { AuthService, User } from 'src/app/services/auth.service';

export interface Comment {
  author: string;
  content: string;
  datePosted: Date;
}

@Component({
  selector: 'app-opencourt-post',
  templateUrl: './opencourt-post.component.html',
  styleUrls: ['./opencourt-post.component.scss']
})
export class OpencourtPostComponent implements OnInit {

  comments:Comment[] = [
  ]

  constructor(private route:ActivatedRoute, private auth:AuthService) { }
  
  post:OpenCourtPost;
  user:User;
  commentText:string;

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      console.log(params.id)
      let posts = JSON.parse(localStorage.getItem('posts'));
      this.post = posts.find(p=>p.postId===parseInt(params.id));
      let comms = JSON.parse(localStorage.getItem(this.post.postId + "-comments"));

      if (comms) this.comments = comms;
      console.log('asdasd', this.comments)

    });
    this.user = this.auth.getUser()
  }

  postComment():void {
    this.comments.push({ author: this.user.email, content: this.commentText, datePosted: new Date() });
    localStorage.setItem(this.post.postId + "-comments", JSON.stringify(this.comments));
    this.commentText = null;
  }
  getFakeContent() {
    return this.post.description;
  }
  parseContent(content:string):string {
    let matches = content.match(new RegExp('\\@[A-z]*', 'g'));
    if (matches) matches.map(match => {
      content = content.replace(match.trim(), `<span>${match.trim()}</span> `);
    });
    return content;
  }
}
