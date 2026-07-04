import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { EventModel } from '../../../core/models/event.model';
import { EventService } from '../services/event.service';

@Component({
  selector: 'app-event-detail',
  templateUrl: './event-detail.component.html',
  styleUrls: ['./event-detail.component.scss']
})
export class EventDetailComponent implements OnInit {
  event: EventModel | null = null;
  isLoading = false;
  isDeleting = false;
  errorMessage = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly eventService: EventService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);

    if (!idParam || !Number.isFinite(id)) {
      this.errorMessage = 'ID de evento inválido.';
      return;
    }

    this.isLoading = true;
    this.eventService.getEventById(id).subscribe({
      next: (event) => {
        this.event = event;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Não foi possível carregar os detalhes do evento.';
        this.isLoading = false;
      }
    });
  }

  deleteEvent(): void {
    if (!this.event || !window.confirm(`Deseja excluir o evento "${this.event.title}"?`)) {
      return;
    }

    this.isDeleting = true;
    this.errorMessage = '';

    this.eventService.deleteEvent(this.event.id).subscribe({
      next: () => this.router.navigate(['/events']),
      error: () => {
        this.errorMessage = 'Não foi possível excluir o evento.';
        this.isDeleting = false;
      }
    });
  }
}
