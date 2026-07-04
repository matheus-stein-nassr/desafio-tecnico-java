import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { PageEvent } from '@angular/material/paginator';

import { EventModel } from '../../../core/models/event.model';
import { EventService } from '../services/event.service';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {
  events: EventModel[] = [];
  pageSize = 10;
  pageIndex = 0;
  total = 0;
  isLoading = false;
  errorMessage = '';

  constructor(private readonly eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  onPageChange(page: PageEvent): void {
    this.pageSize = page.pageSize;
    this.pageIndex = page.pageIndex;
    this.loadEvents();
  }

  loadEvents(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.eventService.getEvents(this.pageIndex, this.pageSize).subscribe({
      next: (page) => {
        this.events = page.content;
        this.total = page.totalElements;
        this.isLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.status === 0
          ? 'Não foi possível conectar ao backend. Verifique se ele está executando na porta 8080.'
          : `Não foi possível carregar os eventos (erro ${error.status}).`;
        this.isLoading = false;
      }
    });
  }

}
