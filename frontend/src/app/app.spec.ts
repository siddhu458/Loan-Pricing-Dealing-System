import { TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { App } from './app';

/* NON-standalone stubs */
@Component({
  selector: 'app-header',
  template: '',
  standalone: false
})
class HeaderStubComponent {}

@Component({
  selector: 'app-footer',
  template: '',
  standalone: false
})
class FooterStubComponent {}

describe('App Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        App,
        HeaderStubComponent,
        FooterStubComponent
      ],
      imports: [RouterTestingModule]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    expect(fixture.componentInstance).toBeTruthy();
  });

});
