import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { STORAGE_KEYS } from '../utils/storage-keys';

export const isLoggedGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
  if (token) {
    router.navigate(['/todos'], { replaceUrl: true });
    return false;
  }

  return true;
};
