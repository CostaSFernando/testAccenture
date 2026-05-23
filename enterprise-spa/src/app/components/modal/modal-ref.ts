import { Subject } from 'rxjs';

export class ModalRef<TResult = unknown> {
  private readonly closedSubject = new Subject<TResult | undefined>();

  afterClosed$ = this.closedSubject.asObservable();

  close(result?: TResult): void {
    this.closedSubject.next(result);
    this.closedSubject.complete();
  }
}
