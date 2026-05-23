import { CommonModule } from '@angular/common';
import { ModalService } from '../../services/modal.service';
import { Component, InjectionToken, Injector, Signal, Type, WritableSignal, signal } from '@angular/core';
import { ModalRef } from './modal-ref';

export const MODAL_DATA = new InjectionToken<unknown>('MODAL_DATA');
export const MODAL_REF = new InjectionToken<ModalRef>('MODAL_REF');

@Component({
  selector: 'app-modal',
  imports: [CommonModule],
  templateUrl: './modal.html',
  styleUrl: './modal.scss',
})
export class Modal {
  component: WritableSignal<Type<unknown>> | null = null;
  injector: Injector | null = null;
  modalRef: ModalRef | null = null;

  constructor(
    private readonly modalService: ModalService,
    private readonly parentInjector: Injector
  ) {
    this.modalService.modalState$.subscribe((state) => {

      if (!state) {
        this.component ? this.component.set(null as any) : null;
        this.injector = null;
        this.modalRef = null;
        return;
      }

      this.component = signal(state.component);
      this.modalRef = state.modalRef;

      this.injector = Injector.create({
        parent: this.parentInjector,
        providers: [
          {
            provide: MODAL_DATA,
            useValue: state.data,
          },
          {
            provide: MODAL_REF,
            useValue: state.modalRef,
          },
        ],
      });
    });
  }

  close(): void {
    this.modalRef?.close();
  }
}
