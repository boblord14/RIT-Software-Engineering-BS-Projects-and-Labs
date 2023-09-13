import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProductsComponent } from './products/products.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { UsersComponent } from './users/users.component';
import { LootboxesComponent } from './lootboxes/lootboxes.component';
import { LootboxDetailComponent } from './lootbox-detail/lootbox-detail.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { JerseyMakerComponent } from './jersey-maker/jersey-maker.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'detail/:id', component: ProductDetailComponent },
  { path: 'products', component: ProductsComponent },
  { path: 'shopping-cart', component: ShoppingCartComponent },
  { path: 'users', component: UsersComponent },
  { path: 'lootboxes', component: LootboxesComponent },
  { path: 'lootboxdetail/:id', component: LootboxDetailComponent },
  { path: 'userdetail/:id', component: UserDetailComponent },
  { path: 'jersey-maker', component: JerseyMakerComponent }



];




@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}