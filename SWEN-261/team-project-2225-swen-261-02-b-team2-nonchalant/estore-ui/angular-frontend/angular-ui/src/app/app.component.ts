import { Component, OnInit } from '@angular/core';
import { CurrentUserService } from './current-user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  constructor(private currentUserService: CurrentUserService) { }
  currentuser: string = '';
  showhead = true;
  ngOnInit(): void {
    this.showhead = this.show();
    this.currentuser = CurrentUserService.currentUser;

  }
  getCurrentUser(): string{
    return CurrentUserService.currentUser;

  }
  // Get the URL pathname
// If the URL ends with "/login"
show() {
  var url = window.location.pathname;
  

  if (url.indexOf('/login') !== -1) {
    return false;
  }
  else {
    return true;
  }
}
isAdmin(): boolean {
  this.currentuser = CurrentUserService.currentUser;
  if (this.currentuser == "ADMIN"){
    return true;
  }
  return false;
}

isReserved(): boolean {
  this.currentuser = CurrentUserService.currentUser;

  if (this.currentuser == "ADMIN" || this.currentuser == "GUEST"){
    return true;
  }
  return false;
}
  
  title = 'Volleyball E-store';
}
