import { Component, OnInit } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Utils } from 'src/app/core/shared/utils';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoriesRequest } from './categories_request';
import { CategoriesService } from '../categories.service';
import { Category } from 'src/app/model/category';

@Component({
  templateUrl: './categories_form.component.html',
  providers: [MessageService, ConfirmationService],
})
export class CategoriesFormComponent implements OnInit {
  constructor(
    private router: Router,
    public route: ActivatedRoute,
    public utils: Utils,
    private categoriesServices: CategoriesService,
    private fb: FormBuilder
  ) {}

  id!: number;
  category!: FormGroup;

  async ngOnInit() {

    this.route.params.subscribe((params) => {
      this.createFormClean();
      this.id = params['id'];
      if (this.id) {
        this._loadEntity(this.id);
      }
    });
  }

  createFormClean() {
    this.category = this.fb.group({
      name: [null, [Validators.minLength(3), Validators.required]],
      description: [null, [Validators.minLength(3), Validators.required]]
    });
  }

  _loadingFormGroup(categoryDTO: Category) {
    this.category.controls['name'].setValue(categoryDTO.name);
    this.category.controls['description'].setValue(categoryDTO.description);
  }

  _loadEntity(id: number) {
    this.categoriesServices.find(id).subscribe(
      (res) => {
        this._loadingFormGroup(res.body!);
      },
      (erro) => {
        console.error('erro', erro);
        this.utils.exibirErro('Erro ao buscar dados da categoria: ' + id);
      }
    );
  }

  onSave() {
    if (this.category.valid) {
      var request = this._formToEntity();

      this.categoriesServices.save(request, this.id).subscribe(
        (res) => {
          this.router.navigate(['/dashboard/categories']);
          this.utils.exibirSucesso('Categoria salva com sucesso!');
        },
        (erro) => {
          console.error('erro', erro);
          this.utils.exibirErroOperation('Salvar Categoria!');
        }
      );
    } else {
      this.utils.exibirErro('Preencha todos os campos!');
    }
  }

  _formToEntity() {
    var request: CategoriesRequest = {
      name: this.category.get('name')?.value,
      description: this.category.get('description')?.value,
    };

    return request;
  }
}
