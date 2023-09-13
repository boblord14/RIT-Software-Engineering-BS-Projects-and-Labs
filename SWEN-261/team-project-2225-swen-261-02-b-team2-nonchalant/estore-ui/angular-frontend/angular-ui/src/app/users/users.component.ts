import { Component, OnInit } from '@angular/core';

import { User } from '../user';
import { UserService } from '../user.service';
import { MessageService } from '../message.service';
import { CurrentUserService } from '../current-user.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  selectedUser?: User;

  users: User[] = [];

  constructor(private userService: UserService, private messageService: MessageService, private currentUserService: CurrentUserService) { }

  ngOnInit(): void {
    this.getUsers();
  }

   x = this.currentUserService;
   
   userMessage = "";

  onSelect(user: User): void {
    this.selectedUser = user;
    this.messageService.add(`UsersComponent: Selected user id=${user.id}`);
  }

  getUsers(): void {
    this.userService.getUsers()
        .subscribe(users => this.users = users);
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { 
      this.userMessage = "No username entered"; 
      return;
    }
    this.userMessage = "";
    this.userService.addUser({name} as User)
      .subscribe(user => {
        this.users.push(user);
      });
  }
  delete(user: User): void {
    this.users = this.users.filter(h => h !== user);
    this.userService.deleteUser(user.id).subscribe();
  }
}