import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { MessageService } from '../message.service';
import { CurrentUserService } from '../current-user.service';
import { UserService } from '../user.service';
import { LootboxService } from  '../lootbox.service'
import { ChangeDetectorRef } from '@angular/core';
import { AddToCartPopupComponentComponent } from '../add-to-cart-popup-component/add-to-cart-popup-component.component';
import { JerseyService } from '../jersey.service';
import { Jersey } from '../jersey';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  selectedProduct?: Product;
adminjersey: Jersey | undefined;
  products: Product[] = [];
  isDeletingOrAddingToCart: boolean = false;

  constructor(private jerseyService: JerseyService, private productService: ProductService, private messageService: MessageService, private currentUserService: CurrentUserService, private userService : UserService, private changeDetection: ChangeDetectorRef, private lootboxService: LootboxService) { }

  x= this.currentUserService;

  ngOnInit(): void {  
    this.jerseyService.getJersey(0).subscribe((adminjerseys: Jersey) => {this.adminjersey = adminjerseys});
    this.getProducts();
  }

  onSelect(product: Product): void {
    this.selectedProduct = product;
    this.messageService.add(`ProductsComponent: Selected product id=${product.id}`);
  }

  getProducts(): void {
    this.productService.getProducts()
        .subscribe((products) => {
          this.products = []

          products.forEach((invProduct) => {
            if (invProduct.quantity > 0 || this.currentUserService.isAdmin()) {
              this.products.push(invProduct)
            }
          })
          
          console.log(this.products)
        });
  }

  add(name: string, price: number, quantity: number): void {
    name = name.trim();
    if (!name) { return; }
    if (price <= 0) { return; }
    if (quantity <= 0) { return; }
    this.productService.addProduct({name, price, quantity} as Product)
      .subscribe(product => {
        this.products.push(product);
      });

  }
  async delete(product: Product){
      await this.deletewrap(product);
  }

  async deletewrap(product: Product): Promise<void> {
    this.products = this.products.filter((h) => h !== product);
    
    this.userService.getUsers().subscribe((users) => {
    users.forEach((user) => {
    let newCart: Product[] = [];
    user.cart.forEach((value) => {
    this.userService.removeAllFromCart(user, product);
    });
    });
    });
    
    this.lootboxService.getLootboxes().subscribe((lootboxes) => {
    lootboxes.forEach((lootbox) => {
    let newPool: Product[] = [];
    lootbox.pool.forEach((value) => {
    if (value.id != product.id) {
    newPool.push(value);
    }
    });
    lootbox.pool = newPool;
    this.lootboxService.updateLootbox(lootbox).subscribe();
    });
    });
    
    this.productService.deleteProduct(product.id).subscribe();
    }
  showPopup = false;

  addToCart(product: Product): void{
    this.showPopup = true;
    setTimeout(() => {
      this.showPopup = false;
    }, 2000);
    this.currentUserService.addToCart(product);
    this.getProducts();
    this.changeDetection.detectChanges(); 

  }
  updatePrice(price: number){
    if(this.adminjersey){
    this.adminjersey.price =  price;
    this.jerseyService.updateJersey(this.adminjersey).subscribe();
    }

    this.jerseyService.getJerseys().subscribe((jerseys :Jersey[]) => {
      for (let index = 1; index < jerseys.length; index++) {
        jerseys[index].price=price;
        this.jerseyService.updateJersey(jerseys[index]).subscribe();
      }
    });

  }
}