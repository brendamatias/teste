import { Component, OnInit } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Utils } from 'src/app/core/shared/utils';
import { CategoriesService } from './categories.service';
import { Category } from 'src/app/model/category';

@Component({
  templateUrl: './categories.component.html',
  providers: [MessageService, ConfirmationService],
})
export class CategoriesComponent implements OnInit {

  constructor(
    public utils: Utils,
    private categoriesService: CategoriesService,
    private confirmationService: ConfirmationService,
  ) {}
  
  
  list!: Category[];
  currentDate: Date = new Date();
  lastYear!: Date;

  ngOnInit() {  
    this.lastYear = this.currentDate;
    this.findAll();
  }

  findAll() {
    this.categoriesService.list().subscribe((res) => {
      this.list = res.body!;
    });
  }

  archive(event: MouseEvent, category: Category) {
    var message = this._activeCategory(category) ? 'arquivar' : 'ativar';
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Realmente deseja '+message+' a categoria: ' + category.name + '?',
      header: this._activeCategory(category) ? 'Arquivamento' : 'Ativação',
      icon: this._activeCategory(category) ? 'pi pi-eye-slash' : 'pi pi-eye',
      acceptIcon: 'none',
      rejectIcon: 'none',
      rejectButtonStyleClass: 'p-button-text',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: async () => {
        await this._exeArchive(category);
      },
      reject: () => {},
    });
  }

  async _exeArchive(category: Category) {
    var message = this._activeCategory(category) ? 'arquivada' : 'ativada';
    var status = this._activeCategory(category) ? 'archived' : 'active';
    this.categoriesService.archiveOrActive(category.id, status).subscribe(
      (res) => {
        this.utils.exibirSucesso('Categoria ' + message + ' com sucesso!');
        this.findAll();
      },
      (erro) => {
        console.error('erro', erro);
        this.utils.exibirErro(
          'Erro ao se comunicar com o serviço, contate a equipe de TI.'
        );
      }
    );
  }

  _activeCategory(category: Category) {
    return category.status == 'active';
  }
 
}
