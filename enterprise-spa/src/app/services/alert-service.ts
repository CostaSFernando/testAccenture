import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type AlertType = 'success' | 'error' | 'warning' | 'info';

export interface AlertData {
  type: AlertType;
  title: string;
  message: string;
  duration?: number;
  action?: {
    label: string;
    callback: () => void;
  };
}

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  private readonly alertSubject = new BehaviorSubject<AlertData | null>(null);

  alert$ = this.alertSubject.asObservable();

  private timeoutId: ReturnType<typeof setTimeout> | null = null;

  show(alert: AlertData): void {
    this.clearTimeout();

    this.alertSubject.next(alert);

    const duration = alert.duration ?? 3500;

    this.timeoutId = setTimeout(() => {
      this.close();
    }, duration);
  }

  success(message: string, title = 'Sucesso!'): void {
    this.show({
      type: 'success',
      title,
      message,
    });
  }

  error(message: string, title = 'Erro'): void {
    this.show({
      type: 'error',
      title,
      message,
    });
  }

  warning(message: string, title = 'Atenção', duration = 3500, action: { label: string; callback: () => void } | undefined = undefined): void {
    this.show({
      type: 'warning',
      title,
      duration,
      message,
      action
    });
  }

  info(message: string, title = 'Informação', action: { label: string; callback: () => void } | undefined = undefined): void {
    this.show({
      type: 'info',
      title,
      message,
      action
    });
  }

  close(): void {
    this.clearTimeout();
    this.alertSubject.next(null);
  }

  private clearTimeout(): void {
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
      this.timeoutId = null;
    }
  }
}
