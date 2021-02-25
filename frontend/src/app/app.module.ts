import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { ZoneComponent } from './pages/zone/zone.component';
import { LayoutComponent } from './auth/layout/layout.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { NavigationComponent } from './auth/layout/navigation/navigation.component';
import { PicksComponent } from './pages/picks/picks.component';
import { DebateComponent } from './pages/debate/debate.component';
import { OpencourtComponent } from './pages/opencourt/opencourt.component';
import { TriviaComponent } from './pages/trivia/trivia.component';
import { OpencourtPostComponent } from './pages/opencourt-post/opencourt-post.component';
import { OpencourtCreateComponent } from './modals/opencourt-create/opencourt-create.component';
import { SettingsComponent } from './modals/settings/settings.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    ZoneComponent,
    LayoutComponent,
    ProfileComponent,
    NavigationComponent,
    PicksComponent,
    DebateComponent,
    OpencourtComponent,
    TriviaComponent,
    OpencourtPostComponent,
    OpencourtCreateComponent,
    SettingsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
