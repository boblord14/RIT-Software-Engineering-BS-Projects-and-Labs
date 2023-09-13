import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { User } from './user';
import { MessageService } from './message.service';
import { Product } from './product';
import { ProductService } from './product.service';

@Injectable({ providedIn: 'root' })
export class UserService {

  private usersUrl = 'http://localhost:8080/users';  // URL to web api

  constructor(
    private http: HttpClient,
    private productService: ProductService,
    private messageService: MessageService) { }

  /** Log a userService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`UserService: ${message}`);
  }

  /** GET users from the server */
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl)
    .pipe(
      catchError(this.handleError<User[]>('getUsers', []))
    );
  }




  /**
 * Handle Http operation that failed.
 * Let the app continue.
 *
 * @param operation - name of the operation that failed
 * @param result - optional value to return as the observable result
 */
private handleError<T>(operation = 'operation', result?: T) {
  return (error: any): Observable<T> => {

    // TODO: send the error to remote logging infrastructure
    console.error(error); // log to console instead

    // TODO: better job of transforming error for user consumption
    this.log(`${operation} failed: ${error.message}`);

    // Let the app keep running by returning an empty result.
    return of(result as T);
  };
}

/** GET user by id. Will 404 if id not found */
getUser(id: number): Observable<User> {
  const url = `${this.usersUrl}/${id}`;
  return this.http.get<User>(url).pipe(
    tap(_ => this.log(`fetched user id=${id}`)),
    catchError(this.handleError<User>(`getUser id=${id}`))
  );
}




  /** PUT: update the user on the server */
  updateUser(user: User): Observable<any> {
    return this.http.put(this.usersUrl, user, this.httpOptions).pipe(
      tap(_ => this.log(`updated user id=${user.id}`)),
      catchError(this.handleError<any>('updateUser'))
    );
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  /** POST: add a new user to the server */
addUser(user: User): Observable<User> {
  return this.http.post<User>(this.usersUrl, user, this.httpOptions).pipe(
    tap((newUser: User) => this.log(`added user w/ id=${newUser.id}`)),
    catchError(this.handleError<User>('addUser'))
  );
}

/** DELETE: delete the user from the server */
deleteUser(id: number): Observable<User> {
  const url = `${this.usersUrl}/${id}`;

  return this.http.delete<User>(url, this.httpOptions).pipe(
    tap(_ => this.log(`deleted user id=${id}`)),
    catchError(this.handleError<User>('deleteUser'))
  );
}

/* GET users whose name contains search term */
searchUsers(term: string): Observable<User[]> {
  if (!term.trim()) {
    // if not search term, return empty user array.
    return of([]);
  }
  return this.http.get<User[]>(`${this.usersUrl}/?name=${term}`).pipe(
    tap(x => x.length ?
       this.log(`found users matching "${term}"`) :
       this.log(`no users matching "${term}"`)),
    catchError(this.handleError<User[]>('searchUsers', []))
  );
}

async removeAllFromCart(user: User, product: Product){
  await this.removeAllFromCart2(user, product);
}

async removeAllFromCart2(user: User, product: Product): Promise<void> {
  var newuser = user;
  this.productService.getProduct(Math.abs(product.id)).subscribe(inventoryVariant=>{this.productService.updateProduct(
    {id : Math.abs(product.id),
    name : product.name,
    price : inventoryVariant.price,
    quantity : (inventoryVariant.quantity+product.quantity),
    image: product.image} as Product).subscribe()});
    let i; 
    for (i = 0; i < user.cart.length; i++) {
      if (user.cart[i].id == product.id) {
        break; 
      }
    }
    newuser.cart.splice(i, 1); 
    this.updateUser(
      {id : user.id, 
      name: user.name,  
      cart: newuser.cart} as User).subscribe();
    
  }
  async removeOneFromCart(user: User, product: Product){
    await this.removeAllFromCart2(user, product);
  }
  
  async removeOneFromCart2(user: User, product: Product): Promise<void> {
    var newuser = user;
    this.productService.getProduct(Math.abs(product.id)).subscribe(inventoryVariant=>{this.productService.updateProduct(
      {id : Math.abs(product.id),
      name : product.name,
      price : inventoryVariant.price,
      quantity : (inventoryVariant.quantity+1),
      image: product.image} as Product).subscribe()});
      let i; 
      for (i = 0; i < user.cart.length; i++) {
        if (user.cart[i].id == product.id) {
          break; 
        }
      }
     
      newuser.cart[i].quantity = newuser.cart[i].quantity -1;
        
      
      
      this.updateUser(
        {id : user.id, 
        name: user.name,  
        cart: newuser.cart} as User).subscribe();
      
    }
}