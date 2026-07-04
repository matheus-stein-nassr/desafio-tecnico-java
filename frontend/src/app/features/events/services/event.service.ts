import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { EventModel, EventPayload, PageResponse } from '../../../core/models/event.model';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private readonly endpoint = '/events';

  constructor(private readonly http: HttpClient) {}

  getEvents(page = 0, size = 10): Observable<PageResponse<EventModel>> {
    return this.http.get<PageResponse<EventModel>>(this.endpoint, {
      params: { page, size }
    });
  }

  getEventById(id: number): Observable<EventModel> {
    return this.http.get<EventModel>(`${this.endpoint}/${id}`);
  }

  createEvent(payload: EventPayload): Observable<EventModel> {
    return this.http.post<EventModel>(this.endpoint, payload);
  }

  updateEvent(id: number, payload: EventPayload): Observable<EventModel> {
    return this.http.put<EventModel>(`${this.endpoint}/${id}`, payload);
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.endpoint}/${id}`);
  }
}
