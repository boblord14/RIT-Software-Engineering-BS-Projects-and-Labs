import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { UserService } from '../user.service';
import { CurrentUserService } from '../current-user.service';
import { User } from '../user';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  cart: Product[] = [];
  user: User | undefined;
  jerseys: Jersey[] = [];
  

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location,
    private http: HttpClient, 
    private userService: UserService,
   private changeDetection: ChangeDetectorRef,
   private jerseyService: JerseyService,

  ) {}


  ngOnInit(): void {
    this.getUser();
    
 
   
     
      
  }

  
  getUser(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.userService.getUser(id)
      .subscribe(user => {this.user = user
      
        this.jerseyService.searchJerseys(user.name).subscribe((jersey : Jersey[]) =>{
          this.jerseys = jersey;
        });
      });
      
  }

  removeAll(product: Product){
    if(this.user){
    this.userService.removeAllFromCart(this.user, product);
    }
  }

  removeOne(product: Product){
    if(this.user){
    this.userService.removeOneFromCart(this.user, product);
    }
  }

  goBack(): void {
    this.location.back();
  }


}
