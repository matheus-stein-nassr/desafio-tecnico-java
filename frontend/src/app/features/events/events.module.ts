import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatPaginatorIntl } from '@angular/material/paginator';

import { SharedModule } from '../../shared/shared.module';
import { createPortuguesePaginatorIntl } from '../../core/i18n/portuguese-paginator-intl';
import { EventDetailComponent } from './event-detail/event-detail.component';
import { EventFormComponent } from './event-form/event-form.component';
import { EventListComponent } from './event-list/event-list.component';
import { EventsRoutingModule } from './events-routing.module';

@NgModule({
  declarations: [EventListComponent, EventFormComponent, EventDetailComponent],
  imports: [CommonModule, ReactiveFormsModule, SharedModule, EventsRoutingModule],
  providers: [
    { provide: MatPaginatorIntl, useFactory: createPortuguesePaginatorIntl }
  ]
})
export class EventsModule {}
