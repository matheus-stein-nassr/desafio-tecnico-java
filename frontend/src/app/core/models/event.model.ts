export interface EventModel {
  id: number;
  title: string;
  description: string | null;
  eventDateTime: string;
  location: string;
  createdAt: string;
  updatedAt: string;
}

export interface EventPayload {
  title: string;
  description: string;
  eventDateTime: string;
  location: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}
