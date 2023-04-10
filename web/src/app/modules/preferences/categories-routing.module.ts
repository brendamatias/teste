import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PreferencesFormComponent } from './form/preferences_form.compoment';

@NgModule({
  imports: [
    RouterModule.forChild([
      { path: '', component: PreferencesFormComponent },
    ]),
  ],
  exports: [RouterModule],
})
export class PreferencesRoutingModule {}
