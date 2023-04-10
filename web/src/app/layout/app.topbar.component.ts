import { Component, ElementRef, ViewChild } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { LayoutService } from './service/app.layout.service';
import { UserService } from '../core/services/user.service';
import { LocalService } from '../core/services/local.service';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
})
export class AppTopBarComponent {
  items!: MenuItem[];
  routeItems!: MenuItem[];
  user!: String;
  isAdmin: boolean = false;

  @ViewChild('menubutton') menuButton!: ElementRef;

  @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;

  @ViewChild('topbarmenu') menu!: ElementRef;

  constructor(
    private router: Router,
    public layoutService: LayoutService,
    public userService: UserService,
    private localStore: LocalService
  ) {
    this.userService.userName$.subscribe((user) => {
      this.isAdmin = true;
      this.user = user!.slice(0, 1).toUpperCase();
      this.items = [
        {
          label: user,
          items: [
            {
              label: 'Sair',
              icon: 'pi pi-external-link',
              command: () => {
                this.logout();
              },
            },
          ],
        },
      ];
      this.routeItems = [
        {
          label: 'Dashboard',
          command: () => {
            this.router.navigate(['/dashboard']);
          },
        },
        {
          label: 'Notas Fiscais',
          command: () => {
            this.router.navigate(['/dashboard/invoices']);
          },
        },
        {
          label: 'Despesas',
          command: () => {
            this.router.navigate(['/dashboard/expenses']);
          },
        },
        {
          label: 'Empresas',
          command: () => {
            this.router.navigate(['/dashboard/companies']);
          },
        },
        {
          label: 'Categorias',
          command: () => {
            this.router.navigate(['/dashboard/categories']);
          },
        },
        {
          label: 'Preferências',
          command: () => {
            this.router.navigate(['/dashboard/preferences']);
          },
        },
      ];
    });
  }

  logout(): void {
    this.localStore.removeData('token');
    this.router.navigate(['/login']);
  }
}
