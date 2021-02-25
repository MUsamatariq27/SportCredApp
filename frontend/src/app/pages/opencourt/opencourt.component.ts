import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApiService } from 'src/app/services/api.service';
import { OpencourtCreateComponent } from '../../modals/opencourt-create/opencourt-create.component';
import { AuthService, User } from '../../services/auth.service';

export interface OpenCourtPost {
  postId: number,
  title: string;
  description: string;
  email: string;
  postScore: number;
  currentUserVote: -1 | 0 | 1;
  datePosted: Date;
}

@Component({
  selector: 'app-opencourt',
  templateUrl: './opencourt.component.html',
  styleUrls: ['./opencourt.component.scss']
})
export class OpencourtComponent implements OnInit {
  //TEMP
  //currentIdHead:number = 2368;

  constructor(private router:Router, private modalService:NgbModal, private auth:AuthService, private api:ApiService) { }

  private user:User;
  postsRef:OpenCourtPost[];
  posts:OpenCourtPost[];
  sortDirection: 1 | -1 = -1;

  downVoteApi(id) {
    this.api.put('/disagreePost', { email: this.user.email, password: this.user.password, postId: id }).subscribe(res => {
      console.log(res);
    });
  }

  upVoteApi(id) {
    this.api.put('/agreePost', { email: this.user.email, password: this.user.password, postId: id }).subscribe(res => {
      console.log(res);
    });
  }

  vote(id:number, direction: 1 | -1) {
    let post:OpenCourtPost = this.posts.find(s=>s.postId===id);
    let postVotes:{[key:number]: number} = <{[key:number]: number}>JSON.parse(localStorage.getItem("postVotes"));
    if (post.currentUserVote === 0) {
      post.currentUserVote = direction;
      post.postScore += direction;
      postVotes[id] = direction;
      if (direction === 1) this.upVoteApi(id);
      else this.downVoteApi(id);
    } else if (post.currentUserVote === direction) {
      post.currentUserVote = 0;
      post.postScore -= direction;
      postVotes[id] = 0;
      if (direction === 1) this.downVoteApi(id);
      else this.upVoteApi(id);
    } else {
      post.currentUserVote = direction;
      post.postScore += direction * 2;
      postVotes[id] = direction;
      if (direction === 1) { this.upVoteApi(id); }
      else { this.downVoteApi(id); }
    }
    localStorage.setItem('postVotes', JSON.stringify(postVotes));
  }

  openPost(id:number) {
    this.router.navigate(['opencourt',id])
  }

  ngOnInit(): void {
    if (!localStorage.getItem('postVotes')) localStorage.setItem('postVotes', JSON.stringify({}));
    this.updateData();
    this.user = this.auth.getUser();
  }

  updateData() {
    this.api.get("/getPosts", {}).subscribe(res =>{
      let postVotes:{[key:number]: number} = <{[key:number]: number}>JSON.parse(localStorage.getItem("postVotes"));
      this.posts = res.posts.map(p => {
        let x = (Object.keys(postVotes).includes(String(p.postId))? postVotes[String(p.postId)] : 0);
        console.log(x);
        return { ...p, datePosted: new Date(), currentUserVote: x }
      });
      this.postsRef = this.posts.map(p=>p);
      localStorage.setItem('posts', JSON.stringify(this.posts) );
      this.sort();
    }, err => { console.error(err); });
  }
  
  sort():void {
    if (this.sortDirection === 1) this.sortDirection = -1;
    else this.sortDirection = 1;
    this.posts.sort((a,b) => a.postId < b.postId? this.sortDirection : 0-this.sortDirection);
  }

  searchInput(e):void {
    console.log(e.target.value)
    if (e.target.value.length > 2) this.search(e.target.value);
    else this.search(null);
  }

  search(query:string):void {
    if (!query) this.posts = this.postsRef.map(a=>a);
    else {
      query = query.toLowerCase();
      this.posts = this.postsRef.filter(a=>{
        return a.title && a.title.toLowerCase().includes(query) || a.description && a.description.toLowerCase().includes(query) || a.email && a.email.toLowerCase().includes(query);
      });
    }
    if (this.sortDirection === -1) this.sort();
  }

  createPost():void {
    const ref = this.modalService.open(OpencourtCreateComponent, { size: 'lg', centered: true });
    ref.result.then(res=> {
      this.api.put('/addPost', { email: this.user.email, password: this.user.password, title: res.title, description: res.content }).subscribe(res => {
        console.log(res);
        this.updateData();
      });
      localStorage.setItem('posts', JSON.stringify(this.postsRef));
      if (this.sortDirection === -1) this.sort();
    }).catch(err => console.log(err));
  }
}


/*
export const OpenCourtPosts:OpenCourtPost[] = [
  { 
    id: 1234,
    title: 'Drinking sports drinks should be considered doping!',
    description: 'In the past sports drinks weren\'t that studied or effective but nowadays they are too good...',
    author: 'DumbVisa',
    upvotes: -62,
    currentUserVote: -1,
    datePosted: new Date("11-19-2020 10:19:23 AM"),
  },
  { 
    id: 2364,
    title: 'The idiot that thinks sports drinks are doping should be banned!',
    description: 'Refer to title.',
    author: 'SmartVisa',
    upvotes: 80,
    currentUserVote: 0,
    datePosted: new Date("11-19-2020 10:28:45 AM"),
  },
  { 
    id: 2365,
    title: 'Checkout our new line of designer sports shoes at www.somespam.com!',
    description: 'We have the latest and greatest in footwear for athletes. Come out and check our insane promo...',
    author: 'ShamelessVisa',
    upvotes: -42,
    currentUserVote: 1,
    datePosted: new Date("11-19-2020 9:45:47 AM"),
  },
  { 
    id: 2366,
    title: 'Spam Spam Spam Spam Spam!',
    description: 'Spam Spam Spam Spam Spam Spam Spam Spam Spam Spam Spam Spam Spam Spam Spam Spam Spam Spam...',
    author: 'SpamVisa',
    upvotes: -23,
    currentUserVote: 1,
    datePosted: new Date("11-19-2020 9:19:52 AM"),
  },
  { 
    id: 2367,
    title: 'This site sucks, I\'m out!',
    description: 'Too much spam and ad posts. Its filled with unintellectual crap too. You couldn\'t pay me to stay...',
    author: 'SelfImportantVisa',
    upvotes: -9,
    currentUserVote: -1,
    datePosted: new Date("11-19-2020 9:00:12 AM"),
  },
  { 
    id: 2368,
    title: 'This is a filler post for testing!',
    description: 'Filler post for testing!',
    author: 'FillerVisa',
    upvotes: 8,
    currentUserVote: 1,
    datePosted: new Date("11-19-2020 8:23:12 AM"),
  },
];  

 */