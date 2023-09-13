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
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {
  cart: Product[] = [];
  jerseys: Jersey[] = [];
  total: number = 0;
  checkoutTotal: number = 0;
  showRecipt: Boolean = false;
  reciptCopy: Product[] = [];
  jersyReciptCopy: Jersey[] = [];
  isConfirmed = false;

  constructor(private productService: ProductService,
    private http: HttpClient, private userService: UserService, private currentUserService : CurrentUserService,
    private location: Location, private changeDetection: ChangeDetectorRef, private jerseyService: JerseyService
  ) {}

  addToCart(product: Product) {
    this.currentUserService.addToCart(product);
    this.cart = this.currentUserService.getCurrentUserCart(); 
    this.jerseys = this.currentUserService.getCurrentUserJerseys();
    this.changeDetection.detectChanges(); 
    
  }



  getCartTotal(){

      this.total = 0;

    for (let index = 0; index < this.cart.length; index++) {
      this.total += (this.cart[index].price*this.cart[index].quantity);
      
    }

    for (let index = 0; index < this.jerseys.length; index++) {
      this.total += (this.jerseys[index].price);
    }
    
    return this.total;

  }

  ngOnInit(): void {

      this.userService.searchUsers(this.currentUserService.getCurrentUser())
      .subscribe((currentcart: User[]) => {this.cart = currentcart[0].cart});
      
      this.jerseyService.searchJerseys(this.currentUserService.getCurrentUser())
      .subscribe((currentjerseys: Jersey[]) => {this.jerseys = currentjerseys});

      this.cart = this.currentUserService.getCurrentUserCart(); 
      this.jerseys = this.currentUserService.getCurrentUserJerseys();
      this.getCartTotal();
      this.changeDetection.detectChanges(); 
  }

  goBack(): void {
    this.location.back();
  }

  removeOneFromCart(product: Product): void {
    this.currentUserService.removeOneFromCart(product); 
    this.cart = this.currentUserService.getCurrentUserCart(); 
    this.jerseys = this.currentUserService.getCurrentUserJerseys();
    this.getCartTotal();
    this.changeDetection.detectChanges(); 

  }

  removeAllFromCart(product: Product): void {
    this.currentUserService.removeAllFromCart(product); 
    this.cart = this.currentUserService.getCurrentUserCart(); 
    this.jerseys = this.currentUserService.getCurrentUserJerseys();
    this.getCartTotal();
    this.changeDetection.detectChanges(); 

  }

  async removeJerseyFromCart3(jersey: Jersey){
    await this.removeJerseyFromCart(jersey);
    
  }
  
  async removeJerseyFromCart(jersey: Jersey): Promise<void>{
    await this.removeJerseyFromCart2(jersey);
    this.cart = this.currentUserService.getCurrentUserCart(); 
    this.jerseys = this.currentUserService.getCurrentUserJerseys();
    this.getCartTotal();
    this.changeDetection.detectChanges(); 
  }

  async removeJerseyFromCart2(jersey: Jersey): Promise<void>{
    this.jerseyService.deleteJersey(jersey.id).subscribe(()=>{
      this.ngOnInit();
      this.changeDetection.detectChanges(); });
    }

  confirmCheckout() {
    this.showRecipt = false
  }

  async checkout() {
    this.reciptCopy = this.cart;
    this.jersyReciptCopy = this.jerseys;
    this.checkoutTotal = this.total;
    await this.currentUserService.checkout();
    this.cart = await this.currentUserService.getCurrentUserCart2();
    this.changeDetection.detectChanges();
    this.showRecipt = true;
    this.ngOnInit();
  }

  async areYouSure() {
    this.isConfirmed = window.confirm("Are you sure you want to checkout? You will be charged $" + this.total  + ".");
    if (this.isConfirmed == true) {
      await this.checkout();
    } else {
      this.isConfirmed = false;
    }
  }

}
