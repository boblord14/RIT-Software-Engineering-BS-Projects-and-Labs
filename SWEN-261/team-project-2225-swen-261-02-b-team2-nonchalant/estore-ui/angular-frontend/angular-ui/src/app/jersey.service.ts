import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Jersey } from './jersey';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class JerseyService {
  
  jerseysUrl = 'http://localhost:8080/jerseys'; 
  
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  
  constructor(
    private http: HttpClient, 
    private messageService: MessageService ) { }
    
  /** Log a jerseyService message with the MessageService */
  private log(message: string) {
    console.log(message); 
    this.messageService.add(`JerseyService: ${message}`); 
  }
    
  /** GET jerseys from the server. */
  getJerseys(): Observable< Jersey[] > {
    return this.http.get<Jersey[]>( this.jerseysUrl )
    .pipe(
      catchError(this.handleError<Jersey[]>('getJerseys', []))
      );
  }
      
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      
      console.error(error); // Log to console. 
      
      this.log(`${operation} failed: ${error.message}`);
      
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
      
  /** GET jersey by id. Will throw 404 if id isn't found. */
  getJersey(id: number): Observable<Jersey> {
    const url = `${this.jerseysUrl}/${id}`;
    return this.http.get<Jersey>(url).pipe(
      tap(_ => this.log(`fetched jersey id=${id}`)),
      catchError(this.handleError<Jersey>(`getJersey id=${id}`))
      );
  }
  
  /** PUT: Update a jersey on the server. */
  updateJersey(jersey: Jersey): Observable<any> {
    return this.http.put(this.jerseysUrl, jersey, this.httpOptions).pipe(
      tap(_ => this.log(`updated jersey id=${jersey.id}`)),
      catchError(this.handleError<any>('updateJersey'))
    );
  }
        
  /** POST: Add a jersey to the server. */
  addJersey(jersey: Jersey): Observable<Jersey> {
    console.log("yipeee"); 
    return this.http.post<Jersey>(this.jerseysUrl, jersey, this.httpOptions).pipe(
      tap((newJersey: Jersey) => this.log(`added jersey w/ id=${newJersey.id}`)),
      catchError(this.handleError<Jersey>('addJersey'))
    );
  }

  /** DELETE: delete the jersey from the server */
  deleteJersey(id: number): Observable<Jersey> {
      const url = `${this.jerseysUrl}/${id}`;
        
      return this.http.delete<Jersey>(url, this.httpOptions).pipe(
        tap(_ => this.log(`deleted jersey with id ${id}`)),
        catchError(this.handleError<Jersey>('deleteJersey'))
        );
  }
          
  /** GET jerseys made by a certain creator. */
  searchJerseys(creator: string): Observable<Jersey[]> {
    if (!creator.trim()) return of([]); 
    const url = `${this.jerseysUrl}/?creator=${creator}`; 
    return this.http.get<Jersey[]>(url).pipe(
      tap(x => x.length ?
        this.log(`found jerseys made by user "${creator}"`) :
        this.log(`no jerseys made by user "${creator}"`)), 
        catchError(this.handleError<Jersey[]>('searchJerseys', []))
      ); 
  }
              
}
            