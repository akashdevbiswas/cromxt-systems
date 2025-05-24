import { NgClass } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  input,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faEye,
  faEyeSlash,
  IconDefinition,
} from '@fortawesome/free-regular-svg-icons';

@Component({
  selector: 'input-component',
  template: `
    <div class="input">
      <label class="input-label" [for]="'id-' + title()">{{ title() }}</label>
      <div
        [ngClass]="{
          'input-field': true,
          'input-focus': isFocus,
          'input-error': isError
        }"
      >
        @if (type() === 'password') {
        <input
          #inputField
          [autocomplete]="'off'"
          [placeholder]="placeholder()"
          (focus)="onFucus()"
          (blur)="onBlur()"
          class="input-tag"
          [id]="'id-' + title()"
          [type]="isPassword ? 'password' : 'text'"
          [formControl]="formControlName()"
        />
        @if(passwordView()) {
        <span class="icon"
          ><fa-icon
            class="w-full"
            (click)="isPassword = !isPassword"
            [icon]="isPassword ? faEyeSlash : faEye"
          ></fa-icon
        ></span>
        } } @else if (type() === 'options' ) {
        <select
          (focus)="onFucus()"
          (blur)="onBlur()"
          class="input-tag"
          [id]="'id-' + title()"
          [formControl]="formControlName()"
        >
        <option disabled [selected]="true" >Select {{ title() }}</option>
        @for(item of options(); track item) {
          <option>{{ item }}</option>
        }
        </select>
        } @else {
        <input
          #inputField
          [autocomplete]="'off'"
          (focus)="onFucus()"
          (blur)="onBlur()"
          [placeholder]="placeholder()"
          class="input-tag"
          [id]="'id-' + title()"
          [type]="type()"
          [formControl]="formControlName()"
        />
        @if (icon()) {
        <span class="icon"><fa-icon [icon]="inputIcon"></fa-icon></span>
        } }
      </div>
    </div>
  `,
  standalone: true,
  imports: [ReactiveFormsModule, FontAwesomeModule, NgClass],
})
export class InputComponent implements OnInit, AfterViewInit {
  // Icons
  faEye = faEye;
  faEyeSlash = faEyeSlash;

  // Input fields

  type = input<InputType>('text');
  title = input<string>('');
  formControlName = input<FormControl<string | null>>(new FormControl(''));
  icon = input<IconDefinition | null>(null);
  options = input<string[]>([]);
  placeholder = input<string>('');
  focus = input<boolean>(false);
  passwordView = input<boolean>(false);
  isError = false;

  isPassword: boolean = false;
  inputIcon: IconDefinition = faEye;

  isFocus!: boolean;

  onFucus() {
    this.isFocus = true;
  }
  onBlur() {
    this.isFocus = false;
    this.isError = this.formControlName().invalid;
  }

  ngOnInit(): void {
    this.isPassword = this.type() === 'password';
    if (this.icon()) {
      this.inputIcon = this.icon()!;
    }
    this.isFocus = this.focus();
  }

  @ViewChild('inputField') inputElement!: ElementRef<
    HTMLInputElement | HTMLSelectElement
  >;

  ngAfterViewInit(): void {
    if (this.focus()) {
      this.inputElement.nativeElement.focus();
    }
  }
}

type InputType =
  | 'text'
  | 'password'
  | 'email'
  | 'number'
  | 'date'
  | 'time'
  | 'options';
