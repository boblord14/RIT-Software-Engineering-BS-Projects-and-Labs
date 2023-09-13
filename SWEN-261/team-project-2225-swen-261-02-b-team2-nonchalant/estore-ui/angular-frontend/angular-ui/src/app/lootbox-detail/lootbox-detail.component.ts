import { Component, Renderer2, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Product } from '../product';
import { Lootbox } from '../lootbox';
import { LootboxService } from '../lootbox.service';
import { CurrentUserService } from '../current-user.service';
import { ProductService } from '../product.service';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-lootbox-detail',
  templateUrl: './lootbox-detail.component.html',
  styleUrls: [ './lootbox-detail.component.css' ]
})
export class LootboxDetailComponent implements OnInit {
  lootbox: Lootbox | undefined;
  products: Product[] = [];
  wonitems: Product[] = [];
pool : Product[] = [];
  constructor(
    private route: ActivatedRoute,
    private lootboxService: LootboxService,
    private productService: ProductService,
    private location: Location,
    private currentUserService: CurrentUserService,
    private renderer: Renderer2,
  ) {}

  x = this.currentUserService;
  showgif = false;
  isConfirmed = false;
  roll = true;
  dollaramountwon = 0;


  ngOnInit(): void {
    this.getLootbox();
    this.getProducts();


  }
  getProducts(): void {
      this.productService.getProducts()
          .subscribe(products => this.products = products);
    }
  getLootbox(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.lootboxService.getLootbox(id)
      .subscribe(lootbox => this.lootbox = lootbox);
  }
  getpool(){
    const productPool: Array<Product>= [];
    this.getCheckedProductIds().forEach((number) => {
      productPool.push(this.getProductLocal(number));
      console.log(number);
      console.log(this.getProductLocal(number))

    });
    return productPool;
  }
  inPool(x: number){
    if(this.lootbox){
    for (let index = 0; index < this.lootbox.pool.length; index++) {
      const element = this.lootbox.pool[index];
      if(element.id == x){
        return true;
      }
    }
  }
  return false;
  }

  getCartIds(){
    var cartIds : Array<number> = [];
    for (let index = 0; index < this.currentUserService.getCurrentUserCart().length; index++) {
      const element = this.currentUserService.getCurrentUserCart()[index];
        cartIds.push(element.id)
      }
    return cartIds;
  }

  getProductLocal(id: number){
    for (let index = 0; index < this.products.length; index++) {
      const element = this.products[index];
      if(element.id == id){
        return element;
      }
    }
    return this.products[-1];
  }

  getCheckedProductIds(): number[] {
    const checkedProducts: NodeListOf<HTMLInputElement> = document.querySelectorAll(".product:checked");
    console.log(checkedProducts);
    const checkedProductIds: number[] = [];
    checkedProducts.forEach((product) => {
      checkedProductIds.push((+product.id));
      console.log(product.id);

    });
    return checkedProductIds;
  }
  

  getImage(): string{
    if(this.lootbox?.image == undefined){
      return "https://artsmidnorthcoast.com/wp-content/uploads/2014/05/no-image-available-icon-6.png";
    }
    else{
      return this.lootbox.image;
    }
  }



  goBack(): void {
    this.location.back();
  }
  save(): void {
    if (this.lootbox) {
      this.lootbox = {id: this.lootbox.id, name: this.lootbox.name, price: this.lootbox.price, pool: this.getpool(), image: this.lootbox.image }

      this.lootboxService.updateLootbox(this.lootbox)
        .subscribe(() => this.goBack());
    }
  }
  areYouSure() {
    this.isConfirmed = window.confirm("Are you sure you want to buy this lootbox for $"+ this.lootbox?.price + "?");
    if (this.isConfirmed == true) {
      this.renderer.setStyle(document.getElementById('myModal'), 'display', 'block');
      this.buy();
    } else {
      this.isConfirmed = false;
    }
  }
  async buy(){
    await this.buy2()
  }

  async buy2(): Promise<void>{
    this.showgif = true;
    this.wonitems = [];
    if(this.lootbox){
      var product1 =  this.lootbox.pool[Math.floor(Math.random()*this.lootbox.pool.length)];
      var product2 =  this.lootbox.pool[Math.floor(Math.random()*this.lootbox.pool.length)];
      var product3 =  this.lootbox.pool[Math.floor(Math.random()*this.lootbox.pool.length)];
      this.wonitems.push(product1, product2, product3);
      this.dollaramountwon = product1.price + product2.price + product3.price;

      if(product1.id == product2.id && product1.id == product3.id){
        this.currentUserService.addItemsToCart({id: product1.id*-1, name: product1.name, price: 0, quantity: product1.quantity, image: product1.image } as Product, 3);
      }
      else if(product1.id == product2.id){
        this.currentUserService.addItemsToCart({id: product1.id*-1, name: product1.name, price: 0, quantity: product1.quantity, image: product1.image } as Product, 2);
        this.currentUserService.addToCart({id: product3.id*-1, name: product3.name, price: 0, quantity: product3.quantity, image: product3.image } as Product);

      }
      else if(product1.id == product3.id){
        this.currentUserService.addItemsToCart({id: product1.id*-1, name: product1.name, price: 0, quantity: product1.quantity, image: product1.image } as Product, 2);
        this.currentUserService.addToCart({id: product2.id*-1, name: product2.name, price: 0, quantity: product2.quantity, image: product2.image } as Product);

      }
      else if(product2.id == product3.id){
        this.currentUserService.addItemsToCart({id: product1.id*-1, name: product1.name, price: 0, quantity: product1.quantity, image: product1.image } as Product, 2);
        this.currentUserService.addToCart({id: product3.id*-1, name: product3.name, price: 0, quantity: product3.quantity, image: product3.image } as Product);

      }
      else{
        this.currentUserService.addToCart({id: product1.id*-1, name: product1.name, price: 0, quantity: product1.quantity, image: product1.image } as Product);
        this.currentUserService.addToCart({id: product2.id*-1, name: product2.name, price: 0, quantity: product2.quantity, image: product2.image } as Product);
        this.currentUserService.addToCart({id: product3.id*-1, name: product3.name, price: 0, quantity: product3.quantity, image: product3.image } as Product);

      }
    

      


      
    
      
      //this.productService.getProduct(Math.abs(product1.id)).subscribe(inventoryVariant=>{this.productService.updateProduct(
      //  {id : Math.abs(product1.id),
      //  name : product1.name,
      //  price : inventoryVariant.price,
      //  quantity : (inventoryVariant.quantity- 1),
      //  image: product1.image} as Product).subscribe()});
//
      //this.productService.getProduct(Math.abs(product2.id)).subscribe(inventoryVariant=>{this.productService.updateProduct(
      //  {id : Math.abs(product2.id),
      //  name : product2.name,
      //  price : inventoryVariant.price,
      //  quantity : (inventoryVariant.quantity- 1),
      //  image: product2.image} as Product).subscribe()});
//
      //this.productService.getProduct(Math.abs(product3.id)).subscribe(inventoryVariant=>{this.productService.updateProduct(
      //  {id : Math.abs(product3.id),
      //  name : product3.name,
      //  price : inventoryVariant.price,
      //  quantity : (inventoryVariant.quantity- 1),
      //  image: product3.image} as Product).subscribe()});

      
    }
    this.isConfirmed = false;
    this.roll = true;
  }
  


    
  

  




}