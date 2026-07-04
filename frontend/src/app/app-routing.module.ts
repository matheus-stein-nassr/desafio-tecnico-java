import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'events',
    loadChildren: () =>
      import('./features/events/events.module').then((m) => m.EventsModule)
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'events'
  },
  {
    path: '**',
    redirectTo: 'events'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
