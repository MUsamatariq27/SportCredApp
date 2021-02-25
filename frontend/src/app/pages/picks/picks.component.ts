import { Component, OnInit } from '@angular/core';

declare interface Pick {
  sideA: string;
  aVotes: number;
  sideB: string;
  bVotes: number;
  date: Date;
  userChoice: 'A' | 'B' | '';
}

const pickArray:Pick[] = [
  { sideA: 'Orlando Magic', aVotes: 123, sideB: "Atlanta Hawks", bVotes: 321, date: new Date("2020-12-11"), userChoice: 'B'},
  { sideA: 'New York Knicks', aVotes: 202, sideB: "Detroit Pistons", bVotes: 152, date: new Date("2020-12-11"), userChoice: 'A'},
  { sideA: 'Houston Rockets', aVotes: 445, sideB: "Chicago Bulls", bVotes: 24, date: new Date("2020-12-11"), userChoice: ''},

  { sideA: 'Oklahoma City Thunder', aVotes: 165, sideB: "San Antino Spurs", bVotes: 123, date: new Date("2020-12-12"), userChoice: ''},
  { sideA: 'Toronto Raptors', aVotes: 934, sideB: "Charlotte Hornets", bVotes: 156, date: new Date("2020-12-12"), userChoice: ''},

  { sideA: 'Memphis Grizzlies', aVotes: 24, sideB: "Minnesota Timberwolves", bVotes: 52, date: new Date("2020-12-15"), userChoice: ''},
  { sideA: 'Golden State Warriors', aVotes: 126, sideB: "Sacramento Kings", bVotes: 65, date: new Date("2020-12-15"), userChoice: ''},

]

@Component({
  selector: 'app-picks',
  templateUrl: './picks.component.html',
  styleUrls: ['./picks.component.scss']
})
export class PicksComponent implements OnInit {

  pickByDates: {[key:string]:Pick[]} = {};
  dateGroups: string[] = [];

  constructor() { }

  ngOnInit(): void {
    let picks:Pick[] = pickArray;
    picks.map((p:Pick)=> {
      if (!Object.keys(this.pickByDates).includes(p.date.toLocaleDateString())) 
            this.pickByDates[p.date.toLocaleDateString()] = [];
      this.pickByDates[p.date.toLocaleDateString()].push(p);
    });
    this.dateGroups = Object.keys(this.pickByDates).sort((a,b) => { return new Date(a) < new Date(b)? -1 : 1 });
  }

  vote(key:string, index: number, choice: 'A' | 'B'): void {
    console.log(key, index, choice);
    this.pickByDates[key][index].userChoice = choice;
    if (choice === 'A') this.pickByDates[key][index].aVotes += 1;
    else this.pickByDates[key][index].bVotes += 1;
  }
  getGroup(date:string) {
    return this.pickByDates[date];
  }

}
