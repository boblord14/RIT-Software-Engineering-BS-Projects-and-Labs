import { ChangeDetectorRef, Component } from '@angular/core';

import { Jersey } from '../jersey';
import { User } from '../user';
import { JerseyService } from '../jersey.service';
import { CurrentUserService } from '../current-user.service';
import { UserService } from '../user.service';
import { MessageService } from '../message.service';


@Component({
  selector: 'app-jersey-maker',
  templateUrl: './jersey-maker.component.html',
  styleUrls: ['./jersey-maker.component.css']
})
export class JerseyMakerComponent {

  current_jersey: Jersey; 
  current_user: string; 
  jersey_pricing: number;

  constructor(
    private messageService: MessageService,
    private currentUserService: CurrentUserService, 
    private userService: UserService, 
    private jerseyService: JerseyService,
    private changeDetection: ChangeDetectorRef
  ) {  }

    resetJersey(): void {
      this.current_jersey = {
        id: 1, 
        creator: this.currentUserService.getCurrentUser(), 
        primary_color: "blue", 
        secondary_color: "orange", 
        name: "johnson", 
        number: 12, 
        logo: "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/SNice.svg/1200px-SNice.svg.png", // A smiley!
        price: this.jersey_pricing
      }
    }

  ngOnInit() {
    this.resetJersey(); 
    console.log(this.current_jersey); 
    this.current_user = this.currentUserService.getCurrentUser(); 
    this.jerseyService.getJersey(0).subscribe((tempPrice :Jersey) => {
      this.jersey_pricing = tempPrice.price;
    });
  }

  setCurrentJersey(): void {
    this.current_jersey = {
      id: 1, 
      creator: this.currentUserService.getCurrentUser(), 
      primary_color: (<HTMLInputElement>document.getElementById("primary-color")).value, 
      secondary_color: (<HTMLInputElement>document.getElementById("secondary-color")).value,
      name: (<HTMLInputElement>document.getElementById("jersey-name")).value, 
      number: +(<HTMLInputElement>document.getElementById("jersey-number")).value, 
      logo: (<HTMLInputElement>document.getElementById("jersey-logo")).value, 
      price: this.jersey_pricing
    }
    this.changeDetection.detectChanges(); 
  }

  /** Confirms the jersey, adding it to the "cart" */
  confirmJersey() {
    console.log(this.jersey_pricing);
    this.setCurrentJersey(); 
    this.jerseyService.addJersey(this.current_jersey).subscribe(); 
    this.resetJersey(); 
    this.changeDetection.detectChanges(); 
  }

}
