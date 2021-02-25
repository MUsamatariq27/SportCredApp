import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, ObservableInput, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

const HttpHeader:HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http:HttpClient) { }

  private _url:string = ""

  private errorHandle = (error: HttpErrorResponse):ObservableInput<any> => {
    if (error.error instanceof ErrorEvent) { // Error Occured In Client
      console.error('An error has occurred:', error.error);
    } else { // Server Returned an Error
      console.error('Server Responded with:', error.status, error.error);
    }
    return throwError('Something bad happened; please try again!');
  }

  private devBypass = (requestType:'POST'|'GET', url:string, payload:object = {}):Observable<any> => {
    if (requestType === 'GET') return new Observable<any>(obs=>{ obs.next(JSON.parse(localStorage.getItem(url))); obs.complete(); });
    else return new Observable<any>(obs=>{ localStorage.setItem(url, JSON.stringify(payload)); obs.next({message:'Done'}); obs.complete(); });
  }

  // CRUD Api Calls
  post(url:string, params:any):Observable<any> {
    return this.http.post<any>(this._url + url, params).pipe(
      tap(_=>{ console.log("POST:", url) }),
      catchError(this.errorHandle)
    );
  }
  put(url:string, params:any):Observable<any> {
    return this.http.put<any>(this._url + url, params).pipe(
      tap(_=>{ console.log("PUT:", url) }),
      catchError(this.errorHandle)
    );
  }
  get(url:string, queryParams:{[key:string]:string}):Observable<any> {
    let query = queryParams? '?' + Object.keys(queryParams).map(x=>x+'='+queryParams[x]).join('&') : '';
    const opts = {
      headers: HttpHeader,
      body: queryParams
    }
    return this.http.get<any>(this._url + url, opts).pipe(
      tap(_=>{ console.log("GET:", url) }),
      catchError(this.errorHandle)
    );
  }
  patch(url:string, params:any):Observable<any> {
    return this.http.patch<any>(this._url + url, params).pipe(
      tap(_=>{ console.log("PATCH:", url) }),
      catchError(this.errorHandle)
    );
  }
  delete(url:string, params:any):Observable<any> {
    const opts = {
      headers: HttpHeader,
      body: params
    }
    return this.http.delete<any>(this._url + url, opts).pipe(
      tap(_=>console.log("DELETE:", url)),
      catchError(this.errorHandle)
    )
  }
  
}
