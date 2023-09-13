import { ChangeDetectorRef, Component, OnInit, Renderer2 } from '@angular/core';
import { CurrentUserService } from '../current-user.service';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { UserService } from '../user.service';
import { User } from '../user';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  products: Product[] = [];

  constructor(private changeDetection: ChangeDetectorRef, private userService: UserService, private renderer: Renderer2,private productService: ProductService, private currentUserService: CurrentUserService) { }

  currentuser = CurrentUserService.currentUser;
  
  currentuserID = this.currentUserService.getCurrentUserID();

  currentusercart = this.currentUserService.getCurrentUserCart() ;

  cartsize = this.currentUserService.getCurrentUserCartSize();
  cart = this.currentUserService.getCurrentUserCart();


  isAdmin(): boolean {
    if (this.currentuser == "ADMIN"){
      return true;
    }
    return false;
  }
  
  isReserved(): boolean {
    if (this.currentuser == "ADMIN" || this.currentuser == "GUEST"){
      return true;
    }
    return false;
  }

  ngOnInit(): void {
    this.updateSize();

    

    this.getProducts();
    this.changeBackground();

  }
  updateSize(){
    this.currentusercart = this.currentUserService.getCurrentUserCart() ;

    this.userService.searchUsers(CurrentUserService.currentUser)
      .subscribe((currentcart: User[]) => {this.cart = currentcart[0].cart});
      console.log(this.cart);
      this.cartsize = this.currentUserService.getCurrentUserCartSize();

  }

  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products.slice(1, 5));
  }
  changeBackground(){
    this.renderer.setStyle(document.body, 'background-image', 'url("https://www.colorhexa.com/fffff2.png")');

  }
}