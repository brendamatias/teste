import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from './core/services/user.service';
import { LocalService } from './core/services/local.service';
import { PrimeNGConfig, FilterService } from 'primeng/api';

import ptBR from '../assets/i18n/pt-br.json';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  userName: string | undefined;

  constructor(
    private router: Router,
    private userService: UserService,
    private cdr: ChangeDetectorRef,
    private localStore: LocalService,
    private primengConfig: PrimeNGConfig,
    private filterService: FilterService,
  ) {}

  ngOnInit(): void {
    this.primengConfig.ripple = true;

    this.primengConfig.setTranslation(ptBR.ptbr);

    this.filterService.register('rangeDates', (dateField: Date, dateS: Date, dateE: Date): boolean => {
      if (dateS === undefined || dateS === null || dateE === undefined || dateE === null|| dateField === undefined || dateField === null) {
          return true;
      }
      return dateField.getMilliseconds() >= dateS.getMilliseconds() && dateField.getMilliseconds() <= dateE.getMilliseconds();
    });

    this.userService.userName$.subscribe((newUserName) => {
      this.userName = newUserName;
      this.cdr.detectChanges();
    });
  }

  public actualPage(): string {
    const currentUrl = this.router.url;
    const urlWithoutParams = currentUrl.split('?')[0];
    const segments = urlWithoutParams.split('/');
    const pageName = segments[1];
    return pageName;
  }
}
