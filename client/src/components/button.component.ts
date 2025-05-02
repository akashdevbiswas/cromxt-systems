import { Component, Input, input, InputSignal, output, OutputEmitterRef } from '@angular/core';

@Component({
  selector: 'button-component',
  template: `<button [class]="'btn' + ' '+ type() + ' ' + class()" (click)="onClick.emit()">
    {{buttonName()}}
  </button>`,
})
export default class ButtonComponent {
  class: InputSignal<string | undefined> = input<string>();
  buttonName: InputSignal<string> = input.required<string>();
  onClick:OutputEmitterRef<void> = output();
  type:InputSignal<ButtonTypes> = input<ButtonTypes>('btn-neutral');
}


export type ButtonTypes = 'btn-neutral' | 'btn-primary' | 'btn-danger';