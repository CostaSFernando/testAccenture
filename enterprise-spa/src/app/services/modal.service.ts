import { Injectable, Type } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ModalRef } from '../components/modal/modal-ref';


export interface ModalConfig<TData = unknown, TResult = unknown> {
  component: Type<unknown>;
  data?: TData;
  modalRef: ModalRef<TResult>;
}

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  private readonly modalStateSubject = new BehaviorSubject<ModalConfig<any, any> | null>(null);

  modalState$ = this.modalStateSubject.asObservable();

  open<TData = unknown, TResult = unknown>(
    component: Type<unknown>,
    data?: TData
  ): ModalRef<TResult> {
    const modalRef = new ModalRef<TResult>();

    this.modalStateSubject.next({
      component,
      data,
      modalRef,
    });

    modalRef.afterClosed$.subscribe(() => {
      this.modalStateSubject.next(null);
    });

    return modalRef;
  }

  close(): void {
    this.modalStateSubject.next(null);
  }
}
