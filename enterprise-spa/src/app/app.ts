import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Modal } from './components/modal/modal';
import { Alert } from "./components/alert/alert";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Modal, Alert],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('enterprise-spa');
}
