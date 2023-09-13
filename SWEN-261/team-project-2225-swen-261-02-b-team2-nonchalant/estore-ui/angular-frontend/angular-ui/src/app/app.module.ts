import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { ProductsComponent } from './products/products.component';
import { MessagesComponent } from './messages/messages.component';

import { AppRoutingModule } from './app-routing.module';
import { ProductSearchComponent } from './product-search/product-search.component';
import { LoginComponent } from './login/login.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { UsersComponent } from './users/users.component';
import { LootboxesComponent } from './lootboxes/lootboxes.component';
import { LootboxDetailComponent } from './lootbox-detail/lootbox-detail.component';
import { AddToCartPopupComponentComponent } from './add-to-cart-popup-component/add-to-cart-popup-component.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { JerseyMakerComponent } from './jersey-maker/jersey-maker.component';
import { JerseyImageComponent } from './jersey-image/jersey-image.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  declarations: [
    AppComponent,
    DashboardComponent,
    ProductsComponent,
    ProductDetailComponent,
    MessagesComponent,
    ProductSearchComponent,
    ShoppingCartComponent,
    LoginComponent,
    UsersComponent,
    LootboxesComponent,
    LootboxDetailComponent,
    AddToCartPopupComponentComponent,
    UserDetailComponent,
    JerseyMakerComponent,
    JerseyImageComponent
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }