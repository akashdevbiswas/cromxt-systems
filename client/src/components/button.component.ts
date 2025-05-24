import { Component, input, OnInit, output } from '@angular/core';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faArrowRight,
  IconDefinition,
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'button-component',
  template: `
  @if (buttonType() === 'navigation') {
    <a (click)="onRoute()" [class]=" 'font-medium text-blue-700 hover:text-blue-800 hover:underline ' + buttonStyle+ ' ' + classes()">{{buttonName()}} <span><fa-icon [icon]="iconDefination()"></fa-icon></span></a>
  }@else {
    <button
      (click)="onClick.emit()"
      [class]="
        ' text-white bg-blue-700 hover:bg-blue-800 ' + buttonStyle + ' '+
        classes()
      "
    >
      {{ buttonName() }}
      <span><fa-icon [icon]="iconDefination()"></fa-icon></span>
    </button>
  }
  `,
  imports: [FontAwesomeModule],
  standalone: true,
})
export class ButtonComponent {
  buttonName = input<string>('Submit');
  iconDefination = input<IconDefinition>(faArrowRight);
  onClick = output<void>();
  classes = input<string>('');
  buttonType = input<ButtonType>('submit');
  navigationUrl = input<string>('/');

  buttonStyle = 'cursor-pointer w-fit rounded-xl md:rounded-2xl px-3 py-1 md:px-4 md:py:2 lg:px-5 lg:py-2.5 text-center inline-flex items-center gap-2 text-lg md:text-xl lg:text-2xl';

  constructor(private router: Router) {}

  onRoute() {
    this.router.navigateByUrl(this.navigationUrl());
  }
}


type ButtonType = 'submit' | 'navigation';