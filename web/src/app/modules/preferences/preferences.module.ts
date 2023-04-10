import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PreferencesRoutingModule } from './categories-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { PreferencesService } from './preferences.service';
import { RxReactiveFormsModule } from "@rxweb/reactive-form-validators";
import { PreferencesFormComponent } from './form/preferences_form.compoment';

@NgModule({
	imports: [
		CommonModule,
		PreferencesRoutingModule,
		SharedModule,
		RxReactiveFormsModule,
	],
	declarations: [PreferencesFormComponent],
	providers: [
		PreferencesService
	]
})
export class PreferencesModule { }
