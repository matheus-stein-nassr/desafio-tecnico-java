import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { EventPayload } from '../../../core/models/event.model';
import { EventService } from '../services/event.service';

interface ApiErrorResponse {
  message?: string;
  fieldErrors?: Record<string, string>;
}

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.scss']
})
export class EventFormComponent implements OnInit {

  readonly eventForm = this.fb.group({
  title: this.fb.nonNullable.control('', [
    Validators.required,
    Validators.maxLength(100)
  ]),
  description: this.fb.nonNullable.control('', [
    Validators.maxLength(1000)
  ]),
  eventDate: this.fb.control<Date | null>(null, Validators.required),
  eventTime: this.fb.nonNullable.control('', Validators.required),
  location: this.fb.nonNullable.control('', [
    Validators.required,
    Validators.maxLength(200)
  ])
});

  isEditMode = false;
  isLoading = false;
  isSubmitting = false;
  errorMessage = '';

  private eventId: number | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly eventService: EventService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      return;
    }

    const id = Number(idParam);

    if (!Number.isFinite(id)) {
      this.errorMessage = 'ID de evento inválido.';
      return;
    }

    this.isEditMode = true;
    this.eventId = id;
    this.loadEvent(id);
  }

  onSubmit(): void {
    if (this.eventForm.invalid) {
      this.eventForm.markAllAsTouched();
      return;
    }

    const form = this.eventForm.getRawValue();

    const eventDate = new Date(form.eventDate!);

    const [hours, minutes] = form.eventTime.split(':');

    eventDate.setHours(Number(hours));
    eventDate.setMinutes(Number(minutes));
    eventDate.setSeconds(0);
    eventDate.setMilliseconds(0);

    if (eventDate.getTime() < Date.now()) {
      this.errorMessage = 'A data do evento deve ser atual ou futura.';
      return;
    }

    const payload: EventPayload = {
      title: form.title,
      description: form.description,
      eventDateTime: eventDate.toISOString(),
      location: form.location
    };

    this.isSubmitting = true;
    this.errorMessage = '';

    const request$ =
      this.isEditMode && this.eventId !== null
        ? this.eventService.updateEvent(this.eventId, payload)
        : this.eventService.createEvent(payload);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.router.navigate(['/events']);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = this.getErrorMessage(error);
        this.isSubmitting = false;
      }
    });
  }

  private loadEvent(id: number): void {
    this.isLoading = true;
    this.eventService.getEventById(id).subscribe({
      next: (event) => {
        const date = new Date(event.eventDateTime);

        this.eventForm.patchValue({
          title: event.title,
          description: event.description ?? '',
          eventDate: date,
          eventTime:
            `${date.getHours().toString().padStart(2, '0')}:` +
            `${date.getMinutes().toString().padStart(2, '0')}`,
          location: event.location
        });
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Não foi possível carregar o evento para edição.';
        this.isLoading = false;
      }
    });
  }

  private getErrorMessage(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'Não foi possível conectar ao backend. Verifique se ele está executando na porta 8080.';
    }

    const response = error.error as ApiErrorResponse | null;

    const fieldMessage = response?.fieldErrors
      ? Object.values(response.fieldErrors)[0]
      : null;

    return fieldMessage ?? response?.message ?? 'Não foi possível salvar o evento.';
  }
}
