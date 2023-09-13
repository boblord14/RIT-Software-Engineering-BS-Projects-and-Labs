import { Component } from '@angular/core';
import { Input } from '@angular/core';

import { Jersey } from '../jersey';

@Component({
  selector: 'app-jersey-image',
  templateUrl: './jersey-image.component.html',
  styleUrls: ['./jersey-image.component.css']
})
export class JerseyImageComponent {

  @Input() displayed_jersey: Jersey; 

  primary_url: string; 
  secondary_url: string; 

  constructor( ) {  }

  ngOnChanges( ) {
    this.primary_url = "assets/jersey-primary/primary-"  + this.displayed_jersey.primary_color + ".png"; 
    this.secondary_url = "assets/jersey-secondary/secondary-"  + this.displayed_jersey.secondary_color + ".png";
  }

}
