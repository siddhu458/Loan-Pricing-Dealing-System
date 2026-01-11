import { Component, signal } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('loan-pricing-deal');
  
  isAuthPage = false;
  
  constructor(private router: Router) {
  this.router.events.subscribe(() => {
    this.isAuthPage = this.router.url.includes('/auth');
  });
}

}
