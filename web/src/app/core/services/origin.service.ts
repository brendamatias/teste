import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class OriginService {
  private originKey = 'userOrigin';

  setOrigin(origin: string): void {
    localStorage.setItem(this.originKey, origin);
  }

  getOrigin(): string | null {
    return localStorage.getItem(this.originKey);
  }

  clearOrigin(): void {
    localStorage.removeItem(this.originKey);
  }
}