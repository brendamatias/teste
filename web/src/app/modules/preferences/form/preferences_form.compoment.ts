import { Component, OnInit } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Utils } from 'src/app/core/shared/utils';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PreferencesService } from '../preferences.service';
import { Preference } from 'src/app/model/preference';

@Component({
  templateUrl: './preferences_form.component.html',
  providers: [MessageService, ConfirmationService],
})
export class PreferencesFormComponent implements OnInit {
  constructor(
    private router: Router,
    public route: ActivatedRoute,
    public utils: Utils,
    private preferencesService: PreferencesService,
    private fb: FormBuilder
  ) {}

  id!: number;
  preference!: FormGroup;

  async ngOnInit() {
    this.createFormClean();
    this._loadEntity();    
  }

  createFormClean() {
    this.preference = this.fb.group({
      annualInvoiceLimit: [null, Validators.required],
      emailNotificationsEnabled: new FormControl<boolean>(false),
      smsNotificationsEnabled: new FormControl<boolean>(false)
    });
  }

  _loadingFormGroup(preferenceDTO: Preference) {
    this.preference.controls['annualInvoiceLimit'].setValue(preferenceDTO.annualInvoiceLimit);
    this.preference.controls['emailNotificationsEnabled'].setValue(preferenceDTO.emailNotificationsEnabled);
    this.preference.controls['smsNotificationsEnabled'].setValue(preferenceDTO.smsNotificationsEnabled);
  }

  _loadEntity() {
    this.preferencesService.find().subscribe(
      (res) => {
        this._loadingFormGroup(res.body!);
      },
      (erro) => {
        console.error('erro', erro);
        this.utils.exibirErro('Erro ao buscar as preferências do sistema!');
      }
    );
  }

  onSave() {
    if (this.preference.valid) {
      var request = this._formToEntity();

      this.preferencesService.save(request).subscribe(
        (res) => {
          this.router.navigate(['/dashboard/preferences']);
          this.utils.exibirSucesso('Preferência salva com sucesso!');
        },
        (erro) => {
          console.error('erro', erro);
          this.utils.exibirErroOperation('Salvar Preferência!');
        }
      );
    } else {
      this.utils.exibirErro('Preencha todos os campos!');
    }
  }

  _formToEntity() {
    var request: Preference = {
      annualInvoiceLimit: this.preference.get('annualInvoiceLimit')?.value,
      emailNotificationsEnabled: this.preference.get('emailNotificationsEnabled')?.value,
      smsNotificationsEnabled: this.preference.get('smsNotificationsEnabled')?.value
    };

    return request;
  }
}
