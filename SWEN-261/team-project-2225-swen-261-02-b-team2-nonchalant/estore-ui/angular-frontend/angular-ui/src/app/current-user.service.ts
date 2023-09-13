import { Injectable } from '@angular/core';
import { Product } from './product';
import { ProductService } from './product.service';
import { User } from './user';
import { UserService } from './user.service';
import { BehaviorSubject, firstValueFrom, lastValueFrom, map, Subscription } from 'rxjs';
import { Location } from '@angular/common';
import { Jersey } from './jersey';
import { JerseyService } from './jersey.service';


@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {
  static currentUser: string = "GUEST";
  static currentUserID: number = -1;
  static currentUserCart: Product[] = [];
  static currentUserJerseys : Jersey[];


 constructor(private userService: UserService, private productService: ProductService, private location: Location, private jerseyService: JerseyService) { }


  /**
   * Checks whether the user is a special type of user (admin or guest). 
   * @returns Whether the current user is a special type of user. 
   */
  isReserved(){
    if( CurrentUserService.currentUser == "ADMIN" || CurrentUserService.currentUser == "GUEST"){
      return true
    }
    return false;
  }

  /**
   * Checks if the current user is an admin. 
   * @returns Whether the user is an admin. 
   */
  isAdmin(){
    if( CurrentUserService.currentUser == "ADMIN"){
      return true
    }
    return false;
  }

  /**
   * Sets the current user, logging in as the user indicated. 
   * @param username The username being logged into. 
   */
  setCurrentUser(username: string){
    CurrentUserService.currentUser = username;
    CurrentUserService.currentUserID = this.getCurrentUserID();
    CurrentUserService.currentUserCart = this.getCurrentUserCart();
    //this.getCurrentUserCartSize();
  }

  /**
   * Gets the current user. 
   * @returns The current user. 
   */
  getCurrentUser(){
    return CurrentUserService.currentUser;
  }
  id: any;
  updater(){
    CurrentUserService.currentUserID = this.id;
  }

  /**
   * Gets the current user's ID. 
   * @returns The current user's ID. 
   */
  getCurrentUserID(){
    
    this.userService.searchUsers(CurrentUserService.currentUser).subscribe((users : User[]) => {this.id = users[0].id;
      CurrentUserService.currentUserID = users[0].id;
    this.updater();   

    }   );
    return CurrentUserService.currentUserID;

    
      
  }

  /**
   * Gets the current user's cart. 
   * @returns The current user's cart. 
   */
  getCurrentUserCartSize(){
    this.userService.searchUsers(CurrentUserService.currentUser).subscribe((users : User[]) => {CurrentUserService.currentUserCart = users[0].cart});
    var sum = 0;
    for (let index = 0; index < CurrentUserService.currentUserCart.length; index++) {
      const element = CurrentUserService.currentUserCart[index];
      sum += element.quantity;
      
    }
    return sum;

  }

  getCurrentUserCart(){
    this.userService.searchUsers(CurrentUserService.currentUser).subscribe((users : User[]) => {CurrentUserService.currentUserCart = users[0].cart});
  
    return CurrentUserService.currentUserCart;

  }

  async getCurrentUserCart2(): Promise<Product[]> {
    await new Promise<Product[]>(resolve => {
      this.userService.searchUsers(CurrentUserService.currentUser).subscribe((users : User[]) => {
        CurrentUserService.currentUserCart = users[0].cart
        resolve(users[0].cart)
      });
    })
    return CurrentUserService.currentUserCart
  }

  /**
   * Increments a product's quantity in the cart, adding it if there
   * isn't already an instance of the product being added in the cart. 
   * @param product The product being added to the cart. 
   */
  async addToCart(product: Product){
    try {
      const inventoryVariant = await firstValueFrom(this.productService.getProduct(Math.abs(product.id)));
      await lastValueFrom(this.productService.updateProduct({
        id: Math.abs(product.id),
        name: product.name,
        price: inventoryVariant.price,
        quantity: (inventoryVariant.quantity-1),
        image: product.image
      } as Product));
      console.log('Product updated');
      for (let i = 0; i <= CurrentUserService.currentUserCart.length; i++) {
        // If product isn't found in cart, push it to the end.
        if (i == CurrentUserService.currentUserCart.length) {
          let updatedProduct = structuredClone(product);
          updatedProduct.quantity = 1;
          CurrentUserService.currentUserCart.push(updatedProduct);
          break;
        }
        // If the product is found, increment its quantity.
        if (product.id == CurrentUserService.currentUserCart[i].id) {
          CurrentUserService.currentUserCart[i].quantity++;
          break;
        }
      }
      await this.userService.updateUser({
        id: this.getCurrentUserID(),
        name: this.getCurrentUser(),
        cart: this.getCurrentUserCart()
      } as User).toPromise();
      console.log('User updated');
    } catch (error) {
      console.error('Error updating user or product:', error);
    }
  }
  


    async addItemsToCart(product: Product, numitems: number){
      for (let i = 0; i <= CurrentUserService.currentUserCart.length; i++) {
        // If product isn't found in cart, push it to the end. 
        if (i == CurrentUserService.currentUserCart.length) {
          let updatedProduct = structuredClone(product);
          updatedProduct.quantity = numitems;
          CurrentUserService.currentUserCart.push(updatedProduct);
          break; 
        }
        // If the product is found, increment its quantity. 
        if (product.id == CurrentUserService.currentUserCart[i].id) {
          CurrentUserService.currentUserCart[i].quantity += numitems; 
          break; 
        }}
  
        this.productService.getProduct(Math.abs(product.id)).subscribe(inventoryVariant=>{this.productService.updateProduct(
          {id : Math.abs(product.id),
          name : product.name,
          price : inventoryVariant.price,
          quantity : (inventoryVariant.quantity-numitems),
          image: product.image} as Product).subscribe()});
  
          this.userService.updateUser(
            {id : this.getCurrentUserID(), 
            name: this.getCurrentUser(),  
            cart: (this.getCurrentUserCart())} as User).subscribe();
        
  
      }
  

  
  async checkout() {
    let total = 0;
    await new Promise<void>(resolve => {
      this.productService.getProducts().subscribe((inventory: Product[]) => {
        
        inventory.forEach(prod => {
          CurrentUserService.currentUserCart.forEach(element => {
            if (element.name.includes(prod.name)) {
              total += prod.price * element.quantity
            }
          });
          
        });

        if(CurrentUserService.currentUserJerseys != null){
        for (let index = 0; index < CurrentUserService.currentUserJerseys.length; index++) {
          this.jerseyService.deleteJersey(CurrentUserService.currentUserJerseys[index].id).subscribe();
        }
      }
        CurrentUserService.currentUserCart = []
        CurrentUserService.currentUserJerseys = []

        this.userService.updateUser(
          {id : this.getCurrentUserID(), 
          name: this.getCurrentUser(),  
          cart: (this.getCurrentUserCart()),} as User).subscribe(() => {

            resolve();
          });
      });
    });
    return total;
  }
  

  /**
   * Decrements a product's quantity in the cart, deleting it if it 
   * reaches zero. 
   * @param product The product being removed from the cart. 
   */
  async removeOneFromCart(product: Product){
    await this.removeOneFromCart2(product);
  }

  async removeOneFromCart2(product: Product): Promise<void>{
    this.productService.getProduct(Math.abs(product.id)).subscribe(inventoryVariant=>{this.productService.updateProduct(
        {id : Math.abs(product.id),
        name : product.name,
        price : inventoryVariant.price,
        quantity : (inventoryVariant.quantity+1),
        image: product.image} as Product).subscribe()});
    let i; 
    for (i = 0; i < CurrentUserService.currentUserCart.length; i++) {
      if (CurrentUserService.currentUserCart[i].id == product.id) {
        break; 
      }
    }

    if(CurrentUserService.currentUserCart[i].quantity ==1){
      CurrentUserService.currentUserCart.splice(i, 1); 
    }
    else{
      CurrentUserService.currentUserCart[i].quantity = CurrentUserService.currentUserCart[i].quantity -1;
      
    }
    this.userService.updateUser(
      {id : this.getCurrentUserID(), 
      name: this.getCurrentUser(),  
      cart: (this.getCurrentUserCart())} as User).subscribe();
    

    
    
    
    
    
  




    //this.productService.getProduct(Math.abs(product.id)).subscribe(inventoryVariant=>{this.productService.updateProduct(
    //  {id : product.id,
    //  name : product.name,
    //  price : product.price,
    //  quantity : (inventoryVariant.quantity+1),
    //  image: product.image} as Product).subscribe(()=>{
    //    let i; 
    //    for (i = 0; i < CurrentUserService.currentUserCart.length; i++) {
    //      if (CurrentUserService.currentUserCart[i].id == product.id) {
    //        break; 
    //      }
    //    }
    //    CurrentUserService.currentUserCart[i].quantity--; 
    //    if (CurrentUserService.currentUserCart[i].quantity <= 0) {
    //      CurrentUserService.currentUserCart.splice(i,1); 
    //    }
    //
    //    this.userService.updateUser(
    //      {id : this.getCurrentUserID(), 
    //      name: this.getCurrentUser(),  
    //      cart: (this.getCurrentUserCart())} as User).subscribe();
    //
    //  });});
    

   
  }

  /**
   * Fully removes a product from the cart, regardless of quantity. 
   * @param product The product being removed from the cart. 
   */
  async removeAllFromCart(product: Product){
    await this.removeAllFromCart2(product);
  }

  async removeAllFromCart2(product: Product): Promise<void> {
    this.productService.getProduct(Math.abs(product.id)).subscribe(inventoryVariant=>{this.productService.updateProduct(
      {id : Math.abs(product.id),
      name : product.name,
      price : inventoryVariant.price,
      quantity : (inventoryVariant.quantity+product.quantity),
      image: product.image} as Product).subscribe()});
    let i; 
    for (i = 0; i < CurrentUserService.currentUserCart.length; i++) {
      if (CurrentUserService.currentUserCart[i].id == product.id) {
        break; 
      }
    }
    let updatedProduct = structuredClone(CurrentUserService.currentUserCart[i]);
    CurrentUserService.currentUserCart.splice(i, 1); 
    this.userService.updateUser(
      {id : this.getCurrentUserID(), 
      name: this.getCurrentUser(),  
      cart: (this.getCurrentUserCart())} as User).subscribe();
    
  }
  getCurrentUserJerseys(){
    this.jerseyService.searchJerseys(CurrentUserService.currentUser).subscribe((jersey : Jersey[]) => {CurrentUserService.currentUserJerseys = jersey});
  
    return CurrentUserService.currentUserJerseys;

  }

}
