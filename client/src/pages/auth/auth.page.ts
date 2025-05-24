import { Component } from "@angular/core";
import { Router, RouterOutlet } from "@angular/router";



@Component({
  selector: "auth-page",
  templateUrl: "auth.page.html",
  imports: [RouterOutlet],
  standalone: true
})
export class AuthPage {
  constructor(protected router: Router) {}
}