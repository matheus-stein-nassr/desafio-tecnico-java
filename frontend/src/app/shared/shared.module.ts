import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { EmptyStateComponent } from './components/empty-state/empty-state.component';
import { TruncatePipe } from './pipes/truncate.pipe';

@NgModule({
  declarations: [EmptyStateComponent, TruncatePipe],
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatCardModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatToolbarModule,
    MatProgressSpinnerModule
  ],
  exports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatCardModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    EmptyStateComponent,
    TruncatePipe
  ]
})
export class SharedModule {}
