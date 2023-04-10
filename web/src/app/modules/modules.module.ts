import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { ModulesRoutingModule } from './modules-routing.module';

@NgModule({
  imports: [CommonModule, ModulesRoutingModule],
})
export class ModulesModule {}
