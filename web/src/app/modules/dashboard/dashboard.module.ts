import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard.component';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { DashboardService } from './dashboard.service';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { ChartModule } from 'primeng/chart';

@NgModule({
	imports: [
		CommonModule,
		DashboardRoutingModule,
		SharedModule,
		TooltipModule,
		ConfirmPopupModule,
		ChartModule
	],
	declarations: [DashboardComponent],
	providers: [
		DashboardService
	]
})
export class DashboardModule { }
