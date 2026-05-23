import { Component } from '@angular/core';
import { AlertData, AlertService } from '../../services/alert-service';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-alert',
  imports: [CommonModule],
  templateUrl: './alert.html',
  styleUrl: './alert.scss',
})
export class Alert {
  alert$: Observable<AlertData | null>;

  constructor(
    private readonly alertService: AlertService
  ) {
    this.alert$ = this.alertService.alert$;
  }

  close(): void {
    this.alertService.close();
  }

  callback(action: { label: string; callback: () => void }): void {
    action.callback();
    this.close();
  }

  getIcon(alert: AlertData): string {
    const icons = {
      success: '✓',
      error: '!',
      warning: '!',
      info: 'i',
    };

    return icons[alert.type];
  }
}
