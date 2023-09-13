import { Component, OnInit, Renderer2 } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {
  debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';
import { Observable, Subject } from 'rxjs';
import { CurrentUserService } from '../current-user.service';
import { Router } from '@angular/router';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: [ './login.component.css' ]
})
export class LoginComponent implements OnInit{

  users$!: Observable<User[]>;
  private searchTerms = new Subject<string>();

  constructor(private renderer: Renderer2, private userService: UserService, private currentUser: CurrentUserService, private router: Router) {}

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }
  changeBackground() {
    this.renderer.setStyle(document.body, 'background-image', 'url("https://mcdn.wallpapersafari.com/medium/84/72/HWl7g9.jpg")');
  }

  ngOnInit(): void {
    this.changeBackground();
    this.currentUser.setCurrentUser("GUEST");
    
    this.users$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.userService.searchUsers(term)),
      
    );
    this.userService.getUsers().subscribe((checkusers: User[]) =>{this.users = checkusers;
      for (let index = 0; index < checkusers.length; index++) {
        this.usernames[index] = checkusers[index].name;
      }
      })
  }
  link : string = "/login"
  admin = "ADMIN";
  guest = "GUEST";
  username = "";
  users: User[] = [];
  usernames : string[] = [];
  isDisabled = true;
  loginMessage= "";
  currentuser : User = {id: 0, name: "", cart : []}; 
  //currentuserName : string = this.currentUser.getCurrentUser().name

  addUser(name: string): void {
    name = name.trim();

    this.loginMessage= "";

    if(name == ""){
      this.loginMessage = "No Username Submitted";


    }
    else if(name.toUpperCase() == this.admin || name.toUpperCase() == this.guest){
      this.loginMessage = "Username reserved";

      
    }
    else if(this.usernames.includes(name)){
      this.loginMessage = "Account Already Exists";

    }
    else{
      name = name.trim();
      if (!name) { return; }
      this.userService.addUser({name} as User)
        .subscribe(user => {
          this.users.push(user);
        });
      this.loginMessage = "User  \""+name+"\" was added!"

      this.usernames[this.usernames.length] = name.trim();
    }
  }

  goto(){
    this.router.navigate([this.link]);
    return this.link;
  }

  loginAsGuest(){
    this.currentUser.setCurrentUser("GUEST");
      this.link =  '/dashboard';
      this.goto()
  }



  login(username: string){
    if(username == ""){
      this.isDisabled = true;
    }
    else if(username.toUpperCase() == this.admin){
      this.isDisabled = false;
      this.currentUser.setCurrentUser("ADMIN");
      this.link =  '/dashboard';
      this.goto()

    }
    else if(this.usernames.includes(username)){
      //this.userService.getUserByName(username).subscribe((currentuser: User) =>{this.currentuser = currentuser;})
     this.isDisabled = false;
     this.currentUser.setCurrentUser(username);
     console.log(username);

     this.link =  '/dashboard';
     this.goto()

    }
    else{
      this.isDisabled = false;
      this.link =  '/login';
      this.loginMessage = "This user doesn't exist. Create new user?";
    }
  }


}