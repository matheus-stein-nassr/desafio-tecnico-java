import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApiConfigService } from '../services/api-config.service';

@Injectable()
export class ApiPrefixInterceptor implements HttpInterceptor {
  constructor(private readonly apiConfigService: ApiConfigService) {}

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (req.url.startsWith('http://') || req.url.startsWith('https://')) {
      return next.handle(req);
    }

    const normalizedUrl = req.url.startsWith('/') ? req.url : `/${req.url}`;
    const hasApiPrefix = normalizedUrl.startsWith('/api/');
    const targetUrl = hasApiPrefix
      ? normalizedUrl
      : `${this.apiConfigService.apiBaseUrl}${normalizedUrl}`;

    return next.handle(req.clone({ url: targetUrl }));
  }
}
