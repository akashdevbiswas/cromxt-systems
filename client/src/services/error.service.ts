import { Injectable, signal, WritableSignal } from "@angular/core";



@Injectable({providedIn: 'root'})
export class ErrorService {
  isError:WritableSignal<boolean> = signal(false);
  errorMessage:WritableSignal<string> = signal('');

  setError(message: string) {
    setTimeout(() => {
      this.isError.set(false);
    this.errorMessage.set('');
    }, 3000);
    this.isError.set(true);
    this.errorMessage.set(message);
  }
}