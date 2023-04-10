import { Overlay, OverlayRef } from '@angular/cdk/overlay';
import { ComponentPortal } from '@angular/cdk/portal';
import { Injectable } from '@angular/core';
import { LoaderComponent } from '../components/loader/loader.component';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private overlayRef: OverlayRef;

  constructor(private overlay: Overlay) {
    this.overlayRef = this.overlay.create({ width: '100vw', height: '100vh' })
  }

  public present(): void {
    const loaderComponent = new ComponentPortal(LoaderComponent);
    this.overlayRef.attach(loaderComponent);
  }

  public dismiss(): void {
    this.overlayRef.detach();
  }
}
