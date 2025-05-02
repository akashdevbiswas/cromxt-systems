import { Inject, Injectable, InjectionToken } from '@angular/core';

export const BROWSER_STORAGE = new InjectionToken<Storage>('Browser Storage', {
  providedIn: 'root',
  factory: () => localStorage && localStorage
});

@Injectable({
  providedIn: 'root'
})
export class BrowserStorageService {
  private storage = Inject(BROWSER_STORAGE);
  public get(key: string) {
    return this.storage && this.storage.getItem(key);
  }
  public set(key: string, value: string) {
    if(!this.storage) return;
    this.storage.setItem(key, value);
  }
  public remove(key: string) {
    if(!this.storage) return;
    this.storage.removeItem(key);
  }
}