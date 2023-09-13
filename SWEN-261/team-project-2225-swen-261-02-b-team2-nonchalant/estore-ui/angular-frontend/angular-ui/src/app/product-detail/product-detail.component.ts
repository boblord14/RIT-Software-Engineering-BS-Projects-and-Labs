import { Component, Renderer2, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { CurrentUserService } from '../current-user.service';
import { UserService } from '../user.service';
import { LootboxService } from  '../lootbox.service'
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: [ './product-detail.component.css' ]
})
export class ProductDetailComponent implements OnInit {
  product: Product | undefined;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location,
    private currentUserService: CurrentUserService,
    private userService: UserService,
    private changeDetection: ChangeDetectorRef,
    private lootboxService: LootboxService
  ) {}

  x = this.currentUserService;
  showPopup = false;

  ngOnInit(): void {
    this.showPopup = false;
    this.getProduct();

  }

  async getProduct(): Promise<void> {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    try {
      this.product = await lastValueFrom(this.productService.getProduct(id));
      console.log('Product fetched:', this.product);
    } catch (error) {
      console.error('Error fetching product:', error);
    }
  }


  getImage(): string{
    if(this.product?.image == undefined){
      return "https://artsmidnorthcoast.com/wp-content/uploads/2014/05/no-image-available-icon-6.png";
    }
    else{
      return this.product.image;
    }
  }

  getProductQuantity(){
    //this.getProduct();
    return this.product?.quantity;
  }

  goBack(): void {
    this.location.back();
  }
  save(): void {
    if (this.product) {
      this.productService.updateProduct(this.product).subscribe(() => {
          this.lootboxService.getLootboxes().subscribe((lootboxes) => {
            lootboxes.forEach((lootbox) => {
              lootbox.pool.forEach((item) => {
                if (item.id == this.product?.id) {
                  item.name = this.product.name
                  item.image = this.product.image
                  item.price = this.product.price
                  item.quantity = this.product.quantity
                  this.lootboxService.updateLootbox(lootbox).subscribe()
                }
              })
            })
          })

          this.userService.getUsers().subscribe((users) => {
            users.forEach((user) => {
              user.cart.forEach((cartProduct) => {
                if (cartProduct.id == this.product?.id) {
                  cartProduct.name = this.product.name;
                  cartProduct.price = this.product.price;
                  cartProduct.image = this.product.image
                  cartProduct.quantity = this.product.quantity
                  this.userService.updateUser(user).subscribe()
                }
              })
            })

            this.goBack()
          })
        });
    }
  }

  async addToCart(product: Product){
    await this.updateProduct(product);
    await this.getProduct();
    await this.getProductQuantity();
    this.changeDetection.detectChanges();
  }

  async updateProduct(product: Product): Promise<void> {
    await this.getProduct();
    await this.getProductQuantity();
    this.currentUserService.addToCart(product);
    this.showPopup = true;
    setTimeout(() => {
      this.showPopup = false;
    }, 2000);
    await this.getProduct();
    await this.getProductQuantity();
    this.changeDetection.detectChanges();
  }

}