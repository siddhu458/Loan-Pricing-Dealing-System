import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Footer } from './components/footer/footer';
import { Header } from './components/header/header';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    Header,
    Footer
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  exports: [
    CommonModule,
    ReactiveFormsModule,
    Header,
    Footer
  ]
})
export class SharedModule {}
