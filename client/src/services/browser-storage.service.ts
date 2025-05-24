import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';



@Injectable({
  providedIn: 'root'
})
export class BrowserStorageService {
  private isBrowser:boolean = false;
  private storage: Storage | null = null;

  constructor(@Inject(PLATFORM_ID) platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);
    if(this.isBrowser) {
      this.storage = localStorage;
    }
  }
  get(key: string) {
    if(!this.isBrowser || !this.storage) {
      return null;
    }
    return this.storage.getItem(key);
  }
  set(key: string, value: string) {
    if(!this.isBrowser || !this.storage) {
      return;
    }
    this.storage.setItem(key, value);
  }
  remove(key: string) {
    if(!this.isBrowser || !this.storage) {
      return;
    }
    this.storage.removeItem(key);
  }
}