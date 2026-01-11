import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoanList } from './components/loanlist/loanlist';
import { Loancreate } from './components/loancreate/loancreate';
import { LoansRoutingModule } from './loan-routing.module';



@NgModule({
  declarations: [
    LoanList,
    Loancreate
  ],
  imports: [
    CommonModule,
    FormsModule,
    LoansRoutingModule
  ]
})
export class LoansModule {}
