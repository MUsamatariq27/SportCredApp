import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginGuard } from './auth/login.guard';
import { LayoutComponent } from './auth/layout/layout.component';

import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { ZoneComponent } from './pages/zone/zone.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { OpencourtComponent } from './pages/opencourt/opencourt.component';
import { PicksComponent } from './pages/picks/picks.component';
import { TriviaComponent } from './pages/trivia/trivia.component';
import { DebateComponent } from './pages/debate/debate.component';
import { OpencourtPostComponent } from './pages/opencourt-post/opencourt-post.component';

const routes: Routes = [
    { path: '', redirectTo: 'opencourt', pathMatch: 'full' }, // Redirect blank routes to /zone
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    { path: '', canActivate:[LoginGuard], component: LayoutComponent, pathMatch: 'prefix', children: [ // Login Guard, must login to access these routes.
        { path: 'zone', component: ZoneComponent },
        { path: 'opencourt', component: OpencourtComponent },
        { path: 'opencourt/:id', component: OpencourtPostComponent },
        { path: 'picks', component: PicksComponent },
        { path: 'trivia', component: TriviaComponent },
        { path: 'debate', component: DebateComponent },
        { path: 'profile', component: ProfileComponent },
    ]},
    { path: '**', redirectTo: 'opencourt', pathMatch: 'full' }, // Redirect any other not known routes to /zone
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
