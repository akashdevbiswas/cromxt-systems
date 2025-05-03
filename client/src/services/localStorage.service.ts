import { inject, Injectable, InjectionToken } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class BrowserStorageService {

  public get(key: string) {
    return localStorage.getItem(key);
  }
  public set(key: string, value: string) {
    localStorage.setItem(key, value);
  }
  public remove(key: string) {
    localStorage.removeItem(key);
  }
}