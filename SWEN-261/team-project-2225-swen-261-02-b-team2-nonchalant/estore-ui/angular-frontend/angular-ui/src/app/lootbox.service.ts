import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Lootbox } from './lootbox';
import { MessageService } from './message.service';

@Injectable({ providedIn: 'root' })
export class LootboxService {

  private lootboxesUrl = 'http://localhost:8080/lootboxes';  // URL to web api

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }

  /** Log a roductService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`LootboxService: ${message}`);
  }

  /** GET lootboxes from the server */
  getLootboxes(): Observable<Lootbox[]> {
    return this.http.get<Lootbox[]>(this.lootboxesUrl)
    .pipe(
      catchError(this.handleError<Lootbox[]>('getLootboxes', []))
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

/** GET lootbox by id. Will 404 if id not found */
getLootbox(id: number): Observable<Lootbox> {
  const url = `${this.lootboxesUrl}/${id}`;
  return this.http.get<Lootbox>(url).pipe(
    tap(_ => this.log(`fetched lootbox id=${id}`)),
    catchError(this.handleError<Lootbox>(`getLootbox id=${id}`))
  );
}


  /** PUT: update the lootbox on the server */
  updateLootbox(lootbox: Lootbox): Observable<any> {
    return this.http.put(this.lootboxesUrl, lootbox, this.httpOptions).pipe(
      tap(_ => this.log(`updated lootbox id=${lootbox.id}`)),
      catchError(this.handleError<any>('updateLootbox'))
    );
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  /** POST: add a new lootbox to the server */
addLootbox(lootbox: Lootbox): Observable<Lootbox> {
  return this.http.post<Lootbox>(this.lootboxesUrl, lootbox, this.httpOptions).pipe(
    tap((newLootbox: Lootbox) => this.log(`added lootbox w/ id=${newLootbox.id}`)),
    catchError(this.handleError<Lootbox>('addLootbox'))
  );
}

/** DELETE: delete the lootbox from the server */
deleteLootbox(id: number): Observable<Lootbox> {
  const url = `${this.lootboxesUrl}/${id}`;

  return this.http.delete<Lootbox>(url, this.httpOptions).pipe(
    tap(_ => this.log(`deleted lootbox id=${id}`)),
    catchError(this.handleError<Lootbox>('deleteLootbox'))
  );
}

/* GET lootboxes whose name contains search term */
searchLootboxes(term: string): Observable<Lootbox[]> {
  if (!term.trim()) {
    // if not search term, return empty lootbox array.
    return of([]);
  }
  return this.http.get<Lootbox[]>(`${this.lootboxesUrl}/?name=${term}`).pipe(
    tap(x => x.length ?
       this.log(`found lootboxes matching "${term}"`) :
       this.log(`no lootboxes matching "${term}"`)),
    catchError(this.handleError<Lootbox[]>('searchLootboxes', []))
  );
}


}