import { Component, input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {faEye,faEyeSlash} from "@fortawesome/free-regular-svg-icons";


@Component({
  selector: 'input-component',
  template: `
    @if(type() === 'date') {
    <label [class]="'w-full input' + ' ' + class()">
      <span class="label">{{ lable() }}</span>
      <input type="date" [name]="lable()" [formControl]="inputControl()" />
    </label>
    }
    @else if (type() === 'select' && selectOptions()) {
    <select class="select w-full" [formControl]="inputControl()">
      <option disabled selected>{{lable()}}</option>
      @for(option of selectOptions(); track option ) {
      <option>{{ option }}</option>
      }
    </select>
    }@else if (type()=== 'password') {
    <label [class]="'input w-full' + ' ' + class()">
      <span class="label">{{ lable() }}</span>
      <input [formControl]="inputControl()" [type]="visible ? 'text' : 'password'" [name]="lable()" [formControl]="inputControl()" />
      <fa-icon class="cursor-pointer" [icon]="visible ? faEye : faEyeSlash" (click)="onChangeVisibility()"></fa-icon>
    </label>
    }@else {
    <label [class]="'input w-full' + ' ' + class()">
      <span class="label">{{ lable() }}</span>
      <input [formControl]="inputControl()" type="text" [name]="lable()" [formControl]="inputControl()" />
    </label>
    }
  `,
  imports: [ReactiveFormsModule,FontAwesomeModule ],
  standalone: true,
})
export default class InputComponent {
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  type = input<InputTypes>('text');
  lable = input.required<string>();
  class = input<string>();
  inputControl = input.required<FormControl>();
  visible = false;
  selectOptions = input<string[]>(['Invaid']);

  onChangeVisibility() {
    this.visible = !this.visible
  }
}

export type InputTypes = 'text' | 'select' | 'password' | 'date';
